package com.tinuvile.domain.activity.service.discount.impl;


import com.tinuvile.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.tinuvile.domain.activity.service.discount.AbstractDiscountCalculateService;
import com.tinuvile.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/5
 */
@Slf4j
@Service("MJ")
public class MJCalculateService extends AbstractDiscountCalculateService {

    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupByDiscount) {
        log.info("优惠策略折扣计算：{}", groupByDiscount.getDiscountType().getCode());

        // 折扣表达式
        String marketExpr = groupByDiscount.getMarketExpr();
        String[] splits = marketExpr.split(Constants.SPLIT);
        BigDecimal x = new BigDecimal(splits[0].trim());
        BigDecimal y = new BigDecimal(splits[1].trim());

        if (originalPrice.compareTo(x) < 0) {
            return originalPrice;
        }

        BigDecimal payPrice = originalPrice.subtract(y);

        if (payPrice.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        return payPrice;
    }
}
