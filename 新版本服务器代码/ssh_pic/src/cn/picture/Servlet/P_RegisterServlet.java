package cn.picture.Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import cn.picture.service.tranManager;

@WebServlet(urlPatterns="/P_register.jsp")
public class P_RegisterServlet extends BaseServlet
{
    public void service(HttpServletRequest request ,HttpServletResponse response)
    		throws IOException , ServletException
	{
    	request.setCharacterEncoding("gbk");
		String pptelephone = request.getParameter("pptelephone");
		String ppassword = request.getParameter("ppassword");
		// 获取系统的业务逻辑组件
		tranManager tranManager = (tranManager)getCtx().getBean("tranManager");
		// 验证用户注册
		String reg_answer= tranManager.register1(pptelephone , ppassword);
		response.setContentType("text/html; charset=UTF-8");
		// 注册成功
		try
		{
			// 把验证的userId封装成JSONObject
			JSONObject jsonObj = new JSONObject().put("reg_answer" ,reg_answer);
			// 输出响应
			response.getWriter().println(jsonObj.toString());
		}
		catch (JSONException ex)
		{
			ex.printStackTrace();
		}
	}
}