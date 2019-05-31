package cn.itcast.estore.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.estore.domain.Category;
import cn.itcast.estore.service.CategoryService;
import cn.itcast.estore.utils.BaseServlet;
/**
 * 分类管理的Servlet:前台
 */
public class CategoryServlet extends BaseServlet {

	/**
	 * 查询所有分类的方法:findAll
	 */
	public String findAll(HttpServletRequest req,HttpServletResponse resp){
		/**
		 * 1.调用业务层:
		 * 2.页面跳转:
		 */
		// 1.调用业务层:
		CategoryService categoryService = new CategoryService();
		List<Category> list = categoryService.findAll();
		// 2.页面跳转:
		// 将list存入到request域.
		req.setAttribute("list", list);
		return "/jsps/left.jsp";
	}
}
