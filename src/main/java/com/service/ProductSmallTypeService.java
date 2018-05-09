package com.service;


import com.dao.impl.BaseDAO;
import com.po.PageBean;
import com.po.ProductSmallType;
import org.springframework.stereotype.Service;
import util.StringUtil;

import javax.annotation.Resource;
import java.util.List;

@Service("productSmallTypeService")
public class ProductSmallTypeService {
/*
    findProductSmallTypeList(s_type,pageBean)返回符合需求的smallType的List（名字相似或者属于某个bigType）
    existSmallTypeWithBigTypeId(int bigTypeId) 查询是否存在属于该bigType的SmallType
    getProductSmallTypeCount(s_type)返回符合需求的smallType的数量（名字相似）
    getProductSmallTypeById(id) 根据smallType的id返回一个smallType对象
    saveProductSmallType(smallType)
    delete(smallType)
 */
    @Resource
    private BaseDAO<ProductSmallType> baseDAO;

    public List<ProductSmallType> findProductSmallTypeList(ProductSmallType s_productSmallType, PageBean pageBean){
        String hql = "from ProductSmallType";
        if(s_productSmallType != null){
            if(StringUtil.isNotEmpty(s_productSmallType.getName())){
                hql += " and name like '%" + s_productSmallType.getName() +"%'";
            }
            if(s_productSmallType.getBigType() != null && s_productSmallType.getBigType().getId() != 0){
                hql += " and bigType.id = " + s_productSmallType.getBigType().getId();
            }
        }
        if(pageBean != null){
            return baseDAO.findList(hql, pageBean);
        }else{
            return baseDAO.findList(hql);
        }
    }

    public boolean existSmallTypeWithBigTypeId(int bigTypeId){
        String hql = "from ProductSmallType where bigType.id = " + bigTypeId;

        boolean flag = baseDAO.findList(hql).size() > 0;
        return flag;
    }

    public Long getProductSmallTypeCount(ProductSmallType productSmallType){
        String hql = "select count(*) from ProductSmallType";
        if(productSmallType != null){
            if(StringUtil.isNotEmpty(productSmallType.getName())){
                hql += "  where name like '%" + productSmallType.getName() + "%'";
            }
        }
        return baseDAO.count(hql);
    }

    public ProductSmallType getProductSmallTypeById(int id){
        return baseDAO.get(ProductSmallType.class, id);
    }

    public void saveProductSmallType(ProductSmallType productSmallType){
        baseDAO.merge(productSmallType);
    }

    public void delete(ProductSmallType productSmallType){
        baseDAO.delete(productSmallType);
    }



}
