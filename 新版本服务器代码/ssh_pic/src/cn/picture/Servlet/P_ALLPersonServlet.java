package cn.picture.Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.picture.service.tranManager;


//用于管理员获取所有图片
@WebServlet(urlPatterns="/Pallperson.jsp")
public class P_ALLPersonServlet extends BaseServlet
{
    public void service(HttpServletRequest request ,HttpServletResponse response)
    		throws IOException , ServletException
	{
    	request.setCharacterEncoding("utf-8");
		// 获取系统的业务逻辑组件
		tranManager tranManager = (tranManager)getCtx().getBean("tranManager");
		// 返回用户信息
		JSONArray jsonA= tranManager.P_getallPerson();
		response.setContentType("text/html; charset=UTF-8");
		// 输出响应
		response.getWriter().println(jsonA.toString());
	}
}