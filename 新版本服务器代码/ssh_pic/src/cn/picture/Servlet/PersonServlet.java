package cn.picture.Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import cn.picture.service.tranManager;

@WebServlet(urlPatterns="/person.jsp")
public class PersonServlet extends BaseServlet
{
    public void service(HttpServletRequest request ,HttpServletResponse response)
    		throws IOException , ServletException
	{
    	request.setCharacterEncoding("utf-8");
		String pptelephone = request.getParameter("pptelephone");
		// 获取系统的业务逻辑组件
		tranManager tranManager = (tranManager)getCtx().getBean("tranManager");
		// 返回用户信息
		JSONObject jsonObj= tranManager.getPerson(pptelephone);
		response.setContentType("text/html; charset=UTF-8");
		// 输出响应
		response.getWriter().println(jsonObj.toString());
	}
}