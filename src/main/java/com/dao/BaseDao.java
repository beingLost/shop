package com.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//Spring注解，标注数据访问组件即DAO组件
@Repository("baseDao")
//Spring注解，使编译器不显示警告（方法未使用、List未进行参数化设置之类的不会影响运行的警告）
@SuppressWarnings("all")
public class BaseDao {

    private static SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

//    让Spring可以注入
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    protected void closeSession() {}
}
