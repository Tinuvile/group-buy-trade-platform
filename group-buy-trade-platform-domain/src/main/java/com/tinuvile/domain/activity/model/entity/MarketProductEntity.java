package com.tinuvile.domain.activity.model.entity;


import lombok.Builder;
import lombok.Data;

/**
 * @description 商品实体
 * @author Tinuvile
 * @since 2025/11/1
 */
@Data
@Builder
public class MarketProductEntity {
    /* 用户ID */
    private String userId;

    /* 商品ID */
    private String goodsId;

    /* 来源 */
    private String source;

    /* 渠道 */
    private String channel;
}
