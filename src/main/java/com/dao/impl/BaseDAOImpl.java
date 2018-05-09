package com.dao.impl;

import com.po.PageBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository("baseDAO")
public class BaseDAOImpl<T>  implements BaseDAO<T>{

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    private Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void merge(T o) {
        this.getCurrentSession().merge(o);
    }

    @Override
    public void delete(T o) {
        this.getCurrentSession().delete(o);
    }

    @Override
    public void executeHql(String hql){
        Query q = this.getCurrentSession().createQuery(hql);

        q.executeUpdate();
    }

    @Override
    public Long count(String hql) {
        Query q = this.getCurrentSession().createQuery(hql);
        return (Long) q.uniqueResult();
    }

    @Override
    public T find(String hql) {
        Query q = this.getCurrentSession().createQuery(hql);
        return (T)q.uniqueResult();
    }

    @Override
    public T find(String hql, List<Object> params) {
        Query q = this.getCurrentSession().createQuery(hql);
        for(int i = 0; i < params.size(); i++){
            q.setParameter(i,params.get(i));
        }
        return (T) q.uniqueResult();
    }

    @Override
    public T get(Class<T> c, Serializable id){
        return (T)this.getCurrentSession().get(c,id);
    }

    @Override
    public List<T> findList(String hql) {
        Query q = this.getCurrentSession().createQuery(hql);

        return q.list();
    }

    @Override
    public List<T> findList(String hql, PageBean pageBean) {
        Query q = this.getCurrentSession().createQuery(hql);

        return q.setFirstResult(pageBean.getStart()).setMaxResults(pageBean.getPageSize()).list();
    }

    @Override
    public List<T> findList(String hql, List<Object> params) {
        Query q = this.getCurrentSession().createQuery(hql);
        for(int i = 0; i < params.size(); i++){
            q.setParameter(i, params.get(i));
        }
        return q.list();
    }

    @Override
    public List<T> findList(String hql, List<Object> params, PageBean pageBean) {
        Query q = this.getCurrentSession().createQuery(hql);
        for(int i = 0; i < params.size(); i++){
            q.setParameter(i, params.get(i));
        }
        return q.setFirstResult(pageBean.getStart()).setMaxResults(pageBean.getPageSize()).list();
    }
}
