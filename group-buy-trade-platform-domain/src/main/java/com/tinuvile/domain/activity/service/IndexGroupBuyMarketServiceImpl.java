package com.tinuvile.domain.activity.service;


import com.tinuvile.domain.activity.model.entity.MarketProductEntity;
import com.tinuvile.domain.activity.model.entity.TrialBalanceEntity;
import com.tinuvile.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import com.tinuvile.types.design.framework.tree.StrategyHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Tinuvile
 * @since 2025/11/1
 */
@Service
public class IndexGroupBuyMarketServiceImpl implements IIndexGroupBuyMarketService {
    @Resource
    private DefaultActivityStrategyFactory defaultActivityStrategyFactory;

    @Override
    public TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception {
        StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> strategyHandler = defaultActivityStrategyFactory.strategyHandler();

        return strategyHandler.apply(marketProductEntity, new DefaultActivityStrategyFactory.DynamicContext());
    }
}
