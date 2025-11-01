package com.tinuvile.domain.activity.service.trial.node;


import com.tinuvile.domain.activity.model.entity.MarketProductEntity;
import com.tinuvile.domain.activity.model.entity.TrialBalanceEntity;
import com.tinuvile.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import com.tinuvile.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import com.tinuvile.types.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Tinuvile
 * @since 2025/11/1
 */
@Slf4j
@Service
public class EndNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {
    @Override
    public TrialBalanceEntity apply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return null;
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        return null;
    }
}
