package com.tinuvile.domain.activity.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tinuvile
 * @description 队伍统计值对象
 * @since 2025/11/17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamStatisticVO {

    // 开团队伍数量
    private Integer allTeamCount;

    // 成团队伍数量
    private Integer allTeamCompleteCount;

    // 参团人数总量 - 一个商品的总参团人数
    private Integer allTeamUserCount;

}
