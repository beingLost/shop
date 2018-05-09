package com.po;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product {
    private int id;
    private String description;
    private int hot;
    private Date hotTime;
    private String name;
    private int price;
    private String proPic;
    private int specialPrice;
    private Date specialPriceTime;
    private int stock;

    private ProductBigType bigType;
    private ProductSmallType smallType;
    private List<OrderProduct> orderProductList=new ArrayList<OrderProduct>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public Date getHotTime() {
        return hotTime;
    }

    public void setHotTime(Date hotTime) {
        this.hotTime = hotTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProPic() {
        return proPic;
    }

    public void setProPic(String proPic) {
        this.proPic = proPic;
    }

    public int getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(int specialPrice) {
        this.specialPrice = specialPrice;
    }

    public Date getSpecialPriceTime() {
        return specialPriceTime;
    }

    public void setSpecialPriceTime(Date specialPriceTime) {
        this.specialPriceTime = specialPriceTime;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ProductBigType getBigType() {
        return bigType;
    }

    public void setBigType(ProductBigType bigType) {
        this.bigType = bigType;
    }

    public ProductSmallType getSmallType() {
        return smallType;
    }

    public void setSmallType(ProductSmallType smallType) {
        this.smallType = smallType;
    }

    public List<OrderProduct> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderProduct> orderProductList) {
        this.orderProductList = orderProductList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (hot != product.hot) return false;
        if (price != product.price) return false;
        if (specialPrice != product.specialPrice) return false;
        if (stock != product.stock) return false;
        if (description != null ? !description.equals(product.description) : product.description != null)
            return false;
        if (hotTime != null ? !hotTime.equals(product.hotTime) : product.hotTime != null) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (proPic != null ? !proPic.equals(product.proPic) : product.proPic != null) return false;
        if (specialPriceTime != null ? !specialPriceTime.equals(product.specialPriceTime) : product.specialPriceTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + hot;
        result = 31 * result + (hotTime != null ? hotTime.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + price;
        result = 31 * result + (proPic != null ? proPic.hashCode() : 0);
        result = 31 * result + specialPrice;
        result = 31 * result + (specialPriceTime != null ? specialPriceTime.hashCode() : 0);
        result = 31 * result + stock;
        return result;
    }
}
