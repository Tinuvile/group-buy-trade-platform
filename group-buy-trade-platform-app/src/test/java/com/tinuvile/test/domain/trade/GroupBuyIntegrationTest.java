package com.tinuvile.test.domain.trade;


import com.tinuvile.api.IDCCService;
import com.tinuvile.domain.activity.model.entity.MarketProductEntity;
import com.tinuvile.domain.activity.model.entity.TrialBalanceEntity;
import com.tinuvile.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.tinuvile.domain.activity.service.IIndexGroupBuyMarketService;
import com.tinuvile.domain.trade.model.entity.MarketPayOrderEntity;
import com.tinuvile.domain.trade.model.entity.PayActivityEntity;
import com.tinuvile.domain.trade.model.entity.PayDiscountEntity;
import com.tinuvile.domain.trade.model.entity.TradePaySettlementEntity;
import com.tinuvile.domain.trade.model.entity.TradePaySuccessEntity;
import com.tinuvile.domain.trade.model.entity.UserEntity;
import com.tinuvile.domain.trade.model.valobj.GroupBuyProcessVO;
import com.tinuvile.domain.trade.service.ITradeLockOrderService;
import com.tinuvile.domain.trade.service.ITradeSettlementOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Tinuvile
 * @description 拼团完整流程集成测试：锁单 → 结算
 * @since 2025/11/14
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupBuyIntegrationTest {

    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;

    @Resource
    private ITradeLockOrderService tradeLockOrderService;

    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;

    @Resource
    private IDCCService dccService;

    /**
     * 完整拼团流程测试：从商品试算 → 锁单 → 支付结算
     * 专注于验证各服务间协作，不重复实现业务逻辑
     */
    @Test
    @Transactional
    @Rollback
    public void test_completeGroupBuyFlow() throws Exception {
        log.info("🚀 开始拼团完整流程集成测试");

        // 环境准备
        setupTestEnvironment();

        // 准备测试数据
        String userId = "TestUser_" + System.currentTimeMillis();
        Long activityId = 100123L;
        String outTradeNo = RandomStringUtils.randomNumeric(12);

        log.info("📊 第1步：商品试算");
        TrialBalanceEntity trial = indexGroupBuyMarketService.indexMarketTrial(
                buildMarketProduct(userId, activityId));
        log.info("✅ 试算完成，优惠金额: {}, 支付金额: {}", 
                trial.getDeductionPrice(), trial.getPayPrice());

        log.info("🔒 第2步：拼团锁单（创建新团）");
        MarketPayOrderEntity lockResult = tradeLockOrderService.lockMarketPayOrder(
                buildUserEntity(userId),
                buildPayActivity(trial, null), // null表示创建新团
                buildPayDiscount(trial, outTradeNo));
        log.info("✅ 锁单完成，订单ID: {}, 团队ID: {}", 
                lockResult.getOrderId(), lockResult.getTeamId());

        log.info("💰 第3步：支付结算");
        TradePaySettlementEntity settlement = tradeSettlementOrderService
                .settlementMarketPayOrder(buildPaySuccess(userId, outTradeNo));
        log.info("✅ 结算完成，团队ID: {}", settlement.getTeamId());

        log.info("📊 第4步：验证结果");
        // 验证业务结果
        assert lockResult.getTeamId() != null : "团队ID不能为空";
        assert settlement.getTeamId().equals(lockResult.getTeamId()) : "结算团队ID应与锁单团队ID一致";

        GroupBuyProcessVO progress = tradeLockOrderService.queryGroupBuyProcess(settlement.getTeamId());
        assert progress.getCompleteCount() == 1 : "完成数量应为1";
        assert progress.getTargetCount() == 3 : "目标数量应为3";

        log.info("✅ 单用户拼团流程验证通过：拼团进度 {}/{}", 
                progress.getCompleteCount(), progress.getTargetCount());
        log.info("🎉 拼团完整流程集成测试成功完成！");
    }

    /**
     * 设置测试环境配置
     */
    private void setupTestEnvironment() throws InterruptedException {
        try {
            // 确保DCC配置对测试友好
            dccService.updateConfig("downgradeSwitch", "0");    // 关闭降级
            dccService.updateConfig("cutRange", "100");         // 100%通过率
            dccService.updateConfig("whiteListSwitch", "0");    // 关闭白名单

            Thread.sleep(500); // 等待配置生效
            log.info("✅ 测试环境配置完成");
        } catch (Exception e) {
            log.warn("⚠️ DCC配置失败，继续测试: {}", e.getMessage());
        }
    }

    // ============ 数据构建方法 ============

    /**
     * 构建商品试算请求对象
     */
    private MarketProductEntity buildMarketProduct(String userId, Long activityId) {
        return MarketProductEntity.builder()
                .userId(userId)
                .activityId(activityId)
                .goodsId("9890001")
                .source("s01")
                .channel("c01")
                .build();
    }

    /**
     * 构建用户实体
     */
    private UserEntity buildUserEntity(String userId) {
        return UserEntity.builder().userId(userId).build();
    }

    /**
     * 构建支付活动实体
     */
    private PayActivityEntity buildPayActivity(TrialBalanceEntity trial, String teamId) {
        GroupBuyActivityDiscountVO activity = trial.getGroupBuyActivityDiscountVO();
        return PayActivityEntity.builder()
                .teamId(teamId)
                .activityId(activity.getActivityId())
                .activityName(activity.getActivityName())
                .startTime(activity.getStartTime())
                .endTime(activity.getEndTime())
                .validTime(activity.getValidTime())
                .targetCount(activity.getTarget())
                .build();
    }

    /**
     * 构建支付折扣实体
     */
    private PayDiscountEntity buildPayDiscount(TrialBalanceEntity trial, String outTradeNo) {
        return PayDiscountEntity.builder()
                .source("s01")
                .channel("c01")
                .goodsId("9890001")
                .goodsName(trial.getGoodsName())
                .originalPrice(trial.getOriginalPrice())
                .deductionPrice(trial.getDeductionPrice())
                .payPrice(trial.getPayPrice())
                .outTradeNo(outTradeNo)
                .build();
    }

    /**
     * 构建支付成功实体
     */
    private TradePaySuccessEntity buildPaySuccess(String userId, String outTradeNo) {
        TradePaySuccessEntity paySuccess = new TradePaySuccessEntity();
        paySuccess.setSource("s01");
        paySuccess.setChannel("c01");
        paySuccess.setUserId(userId);
        paySuccess.setOutTradeNo(outTradeNo);
        return paySuccess;
    }

    /**
     * 多用户拼团场景测试：验证用户能够正确加入拼团并完成满员
     * 专注于测试拼团业务协作流程
     */
    @Test
    @Transactional
    @Rollback
    public void test_multiUserGroupBuyScenario() throws Exception {
        log.info("🚀 开始多用户拼团场景测试");
        setupTestEnvironment();

        Long activityId = 100123L;
        String teamId = null;

        // 用户1：创建新团
        log.info("👤 用户1：创建拼团");
        String user1 = "GroupUser1_" + System.currentTimeMillis();
        String outTradeNo1 = RandomStringUtils.randomNumeric(12);

        TrialBalanceEntity trial1 = indexGroupBuyMarketService.indexMarketTrial(
                buildMarketProduct(user1, activityId));
        MarketPayOrderEntity lock1 = tradeLockOrderService.lockMarketPayOrder(
                buildUserEntity(user1),
                buildPayActivity(trial1, null), // 创建新团
                buildPayDiscount(trial1, outTradeNo1));
        tradeSettlementOrderService.settlementMarketPayOrder(buildPaySuccess(user1, outTradeNo1));

        teamId = lock1.getTeamId();
        log.info("✅ 用户1完成，创建团队: {}", teamId);

        // 用户2：加入现有团队
        log.info("👤 用户2：加入拼团");
        String user2 = "GroupUser2_" + System.currentTimeMillis();
        String outTradeNo2 = RandomStringUtils.randomNumeric(12);

        TrialBalanceEntity trial2 = indexGroupBuyMarketService.indexMarketTrial(
                buildMarketProduct(user2, activityId));
        MarketPayOrderEntity lock2 = tradeLockOrderService.lockMarketPayOrder(
                buildUserEntity(user2),
                buildPayActivity(trial2, teamId), // 加入现有团队
                buildPayDiscount(trial2, outTradeNo2));
        tradeSettlementOrderService.settlementMarketPayOrder(buildPaySuccess(user2, outTradeNo2));

        log.info("✅ 用户2完成，加入团队: {}", lock2.getTeamId());

        // 用户3：完成拼团
        log.info("👤 用户3：完成拼团");
        String user3 = "GroupUser3_" + System.currentTimeMillis();
        String outTradeNo3 = RandomStringUtils.randomNumeric(12);

        TrialBalanceEntity trial3 = indexGroupBuyMarketService.indexMarketTrial(
                buildMarketProduct(user3, activityId));
        MarketPayOrderEntity lock3 = tradeLockOrderService.lockMarketPayOrder(
                buildUserEntity(user3),
                buildPayActivity(trial3, teamId), // 加入现有团队
                buildPayDiscount(trial3, outTradeNo3));
        tradeSettlementOrderService.settlementMarketPayOrder(buildPaySuccess(user3, outTradeNo3));

        log.info("✅ 用户3完成，加入团队: {}", lock3.getTeamId());

        // 验证拼团结果
        GroupBuyProcessVO finalProgress = tradeLockOrderService.queryGroupBuyProcess(teamId);
        assert finalProgress.getCompleteCount() == 3 : "应该有3个用户完成支付";
        assert finalProgress.getTargetCount() == 3 : "目标人数应为3";

        log.info("🎉 拼团成功！最终进度: {}/{}", finalProgress.getCompleteCount(), finalProgress.getTargetCount());
        log.info("🏁 多用户拼团场景测试完成");
    }

}
