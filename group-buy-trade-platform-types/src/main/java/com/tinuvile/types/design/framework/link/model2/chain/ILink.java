package com.tinuvile.types.design.framework.link.model2.chain;


/**
 * @author Tinuvile
 * @description 多例链的设计需要解耦链路和执行，链路作为一个 LinkedList列表处理
 * @since 2025/11/11
 */
public interface ILink<E> {

    boolean add(E e);

    boolean addFirst(E e);

    boolean addLast(E e);

    boolean remove(Object o);

    E get(int index);

    void printLinkedList();

}
