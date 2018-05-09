package com.dao.impl;


import com.po.PageBean;

import java.io.Serializable;
import java.util.List;

/*

用到的基本操作：
合并对象merge（T O）
删除对象delete(T 0)
查询单个对象 T find(String hql)
根据逐渐找单个对象 T get(Class<T> c,Serializable id)
查询数量 Long count(String hql)
查询集合 List<T> findList(String hql)
查询集合带分页 List<T> findList(String hql, PageBean pageBean)

 */
public interface BaseDAO<T> {

    void merge(T o);

    void delete(T o);

    void executeHql(String hql);

    Long count(String hql);

    T find(String hql);

    T find(String hql, List<Object> params);

    T get(Class<T> c,Serializable id);

    List<T> findList(String hql);

    List<T> findList(String hql, PageBean pageBean);

    List<T> findList(String hql, List<Object> params);

    List<T> findList(String hql, List<Object> params, PageBean pageBean);

}
