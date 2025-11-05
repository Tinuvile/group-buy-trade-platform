package com.tinuvile.domain.activity.service.discount;


import com.tinuvile.domain.activity.model.valobj.DiscountTypeEnum;
import com.tinuvile.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.math.BigDecimal;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/5
 */
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService {

    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupByDiscount) {
        if (DiscountTypeEnum.TAG.equals(groupByDiscount.getDiscountType())) {
            boolean isCrowdRange = filterTagId(userId, groupByDiscount.getTagId());
            if (!isCrowdRange) {
                return originalPrice;
            }
        }

        return doCalculate(originalPrice, groupByDiscount);
    }

    /**
     * TODO: 过滤用户标签，限定人群优惠
     * @description 过滤用户标签，限定人群优惠
     * @param userId 用户ID
     * @param TagId 标签ID
     * @return 是否符合标签
     */
    private boolean filterTagId(String userId, String TagId) {
        return true;
    }

    protected abstract BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupByDiscount);
}
