package com.tinuvile.api;


import com.tinuvile.api.dto.LockMarketPayOrderRequestDTO;
import com.tinuvile.api.dto.LockMarketPayOrderResponseDTO;
import com.tinuvile.api.dto.SettlementMarketPayOrderRequestDTO;
import com.tinuvile.api.dto.SettlementMarketPayOrderResponseDTO;
import com.tinuvile.api.response.Response;

/**
 * @author Tinuvile
 * @description 营销交易服务接口
 * @since 2025/11/11
 */
public interface IMarketTradeService {

    /**
     * 锁定营销支付订单
     * @param requestDTO 锁单商品信息
     * @return 锁单结果信息
     */
    Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(LockMarketPayOrderRequestDTO requestDTO);

    /**
     * 结算营销支付订单
     * @param requestDTO 结算商品信息
     * @return 结算结果信息
     */
    Response<SettlementMarketPayOrderResponseDTO> settlementMarketPayOrder(SettlementMarketPayOrderRequestDTO requestDTO);

}
