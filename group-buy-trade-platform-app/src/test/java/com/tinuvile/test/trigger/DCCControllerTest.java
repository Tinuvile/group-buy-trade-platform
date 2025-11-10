package com.tinuvile.test.trigger;


import com.alibaba.fastjson.JSON;
import com.tinuvile.api.IDCCService;
import com.tinuvile.domain.activity.model.entity.MarketProductEntity;
import com.tinuvile.domain.activity.model.entity.TrialBalanceEntity;
import com.tinuvile.domain.activity.service.IIndexGroupBuyMarketService;
import com.tinuvile.types.enums.ResponseCode;
import com.tinuvile.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/7
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DCCControllerTest {

    @Resource
    private IDCCService dccService;

    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;

    @Test
    public void test_updateConfig() {
        // 动态调整配置
        dccService.updateConfig("downgradeSwitch", "1");
    }

    @Test
    public void test_updateConfig2indexMarketTrial() throws Exception {
        // 动态调整配置 - 开启降级开关
        dccService.updateConfig("downgradeSwitch", "1");
        // 超时等待异步配置生效
        Thread.sleep(1000);

        // 营销验证
        MarketProductEntity marketProductEntity = new MarketProductEntity();
        marketProductEntity.setUserId("Tinuvile");
        marketProductEntity.setSource("s01");
        marketProductEntity.setChannel("c01");
        marketProductEntity.setGoodsId("9890001");

        try {
            // 调用业务方法，预期会被降级开关拦截
            indexGroupBuyMarketService.indexMarketTrial(marketProductEntity);
            Assert.fail("应该抛出降级拦截异常");
        } catch (AppException e) {
            // 验证异常信息
            log.info("请求参数: {}", JSON.toJSONString(marketProductEntity));
            log.info("捕获到预期异常: code={}, info={}", e.getCode(), e.getInfo());
            
            // 验证异常码和异常信息
            Assert.assertEquals("E0003", e.getCode());
            Assert.assertEquals("拼团活动降级拦截", e.getInfo());
            
            log.info("✅ 动态配置中心测试成功：降级开关正常工作，成功拦截请求");
        }
    }

    @Test
    public void test_updateConfig2indexMarketTrial_Success() throws Exception {
        // 动态调整配置 - 关闭降级开关，同时确保切量范围包含所有用户
        dccService.updateConfig("downgradeSwitch", "0");
        dccService.updateConfig("cutRange", "100");  // 设置切量范围为100，允许所有用户通过
        // 超时等待异步配置生效
        Thread.sleep(1000);

        // 营销验证
        MarketProductEntity marketProductEntity = new MarketProductEntity();
        marketProductEntity.setUserId("Tinuvile");
        marketProductEntity.setSource("s01");
        marketProductEntity.setChannel("c01");
        marketProductEntity.setGoodsId("9890001");

        try {
            // 调用业务方法，应该正常执行不被拦截
            TrialBalanceEntity trialBalanceEntity = indexGroupBuyMarketService.indexMarketTrial(marketProductEntity);
            
            log.info("请求参数: {}", JSON.toJSONString(marketProductEntity));
            log.info("返回结果: {}", JSON.toJSONString(trialBalanceEntity));
            log.info("✅ 动态配置中心测试成功：降级开关关闭，切量范围100%，请求正常通过");
            
            // 验证结果不为空
            Assert.assertNotNull("业务处理结果不应为空", trialBalanceEntity);
            
        } catch (AppException e) {
            log.error("❌ 降级开关已关闭且切量范围100%，不应该抛出拦截异常: code={}, info={}", e.getCode(), e.getInfo());
            Assert.fail("降级开关已关闭且切量范围100%，不应该抛出拦截异常: " + e.getInfo());
        }
    }

    @Test
    public void test_updateConfig2indexMarketTrial_CutRange() throws Exception {
        // 动态调整配置 - 关闭降级开关，但设置切量范围为0，拦截所有用户
        dccService.updateConfig("downgradeSwitch", "0");
        dccService.updateConfig("cutRange", "0");  // 设置切量范围为0，拦截所有用户
        // 超时等待异步配置生效
        Thread.sleep(1000);

        // 营销验证
        MarketProductEntity marketProductEntity = new MarketProductEntity();
        marketProductEntity.setUserId("Tinuvile");
        marketProductEntity.setSource("s01");
        marketProductEntity.setChannel("c01");
        marketProductEntity.setGoodsId("9890001");

        try {
            // 调用业务方法，预期会被切量开关拦截
            indexGroupBuyMarketService.indexMarketTrial(marketProductEntity);
            Assert.fail("应该抛出切量拦截异常");
        } catch (AppException e) {
            // 验证异常信息
            log.info("请求参数: {}", JSON.toJSONString(marketProductEntity));
            log.info("捕获到预期异常: code={}, info={}", e.getCode(), e.getInfo());
            
            // 验证异常码和异常信息
            Assert.assertEquals("E0004", e.getCode());
            Assert.assertEquals("拼团活动切量拦截", e.getInfo());
            
            log.info("✅ 动态配置中心测试成功：切量开关正常工作，成功拦截请求");
        }
    }

    @Test
    public void test_whitelistUsers_bypass_degradeAndCut() throws Exception {
        // 全部拦截
        dccService.updateConfig("downgradeSwitch", "1");
        dccService.updateConfig("cutRange", "0");

        dccService.updateConfig("whiteListSwitch", "1");
        dccService.updateConfig("whiteListUsers", "Tinuvile,Erchamion,xiaofuge");

        Thread.sleep(1000);

        // 白名单用户正常通过
        MarketProductEntity marketProductEntity = new MarketProductEntity();
        marketProductEntity.setUserId("Tinuvile");
        marketProductEntity.setSource("s01");
        marketProductEntity.setChannel("c01");
        marketProductEntity.setGoodsId("9890001");

        try {
            // 调用业务方法，预期会被切量开关拦截
            TrialBalanceEntity result = indexGroupBuyMarketService.indexMarketTrial(marketProductEntity);
            log.info("✅ 动态配置中心测试成功：白名单用户正常通过");
            Assert.assertNotNull("白名单用户正常通过，返回结果不应为空", result);
        } catch (AppException e) {
            Assert.fail("白名单用户不应该被拦截: " + e.getInfo());
        }
    }

    @Test
    public void test_normalUser_intercepted_when_whitelist_enable() throws Exception {
        // 开启降级开关
        dccService.updateConfig("downgradeSwitch", "1");

        dccService.updateConfig("whiteListSwitch", "1");
        dccService.updateConfig("whiteListUsers", "Erchamion,xiaofuge");

        Thread.sleep(1000);

        // 非白名单用户测试
        MarketProductEntity marketProductEntity = new MarketProductEntity();
        marketProductEntity.setUserId("Tinuvile");
        marketProductEntity.setSource("s01");
        marketProductEntity.setChannel("c01");
        marketProductEntity.setGoodsId("9890001");

        try {
            indexGroupBuyMarketService.indexMarketTrial(marketProductEntity);
            Assert.fail("非白名单用户应该被拦截");
        } catch (AppException e) {
            Assert.assertEquals("E0003", e.getCode());
            Assert.assertEquals("拼团活动降级拦截", e.getInfo());
            log.info("✅ 非白名单用户被正常拦截");
        }
    }

}
