package com.tinuvile.domain.trade.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tinuvile
 * @description 交易结算订单实体对象
 * @since 2025/11/14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradePaySettlementEntity {

    /** 来源 */
    private String source;

    /** 渠道 */
    private String channel;

    /** 用户ID */
    private String userId;

    /** 拼单组队ID */
    private String teamId;

    /** 活动ID */
    private Long activityId;

    /** 外部交易单号 */
    private String outTradeNo;

}
