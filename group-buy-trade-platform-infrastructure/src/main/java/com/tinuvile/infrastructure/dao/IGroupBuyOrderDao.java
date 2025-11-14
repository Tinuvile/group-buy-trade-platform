package com.tinuvile.infrastructure.dao;


import com.tinuvile.infrastructure.dao.po.GroupBuyOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tinuvile
 * @description 用户拼单
 * @since 2025/11/11
 */
@Mapper
public interface IGroupBuyOrderDao {

    void insert(GroupBuyOrder groupBuyOrder);

    int updateAddLockCount(String teamId);

    int updateAddCompleteCount(String teamId);

    int updateSubtractionLockCount(String teamId);

    Integer updateOrderStatus2COMPLETE(String teamId);

    GroupBuyOrder queryGroupBuyProgress(String teamId);

    GroupBuyOrder queryGroupBuyTeamByTeamId(String teamId);

}
