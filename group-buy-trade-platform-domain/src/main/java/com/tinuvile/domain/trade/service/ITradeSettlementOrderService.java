package com.tinuvile.domain.trade.service;


import com.tinuvile.domain.trade.model.entity.TradePaySettlementEntity;
import com.tinuvile.domain.trade.model.entity.TradePaySuccessEntity;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/14
 */
public interface ITradeSettlementOrderService {

    /**
     * 营销结算
     * @param tradePaySuccessEntity 交易支付订单实体对象
     * @return 交易结算订单实体
     */
    TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity);

}
