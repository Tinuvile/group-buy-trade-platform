package com.tinuvile.api;


import com.tinuvile.api.dto.GoodsMarketRequestDTO;
import com.tinuvile.api.dto.GoodsMarketResponseDTO;
import com.tinuvile.api.response.Response;

/**
 * @author Tinuvile
 * @description 营销首页服务接口
 * @since 2025/11/17
 */
public interface IMarketIndexService {

    /**
     * 查询拼团营销配置
     *
     * @param goodsMarketRequestDTO 营销商品信息
     * @return 营销配置信息
     */
    Response<GoodsMarketResponseDTO> queryGroupBuyMarketConfig(GoodsMarketRequestDTO goodsMarketRequestDTO);

}
