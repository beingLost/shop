package com.po;

import java.util.ArrayList;
import java.util.List;

public class ProductBigType {
    private int id;
    private String name;
    private String remarks;

    private List<Product> productList=new ArrayList<Product>();
    private List<ProductSmallType> smallTypeList=new ArrayList<ProductSmallType>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<ProductSmallType> getSmallTypeList() {
        return smallTypeList;
    }

    public void setSmallTypeList(List<ProductSmallType> smallTypeList) {
        this.smallTypeList = smallTypeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductBigType productBigType = (ProductBigType) o;

        if (id != productBigType.id) return false;
        if (name != null ? !name.equals(productBigType.name) : productBigType.name != null) return false;
        if (remarks != null ? !remarks.equals(productBigType.remarks) : productBigType.remarks != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        return result;
    }
}
