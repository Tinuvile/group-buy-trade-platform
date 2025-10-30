package com.tinuvile.infrastructure.dao;

import com.tinuvile.infrastructure.dao.po.GroupBuyDiscount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tinuvile
 * @since 2025-10-30
 **/
@Mapper
public interface IGroupBuyDiscountDao {
    List<GroupBuyDiscount> queryGroupBuyDiscountList();
}
