package com.tinuvile.domain.activity.model.entity;


import com.tinuvile.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 试算结果实体
 * @author Tinuvile
 * @since 2025/11/1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrialBalanceEntity {

    /* 商品ID */
    private String goodsId;

    /* 商品名称 */
    private String goodsName;

    /* 原价 */
    private BigDecimal originalPrice;

    /* 折扣金额 */
    private BigDecimal deductionPrice;

    /* 支付金额 */
    private BigDecimal payPrice;

    /* 目标数量 */
    private Integer targetCount;

    /* 开始时间 */
    private Date startTime;

    /* 结束时间 */
    private Date endTime;

    /* 是否可见 */
    private Boolean isVisible;

    /* 是否可参与 */
    private Boolean isEnable;

    /* 活动配置信息 */
    private GroupBuyActivityDiscountVO groupBuyActivityDiscountVO;

}
