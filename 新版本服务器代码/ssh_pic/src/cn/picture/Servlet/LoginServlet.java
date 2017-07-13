package cn.picture.Servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.*;

import cn.picture.schedule.pic_push;
import cn.picture.service.tranManager;


@WebServlet(urlPatterns="/login.jsp")
public class LoginServlet extends BaseServlet
{
    public void service(HttpServletRequest request ,HttpServletResponse response)
    		throws IOException , ServletException
	{
    	request.setCharacterEncoding("utf-8");
		String pptelephone = request.getParameter("pptelephone");
		String ppassword = request.getParameter("ppassword");
		// 获取系统的业务逻辑组件
		tranManager tranManager = (tranManager)getCtx().getBean("tranManager");
		// 验证用户登录
		String ptelephone= tranManager.validLogin(pptelephone , ppassword);
		response.setContentType("text/html; charset=UTF-8");
		// 登录成功
		if (!ptelephone.equals("-1"))
		{
			request.getSession(true).setAttribute("pptelephone" , ptelephone);
			System.out.print(request.getSession(true).getAttribute("pptelephone"));
		}
		try
		{
			// 把验证的userId封装成JSONObject
			JSONObject jsonObj = new JSONObject().put("pptelephone" , ptelephone);
			// 输出响应
			response.getWriter().println(jsonObj.toString());
		}
		catch (JSONException ex)
		{
			ex.printStackTrace();
		}
	}
}
