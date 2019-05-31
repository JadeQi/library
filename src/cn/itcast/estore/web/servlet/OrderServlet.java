package cn.itcast.estore.web.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.estore.domain.Cart;
import cn.itcast.estore.domain.CartItem;
import cn.itcast.estore.domain.Order;
import cn.itcast.estore.domain.OrderItem;
import cn.itcast.estore.domain.User;
import cn.itcast.estore.service.OrderService;
import cn.itcast.estore.utils.BaseServlet;
import cn.itcast.estore.utils.PaymentUtil;
import cn.itcast.estore.utils.UUIDUtils;

/**
 * 订单模块的Servlet:
 */
public class OrderServlet extends BaseServlet {

	/**
	 * 生成订单的方法:saveOrder
	 */
	public String saveOrder(HttpServletRequest req, HttpServletResponse resp) {
		/**
		 * 封装Order对象: 调用业务层保存订单: 清空购物车: 页面跳转
		 */
		// 封装Order对象:
		Order order = new Order();
		order.setOid(UUIDUtils.getUUID());
		order.setOrdertime(new Date());
		order.setState(1); // 1:未付款.
		order.setAddress(null);
		// 订单总金额从购物车中获得的.
		// 获得购物车中的总计值.
		Cart cart = (Cart) req.getSession().getAttribute("cart");
		if (cart == null) {
			req.setAttribute("msg", "您还没有购物!不能产生订单!");
			return "/jsps/msg.jsp";
		}
		order.setTotal(cart.getTotal());
		// 设置订单所属用户的信息:
		User user = (User) req.getSession().getAttribute("existUser");
		if (user == null) {
			req.setAttribute("msg", "您还没有登录!不能生成订单!");
			return "/jsps/user/login.jsp";
		}
		order.setUser(user);
		// 设置订单项的集合:
		// 订单项的数据从购物项中获得:
		for (CartItem cartItem : cart.getCartItems()) {
			// 封装成订单项:
			OrderItem orderItem = new OrderItem();
			orderItem.setItemid(UUIDUtils.getUUID());
			orderItem.setBook(cartItem.getBook());
			orderItem.setCount(cartItem.getCount());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setOrder(order);

			order.getOrderItems().add(orderItem);
		}
		// 调用业务层保存订单:
		OrderService orderService = new OrderService();
		orderService.save(order);
		// 清空购物车:
		cart.clearCart();
		// 页面跳转 :
		// 将订单的对象保存到request域中.// 最好根据oid查询订单.
		order = orderService.findByOid(order.getOid());
		req.setAttribute("order", order);
		return "/jsps/order/desc.jsp";
	}

	/**
	 * 我的订单:根据用户的ID查询用户订单:findByUid
	 */
	public String findByUid(HttpServletRequest req, HttpServletResponse resp) {
		// 从session中获得用户的ID:
		User existUser = (User) req.getSession().getAttribute("existUser");
		// 调用业务层根据用户的ID查询用户的订单:
		OrderService orderService = new OrderService();
		List<Order> list = orderService.findByUid(existUser.getUid());
		// 页面跳转
		req.setAttribute("list", list);
		return "/jsps/order/list.jsp";
	}

	/**
	 * 根据订单的ID查询订单的方法:findByOid
	 */
	public String findByOid(HttpServletRequest req, HttpServletResponse resp) {
		// 接收oid:
		String oid = req.getParameter("oid");
		// 调用业务层:
		OrderService orderService = new OrderService();
		Order order = orderService.findByOid(oid);
		// 页面跳转
		req.setAttribute("order", order);
		return "/jsps/order/desc.jsp";
	}

	/**
	 * 为订单付款的执行的方法:payOrder
	 * @throws IOException 
	 */
	public String payOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/**
		 * 接收参数: 
		 * 调用易宝接口: 
		 * 页面跳转:
		 */
		// 接收参数:
		String oid = req.getParameter("oid");
		String address = req.getParameter("address");
		// 修改订单的地址:
		OrderService orderService = new OrderService();
		// orderService.update(address,oid);
		Order order = orderService.findByOid(oid);
		order.setAddress(address);
		orderService.update(order);
		
		String pd_FrpId = req.getParameter("pd_FrpId");
		// 调用易宝接口:
		// 组装参数:
		String p0_Cmd = "Buy"; // 业务类型
		String p1_MerId = "10001126856"; // 商户编号
		String p2_Order = oid; // 订单编号
		String p3_Amt = "0.01"; // 支付金额
		String p4_Cur = "CNY"; // 交易币种
		String p5_Pid = ""; // 商品名称
		String p6_Pcat = ""; // 商品种类
		String p7_Pdesc = ""; // 商品描述
		String p8_Url = "http://localhost:8080/estore/orderServlet?method=callBack"; // URL
		String p9_SAF = ""; // 送货地址
		String pa_MP = ""; // 商户扩展信息
		String pr_NeedResponse = "1"; // 应答机制
		String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue); // 签名数据
		
		// 完成重定向向易宝发送请求
		StringBuffer buffer = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
		buffer.append("p0_Cmd=").append(p0_Cmd).append("&");
		buffer.append("p1_MerId=").append(p1_MerId).append("&");
		buffer.append("p2_Order=").append(p2_Order).append("&");
		buffer.append("p3_Amt=").append(p3_Amt).append("&");
		buffer.append("p4_Cur=").append(p4_Cur).append("&");
		buffer.append("p5_Pid=").append(p5_Pid).append("&");
		buffer.append("p6_Pcat=").append(p6_Pcat).append("&");
		buffer.append("p7_Pdesc=").append(p7_Pdesc).append("&");
		buffer.append("p8_Url=").append(p8_Url).append("&");
		buffer.append("p9_SAF=").append(p9_SAF).append("&");
		buffer.append("pa_MP=").append(pa_MP).append("&");
		buffer.append("pd_FrpId=").append(pd_FrpId).append("&");
		buffer.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
		buffer.append("hmac=").append(hmac);
		
		// 完成重定向:
		resp.sendRedirect(buffer.toString());
		return null;
	}
	
	/**
	 * 付款成功后执行的方法:callBack
	 */
	public String callBack(HttpServletRequest req,HttpServletResponse resp){
		/**
		 * 接收参数:
		 * 修改订单状态:
		 * 页面跳转:
		 */
		// 接收参数:
		String p1_MerId = req.getParameter("p1_MerId");
		String r0_Cmd = req.getParameter("r0_Cmd");
		String r1_Code = req.getParameter("r1_Code");
		String r2_TrxId = req.getParameter("r2_TrxId");
		String r3_Amt = req.getParameter("r3_Amt");
		String r4_Cur = req.getParameter("r4_Cur");
		String r5_Pid = req.getParameter("r5_Pid");
		String r6_Order = req.getParameter("r6_Order");
		String r7_Uid = req.getParameter("r7_Uid");
		String r8_MP = req.getParameter("r8_MP");
		String r9_BType = req.getParameter("r9_BType");
		String hmac = req.getParameter("hmac");
		String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
		boolean flag = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
		if(flag == true){
			// 修改订单状态:
			OrderService orderService = new OrderService();
			Order order = orderService.findByOid(r6_Order);
			order.setState(2); // 2:已经付款.
			orderService.update(order);
			
			req.setAttribute("msg", "亲!您已经为订单: "+r6_Order+" 付款成功!付款金额为: "+r3_Amt);
			return "/jsps/msg.jsp";
		}
		return null;
	}
	
	/**
	 * 修改订单状态的方法:updateState
	 */
	public String updateState(HttpServletRequest req,HttpServletResponse resp){
		// 接收订单的id:
		String oid = req.getParameter("oid");
		// 调用业务层根据OID查询订单.
		OrderService orderService = new OrderService();
		Order order = orderService.findByOid(oid);
		order.setState(4); 
		orderService.update(order);
		// 页面跳转:
		return findByUid(req,resp);
	}
}
