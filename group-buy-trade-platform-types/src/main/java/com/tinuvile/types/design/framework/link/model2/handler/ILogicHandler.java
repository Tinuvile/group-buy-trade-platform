package com.tinuvile.types.design.framework.link.model2.handler;


/**
 * @author Tinuvile
 * @description 逻辑处理器
 * @since 2025/11/11
 */
public interface ILogicHandler<T, D, R> {

    default R next(T requestParameter, D dynamicParameter) {
        return null;
    }

    R apply(T requestParameter, D dynamicParameter) throws Exception;

}
