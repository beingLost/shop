package com.dao;

import com.po.Comment;
import com.po.PageBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import util.StringUtil;

import java.util.List;

@Repository("commentDao")

@SuppressWarnings("all")
public class CommentDao extends BaseDao {
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
    Session session;//记得在service里面关闭

    public CommentDao(){
        super();
//        getCurrentSession()和openSession()的区别，
// 在于open是直接打开一个新的session，get是先获取当前线程里的session，没有在新建
//        此处该用get还是open存疑
        this.session = super.getCurrentSession();
    }

    public void closeSession(){
        if(session != null)
            session.close();
    }

//    返回完整的CommentList，或者含有关键词的CommentList
    public List<Comment> findList(Comment s_Comment, PageBean pageBean) {

        if(pageBean == null)
            return null;

//        List<Object> param=new LinkedList<Object>();
        String hql = "from Comment";
        if(s_Comment!=null){
            if(StringUtil.isNotEmpty(s_Comment.getContent())){
//                hql += " and content like ?";
//                param.add("%"+s_Comment.getContent()+"%");
                hql += "where content like '%" + s_Comment.getContent() + "%'";
            }
        }
        hql += " order by createTime desc ";
//            return baseDAO.find(hql.toString().replaceFirst("and", "where"), param, pageBean);

//        开始一个session
//        Session session = super.getCurrentSession();
        Query q = session.createQuery(hql);

        List<Comment> commentList = null;
        commentList = q.setFirstResult(pageBean.getStart()).setMaxResults(pageBean.getPageSize()).list();

//        完成后session需关闭
//        session.close();
        return commentList;
    }

//    返回已有评论的数量，或者是含有s_Comment里面一样关键词的评论数量
    public Long getCount(Comment s_Comment) {
        String hql = "select count(*) from Comment";
//        List<Object> param=new LinkedList<Object>();
//        StringBuffer hql=new StringBuffer("select count(*) from Comment");
        if(s_Comment!=null){
            if(StringUtil.isNotEmpty(s_Comment.getContent())){
                hql += "where content like '%" + s_Comment.getContent() + "%'";
//                hql.append(" and content like ?");
//                param.add("%"+s_Comment.getContent()+"%");
            }
        }
//        Session session = super.getCurrentSession();

        Query q = session.createQuery(hql);
        Long count = (Long)q.uniqueResult();

//        session.close();

        return count;
    }

    public void save(Comment comment) {
//        Session session = super.getCurrentSession();
        session.merge(comment);
//        session.close();

//        baseDAO.merge(comment);
    }


    public Comment getById(int commentId) {
//        Session session = super.getCurrentSession();
        Comment comment = (Comment)session.get(Comment.class, commentId);
//        session.close();

        return comment;
    }

    public void delete(Comment comment) {
//        baseDAO.delete(comment);
//        Session session = super.getCurrentSession();
        session.delete(comment);

//        session.close();
    }




}
