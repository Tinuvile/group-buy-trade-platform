package com.tinuvile.domain.activity.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/7
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TagScopeEnumVO {
    VISIBLE(true, false, "是否可见拼团"),
    ENABLE(true, false, "是否可参与拼团")
    ;

    private Boolean allow;
    private Boolean refuse;
    private String desc;
}
