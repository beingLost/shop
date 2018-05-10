package com.action;

import com.opensymphony.xwork2.ActionSupport;
import com.po.Product;
import com.po.ShoppingCart;
import com.po.ShoppingCartItem;
import com.po.User;
import com.service.ProductService;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import sun.security.provider.SHA;
import util.ResponseUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.PortUnreachableException;
import java.util.List;

public class ShoppingAction extends ActionSupport implements ServletRequestAware {


    private HttpServletRequest request;

    private String mainPage;

    private int count;

    @Resource
    private ProductService productService;

    private int productId;


    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getMainPage() {
        return mainPage;
    }

    public void setMainPage(String mainPage) {
        this.mainPage = mainPage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public void setServletRequest(HttpServletRequest request){
        this.request = request;
    }

//    整合了加入购物车和购买的共有代码
    public ShoppingCart updateShoppingCart(HttpSession session){
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        if (shoppingCart == null) {
            //购物车不存在，则新建一个
            User currentUser = (User) session.getAttribute("currentUser");
            shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(currentUser.getId());
        }

//        先判断这件商品是否已存在于购物车中，若存在则count+1,否则把该商品加入购物车,count=1
        List<ShoppingCartItem> shoppingCartItemList = shoppingCart.getShoppingCartItems();
        boolean flag = false;
        for (ShoppingCartItem shoppingCartItem : shoppingCartItemList) {
            if (productId == shoppingCartItem.getProduct().getId()) {
                shoppingCartItem.setCount(shoppingCartItem.getCount() + 1);
                flag = true;
                break;
            }
        }
        if (!flag) {
            Product product = productService.getProductById(productId);
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            shoppingCartItem.setCount(1);
            shoppingCartItem.setProduct(product);
            shoppingCartItemList.add(shoppingCartItem);
        }
        shoppingCart.setShoppingCartItems(shoppingCartItemList);

        return shoppingCart;
    }

//    向购物车添加商品,加入购物车按钮
    public String addShoppingCartItem() throws Exception {
        HttpSession session = request.getSession();

        ShoppingCart shoppingCart = updateShoppingCart(session);

        session.setAttribute("shoppingCart", shoppingCart);

        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;
    }

//    购买按钮
    public String buy() throws Exception {
        HttpSession session = request.getSession();

        ShoppingCart shoppingCart = updateShoppingCart(session);

        session.setAttribute("shoppingCart", shoppingCart);

        mainPage = "shopping/shopping.jsp";

        return "success";
    }


//    删除按钮
    public String removeShoppingCartItem() {
        HttpSession session = request.getSession();
        ShoppingCart shoppingCart = (ShoppingCart)session.getAttribute("shoppingCart");

        List<ShoppingCartItem> shoppingCartItemList = shoppingCart.getShoppingCartItems();
        for(int i=0; i<shoppingCartItemList.size(); i++){
            if(shoppingCartItemList.get(i).getProduct().getId() == productId){
                shoppingCartItemList.remove(i);
                break;
            }
        }
        shoppingCart.setShoppingCartItems(shoppingCartItemList);
        session.setAttribute("shoppingCart", shoppingCart);

        return "list";

    }

//    更新购物车中的商品在购物车页面更新的数量（点击加减按钮的时候就刷新了session并更新了数据库）效率感人
    public String updateShoppingCartItem() throws Exception {
        HttpSession session = request.getSession();
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        List<ShoppingCartItem> shoppingCartItemList = shoppingCart.getShoppingCartItems();
        for(ShoppingCartItem shoppingCartItem : shoppingCartItemList){
            if(shoppingCartItem.getProduct().getId() == productId){
                shoppingCartItem.setCount(count);
                break;
            }
        }
        shoppingCart.setShoppingCartItems(shoppingCartItemList);
        session.setAttribute("shoppingCart",shoppingCart);

        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;
    }

//    跳转至购物车页面
    public String list() throws Exception {
        mainPage = "shopping/shopping.jsp";

        return "success";
    }




}
