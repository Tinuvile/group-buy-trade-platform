package com.tinuvile.infrastructure.dao;


import com.tinuvile.infrastructure.dao.po.SCSkuActivity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/6
 */
@Mapper
public interface ISCSkuActivityDao {

    SCSkuActivity querySCSkuActivityBySCGoodsId(SCSkuActivity skuActivity);

}
