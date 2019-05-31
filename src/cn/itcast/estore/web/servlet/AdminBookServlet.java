package cn.itcast.estore.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.estore.domain.Book;
import cn.itcast.estore.domain.Category;
import cn.itcast.estore.service.BookService;
import cn.itcast.estore.service.CategoryService;
import cn.itcast.estore.utils.BaseServlet;
/**
 * 后台图书管理的Servlet:
 */
public class AdminBookServlet extends BaseServlet {

	/**
	 * 后台查询所有图书的方法;
	 */
	public String findAll(HttpServletRequest req,HttpServletResponse resp){
		// 调用业务层:
		BookService bookService = new BookService();
		List<Book> list = bookService.findAll();
		// 页面跳转:
		req.setAttribute("list", list);
		return "/adminjsps/admin/book/list.jsp";
	}
	
	/**
	 * 跳转到添加页面的方法:
	 */
	public String saveUI(HttpServletRequest req,HttpServletResponse resp){
		// 查询所有分类
		CategoryService categoryService = new CategoryService();
		List<Category> list = categoryService.findAll();
		// 页面跳转
		req.setAttribute("list", list);
		return "/adminjsps/admin/book/add.jsp";
	}
	
	/**
	 * 跳转到编辑页面的方法:
	 */
	public String edit(HttpServletRequest req,HttpServletResponse resp){
		// 接收数据:
		String bid = req.getParameter("bid");
		// 调用业务层根据图书的ID查询图书:
		BookService bookService = new BookService();
		Book book = bookService.findByBid(bid);
		// 查询所有分类:
		CategoryService categoryService = new CategoryService();
		List<Category> list = categoryService.findAll();
		// 页面跳转
		req.setAttribute("book", book);
		req.setAttribute("list", list);
		return "/adminjsps/admin/book/desc.jsp";
	}
	
	/**
	 * 图书下架的方法:
	 */
	public String pushDown(HttpServletRequest req,HttpServletResponse resp){
		// 接收图书的ID.
		String bid = req.getParameter("bid");
		// 调用业务层:查询图书,修改图书的状态.
		BookService bookService = new BookService();
		Book book = bookService.findByBid(bid);
		book.setIsdel(1); // 0:图书上架,  1:图书下架
		bookService.update(book);
		// 页面跳转 :
		return findAll(req,resp);
	}
	
	/**
	 * 跳转到图书上架页面:显示所有已经下架的图书:
	 */
	public String pushUpUI(HttpServletRequest req,HttpServletResponse resp){
		// 调用业务层:查询所有已经下架的图书.
		BookService bookService = new BookService();
		List<Book> list = bookService.findAllPushDown();
		// 页面跳转:
		req.setAttribute("list", list);
		return "/adminjsps/admin/book/pushUp.jsp";
	}
	
	/**
	 * 图书上架的方法:
	 */
	public String pushUp(HttpServletRequest req,HttpServletResponse resp){
		// 接收图书ID.
		String bid = req.getParameter("bid");
		// 调用业务层:
		BookService bookService = new BookService();
		Book book = bookService.findByBid(bid);
		book.setIsdel(0); // 0:已上架.
		bookService.update(book);
		// 页面跳转:
		return findAll(req,resp);
	}
}
