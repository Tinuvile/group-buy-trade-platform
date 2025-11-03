package com.tinuvile.infrastructure.dao;


import com.tinuvile.infrastructure.dao.po.Sku;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/3
 */
@Mapper
public interface ISkuDao {
    Sku querySkuByGoodsId(String goodsId);
}
