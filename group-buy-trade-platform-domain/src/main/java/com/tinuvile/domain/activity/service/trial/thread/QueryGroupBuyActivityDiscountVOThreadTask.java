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

    private final Long activityId;

    private final String source;

    private final String channel;

    private final String goodsId;

    private final IActivityRepository repository;

    public QueryGroupBuyActivityDiscountVOThreadTask(Long activityId, String source, String channel, String goodsId, IActivityRepository repository) {
        this.activityId = activityId;
        this.source = source;
        this.channel = channel;
        this.goodsId = goodsId;
        this.repository = repository;
    }

    @Override
    public GroupBuyActivityDiscountVO call() throws Exception {
        // 判断是否存在可用的活动ID
        Long availableActivityId = activityId;
        if (null == activityId) {
            // 查询渠道商品活动配置关联配置
            SCSkuActivityVO scSkuActivityVO = repository.querySCSkuActivityByGoodsId(source, channel, goodsId);
            if (null == scSkuActivityVO) {
                return null;
            }
            availableActivityId = scSkuActivityVO.getActivityId();
        }

        return repository.queryGroupBuyActivityDiscountVO(availableActivityId);
    }
}
