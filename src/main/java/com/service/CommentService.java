package com.service;

import com.dao.CommentDao;
import com.dao.impl.BaseDAO;
import com.po.Comment;
import com.po.PageBean;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import util.StringUtil;

import javax.annotation.Resource;
import java.util.List;

@Service("commentService")
public class CommentService {

    @Resource
    private BaseDAO<Comment> baseDAO;

    //    返回完整的CommentList，或者含有关键词的CommentList
    public List<Comment> findCommentList(Comment s_Comment, PageBean pageBean) {
        if(pageBean == null)
            return null;

        String hql = "from Comment";
        if(s_Comment!=null){
            if(StringUtil.isNotEmpty(s_Comment.getContent())){
                hql += " where content like '%" + s_Comment.getContent() + "%'";
            }
        }
        hql += " order by createTime desc ";

        return baseDAO.findList(hql,pageBean);
    }

    //    返回已有评论的数量，或者是含有s_Comment里面一样关键词的评论数量
    public Long getCommentCount(Comment s_Comment) {
        String hql = "select count(*) from Comment";
        if(s_Comment!=null){
            if(StringUtil.isNotEmpty(s_Comment.getContent())){
                hql += " where content like '%" + s_Comment.getContent() + "%'";
            }
        }

        return baseDAO.count(hql);
    }

    public void saveComment(Comment comment) {
        baseDAO.merge(comment);
    }

    public Comment findCommentById(int commentId) {
        return baseDAO.get(Comment.class, commentId);
    }

    public void delete(Comment comment) {
        baseDAO.delete(comment);
    }

}
