package cn.picture.Servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;
import cn.picture.service.tranManager;


@WebServlet(urlPatterns="/passwordchange.jsp")
public class passwordchangeServlet extends BaseServlet
{
    public void service(HttpServletRequest request ,HttpServletResponse response)
    		throws IOException , ServletException
	{
    	request.setCharacterEncoding("utf-8");
		String pptelephone = request.getParameter("pptelephone");
		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("newpassword");
		// 获取系统的业务逻辑组件
		tranManager tranManager = (tranManager)getCtx().getBean("tranManager");
		// 验证用户登录
		int res= tranManager.passwordchange(pptelephone, oldpassword, newpassword);
		response.setContentType("text/html; charset=UTF-8");
		try
		{
			// 把验证的userId封装成JSONObject
			JSONObject jsonObj = new JSONObject().put("result" , res);
			// 输出响应
			response.getWriter().println(jsonObj.toString());
		}
		catch (JSONException ex)
		{
			ex.printStackTrace();
		}
	}
}
