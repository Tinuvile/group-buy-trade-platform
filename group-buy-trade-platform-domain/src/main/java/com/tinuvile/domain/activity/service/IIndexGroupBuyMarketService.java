package com.tinuvile.domain.activity.service;


import com.tinuvile.domain.activity.model.entity.MarketProductEntity;
import com.tinuvile.domain.activity.model.entity.TrialBalanceEntity;
import com.tinuvile.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import com.tinuvile.domain.activity.model.valobj.TeamStatisticVO;

import java.util.List;

/**
 * @author Tinuvile
 * @description 首页营销服务接口
 * @since 2025/11/1
 */
public interface IIndexGroupBuyMarketService {

    TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception;

    /**
     * 查询进行中的拼团订单
     *
     * @param activityId 活动ID
     * @param userId 用户ID
     * @param ownerCount 个人数量
     * @param randomCount 随机数量
     * @return 用户拼团明细数据
     */
    List<UserGroupBuyOrderDetailEntity> queryInProcessUserGroupBuyOrderDetailList(Long activityId, String userId, Integer ownerCount, Integer randomCount);

     /**
     * 查询拼团队伍统计数据
     *
     * @param activityId 活动ID
     * @return 拼团队伍统计数据
     */
    TeamStatisticVO queryTeamStatisticByActivityId(Long activityId);

}
