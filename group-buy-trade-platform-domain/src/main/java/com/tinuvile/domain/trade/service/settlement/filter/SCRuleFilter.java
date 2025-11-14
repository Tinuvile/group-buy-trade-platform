package com.tinuvile.domain.trade.service.settlement.filter;


import com.tinuvile.domain.trade.adapter.repository.ITradeRepository;
import com.tinuvile.domain.trade.model.entity.TradeSettlementRuleCommandEntity;
import com.tinuvile.domain.trade.model.entity.TradeSettlementRuleFilterBackEntity;
import com.tinuvile.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import com.tinuvile.types.design.framework.link.model2.handler.ILogicHandler;
import com.tinuvile.types.enums.ResponseCode;
import com.tinuvile.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Tinuvile
 * @description SC 渠道来源过滤
 * @since 2025/11/14
 */
@Slf4j
@Service
public class SCRuleFilter implements ILogicHandler<TradeSettlementRuleCommandEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradeSettlementRuleFilterBackEntity apply(TradeSettlementRuleCommandEntity requestParameter, TradeSettlementRuleFilterFactory.DynamicContext dynamicParameter) throws Exception {
        log.info("结算规则过滤 - 渠道黑名单校验{} outTradeNo:{}", requestParameter.getUserId(), requestParameter.getOutTradeNo());

        // SC渠道黑名单拦截
        boolean intercept = repository.isSCRuleIntercept(requestParameter.getSource(), requestParameter.getChannel());
        if (intercept) {
            log.error("{}{} 渠道黑名单拦截",requestParameter.getSource(),requestParameter.getChannel());
            throw new AppException(ResponseCode.E0105);
        }

        return next(requestParameter, dynamicParameter);
    }

}
