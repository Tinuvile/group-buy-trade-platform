package com.tinuvile.domain.trade.adapter.repository;


import com.tinuvile.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import com.tinuvile.domain.trade.model.entity.MarketPayOrderEntity;
import com.tinuvile.domain.trade.model.valobj.GroupBuyProcessVO;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/11
 */
public interface ITradeRepository {

    MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo);

    MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate);

    GroupBuyProcessVO queryGroupBuyProcess(String teamId);

}
