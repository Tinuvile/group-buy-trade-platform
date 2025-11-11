package com.tinuvile.types.design.framework.link.model1;


/**
 * @author Tinuvile
 * @description 封装添加节点和执行next节点的方法
 * @since 2025/11/11
 */
public abstract class AbstractLogicLink<T, D, R> implements ILogicLink<T, D, R> {

    private ILogicLink<T, D, R> next;

    @Override
    public ILogicLink<T, D, R> next() {
        return next;
    }

    @Override
    public ILogicLink<T, D, R> appendNext(ILogicLink<T, D, R> next) {
        this.next = next;
        return next;
    }

    protected R next(T requestParameter, D dynamicParameter) throws Exception {
        return next.apply(requestParameter, dynamicParameter);
    }

}
