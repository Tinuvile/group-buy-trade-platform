package com.tinuvile.api.dto;


import lombok.Data;

import java.util.List;

/**
 * @author Tinuvile
 * @description 回调请求对象
 * @since 2025/11/15
 */
@Data
public class NotifyRequestDTO {

    /** 组队ID */
    private String teamId;

    /** 外部单号 */
    private List<String> outTradeNoList;

}
