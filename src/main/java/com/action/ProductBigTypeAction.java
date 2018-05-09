package com.action;

import com.opensymphony.xwork2.ActionSupport;
import com.po.PageBean;
import com.po.ProductBigType;
import com.service.ProductBigTypeService;
import com.service.ProductSmallTypeService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;
import util.ResponseUtil;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class ProductBigTypeAction extends ActionSupport{

    private String page;
    private String rows;
    private ProductBigType s_productBitType;

    private String ids;

    private ProductBigType productBigType;

    @Resource
    private ProductBigTypeService productBigTypeService;

    @Resource
    private ProductSmallTypeService productSmallTypeService;

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

    public ProductBigType getS_productBitType() {
        return s_productBitType;
    }

    public void setS_productBitType(ProductBigType s_productBitType) {
        this.s_productBitType = s_productBitType;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public ProductBigType getProductBigType() {
        return productBigType;
    }

    public void setProductBigType(ProductBigType productBigType) {
        this.productBigType = productBigType;
    }

    public ProductBigTypeService getProductBigTypeService() {
        return productBigTypeService;
    }

    public void setProductBigTypeService(ProductBigTypeService productBigTypeService) {
        this.productBigTypeService = productBigTypeService;
    }

    public ProductSmallTypeService getProductSmallTypeService() {
        return productSmallTypeService;
    }

    public void setProductSmallTypeService(ProductSmallTypeService productSmallTypeService) {
        this.productSmallTypeService = productSmallTypeService;
    }

//    调在bigTypeManage.jsp的table的url属性里面，原理未知。
    public String list() throws Exception{
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        List<ProductBigType> productBigTypeList = productBigTypeService.findBigTypeList(s_productBitType,pageBean);
        Long total = productBigTypeService.getProductBigTypeCount(s_productBitType);

        JsonConfig jsonConfig = new JsonConfig();
        //过滤掉productList和smallTypeList，防止因为一对多关系产生的错误
        jsonConfig.setExcludes(new String[]{"productList", "smallTypeList"});

        JSONArray rows = JSONArray.fromObject(productBigTypeList, jsonConfig);
        JSONObject result = new JSONObject();
        result.put("rows",rows);
        result.put("total", total);

        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;
    }

//    设定商品或者小标签的大标签时需要的选择列表(下拉框里的内容)
    public String comboList() throws Exception {
        List<ProductBigType> productBigTypeList = productBigTypeService.findBigTypeList();//得到所有的大标签List

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id","");
        jsonObject.put("name","请选择...");
        jsonArray.add(jsonObject);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"productList","smallTypeList"});

        JSONArray rows = JSONArray.fromObject(productBigTypeList, jsonConfig);
        jsonArray.addAll(rows);

        ResponseUtil.write(ServletActionContext.getResponse(), jsonArray);

        return null;
    }

    public String save() throws Exception{
        productBigTypeService.saveProductBigType(productBigType);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success",true);

        ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);

        return null;
    }

    public String delete() throws Exception {
        JSONObject result = new JSONObject();
        String[] idsStr = ids.split(",");
        for(String idStr : idsStr){
            int id = Integer.parseInt(idStr);
            boolean flag = productSmallTypeService.existSmallTypeWithBigTypeId(id);
            if(flag){
                result.put("exist", "商品大类包含商品小类");
            }else{
                productBigTypeService.delete(productBigType);
            }
        }
        result.put("success",true);

        ResponseUtil.write(ServletActionContext.getResponse(), result);
        return null;

    }




}
