package com.tinuvile.api;


import com.tinuvile.api.dto.LockMarketPayOrderRequestDTO;
import com.tinuvile.api.dto.LockMarketPayOrderResponseDTO;
import com.tinuvile.api.response.Response;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/11
 */
public interface IMarketTradeService {

    Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO);

}
