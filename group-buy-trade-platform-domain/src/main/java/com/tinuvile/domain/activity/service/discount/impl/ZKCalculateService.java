package com.tinuvile.domain.activity.service.discount.impl;


import com.tinuvile.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.tinuvile.domain.activity.service.discount.AbstractDiscountCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/5
 */
@Slf4j
@Service("ZK")
public class ZKCalculateService extends AbstractDiscountCalculateService {
    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupByDiscount) {
        log.info("优惠策略折扣计算：{}", groupByDiscount.getDiscountType().getCode());

        String marketExpr = groupByDiscount.getMarketExpr();

        BigDecimal payPrice = originalPrice.multiply(new BigDecimal(marketExpr));

        // 最低1分钱
        if (payPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal("0.01");
        }

        return payPrice;
    }
}
