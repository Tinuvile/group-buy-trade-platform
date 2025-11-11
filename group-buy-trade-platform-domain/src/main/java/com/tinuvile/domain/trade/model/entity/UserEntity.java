package com.tinuvile.domain.trade.model.entity;


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
public class UserEntity {

    /** 用户ID */
    private String userId;

}
