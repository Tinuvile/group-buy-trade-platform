package com.tinuvile.infrastructure.adapter.repository;


import com.tinuvile.domain.activity.adapter.repository.IActivityRepository;
import com.tinuvile.domain.activity.model.valobj.DiscountTypeEnum;
import com.tinuvile.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.tinuvile.domain.activity.model.valobj.SCSkuActivityVO;
import com.tinuvile.domain.activity.model.valobj.SkuVO;
import com.tinuvile.infrastructure.dao.IGroupBuyActivityDao;
import com.tinuvile.infrastructure.dao.IGroupBuyDiscountDao;
import com.tinuvile.infrastructure.dao.ISCSkuActivityDao;
import com.tinuvile.infrastructure.dao.ISkuDao;
import com.tinuvile.infrastructure.dao.po.GroupBuyActivity;
import com.tinuvile.infrastructure.dao.po.GroupBuyDiscount;
import com.tinuvile.infrastructure.dao.po.SCSkuActivity;
import com.tinuvile.infrastructure.dao.po.Sku;
import com.tinuvile.infrastructure.dcc.DCCService;
import com.tinuvile.infrastructure.redis.IRedisService;
import org.redisson.api.RBitSet;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/3
 */
@Repository
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;

    @Resource
    private IGroupBuyDiscountDao groupBuyDiscountDao;

    @Resource
    private ISkuDao skuDao;

    @Resource
    private ISCSkuActivityDao skuActivityDao;

    @Resource
    private IRedisService redisService;

    @Resource
    private DCCService dccService;

    /**
     * 根据团购活动ID查询团购活动折扣信息
     *
     * @param activityId 团购活动ID
     * @return 团购活动折扣信息
     */
    @Override
    public GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(Long activityId) {

        GroupBuyActivity groupBuyActivityRes = groupBuyActivityDao.queryValidGroupBuyActivityId(activityId);

        if (null == groupBuyActivityRes) {
            return null;
        }

        String discountId = groupBuyActivityRes.getDiscountId();

        GroupBuyDiscount groupBuyDiscountRes = groupBuyDiscountDao.queryGroupBuyActivityDiscountByDiscountId(discountId);

        if (null == groupBuyDiscountRes) {
            return null;
        }

        GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount = GroupBuyActivityDiscountVO.GroupBuyDiscount.builder()
                .discountName(groupBuyDiscountRes.getDiscountName())
                .discountDesc(groupBuyDiscountRes.getDiscountDesc())
                .discountType(DiscountTypeEnum.get(groupBuyDiscountRes.getDiscountType()))
                .marketPlan(groupBuyDiscountRes.getMarketPlan())
                .marketExpr(groupBuyDiscountRes.getMarketExpr())
                .tagId(groupBuyDiscountRes.getTagId())
                .build();

        return GroupBuyActivityDiscountVO.builder()
                .activityId(groupBuyActivityRes.getActivityId())
                .activityName(groupBuyActivityRes.getActivityName())
                .groupBuyDiscount(groupBuyDiscount)
                .groupType(groupBuyActivityRes.getGroupType())
                .takeLimitCount(groupBuyActivityRes.getTakeLimitCount())
                .target(groupBuyActivityRes.getTarget())
                .validTime(groupBuyActivityRes.getValidTime())
                .status(groupBuyActivityRes.getStatus())
                .startTime(groupBuyActivityRes.getStartTime())
                .endTime(groupBuyActivityRes.getEndTime())
                .tagId(groupBuyActivityRes.getTagId())
                .tagScope(groupBuyActivityRes.getTagScope())
                .build();
    }

    @Override
    public SkuVO querySkuByGoodsId(String goodsId) {
        Sku sku = skuDao.querySkuByGoodsId(goodsId);

        if (null == sku) {
            return null;
        }

        return SkuVO.builder()
                .goodsId(sku.getGoodsId())
                .goodsName(sku.getGoodsName())
                .originalPrice(sku.getOriginalPrice())
                .build();
    }

    @Override
    public SCSkuActivityVO querySCSkuActivityByGoodsId(String source, String channel, String goodsId) {
        SCSkuActivity scSkuActivityReq = new SCSkuActivity();
        scSkuActivityReq.setSource(source);
        scSkuActivityReq.setChannel(channel);
        scSkuActivityReq.setGoodsId(goodsId);

        SCSkuActivity scSkuActivityRes = skuActivityDao.querySCSkuActivityBySCGoodsId(scSkuActivityReq);

        if (null == scSkuActivityRes) {
            return null;
        }

        return SCSkuActivityVO.builder()
                .source(scSkuActivityRes.getSource())
                .channel(scSkuActivityRes.getChannel())
                .activityId(scSkuActivityRes.getActivityId())
                .goodsId(scSkuActivityRes.getGoodsId())
                .build();
    }

    @Override
    public boolean isTagCrowdRange(String tagId, String userId) {
        RBitSet bitSet = redisService.getBitSet(tagId);
        if (!bitSet.isExists()) return true;
        return bitSet.get(redisService.getIndexFromUserId(userId));
    }

    @Override
    public boolean downgradeSwitch() {
        return dccService.isDowngradeSwitch();
    }

    @Override
    public boolean cutRange(String userId) {
        return dccService.isCutRange(userId);
    }
}
