package com.action;

import com.po.*;
import com.service.NewsService;
import com.service.NoticeService;
import com.service.ProductBigTypeService;
import com.service.ProductService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

@Component
public class InitAction implements ServletContextListener,ApplicationContextAware {
//ServletContextListener接口，监听全局
//ApplicationContextAware接口，获取Spring的所有bean
	private static ApplicationContext applicationContext;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// 单纯实现接口，无意义

	}

//	该类仅为application监听器，不是真正的action，使项目部署的时候直接能加载这些必要的数据（否则大类啊什么的在index页面不会正常显示）
//	可以考虑修改一下进入页面为这个action，然后跳转到index页面？
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		System.out.println("initAction running");

		ServletContext application=servletContextEvent.getServletContext();//ServletContext对象
//		加载大类
		ProductBigTypeService productBigTypeService=(ProductBigTypeService)applicationContext.getBean("productBigTypeService");
		List<ProductBigType> bigTypeList=productBigTypeService.findBigTypeList();
		application.setAttribute("bigTypeList", bigTypeList);
//		加载公告
		NoticeService noticeService=(NoticeService)applicationContext.getBean("noticeService");
		List<Notice> noticeList=noticeService.findNoticeList(null, new PageBean(1,7));
		application.setAttribute("noticeList", noticeList);
//		加载新闻
		NewsService newsService=(NewsService)applicationContext.getBean("newsService");
		List<News> newsList=newsService.findNewsList(null, new PageBean(1,7));
		application.setAttribute("newsList", newsList);
//		加载商品
		ProductService productService=(ProductService)applicationContext.getBean("productService");
//		加载特价
		Product s_product=new Product();
		s_product.setSpecialPrice(1);
		List<Product> specialPriceProductList=productService.findProductList(s_product, new PageBean(1,8));
		application.setAttribute("specialPriceProductList", specialPriceProductList);
//		加载热门
		s_product=new Product();
		s_product.setHot(1);
		List<Product> hotProductList=productService.findProductList(s_product, new PageBean(1,6));
		application.setAttribute("hotProductList", hotProductList);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
