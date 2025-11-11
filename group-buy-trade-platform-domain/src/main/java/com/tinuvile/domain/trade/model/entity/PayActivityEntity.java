package com.tinuvile.domain.trade.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayActivityEntity {

    /** 拼单组队ID */
    private String teamId;

    /** 活动ID */
    private Long activityId;

    /** 活动名称 */
    private String activityName;

    /** 拼团开始时间 */
    private Date startTime;

    /** 拼团结束时间 */
    private Date endTime;

    /** 目标数量 */
    private Integer targetCount;

}
