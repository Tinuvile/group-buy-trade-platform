package com.tinuvile.types.design.framework.tree;


/**
 * @description 策略处理器
 * @author Tinuvile
 * @since 2025/11/1
 */
public interface StrategyHandler<T, D, R> {
    StrategyHandler DEFAULT = (T, D) -> null;

    R apply(T requestParameter, D dynamicContext) throws Exception;
}
