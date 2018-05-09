package com.action;

import com.opensymphony.xwork2.ActionSupport;
import com.po.PageBean;
import com.po.ProductBigType;
import com.po.ProductSmallType;
import com.service.ProductService;
import com.service.ProductSmallTypeService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.struts2.ServletActionContext;
import util.ResponseUtil;

import javax.annotation.Resource;
import java.util.List;

public class ProductSmallTypeAction extends ActionSupport {

    private String page;
    private String rows;
    private ProductSmallType s_ProductSmallType;

    private String ids;

    private ProductSmallType productSmallType;

    @Resource
    private ProductSmallTypeService productSmallTypeService;

    @Resource
    private ProductService productService;


    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public ProductSmallType getS_ProductSmallType() {
        return s_ProductSmallType;
    }

    public void setS_ProductSmallType(ProductSmallType s_ProductSmallType) {
        this.s_ProductSmallType = s_ProductSmallType;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public ProductSmallType getProductSmallType() {
        return productSmallType;
    }

    public void setProductSmallType(ProductSmallType productSmallType) {
        this.productSmallType = productSmallType;
    }

    public ProductSmallTypeService getProductSmallTypeService() {
        return productSmallTypeService;
    }

    public void setProductSmallTypeService(ProductSmallTypeService productSmallTypeService) {
        this.productSmallTypeService = productSmallTypeService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }


    public String list() throws Exception{
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        List<ProductSmallType> productSmallTypeList = productSmallTypeService.findProductSmallTypeList(s_ProductSmallType,pageBean);
        Long total = productSmallTypeService.getProductSmallTypeCount(s_ProductSmallType);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"productList"});
        jsonConfig.registerJsonValueProcessor(ProductBigType.class, new ObjectJsonValueProcessor(new String[]{"id","name"}, ProductBigType.class));
        JSONArray rows = JSONArray.fromObject(productSmallTypeList, jsonConfig);

        JSONObject result = new JSONObject();
        result.put("rows", rows);
        result.put("total",total);

        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;
    }

    public String comboList() throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id","");
        jsonObject.put("name","请选择...");
        jsonArray.add(jsonObject);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"productList","bigTypeList"});
        List<ProductSmallType> productSmallTypeList = productSmallTypeService.findProductSmallTypeList(s_ProductSmallType,null);
        JSONArray rows = JSONArray.fromObject(productSmallTypeList, jsonConfig);
        jsonArray.addAll(rows);

        ResponseUtil.write(ServletActionContext.getResponse(), jsonArray);
        return null;
    }

    public String save() throws Exception{
        productSmallTypeService.saveProductSmallType(productSmallType);
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;
    }

    public String delete() throws Exception {
        JSONObject result = new JSONObject();
        String idsStr[] = ids.split(",");
        for(String idStr : idsStr){
            int id = Integer.parseInt(idStr);
            if(productService.existProductWithSmallTypeId(id)){
                result.put("exist","商品小类包含商品");
            }else{
                ProductSmallType productSmallType = productSmallTypeService.getProductSmallTypeById(id);
                productSmallTypeService.delete(productSmallType);
            }
        }
        result.put("success",true);
        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;


    }





}
