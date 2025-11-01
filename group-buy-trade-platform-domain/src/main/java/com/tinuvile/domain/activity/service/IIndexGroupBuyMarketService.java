package com.tinuvile.domain.activity.service;


import com.tinuvile.domain.activity.model.entity.MarketProductEntity;
import com.tinuvile.domain.activity.model.entity.TrialBalanceEntity;

/**
 * @author Tinuvile
 * @since 2025/11/1
 */
public interface IIndexGroupBuyMarketService {
    TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception;
}
