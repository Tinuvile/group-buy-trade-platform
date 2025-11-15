package com.tinuvile.api.dto;

import lombok.Data;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/11
 */
@Data
public class LockMarketPayOrderRequestDTO {

    // 用户ID
    private String userId;

    // 拼单组队ID - 可为空，为空则创建新组队ID
    private String teamId;

    // 活动ID
    private Long activityId;

    // 商品ID
    private String goodsId;

    // 来源
    private String source;

    // 渠道
    private String channel;

    // 外部交易单号
    private String outTradeNo;

    // 回调地址
    private String notifyUrl;

}
