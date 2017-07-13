package cn.picture.Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

import cn.picture.service.tranManager;

@WebServlet(urlPatterns="/changemessage.jsp")
public class ChangemessageServlet extends BaseServlet
{
    public void service(HttpServletRequest request ,HttpServletResponse response)
    		throws IOException , ServletException
	{
    	request.setCharacterEncoding("utf-8");
		String pptelephone = request.getParameter("pptelephone");
		String pnick = request.getParameter("pnick");
		System.out.print(pnick);
		String pemail = request.getParameter("pemail");
		String pmajor = request.getParameter("pmajor");
		String pinter = request.getParameter("pinter");
		int icon =Integer.parseInt(request.getParameter("icon"));
		// 获取系统的业务逻辑组件
		tranManager tranManager = (tranManager)getCtx().getBean("tranManager");
		// 修改用户资料
		String cha_answer= tranManager.changemessage(pptelephone, pnick, pemail, pmajor, pinter,icon);
		response.setContentType("text/html; charset=UTF-8");
		try
		{
			// 把验证的userId封装成JSONObject
			JSONObject jsonObj = new JSONObject().put("cha_answer" ,cha_answer);
			// 输出响应
			response.getWriter().println(jsonObj.toString());
		}
		catch (JSONException ex)
		{
			ex.printStackTrace();
		}
	}
}
