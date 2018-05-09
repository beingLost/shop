package com.action;

import com.dao.CommentDao;
import com.opensymphony.xwork2.ActionSupport;
import com.po.Comment;
import com.po.PageBean;
import com.service.CommentService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;
import util.PageUtil;
import util.ResponseUtil;
import util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class CommentAction extends ActionSupport implements ServletRequestAware {


    private HttpServletRequest request;
    /**@Resource 的执行过程
     * 执行过程：
     * 1.启动加载spring容器
     * 2.将Spring容器中的bean实例化（Person，student）
     * 3.当spring容器执行到解析配置文件处时
     *          <context:annotation-config/>
     *      spring容器会在纳入spring管理的bean的范围内查找哪些类的属性上是否加有@Resource注解
     * 4.如果在属性上找到@Resource注解
     *      如果@Resource的注解name属性为""
     *          则把@Resource所在的属性的名称和Spring容器中的id作匹配
     *              如果匹配成功，则赋值
     *              如果匹配不成功，则会按照类型进行配置
     *                  如果匹配成功，则赋值；匹配不成功，报错
     *      如果@Resource的注解的name的值不为""
     *          则解析@Resource注解name属性的值，把值和spring容器中的ID进行呢匹配
     *              如果匹配成功，则赋值
     *              如果匹配不成功，则报错
     *
     *  说明：
     *      注解代码越来越简单，效率越来越低
     */
    @Resource
    private CommentService commentService;

    private List<Comment> commentList;

    private String page;//当前页
    private String rows;//当前页的commentList（JSON格式）
    private Long total;//全部评论的数量(Count)
    private String pageCode;//翻页按钮的jspCode

    private Comment s_Comment;//用于查询的Comment对象（一般用于根据内容搜索）

    private Comment comment;//从service里面得到的单个comment对象

    private int commentId;//service里面查询得到的commentId

    private String ids;//查到的多个commentId组合的ids（用，分割）



    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public Comment getS_Comment() {
        return s_Comment;
    }

    public void setS_Comment(Comment s_Comment) {
        this.s_Comment = s_Comment;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }


    //加载commentList，用于首次调用，加载pageCode，total等
    public String list() {
        if(StringUtil.isEmpty(page)){
            page = "1";
        }
        PageBean pageBean = new PageBean(Integer.parseInt(page), 3);
        commentList = commentService.findCommentList(s_Comment,pageBean);
        total = commentService.getCommentCount(s_Comment);
        pageCode = PageUtil.genPaginationNoParam(request.getContextPath()+"/comment_list.action",total, Integer.parseInt(page), 3);



        return "success";

    }

    //修改每页显示的评论数量
    public String listComment() throws Exception{
        PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
        List<Comment> commentList =commentService.findCommentList(s_Comment, pageBean);
        Long total = commentService.getCommentCount(s_Comment);

        //jsonArray的格式控制器
        JsonConfig jsonConfig = new JsonConfig();
        //注册一个JsonValueProcessor，其中覆盖了接口两个方法,控制Date在JSONArray中的输出格式
        jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
        //以jsonConfig的格式把commentList存入JSONArray中
        JSONArray rows = JSONArray.fromObject(commentList,jsonConfig);
        JSONObject result = new JSONObject();
        result.put("rows", rows);
        result.put("total",total);

        //把result写进response里？存疑
        ResponseUtil.write(ServletActionContext.getResponse(),result);

        return null;

    }

    //发评论
    public String save(){
        if(comment == null){
            return "error";
        }
        if(comment.getCreateTime()==null){
            comment.setCreateTime(new Date());
        }
        commentService.saveComment(comment);

        return "save";
    }

    //根据commentId删评论，可一次性删多条
    public String delete() throws Exception{
        JSONObject result = new JSONObject();
        String[] idStr = ids.split(",");
        for(String idS : idStr){
            int id = Integer.parseInt(idS);
            Comment comment_toDel = commentService.findCommentById(id);
            commentService.delete(comment_toDel);
        }
        result.put("success",true);
        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;

    }

    //commentManage.jsp
    //后台回复
    public String replay()throws Exception{
        comment.setReplyTime(new Date());
        commentService.saveComment(comment);
        JSONObject result=new JSONObject();
        result.put("success", true);
        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;
    }



    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }


}
