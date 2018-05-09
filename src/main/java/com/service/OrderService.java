package com.service;


import com.dao.impl.BaseDAO;
import com.po.Order;
import com.po.PageBean;
import com.po.User;
import org.springframework.stereotype.Service;
import util.StringUtil;

import java.util.List;

@Service("orderService")
public class OrderService {

    BaseDAO<Order> baseDAO;

    public List<Order> findOrder(Order s_order, PageBean pageBean){
        String hql = "from Order";
        if(s_order != null){
            if(s_order.getUser() != null){
                User user = s_order.getUser();
                if(user.getId() != 0){
                    hql += " and user.id = " + user.getId();
                }
                if(StringUtil.isNotEmpty(user.getUserName())){
                    hql += " abd user.userName like '%"+  user.getUserName() +"%'";
                }
                if(StringUtil.isNotEmpty(s_order.getOrderNo())){
                    hql += " and orderNo like '%" + s_order.getOrderNo() +"%'";
                }
            }
        }
        hql += " order by createTime desc";
        hql = hql.replaceFirst("and" , "where");

        if(pageBean != null){
            return baseDAO.findList(hql,pageBean);
        }else{
            return baseDAO.findList(hql);
        }
    }

    public void updateOrderStatus(int status, String orderNo){
        String hql = "update Order set status = " + status + " where orderNo = " + orderNo;

        baseDAO.executeHql(hql);
    }

    public Long getOrderCount(Order s_order){
        String hql = "select count(*) from Order";
        if(s_order != null){
            if(s_order.getUser() != null){
                User user = s_order.getUser();
                if(user.getId() != 0){
                    hql += " and user.id = " + user.getId();
                }
                if(StringUtil.isNotEmpty(user.getUserName())){
                    hql += " abd user.userName like '%"+  user.getUserName() +"%'";
                }
                if(StringUtil.isNotEmpty(s_order.getOrderNo())){
                    hql += " and orderNo like '%" + s_order.getOrderNo() +"%'";
                }
            }
        }
        hql = hql.replaceFirst("and", "where" );

        return baseDAO.count(hql);
    }

    public Order getOrderById(int id){
        return baseDAO.get(Order.class, id);
    }

    public void save(Order order){
        //源码使用了save，why？
        baseDAO.merge(order);
    }

}
