package com.tinuvile.domain.trade.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tinuvile
 * @description 交易支付订单实体对象
 * @since 2025/11/14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradePaySuccessEntity {

    /** 来源 */
    private String source;

    /** 渠道 */
    private String channel;

    /** 用户ID */
    private String userId;

    /** 外部交易单号 */
    private String outTradeNo;

}
