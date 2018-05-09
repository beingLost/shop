package com.dao;

import com.po.PageBean;
import com.po.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import util.StringUtil;

import java.util.List;

@Repository("userDao")
public class UserDao extends BaseDao {

    Session session;

    public UserDao(){
        super();
        session = super.getCurrentSession();
    }

    public void closeSession(){
        session.close();
    }


    public void save(User user){
        session.merge(user);
    }

    public void delete(User user){
        session.delete(user);
    }

    //检查该用户名的用户是否存在
    public boolean isExistByName(String userName) {
        String hql="select count(*) from User where userName=?";
        Query q = session.createQuery(hql);
        long count = (long)q.uniqueResult();
        if(count>0){
            return true;
        }else{
            return false;
        }
    }

    public User getUserById(int id){
        String hql = "from User u where u.id = :id";
        Query q = session.createQuery(hql);
        User user = (User)q.uniqueResult();
        return user;
    }

    //登录验证，若是管理员登录，还需要验证status属性
    public User login(User user) {
        String hql = "from User u where u.userName=:name and u.password=:pwd";
        if(user.getStatus()==2){
            hql += " and u.status = 2";
        }
        Query q = session.createQuery(hql);
        q.setParameter("name",user.getUserName());
        q.setParameter("pwd",user.getPassword());

        User currentUser = (User)q.uniqueResult();

        return currentUser;
    }

    //管理员用户管理部分，查询符合要求(一般是用户名中含有关键词的用户）的用户列表,却只能查找普通用户（非管理员）
    public List<User> findUserList(User s_user, PageBean pageBean) {
        String hql = "from User";
        if(s_user!=null){
            if(StringUtil.isNotEmpty(s_user.getUserName())){
                hql += " and UserName like '%" + s_user.getUserName() + "%'";
            }
        }
//        hql.append(" and status=1");
        hql += " and status = 1";
        hql = hql.replaceFirst("and", "where");

        Query q = session.createQuery(hql);
        List<User> users = q.setFirstResult(pageBean.getStart()).setMaxResults(pageBean.getPageSize()).list();

        return users;


    }

    public Long getUserCount(User s_user) {
        String hql = "seslect count(*) from User";
        if(s_user!=null){
            if(StringUtil.isNotEmpty(s_user.getUserName())){
                hql += " and userName like '%" + s_user.getUserName() +"%'";
            }
        }
        hql += " and status = 1";
        hql.replaceFirst("and", "where");

        Query q = session.createQuery(hql);
        long count = (long)q.uniqueResult();
        return count;
    }





}
