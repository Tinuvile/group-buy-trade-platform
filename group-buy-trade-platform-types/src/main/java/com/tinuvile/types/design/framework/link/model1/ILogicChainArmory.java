package com.tinuvile.types.design.framework.link.model1;


/**
 * @author Tinuvile
 * @description 装配链，提供添加节点方法和获取节点方法
 * @since 2025/11/11
 */
public interface ILogicChainArmory<T, D, R> {

    ILogicLink<T, D, R> next();

    // 将新节点添加到链条末端
    ILogicLink<T, D, R> appendNext(ILogicLink<T, D, R> next);

}
