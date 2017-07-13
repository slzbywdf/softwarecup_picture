package cn.picture.Servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns="/*")
public class Authority implements Filter
{
	public void init(FilterConfig config)throws ServletException
	{
	}
    public void doFilter(ServletRequest request,ServletResponse response, FilterChain chain) throws IOException , ServletException
	{
		HttpServletRequest hrequest = (HttpServletRequest)request;
		// 获取HttpSession对象
		HttpSession session = hrequest.getSession(true);
		String ptelephone = (String)session.getAttribute("pptelephone");
		// 如果用户已经登录，或用户正在登录,或用户正在注册
		if ((ptelephone != null && ptelephone.length() > 0)|| hrequest.getRequestURI().endsWith("/P_login.jsp")||hrequest.getRequestURI().endsWith("/P_register.jsp")|| hrequest.getRequestURI().endsWith("/login.jsp")||hrequest.getRequestURI().endsWith("/register.jsp")||hrequest.getRequestURI().startsWith("/ssh_pic/image")){
			// “放行”请求
			chain.doFilter(request , response);
		}
		else
		{
			response.setContentType("text/html; charset=UTF-8");
			// 生成错误提示。
			response.getWriter().println(hrequest.getRequestURI()+"您还没有登录系统，请先系统！");
		}
	}

    public void destroy()
    {
    }
}
