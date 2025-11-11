package com.tinuvile.domain.trade.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyProcessVO {

    /** 目标数量 */
    private Integer targetCount;

    /** 完成数量 */
    private Integer completeCount;

    /** 锁单数量 */
    private Integer lockCount;

}
