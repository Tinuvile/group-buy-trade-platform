package com.tinuvile.domain.activity.service.trial.thread;


import com.tinuvile.domain.activity.adapter.repository.IActivityRepository;
import com.tinuvile.domain.activity.model.valobj.SkuVO;

import java.util.concurrent.Callable;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/3
 */
public class QuerySkuVOFromDBThreadTask implements Callable<SkuVO> {

    private final String goodsId;

    private final IActivityRepository activityRepository;

    public QuerySkuVOFromDBThreadTask(String goodsId, IActivityRepository activityRepository) {
        this.goodsId = goodsId;
        this.activityRepository = activityRepository;
    }

    @Override
    public SkuVO call() throws Exception {
        return activityRepository.querySkuByGoodsId(goodsId);
    }
}
