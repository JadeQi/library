package cn.itcast.estore.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.estore.domain.Book;
import cn.itcast.estore.service.BookService;
import cn.itcast.estore.utils.BaseServlet;
/**
 * 图书管理的Servlet:
 */
public class BookServlet extends BaseServlet {

	/**
	 * 显示所有分类的图书:查询所有图书的方法findAll
	 */
	public String findAll(HttpServletRequest req,HttpServletResponse resp){
		/**
		 * 1.调用业务层:
		 * 2.页面跳转:
		 */
		// 1.调用业务层:
		BookService bookService = new BookService();
		List<Book> list = bookService.findAll();
		// 2.页面跳转:
		// 将list集合保存到request域中
		req.setAttribute("list", list);
		return "/jsps/book/list.jsp";
	}
	
	/**
	 * 显示某个分类的图书:findByCid
	 */
	public String findByCid(HttpServletRequest req,HttpServletResponse resp){
		
		/**
		 * 1.接收分类的ID:
		 * 2.调用业务层:
		 * 3.页面跳转:
		 */
		// 接收分类的ID
		String cid = req.getParameter("cid");
		// 调用业务层:
		BookService bookService = new BookService();
		List<Book> list = bookService.findByCid(cid);
		// 将list保存到request域中.
		req.setAttribute("list", list);
		// 页面跳转.
		return "/jsps/book/list.jsp";
	}
	
	/**
	 * 查询某个图书详情的方法:findByBid
	 */
	public String findByBid(HttpServletRequest req,HttpServletResponse resp){
		/**
		 * 1.接收图书的ID:bid
		 * 2.调用业务层:
		 * 3.页面跳转:
		 */
		// 接收图书的ID:
		String bid = req.getParameter("bid");
		// 调用业务层:
		BookService bookService = new BookService();
		Book book = bookService.findByBid(bid);
		// 将book保存到request域中.
		req.setAttribute("book", book);
		// 页面跳转:
		return "/jsps/book/desc.jsp";
	}
}
