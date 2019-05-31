package cn.itcast.estore.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import cn.itcast.estore.domain.User;
import cn.itcast.estore.service.UserService;
import cn.itcast.estore.utils.BaseServlet;
/**
 * 用户管理的Servlet:前台
 */
public class UserServlet extends BaseServlet {

	/**
	 * 编写执行的方法:regist
	 */
	public String regist(HttpServletRequest req,HttpServletResponse resp){
		// System.out.println("regist方法执行了...");
		/**
		 * 1.接收数据:
		 * 2.封装数据:
		 * 3.调用业务层处理数据:
		 * 4.根据处理结果进行页面跳转:
		 */
		// 1.接收数据:
		Map<String,String[]> map = req.getParameterMap();
		// 2.封装数据:
		User user = new User();
		try {
			BeanUtils.populate(user, map);
			// 3.调用业务层处理数据:
			UserService userService = new UserService();
			userService.regist(user);
			// 4.进行页面跳转:
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		req.setAttribute("msg", "用户注册成功,请去邮箱激活!");
		return "/jsps/msg.jsp";
	}
	
	/**
	 * 异步校验用户的执行的方法:checkUsername
	 * @throws IOException 
	 */
	public String checkUsername(HttpServletRequest req,HttpServletResponse resp) throws IOException{
		// 接收用户名参数:
		String username = req.getParameter("username");
		// 调用业务层根据用户名查询用户:
		UserService userService = new UserService();
		User user = userService.findByUsername(username);
		if(user== null){
			// 用户名可以使用
			resp.getWriter().println(1);
		}else{
			// 用户名不可以使用
			resp.getWriter().println(2);
		}
		return null;
	}
	
	/**
	 * 用户激活的方法:active
	 */
	public String active(HttpServletRequest req,HttpServletResponse resp){
		// 接收激活码:
		String code = req.getParameter("code");
		// 调用业务层根据激活码查询用户:
		UserService userService = new UserService();
		User user = userService.findByCode(code);
		// 判断用户是否存在:
		if(user != null){
			// 修改用户的状态,同时将激活码置为null
			user.setState(1);
			user.setCode(null);
			userService.update(user);
			req.setAttribute("msg", "激活成功,可以登录!");
		}else{
			// 直接保存信息跳转页面
			req.setAttribute("msg", "激活码不正确!");
		}
		return "/jsps/msg.jsp";
	}
	
	/**
	 * 用户登录的方法:login
	 * @throws IOException 
	 */
	public String login(HttpServletRequest req,HttpServletResponse resp) throws IOException{
		/**
		 * 1.接收数据:
		 * 2.封装数据:
		 * 3.调用业务层处理数据:
		 * 4.根据处理结果页面跳转:
		 */
		// 接收数据
		Map<String,String[]> map = req.getParameterMap();
		// 封装数据
		User user = new User();
		try {
			BeanUtils.populate(user, map);
			// 调用业务层处理数据:
			UserService userService = new UserService();
			User existUser = userService.login(user);
			// 根据处理结果跳转页面:
			if(existUser == null){
				// 登录失败
				req.setAttribute("msg", "用户名或密码错误或用户未激活!");
				return "/jsps/msg.jsp";
			}else{
				// 登录成功
				// 将登录的用户的信息存入到session.
				req.getSession().setAttribute("existUser", existUser);
				resp.sendRedirect(req.getContextPath()+"/jsps/main.jsp");
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 用户退出的方法:logout
	 * @throws IOException 
	 */
	public String logout(HttpServletRequest req,HttpServletResponse resp) throws IOException{
		/**
		 * 1.获得session的信息.
		 * 2.销毁session的信息.
		 * 3.页面跳转:
		 */
		// 获得session:
		HttpSession session = req.getSession();
		// 销毁session的信息:调用session的invalidate方法.
		session.invalidate();
		// 页面跳转:
		resp.sendRedirect(req.getContextPath()+"/jsps/main.jsp");
		return null;
	}
}
