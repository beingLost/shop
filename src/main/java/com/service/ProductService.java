package com.service;

import com.dao.impl.BaseDAO;
import com.po.PageBean;
import com.po.Product;
import org.springframework.stereotype.Service;
import util.StringUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@Service("productService")
public class ProductService {

    @Resource
    private BaseDAO<Product> baseDAO;

//    根据商品名、所属大类、所属小类、特价、热卖的条件来搜索
    public List<Product> findProductList(Product s_product, PageBean pageBean){
        if(pageBean == null){
            return null;
        }
        String hql = "from Product";
        if(s_product != null){
            if(StringUtil.isNotEmpty(s_product.getName())){
                hql += " and name like '%" + s_product.getName() +"%'";
            }
            if(s_product.getBigType() != null){
                hql += " and bigType.id = " + s_product.getBigType().getId();
            }
            if(s_product.getSmallType() != null){
                hql += " and smallType.id = " + s_product.getSmallType().getId();
            }
            if(s_product.getSpecialPrice() == 1){
                hql += " and specialPrice = 1 order by specialPriceTime desc";
            }
            if(s_product.getHot() == 1){
                hql += " and hot = 1 order by hotTime desc";
            }
        }
        hql = hql.replaceFirst("and", "where");

        return baseDAO.findList(hql, pageBean);
    }
//    根据商品大类、小类、商品名相似 返回商品的数量
    public Long getProductCount(Product s_product){
        String hql = "select count(*) from Product";
        if(s_product != null){
            if(s_product.getBigType() != null){
                hql += " and bigType.id = " + s_product.getBigType().getId();
            }
            if(s_product.getSmallType() != null){
                hql += " and smallType.id = " + s_product.getSmallType().getId();
            }
            if(StringUtil.isNotEmpty(s_product.getName())){
                hql += " and name like '%" + s_product.getName() +"%'";
            }
        }
        hql = hql.replaceFirst("and", "where");

        return baseDAO.count(hql);
    }

    public Product getProductById(int id){
        return baseDAO.get(Product.class, id);
    }

    public void save(Product product){
        baseDAO.merge(product);
    }

    public void delete(Product product){
        baseDAO.delete(product);
    }

//    控制热卖，flag = 1表示设定热卖，0表示取消热卖
    public void setProductWithHot(int productId, int flag){
        Product product = baseDAO.get(Product.class, productId);
        product.setHot(flag);
        product.setHotTime(new Date());
        baseDAO.merge(product);//是否该用save来确保不会产生新的Product？
    }

//    同理控制特价
    public void setProductWithSpecialPrice(int productId, int flag){
        Product product = baseDAO.get(Product.class, productId);
        product.setSpecialPrice(flag);
        product.setSpecialPriceTime(new Date());
        baseDAO.merge(product);
    }


    public boolean existProductWithSmallTypeId(int smallTypeId){
        String hql = "from Product where smallType.id = " + smallTypeId;
        boolean flag = baseDAO.findList(hql).size() > 0;

        return flag;
    }


}
