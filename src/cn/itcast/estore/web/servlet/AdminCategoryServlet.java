package cn.itcast.estore.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.estore.domain.Category;
import cn.itcast.estore.service.CategoryService;
import cn.itcast.estore.utils.BaseServlet;
import cn.itcast.estore.utils.UUIDUtils;
/**
 * 后台分类管理的Servlet
 */
public class AdminCategoryServlet extends BaseServlet {

	/**
	 * 查询所有分类的方法:
	 */
	public String findAll(HttpServletRequest req,HttpServletResponse resp){
		// 调用业务层查询所有:
		CategoryService categoryService = new CategoryService();
		List<Category> list = categoryService.findAll();
		// 页面跳转:
		req.setAttribute("list", list);
		return "/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * 跳转到添加页面的方法:
	 */
	public String saveUI(HttpServletRequest req,HttpServletResponse resp){
		return "/adminjsps/admin/category/add.jsp";
	}
	
	/**
	 * 保存分类的方法
	 */
	public String save(HttpServletRequest req,HttpServletResponse resp){
		// 接收数据
		String cname = req.getParameter("cname");
		// 数据封装:
		Category category = new Category();
		category.setCid(UUIDUtils.getUUID());
		category.setCname(cname);
		// 调用业务层:
		CategoryService categoryService = new CategoryService();
		categoryService.save(category);
		// 页面跳转:
		return findAll(req,resp);
	}
	
	/**
	 * 编辑分类的方法:
	 */
	public String edit(HttpServletRequest req,HttpServletResponse resp){
		// 接收参数:
		String cid = req.getParameter("cid");
		// 调用业务层根据ID查询分类
		CategoryService categoryService = new CategoryService();
		Category category = categoryService.findByCid(cid);
		// 页面跳转
		req.setAttribute("category", category);
		return "/adminjsps/admin/category/mod.jsp";
	}
	
	/**
	 * 修改分类的方法:
	 */
	public String update(HttpServletRequest req,HttpServletResponse resp){
		// 接收参数:
		String cid = req.getParameter("cid");
		String cname = req.getParameter("cname");
		// 封装数据 :
		Category category = new Category();
		category.setCid(cid);
		category.setCname(cname);
		// 调用业务层修改分类:
		CategoryService categoryService = new CategoryService();
		categoryService.update(category);
		// 页面跳转:
		return findAll(req,resp);
	}
	
	/**
	 * 删除分类的方法:
	 */
	public String delete(HttpServletRequest req,HttpServletResponse resp){
		// 接收参数:
		String cid = req.getParameter("cid");
		// 调用业务层:
		CategoryService categoryService = new CategoryService();
		categoryService.delete(cid);
		// 页面跳转
		return findAll(req,resp);
	}
}
