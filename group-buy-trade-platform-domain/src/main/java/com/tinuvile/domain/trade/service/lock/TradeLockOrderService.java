package com.tinuvile.domain.trade.service.lock;


import com.tinuvile.domain.trade.adapter.repository.ITradeRepository;
import com.tinuvile.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import com.tinuvile.domain.trade.model.entity.*;
import com.tinuvile.domain.trade.model.valobj.GroupBuyProcessVO;
import com.tinuvile.domain.trade.service.ITradeLockOrderService;
import com.tinuvile.domain.trade.service.lock.factory.TradeRuleFilterFactory;
import com.tinuvile.types.design.framework.link.model2.chain.BusinessLinkedList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/11
 */
@Slf4j
@Service
public class TradeLockOrderService implements ITradeLockOrderService {

    @Resource
    private ITradeRepository repository;

    @Resource
    private BusinessLinkedList<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity> tradeRuleFilter;

    @Override
    public MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo) {
        log.info("拼团交易 - 查询未支付营销订单：{} outTradeNo={}", userId, outTradeNo);
        return repository.queryMarketPayOrderEntityByOutTradeNo(userId, outTradeNo);
    }

    @Override
    public GroupBuyProcessVO queryGroupBuyProcess(String teamId) {
        log.info("拼团交易 - 查询拼团进度：{}", teamId);
        return repository.queryGroupBuyProcess(teamId);
    }

    @Override
    public MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity) throws Exception {
        log.info("拼团交易 - 锁定订单：{} activityId: {} goodsId: {}", userEntity.getUserId(), payActivityEntity.getActivityId(), payDiscountEntity.getGoodsId());

        // 交易规则过滤
        TradeRuleFilterBackEntity tradeRuleFilterBackEntity = tradeRuleFilter.apply(TradeRuleCommandEntity.builder()
                        .activityId(payActivityEntity.getActivityId())
                        .userId(userEntity.getUserId())
                        .build(),
                new TradeRuleFilterFactory.DynamicContext());

        Integer userTakeOrderCount = tradeRuleFilterBackEntity.getUserTakeOrderCount();

        GroupBuyOrderAggregate groupBuyOrderAggregate = GroupBuyOrderAggregate.builder()
                .userEntity(userEntity)
                .payActivityEntity(payActivityEntity)
                .payDiscountEntity(payDiscountEntity)
                .userTakeOrderCount(userTakeOrderCount)
                .build();

        return repository.lockMarketPayOrder(groupBuyOrderAggregate);
    }

}
