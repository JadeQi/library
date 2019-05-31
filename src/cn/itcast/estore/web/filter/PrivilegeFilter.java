package cn.itcast.estore.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.itcast.estore.domain.User;

/**
 * 权限过滤器
 */
public class PrivilegeFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		// 获得session中的用户信息:
		User existUser = (User) req.getSession().getAttribute("existUser");
		if(existUser == null){
			// 没有登录过
			req.setAttribute("msg", "您还没有登录!没有权限访问");
			req.getRequestDispatcher("/jsps/msg.jsp").forward(req, response);
			return;
		}else{
			// 已经登录过
			chain.doFilter(req, response);
		}
	}


	@Override
	public void destroy() {
		
	}

}
