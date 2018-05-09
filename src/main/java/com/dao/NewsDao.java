package com.dao;


import com.po.News;
import com.po.PageBean;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import util.StringUtil;


import java.util.List;

@Repository("newsDao")
public class NewsDao extends BaseDao{
//    做个实验
//    此处不用private session，每次都getCurrentSession

    public List<News> findList(News s_news, PageBean pageBean) {
        if(pageBean == null){
            return null;
        }
        String hql = "from News";

        if(s_news!=null){
            if(StringUtil.isNotEmpty(s_news.getTitle())){
                hql += " and title like '%" + s_news.getTitle() + "%'";
            }
        }
        hql.replaceFirst("and", "where");

        Query q = super.getCurrentSession().createQuery(hql);
        List<News> newss = (List<News>)q.setFirstResult(pageBean.getStart()).setMaxResults(pageBean.getPageSize());
        return newss;

    }

    public News getById(int newsId) {
        News news = (News)super.getCurrentSession().get(News.class, newsId);

        return news;
    }

    public Long getCount(News s_news) {
        String hql = "select count(*) from News";

        if(s_news!=null){
            if(StringUtil.isNotEmpty(s_news.getTitle())){
                hql += " and title like '%" + s_news.getTitle() +"%'";
            }
        }
        hql.replaceFirst("and", "where");

        Query q = super.getCurrentSession().createQuery(hql);
        Long count = (Long)q.uniqueResult();
        return count;
    }

    public void save(News news) {
        super.getCurrentSession().merge(news);
    }

    public void delete(News news) {
        super.getCurrentSession().delete(news);
    }



}
