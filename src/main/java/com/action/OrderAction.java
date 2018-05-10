package com.action;

import com.opensymphony.xwork2.ActionSupport;
import com.po.*;
import com.service.OrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;
import util.DateUtil;
import util.ResponseUtil;
import util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
public class OrderAction extends ActionSupport implements ServletRequestAware {

    private HttpServletRequest request;
    @Resource
    private OrderService orderService;

    private String mainPage;

    private Order s_order;
    private List<Order> orderList;

    private int status;
    private String orderNo;

    private String page;
    private String rows;

    private String id;

    private String orderNos;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public String getMainPage() {
        return mainPage;
    }

    public void setMainPage(String mainPage) {
        this.mainPage = mainPage;
    }

    public Order getS_order() {
        return s_order;
    }

    public void setS_order(Order s_order) {
        this.s_order = s_order;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNos() {
        return orderNos;
    }

    public void setOrderNos(String orderNos) {
        this.orderNos = orderNos;
    }


//    购买购物车里的所有商品，清空购物车
    public String save() throws Exception {
        HttpSession session = request.getSession();
        User currentUser = (User)session.getAttribute("currentUser");
        //初始化订单的基本信息
        Order order = new Order();
        order.setUser(currentUser);
        order.setCreateTime(new Date());//设定订单时间
        order.setStatus(1);//设定订单的状态
        order.setOrderNo(DateUtil.getCurrentDateStr());//以当前时间的字符串形式作为订单号

        //向订单添加商品信息
        ShoppingCart shoppingCart = (ShoppingCart)session.getAttribute("shoppingCart");
        List<ShoppingCartItem> shoppingCartItemList = shoppingCart.getShoppingCartItems();
        List<OrderProduct> orderProductList = new LinkedList<>();
        double cost = 0;
        for(ShoppingCartItem shoppingCartItem : shoppingCartItemList){
            OrderProduct orderProduct = new OrderProduct();
            Product product = shoppingCartItem.getProduct();
            orderProduct.setNum(shoppingCartItem.getCount());
            orderProduct.setProduct(product);
            orderProduct.setOrder(order);

            cost += product.getPrice() * shoppingCartItem.getCount();
            orderProductList.add(orderProduct);
        }
        order.setCost(cost);
        order.setOrderProductList(orderProductList);

        orderService.save(order);
        mainPage = "shopping/shopping-result.jsp";

        session.removeAttribute("shoppingCart");

        return "success";
    }

//    查询个人订单信息
    public String findOrder() {
        HttpSession session = request.getSession();
        User currentUser = (User)session.getAttribute("currentUser");
        if(s_order == null){
            s_order = new Order();
        }
        s_order.setUser(currentUser);
//        也许可以加入分页管理？应该容易的
        orderList = orderService.findOrder(s_order, null);
        mainPage = "userCenter/orderList.jsp";
        return "orderList";
    }

//    更新订单状态
    public String confirmReceive() throws Exception {
        orderService.updateOrderStatus(status, orderNo);
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;
    }

//    后台显示订单信息
    public String list() throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        List<Order> orderList = orderService.findOrder(s_order, pageBean);
        Long total = orderService.getOrderCount(s_order);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor("yyyy-MM-dd"));
        jsonConfig.registerJsonValueProcessor(User.class, new ObjectJsonValueProcessor(new String[]{"id","name"}, User.class));
        JSONArray rows = JSONArray.fromObject(orderList, jsonConfig);
        JSONObject result = new JSONObject();
        result.put("rows", rows);
        result.put("total", total);
        ResponseUtil.write(ServletActionContext.getResponse(), result);
        return null;
    }
//    后台根据OrderId查询订单的详细信息
    public String findProductListByOrderId() throws Exception{
        if(StringUtil.isEmpty(id)){
            return null;
        }
        Order order = orderService.getOrderById(Integer.parseInt(id));
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        for(OrderProduct orderProduct : order.getOrderProductList()){
            Product product = orderProduct.getProduct();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("productName", product.getName());
            jsonObject.put("proPic", product.getProPic());
            jsonObject.put("price", product.getPrice());
            jsonObject.put("num", orderProduct.getNum());
            jsonObject.put("subtotal", product.getPrice() * orderProduct.getNum());
            rows.add(jsonObject);
        }
        result.put("rows",rows);
        result.put("total", rows.size());

        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;
    }

//    批量更新订单的状态
    public String modifyOrderStatus() throws Exception {
        String[] orderNoStr = orderNos.split(",");
        for(String orderNo : orderNoStr){
            orderService.updateOrderStatus(status, orderNo);
        }
        JSONObject result = new JSONObject();
        result.put("success", true);
        return null;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }
}
