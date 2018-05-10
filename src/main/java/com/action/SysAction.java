package com.action;

import com.opensymphony.xwork2.ActionSupport;
import com.po.*;
import com.service.NewsService;
import com.service.NoticeService;
import com.service.ProductBigTypeService;
import com.service.ProductService;
import com.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.springframework.stereotype.Controller;
import util.ResponseUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
public class SysAction extends ActionSupport implements ApplicationAware {

	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> application;
	
	@Resource
	private ProductBigTypeService productBigTypeService;
	
	@Resource
	private NoticeService noticeService;
	
	@Resource
	private NewsService newsService;
	
	@Resource
	private ProductService productService;

	@Override
	public void setApplication(Map<String, Object> application) {
		// TODO Auto-generated method stub
		this.application=application;
	}

//	用于管理员后台刷新系统缓存，也就是initAction加载的那些东西
	public String refreshSystem()throws Exception{
		System.out.println("systemAction running");
		List<ProductBigType> bigTypeList=productBigTypeService.findBigTypeList();
		application.put("bigTypeList", bigTypeList);
		
//		List<Tag> tagList=tagService.findTagList(null,null);
//		application.put("tagList", tagList);
		
		List<Notice> noticeList=noticeService.findNoticeList(null, new PageBean(1,7));
		application.put("noticeList", noticeList);
		
		List<News> newsList=newsService.findNewsList(null, new PageBean(1,7));
		application.put("newsList", newsList);
		
		Product s_product=new Product();
		s_product.setSpecialPrice(1);
		List<Product> specialPriceProductList=productService.findProductList(s_product, new PageBean(1,8));
		application.put("specialPriceProductList", specialPriceProductList);
		
		s_product=new Product();
		s_product.setHot(1);
		List<Product> hotProductList=productService.findProductList(s_product, new PageBean(1,6));
		application.put("hotProductList", hotProductList);
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(ServletActionContext.getResponse(), result);
		return null;
	}

}