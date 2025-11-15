package com.tinuvile.domain.trade.adapter.repository;


import com.tinuvile.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import com.tinuvile.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import com.tinuvile.domain.trade.model.entity.GroupBuyActivityEntity;
import com.tinuvile.domain.trade.model.entity.GroupBuyTeamEntity;
import com.tinuvile.domain.trade.model.entity.MarketPayOrderEntity;
import com.tinuvile.domain.trade.model.valobj.GroupBuyProcessVO;

/**
 * @author Tinuvile
 * @description 交易仓储服务接口
 * @since 2025/11/11
 */
public interface ITradeRepository {

    MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo);

    MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate);

    GroupBuyProcessVO queryGroupBuyProcess(String teamId);

    GroupBuyActivityEntity queryGroupBuyActivityEntityByActivityId(Long activityId);

    Integer queryOrderCountByActivityId(Long activityId, String userId);

    GroupBuyTeamEntity queryGroupBuyTeamByTeamId(String teamId);

    boolean settlementMarketPayOrder(GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate);

    boolean isSCRuleIntercept(String source, String channel);

}
