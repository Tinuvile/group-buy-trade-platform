package com.tinuvile.domain.trade.service;


import com.tinuvile.domain.trade.model.entity.MarketPayOrderEntity;
import com.tinuvile.domain.trade.model.entity.PayActivityEntity;
import com.tinuvile.domain.trade.model.entity.PayDiscountEntity;
import com.tinuvile.domain.trade.model.entity.UserEntity;
import com.tinuvile.domain.trade.model.valobj.GroupBuyProcessVO;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/11
 */
public interface ITradeOrderService {

    /**
     * 查询未支付的预购订单
     * @param userId 用户id
     * @param outTradeNo 外部订单号
     * @return 预购订单实体
     */
    MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId ,String outTradeNo);

    /**
     * 查询拼团进度
     * @param teamId 团购团队id
     * @return 进度信息
     */
    GroupBuyProcessVO queryGroupBuyProcess(String teamId);

    /**
     * 锁定订单
     * @param userEntity 用户实体
     * @param payActivityEntity 支付活动实体
     * @param payDiscountEntity 支付折扣实体
     * @return 预购订单实体
     */
    MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity);

}
