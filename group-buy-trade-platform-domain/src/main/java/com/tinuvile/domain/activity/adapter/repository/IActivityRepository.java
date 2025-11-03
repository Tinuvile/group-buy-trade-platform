package com.tinuvile.domain.activity.adapter.repository;


import com.tinuvile.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.tinuvile.domain.activity.model.valobj.SkuVO;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/3
 */
public interface IActivityRepository {

    GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(String source, String channel);

    SkuVO querySkuByGoodsId(String goodsId);
}
