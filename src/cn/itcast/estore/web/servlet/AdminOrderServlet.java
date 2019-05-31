package cn.itcast.estore.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

//import sun.org.mozilla.javascript.internal.json.JsonParser;

import cn.itcast.estore.domain.Order;
import cn.itcast.estore.domain.OrderItem;
import cn.itcast.estore.service.OrderService;
import cn.itcast.estore.utils.BaseServlet;
/**
 * 后台订单管理的Servlet:
 */
public class AdminOrderServlet extends BaseServlet {

	/**
	 * 查询所有的订单:
	 */
	public String findAll(HttpServletRequest req,HttpServletResponse resp){
		// 接收状态的参数:
		String value = req.getParameter("state");
		// 创建业务层的类
		OrderService orderService = new OrderService();
		// 定义List集合用于接收查询的数据:
		List<Order> list = null;
		// 判断状态的值是否为null:
		if(value == null || "".equals(value)){
			// 执行查询所有
			list = orderService.findAll();
		}else{
			int state = Integer.parseInt(value);
			// 执行按状态查询订单:
			list = orderService.findByState(state);
		}
		// 页面跳转:
		req.setAttribute("list", list);
		return "/adminjsps/admin/order/ajaxList.jsp";
	}
	
	/**
	 * 修改订单状态的方法:updateState
	 */
	public String updateState(HttpServletRequest req,HttpServletResponse resp){
		// 接收订单的ID.
		String oid = req.getParameter("oid");
		// 调用业务层:
		OrderService orderService = new OrderService();
		Order order = orderService.findByOid(oid);
		order.setState(3);
		orderService.update(order);
		// 页面跳转:
		return findAll(req,resp);
	}
	
	/**
	 * 显示订单的详情:
	 * @throws IOException 
	 */
	public String showDetail(HttpServletRequest req,HttpServletResponse resp) throws IOException{
		// 接收订单id.
		String oid = req.getParameter("oid");
		// 调用业务层查询订单中的订单项的集合.
		OrderService orderService = new OrderService();
		List<OrderItem> list = orderService.showDetail(oid);
		// 向页面输出就可以:
		// resp.setContentType("text/html;charset=UTF-8");
		// 转出JSON格式显示到页面:
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"itemid","order"});
		JSONArray jsonArray = JSONArray.fromObject(list,jsonConfig);
		// System.out.println(jsonArray.toString());
		resp.getWriter().print(jsonArray.toString());
		return null;
	}
}
