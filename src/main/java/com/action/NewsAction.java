package com.action;

import com.opensymphony.xwork2.ActionSupport;
import com.po.News;
import com.po.PageBean;
import com.service.NewsService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;
import util.ResponseUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

@Controller
public class NewsAction extends ActionSupport{


    @Resource
    NewsService newsService;

    private News news;
    private int newsId;
    private String mainPage;
    private String page;//当前页
    private String rows;//每页显示数量
    private News s_news;

    private String ids;

    public NewsService getNewsService() {
        return newsService;
    }

    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getMainPage() {
        return mainPage;
    }

    public void setMainPage(String mainPage) {
        this.mainPage = mainPage;
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

    public News getS_news() {
        return s_news;
    }

    public void setS_news(News s_news) {
        this.s_news = s_news;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }


    public String showNews() {
        news = newsService.getNewsById(newsId);
        mainPage = "news/newsDetails.jsp";

        return "success";
    }

    public String list() throws Exception{
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        List<News> newsList = newsService.findNewsList(s_news,pageBean);
        long total = newsService.getNewsCount(s_news);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor("yyyy-MM-dd"));
        JSONArray rows = JSONArray.fromObject(newsList, jsonConfig);

        JSONObject result = new JSONObject();
        result.put("rows",rows);
        result.put("total",total);

        ResponseUtil.write(ServletActionContext.getResponse(),result);

        return null;
    }

    public String save() throws Exception{
        if(news.getId() == 0){
            //说明该news对象是新建的，其余属性均为空
            news.setCreateTime(new Date());
        }
        newsService.save(news);
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;

    }

    public String delete() throws Exception{
        String[] idsStr = ids.split(",");
        for(String id : idsStr){
            News news = newsService.getNewsById(Integer.parseInt(id));
            newsService.delete(news);
        }
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(ServletActionContext.getResponse(), result);

        return null;
    }

}
