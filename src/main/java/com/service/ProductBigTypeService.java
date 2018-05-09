package com.service;


import com.dao.impl.BaseDAO;
import com.po.PageBean;
import com.po.ProductBigType;
import org.springframework.stereotype.Service;
import util.StringUtil;

import javax.annotation.Resource;
import java.util.List;

@Service("productBigTypeService")
public class ProductBigTypeService  {
    /*
    拥有方法：
    findAllBigTypeList()  返回所有的bigType
    findProductBigTypeList(s_ProductBigType, pageBean)   返回满足要求的bigType，根据pageBean是否为null确定是否分页
    getProductBigTypeCount(s_ProductBigType)   返回满足要求的bigType的数量
    saveProductBigType(productBigType)    save操作
    delete(productBigType)   delete操作
     */
    @Resource
    BaseDAO<ProductBigType> baseDAO;

    public List<ProductBigType> findBigTypeList() {
        return baseDAO.findList("from ProductBigType");
    }

    public List<ProductBigType> findBigTypeList(ProductBigType s_productBigType, PageBean pageBean){
        String hql = "from ProductBigType";
        if(s_productBigType != null){
            if(StringUtil.isNotEmpty(s_productBigType.getName())){
                hql += " where name like '%" + s_productBigType.getName() +"%'";
            }
        }
        if(pageBean != null){
            return baseDAO.findList(hql, pageBean);
        }else{
            return baseDAO.findList(hql);
        }
    }

    public Long getProductBigTypeCount(ProductBigType s_productBigType){
        String hql = "select count(*) from ProductBigType";
        if(s_productBigType != null){
            if(StringUtil.isNotEmpty(s_productBigType.getName())){
                hql += " where name like '%" + s_productBigType.getName() +"%'";
            }
        }
        return baseDAO.count(hql);
    }

    public void saveProductBigType(ProductBigType productBigType){
        baseDAO.merge(productBigType);
    }

    public void delete(ProductBigType productBigType){
        baseDAO.delete(productBigType);
    }


}
