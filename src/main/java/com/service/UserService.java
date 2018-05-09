package com.service;


import com.dao.UserDao;
import com.dao.impl.BaseDAO;
import com.po.PageBean;
import com.po.User;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.StringUtil;

import javax.servlet.annotation.ServletSecurity;
import java.util.ArrayList;
import java.util.List;


@Service("userService")
public class UserService {

    @Autowired
    private BaseDAO<User> baseDAO;


    public void saveUser(User user){
        baseDAO.merge(user);

    }

    public void delete(User user){
        baseDAO.delete(user);
    }


    //检查该用户名的用户是否存在
    public boolean isExistByName(String userName) {
        String hql = "select count(*) from User where userName = '" + userName +"'";
        Long count = baseDAO.count(hql);
        return (count>0);
    }

    public User getUserById(int id){
        return baseDAO.get(User.class, id);
    }

    //登录验证，若是管理员登录，还需要验证status属性
    public User login(User user) {
        List<Object> params = new ArrayList<>();
        String hql = "from User u where u.userName=? and u.password=?";
        if(user.getStatus()==2){
            hql += " and u.status = 2";
        }
        params.add(user.getUserName());
        params.add(user.getPassword());

        return baseDAO.find(hql, params);
    }

    //管理员用户管理部分，查询符合要求(一般是用户名中含有关键词的用户）的用户列表,且只能查找普通用户（非管理员）
    public List<User> findUserList(User s_user, PageBean pageBean) {
        if(pageBean == null)
            return null;
        String hql = "from User";
        if(s_user!=null){
            if(StringUtil.isNotEmpty(s_user.getUserName())){
                hql += " and UserName like '%" + s_user.getUserName() + "%'";
            }
        }
        hql += " and status = 1";
        hql = hql.replaceFirst("and", "where");

        return baseDAO.findList(hql);
    }

    public Long getUserCount(User s_user) {
        String hql = "seslect count(*) from User";
        if(s_user!=null){
            if(StringUtil.isNotEmpty(s_user.getUserName())){
                hql += " and userName like '%" + s_user.getUserName() +"%'";
            }
        }
        hql += " and status = 1";
        hql = hql.replaceFirst("and", "where");

        return baseDAO.count(hql);
    }

}
