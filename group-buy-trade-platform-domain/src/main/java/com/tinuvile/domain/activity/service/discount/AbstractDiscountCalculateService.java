package com.tinuvile.domain.activity.service.discount;


import com.tinuvile.domain.activity.adapter.repository.IActivityRepository;
import com.tinuvile.domain.activity.model.valobj.DiscountTypeEnum;
import com.tinuvile.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/5
 */
@Slf4j
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService {

    @Resource
    private IActivityRepository repository;

    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupByDiscount) {
        if (DiscountTypeEnum.TAG.equals(groupByDiscount.getDiscountType())) {
            boolean isCrowdRange = filterTagId(userId, groupByDiscount.getTagId());
            if (!isCrowdRange) {
                log.info("用户{}不在标签{}的人群范围内，无法享受优惠", userId, groupByDiscount.getTagId());
                return originalPrice;
            }
        }

        return doCalculate(originalPrice, groupByDiscount);
    }

    /**
     * TODO: 过滤用户标签，限定人群优惠
     * @description 过滤用户标签，限定人群优惠
     * @param userId 用户ID
     * @param tagId 标签ID
     * @return 是否符合标签
     */
    private boolean filterTagId(String userId, String tagId) {
        return repository.isTagCrowdRange(tagId, userId);
    }

    protected abstract BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupByDiscount);
}
