package com.tinuvile.api;


import com.tinuvile.api.response.Response;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/7
 */
public interface IDCCService {

    Response<Boolean> updateConfig(String key, String value);

}
