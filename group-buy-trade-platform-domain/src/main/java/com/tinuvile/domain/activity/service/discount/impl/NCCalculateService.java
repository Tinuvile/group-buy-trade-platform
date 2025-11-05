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
@Service("NC")
public class NCCalculateService extends AbstractDiscountCalculateService {

    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupByDiscount) {
        log.info("优惠策略折扣计算：{}", groupByDiscount.getDiscountType().getCode());

        // N元购，表达式直接是优惠价格
        String marketExpr = groupByDiscount.getMarketExpr();

        return new BigDecimal(marketExpr);
    }
}
