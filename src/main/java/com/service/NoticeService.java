package com.service;

import com.dao.impl.BaseDAO;
import com.opensymphony.xwork2.ActionSupport;
import com.po.Notice;
import com.po.PageBean;
import org.springframework.stereotype.Service;
import util.StringUtil;

import javax.annotation.Resource;
import java.util.List;

@Service("noticeService")
public class NoticeService extends ActionSupport {

    @Resource
    private BaseDAO<Notice> baseDAO;

    public BaseDAO<Notice> getBaseDAO() {
        return baseDAO;
    }

    public void setBaseDAO(BaseDAO<Notice> baseDAO) {
        this.baseDAO = baseDAO;
    }

    public List<Notice> findNoticeList(Notice s_notice, PageBean pageBean) {
        if(pageBean == null){
            return null;
        }
        String hql = "from Notice";
        if(s_notice!=null){
            if(StringUtil.isNotEmpty(s_notice.getTitle())){
                hql += " and title like '%" + s_notice.getTitle() + "%'";
            }
        }
        hql = hql.replaceFirst("and", "where");

        return baseDAO.findList(hql,pageBean);
    }

    public Notice getNoticeById(int noticeId) {
        return baseDAO.get(Notice.class, noticeId);
    }

    public Long getNoticeCount(Notice s_notice) {
        String hql = "select count(*) from News";
        if(s_notice!=null){
            if(StringUtil.isNotEmpty(s_notice.getTitle())){
                hql += " and title like '%" + s_notice.getTitle() +"%'";
            }
        }
        hql = hql.replaceFirst("and", "where");
        return baseDAO.count(hql);
    }

    public void saveNotice(Notice notice) {
        baseDAO.merge(notice);
    }

    public void delete(Notice notice) {
        baseDAO.delete(notice);
    }




}
