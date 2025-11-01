package com.tinuvile.types.design.framework.tree;


/**
 * @description 策略映射器
 * @author Tinuvile
 * @since 2025/11/1
 */
public interface StrategyMapper<T, D, R> {
    StrategyHandler<T, D, R> get(T requestParameter, D dynamicContext);
}
