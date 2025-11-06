package com.tinuvile.domain.activity.service.trial.thread;


import com.tinuvile.domain.activity.adapter.repository.IActivityRepository;
import com.tinuvile.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.tinuvile.domain.activity.model.valobj.SCSkuActivityVO;

import java.util.concurrent.Callable;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/3
 */
public class QueryGroupBuyActivityDiscountVOThreadTask implements Callable<GroupBuyActivityDiscountVO> {

    private final String source;

    private final String channel;

    private final String goodsId;

    private final IActivityRepository repository;

    public QueryGroupBuyActivityDiscountVOThreadTask(String source, String channel, String goodsId, IActivityRepository repository) {
        this.source = source;
        this.channel = channel;
        this.goodsId = goodsId;
        this.repository = repository;
    }

    @Override
    public GroupBuyActivityDiscountVO call() throws Exception {
        SCSkuActivityVO scSkuActivityVO = repository.querySCSkuActivityByGoodsId(source, channel, goodsId);
        if (null ==  scSkuActivityVO) {
            return null;
        }

        return repository.queryGroupBuyActivityDiscountVO(scSkuActivityVO.getActivityId());
    }
}
