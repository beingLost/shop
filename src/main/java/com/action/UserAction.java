package com.action;

import com.opensymphony.xwork2.ActionSupport;
import com.po.PageBean;
import com.po.User;
import com.service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.hibernate.Transaction;
import org.springframework.stereotype.Controller;
import util.ResponseUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserAction extends ActionSupport implements ServletRequestAware{

    private HttpServletRequest request;

    @Resource
    private UserService userService;

    private String userName;
    private User user;
    private String error;

    private String mainPage;

    private String imageCode;

    private User s_User;

    private String page;
    private String rows;

    private String ids;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMainPage() {
        return mainPage;
    }

    public void setMainPage(String mainPage) {
        this.mainPage = mainPage;
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public User getS_User() {
        return s_User;
    }

    public void setS_User(User s_User) {
        this.s_User = s_User;
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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Override
    public void setServletRequest(HttpServletRequest request){
        this.request = request;
    }


    public String existUserByUsername() throws Exception{
        boolean isExist = userService.isExistByName(userName);
        JSONObject result = new JSONObject();
        if(isExist){
            result.put("exist", true);
        }else{
            result.put("exist", false);
        }
        ResponseUtil.write(ServletActionContext.getResponse(),result);

        return null;
    }

    public String login(){
        HttpSession session = request.getSession();
        User currentUser = userService.login(user);
//        检测验证码是否正确
        if(!imageCode.equals(session.getAttribute("sRand"))){
            error="验证码错误！";
            if(user.getStatus() == 2){
//            返回管理员登录页面
                return "adminError";
            }else{
                return "error";
            }

        } else if(currentUser == null){
//        若没有返回正确的currentUser（id或密码错误）
            error="用户名或密码错误！";
            if(user.getStatus() == 2){
                return "adminError";
            }else{
                return "error";
            }
        } else{
            session.setAttribute("currentUser",currentUser);
        }
        if(user.getStatus() == 2){
            return "adminLogin";
        }else{
            return "login";
        }
    }

    public String register(){
        userService.saveUser(user);
        return "register_success";
    }

    public String logout(){
        request.getSession().invalidate();
        return "logout";
    }

    public String logout2(){
        request.getSession().invalidate();
        return "logout2";
    }

    public String userCenter(){
        mainPage = "userCenter/ucDefault.jsp";
        return "userCenter";
    }

    public String getUserInfo(){
        mainPage = "userCenter/userInfo.jsp";
        return "userCenter";
    }

//  在修改用户信息前先读取出currentUser的全部信息，
    public String preSave(){
        HttpSession session = request.getSession();
        user = (User)session.getAttribute("currentUser");
        mainPage = "userCenter/userSave.jsp";

        return "userCenter";
    }


    public String save(User user) {
        HttpSession session = request.getSession();
        userService.saveUser(user);
        session.setAttribute("currentUser", user);
        mainPage = "userCenter/userInfo.jsp";

        return "userCenter";
    }

//    读取全部用户的信息（管理员权限），且因为是easyUI框架，所以用JSON格式存储数据
    public String list() throws Exception{
        PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
        List<User> users = userService.findUserList(s_User,pageBean);
        long total = userService.getUserCount(s_User);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"orderList"});//存疑
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
        JSONArray rows = JSONArray.fromObject(users,jsonConfig);

        JSONObject result = new JSONObject();
        result.put("rows",rows);
        result.put("total",total);

        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;
    }

//    批量删除用户（管理员权限）
    public String deleteUser() throws Exception{
        JSONObject result = new JSONObject();
        String []idsStr = ids.split(",");
        for(int i = 0; i < idsStr.length; i++){
            User user = userService.getUserById(Integer.parseInt(idsStr[i]));
            userService.delete(user);
        }
        result.put("success",true);
        ResponseUtil.write(ServletActionContext.getResponse(), result);
        return null;
    }

//    更新用户信息
    public String saveUser() throws Exception{
        JSONObject result = new JSONObject();
        userService.saveUser(user);
        result.put("success", true);
        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;
    }

//    修改密码
    public String modifyPassword() throws Exception{
        JSONObject result = new JSONObject();
        User currentUser = userService.getUserById(user.getId());
        currentUser.setPassword(user.getPassword());
        userService.saveUser(currentUser);

        result.put("success", true);
        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;
    }



}
