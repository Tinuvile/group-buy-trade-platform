package com.tinuvile.domain.activity.service.discount;


import com.tinuvile.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.math.BigDecimal;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/5
 */
public interface IDiscountCalculateService {
    /**
     * 折扣计算
     * @param userId 用户ID
     * @param originalPrice 原价
     * @param groupByDiscount 折扣计划配置
     * @return 折扣金额
     */
    BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupByDiscount);
}
