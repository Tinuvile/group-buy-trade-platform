package com.tinuvile.infrastructure.adapter.repository;


import com.alibaba.fastjson.JSON;
import com.tinuvile.domain.trade.adapter.repository.ITradeRepository;
import com.tinuvile.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import com.tinuvile.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import com.tinuvile.domain.trade.model.entity.*;
import com.tinuvile.domain.trade.model.valobj.GroupBuyProcessVO;
import com.tinuvile.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import com.tinuvile.infrastructure.dao.IGroupBuyActivityDao;
import com.tinuvile.infrastructure.dao.IGroupBuyOrderDao;
import com.tinuvile.infrastructure.dao.IGroupBuyOrderListDao;
import com.tinuvile.infrastructure.dao.INotifyTaskDao;
import com.tinuvile.infrastructure.dao.po.GroupBuyActivity;
import com.tinuvile.infrastructure.dao.po.GroupBuyOrder;
import com.tinuvile.infrastructure.dao.po.GroupBuyOrderList;
import com.tinuvile.infrastructure.dao.po.NotifyTask;
import com.tinuvile.infrastructure.dcc.DCCService;
import com.tinuvile.types.common.Constants;
import com.tinuvile.types.enums.GroupBuyOrderEnumVO;
import com.tinuvile.types.enums.ResponseCode;
import com.tinuvile.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/11
 */
@Slf4j
@Repository
public class TradeRepository implements ITradeRepository {

    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;

    @Resource
    private IGroupBuyOrderDao groupBuyOrderDao;

    @Resource
    private IGroupBuyOrderListDao groupBuyOrderListDao;

    @Resource
    private INotifyTaskDao notifyTaskDao;

    @Resource
    private DCCService dccService;

    @Override
    public MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo) {
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setUserId(userId);
        groupBuyOrderListReq.setOutTradeNo(outTradeNo);
        GroupBuyOrderList groupBuyOrderListRes = groupBuyOrderListDao.queryGroupBuyOrderRecordByOutTradeNo(groupBuyOrderListReq);

        if (null == groupBuyOrderListRes) {
            return null;
        }

        return MarketPayOrderEntity.builder()
                .teamId(groupBuyOrderListRes.getTeamId())
                .orderId(groupBuyOrderListRes.getOrderId())
                .deductionPrice(groupBuyOrderListRes.getDeductionPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.valueOf(groupBuyOrderListRes.getStatus()))
                .build();
    }

    @Transactional(timeout = 500)
    @Override
    public MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate) {
        UserEntity userEntity = groupBuyOrderAggregate.getUserEntity();
        PayActivityEntity payActivityEntity = groupBuyOrderAggregate.getPayActivityEntity();
        PayDiscountEntity payDiscountEntity = groupBuyOrderAggregate.getPayDiscountEntity();
        Integer userTakeOrderCount = groupBuyOrderAggregate.getUserTakeOrderCount();

        String teamId = payActivityEntity.getTeamId();
        // teamId为空 - 新团
        if (StringUtils.isBlank(teamId)) {
            // 使用 RandomStringUtils.randomNumeric ，公司常用雪花算法UUID
            teamId = RandomStringUtils.randomNumeric(8);

            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.MINUTE, payActivityEntity.getValidTime());

            GroupBuyOrder groupBuyOrder = GroupBuyOrder.builder()
                    .teamId(teamId)
                    .activityId(payActivityEntity.getActivityId())
                    .source(payDiscountEntity.getSource())
                    .channel(payDiscountEntity.getChannel())
                    .originalPrice(payDiscountEntity.getOriginalPrice())
                    .deductionPrice(payDiscountEntity.getDeductionPrice())
                    .payPrice(payDiscountEntity.getPayPrice())
                    .targetCount(payActivityEntity.getTargetCount())
                    .completeCount(0)
                    .lockCount(1)
                    .validStartTime(currentDate)
                    .validEndTime(calendar.getTime())
                    .build();

            groupBuyOrderDao.insert(groupBuyOrder);
        } else {
            int updateAddTargetCount = groupBuyOrderDao.updateAddLockCount(teamId);
            if (1 != updateAddTargetCount) {
                throw new AppException(ResponseCode.E0005);
            }
        }

        String orderId = RandomStringUtils.randomNumeric(12);
        GroupBuyOrderList groupBuyOrderListReq = GroupBuyOrderList.builder()
                .userId(userEntity.getUserId())
                .teamId(teamId)
                .orderId(orderId)
                .activityId(payActivityEntity.getActivityId())
                .startTime(payActivityEntity.getStartTime())
                .endTime(payActivityEntity.getEndTime())
                .goodsId(payDiscountEntity.getGoodsId())
                .source(payDiscountEntity.getSource())
                .channel(payDiscountEntity.getChannel())
                .originalPrice(payDiscountEntity.getOriginalPrice())
                .deductionPrice(payDiscountEntity.getDeductionPrice())
                .status(TradeOrderStatusEnumVO.CREATE.getCode())
                .outTradeNo(payDiscountEntity.getOutTradeNo())
                .bizId(payActivityEntity.getActivityId() + Constants.UNDERLINE + userEntity.getUserId() + Constants.UNDERLINE + (userTakeOrderCount + 1))
                .build();
        try {
            groupBuyOrderListDao.insert(groupBuyOrderListReq);
        } catch (DuplicateKeyException e) {
            throw new AppException(ResponseCode.INDEX_DUPLICATE);
        }

        return MarketPayOrderEntity.builder()
                .orderId(orderId)
                .teamId(teamId) // 仅测试使用，后期移除
                .deductionPrice(payDiscountEntity.getDeductionPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.CREATE)
                .build();
    }

    @Override
    public GroupBuyProcessVO queryGroupBuyProcess(String teamId) {
        GroupBuyOrder groupBuyOrder = groupBuyOrderDao.queryGroupBuyProgress(teamId);
        if (null == groupBuyOrder) {
            return null;
        }
        return GroupBuyProcessVO.builder()
                .targetCount(groupBuyOrder.getTargetCount())
                .completeCount(groupBuyOrder.getCompleteCount())
                .lockCount(groupBuyOrder.getLockCount())
                .build();
    }

    @Override
    public GroupBuyActivityEntity queryGroupBuyActivityEntityByActivityId(Long activityId) {

        GroupBuyActivity groupBuyActivity = groupBuyActivityDao.queryGroupBuyActivityByActivityId(activityId);
        return GroupBuyActivityEntity.builder()
                .activityId(groupBuyActivity.getActivityId())
                .activityName(groupBuyActivity.getActivityName())
                .discountId(groupBuyActivity.getDiscountId())
                .groupType(groupBuyActivity.getGroupType())
                .takeLimitCount(groupBuyActivity.getTakeLimitCount())
                .target(groupBuyActivity.getTarget())
                .validTime(groupBuyActivity.getValidTime())
                .status(groupBuyActivity.getStatus())
                .startTime(groupBuyActivity.getStartTime())
                .endTime(groupBuyActivity.getEndTime())
                .tagId(groupBuyActivity.getTagId())
                .tagScope(groupBuyActivity.getTagScope())
                .build();

    }

    @Override
    public Integer queryOrderCountByActivityId(Long activityId, String userId) {

        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setActivityId(activityId);
        groupBuyOrderListReq.setUserId(userId);
        return groupBuyOrderListDao.queryOrderCountByActivityId(groupBuyOrderListReq);

    }

    @Override
    public GroupBuyTeamEntity queryGroupBuyTeamByTeamId(String teamId) {
        GroupBuyOrder groupBuyOrder = groupBuyOrderDao.queryGroupBuyTeamByTeamId(teamId);
        return GroupBuyTeamEntity.builder()
                .teamId(groupBuyOrder.getTeamId())
                .activityId(groupBuyOrder.getActivityId())
                .targetCount(groupBuyOrder.getTargetCount())
                .completeCount(groupBuyOrder.getCompleteCount())
                .lockCount(groupBuyOrder.getLockCount())
                .status(GroupBuyOrderEnumVO.valueOf(groupBuyOrder.getStatus()))
                .validStartTime(groupBuyOrder.getValidStartTime())
                .validEndTime(groupBuyOrder.getValidEndTime())
                .build();
    }

    @Transactional(timeout = 500)
    @Override
    public void settlementMarketPayOrder(GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate) {

        UserEntity userEntity = groupBuyTeamSettlementAggregate.getUserEntity();
        GroupBuyTeamEntity groupBuyTeamEntity = groupBuyTeamSettlementAggregate.getGroupBuyTeamEntity();
        TradePaySuccessEntity tradePaySuccessEntity = groupBuyTeamSettlementAggregate.getTradePaySuccessEntity();

        // 更新拼团订单明细状态
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setUserId(userEntity.getUserId());
        groupBuyOrderListReq.setOutTradeNo(tradePaySuccessEntity.getOutTradeNo());
        groupBuyOrderListReq.setOutTradeTime(tradePaySuccessEntity.getOutTradeTime());

        Integer updateOrderListStatusCount = groupBuyOrderListDao.updateOrderStatus2COMPLETE(groupBuyOrderListReq);
        if (1 != updateOrderListStatusCount) {
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }

        // 更新拼团达成数量
        int updateAddCount = groupBuyOrderDao.updateAddCompleteCount(groupBuyTeamEntity.getTeamId());
        if (1 != updateAddCount) {
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }

        // 更新拼团完成状态
        if (groupBuyTeamEntity.getTargetCount() - groupBuyTeamEntity.getCompleteCount() == 1) {
            Integer updateCompleteCount = groupBuyOrderDao.updateOrderStatus2COMPLETE(groupBuyTeamEntity.getTeamId());
            if (1 != updateCompleteCount) {
                throw new AppException(ResponseCode.UPDATE_ZERO);
            }

            // 查询拼团交易完成外部单号列表
            List<String> outTradeNoList = groupBuyOrderListDao.queryGroupBuyCompleteOrderOutTradeNoListByTeamId(groupBuyTeamEntity.getTeamId());

            // 拼团完成写入回调任务记录
            NotifyTask notifyTask = new NotifyTask();
            notifyTask.setActivityId(groupBuyTeamEntity.getActivityId());
            notifyTask.setTeamId(groupBuyTeamEntity.getTeamId());
            notifyTask.setNotifyUrl("暂无");
            notifyTask.setNotifyCount(0);
            notifyTask.setNotifyStatus(0);
            notifyTask.setParameterJson(JSON.toJSONString(new HashMap<String, Object>() {
                {
                    put("teamId", groupBuyTeamEntity.getTeamId());
                    put("outTradeNoList", outTradeNoList);
                }
            }));

            notifyTaskDao.insert(notifyTask);

        }

    }

    @Override
    public boolean isSCRuleIntercept(String source, String channel) {
        return dccService.isSCBlackIntercept(source, channel);
    }

}
