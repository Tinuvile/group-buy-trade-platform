package com.tinuvile.domain.trade.service;


import com.tinuvile.domain.trade.adapter.repository.ITradeRepository;
import com.tinuvile.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import com.tinuvile.domain.trade.model.entity.MarketPayOrderEntity;
import com.tinuvile.domain.trade.model.entity.PayActivityEntity;
import com.tinuvile.domain.trade.model.entity.PayDiscountEntity;
import com.tinuvile.domain.trade.model.entity.UserEntity;
import com.tinuvile.domain.trade.model.valobj.GroupBuyProcessVO;
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
public class TradeOrderService implements ITradeOrderService {

    @Resource
    private ITradeRepository repository;

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
    public MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity) {
        log.info("拼团交易 - 锁定订单：{} activityId: {} goodsId: {}", userEntity.getUserId(), payActivityEntity.getActivityId(), payDiscountEntity.getGoodsId());

        GroupBuyOrderAggregate groupBuyOrderAggregate = GroupBuyOrderAggregate.builder()
                .userEntity(userEntity)
                .payActivityEntity(payActivityEntity)
                .payDiscountEntity(payDiscountEntity)
                .build();

        return repository.lockMarketPayOrder(groupBuyOrderAggregate);
    }

}
