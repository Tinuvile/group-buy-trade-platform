package com.tinuvile.domain.activity.adapter.repository;


import com.tinuvile.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import com.tinuvile.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.tinuvile.domain.activity.model.valobj.SCSkuActivityVO;
import com.tinuvile.domain.activity.model.valobj.SkuVO;
import com.tinuvile.domain.activity.model.valobj.TeamStatisticVO;

import java.util.List;

/**
 * @author Tinuvile
 * @description 活动仓储
 * @since 2025/11/3
 */
public interface IActivityRepository {

    GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(Long activityId);

    SkuVO querySkuByGoodsId(String goodsId);

    SCSkuActivityVO querySCSkuActivityByGoodsId(String source, String channel, String goodsId);

    boolean isTagCrowdRange(String tagId, String userId);

    boolean downgradeSwitch();

    boolean cutRange(String userId);

    boolean isInWhiteList(String userId);

    List<UserGroupBuyOrderDetailEntity> queryInProgressUserGroupBuyOrderDetailListByOwner(Long activityId, String userId, Integer ownerCount);

    List<UserGroupBuyOrderDetailEntity> queryInProgressUserGroupBuyOrderDetailListByRandom(Long activityId, String userId, Integer randomCount);

    TeamStatisticVO queryTeamStatisticByActivityId(Long activityId);

}
