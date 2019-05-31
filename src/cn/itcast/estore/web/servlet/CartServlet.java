package cn.itcast.estore.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.estore.domain.Book;
import cn.itcast.estore.domain.Cart;
import cn.itcast.estore.domain.CartItem;
import cn.itcast.estore.service.BookService;
import cn.itcast.estore.utils.BaseServlet;
/**
 * 购物模块的Servlet:
 */
public class CartServlet extends BaseServlet {

	/**
	 * 获得购物车的方法:
	 */
	public Cart getCart(HttpServletRequest req){
		Cart cart = (Cart) req.getSession().getAttribute("cart");
		if(cart == null){
			cart = new Cart();
			req.getSession().setAttribute("cart", cart);
		}
		return cart;
	}
	
	/**
	 * 将购物项添加到购物的方法:addCart
	 * @return
	 */
	public String addCart(HttpServletRequest req,HttpServletResponse resp){
		/**
		 * 1.接收参数:图书id,购买的数量
		 * 2.封装CartItem的对象
		 * 3.调用购物车中的addCart
		 * 4.页面跳转
		 */
		// 接收参数:
		String bid = req.getParameter("bid");
		int count = Integer.parseInt(req.getParameter("count"));
		// 根据图书的ID查询图书:
		BookService bookService = new BookService();
		Book book = bookService.findByBid(bid);
		// 封装成CartItem对象:
		CartItem cartItem = new CartItem();
		cartItem.setCount(count);
		cartItem.setBook(book);
		// 调用Cart对象的addCart(CartItem cartItem);
		// 从session中获得购物车的信息.
		Cart cart = getCart(req);
		cart.addCart(cartItem);
		// 页面跳转
		return "/jsps/cart/list.jsp";
	}
	
	/**
	 * 清空购物车的方法:clearCart
	 */
	public String clearCart(HttpServletRequest req,HttpServletResponse resp){
		/**
		 * 1.获得购物车对象.
		 * 2.调用购物车中的方法.
		 * 3.页面跳转
		 */
		// 获得购物车对象:
		Cart cart = getCart(req);
		// 调用cart中clearCart的方法清空购物车.
		cart.clearCart();
		// 页面跳转
		return "/jsps/cart/list.jsp";
	}
	
	/**
	 * 从购物车中移除购物项的方法:removeCart
	 */
	public String removeCart(HttpServletRequest req,HttpServletResponse resp){
		/**
		 * 1.接收图书的ID.
		 * 2.获得购物车的对象.
		 * 3.调用购物车的方法.
		 * 4.页面跳转.
		 */
		// 接收图书的ID.
		String bid = req.getParameter("bid");
		// 获得购物车对象:
		Cart cart = getCart(req);
		// 调用购物车中移除方法:
		cart.removeCart(bid);
		// 页面跳转
		return "/jsps/cart/list.jsp";
	}
}
