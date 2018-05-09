package com.service;


import com.dao.impl.BaseDAO;
import com.po.News;
import com.po.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.StringUtil;

import java.util.List;

@Service("newsService")
public class NewsService  {

    @Autowired
    BaseDAO<News> baseDAO;

    public List<News> findNewsList(News s_news, PageBean pageBean) {
        if(pageBean == null){
            return null;
        }
        String hql = "from News";
        if(s_news!=null){
            if(StringUtil.isNotEmpty(s_news.getTitle())){
                hql += " and title like '%" + s_news.getTitle() + "%'";
            }
        }
        hql = hql.replaceFirst("and", "where");

        return baseDAO.findList(hql,pageBean);

    }

    public News getNewsById(int newsId) {
        return baseDAO.get(News.class, newsId);
    }

    public Long getNewsCount(News s_news) {
        String hql = "select count(*) from News";
        if(s_news!=null){
            if(StringUtil.isNotEmpty(s_news.getTitle())){
                hql += " and title like '%" + s_news.getTitle() +"%'";
            }
        }
        hql = hql.replaceFirst("and", "where");
        return baseDAO.count(hql);
    }

    public void save(News news) {
        baseDAO.merge(news);
    }


    public void delete(News news) {
        baseDAO.delete(news);
    }





}
