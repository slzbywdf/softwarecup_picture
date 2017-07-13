package cn.picture.Servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import cn.picture.service.tranManager;

@WebServlet(urlPatterns="/register.jsp")
public class RegisterServlet extends BaseServlet
{
    public void service(HttpServletRequest request ,HttpServletResponse response)
    		throws IOException , ServletException
	{
    	// 获取系统的业务逻辑组件
    		tranManager tranManager = (tranManager)getCtx().getBean("tranManager");
    	 /*try {
             BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/q.txt")), "UTF-8"));
             String lineTxt = null;
             lineTxt = br.readLine();
             br.close();
             tranManager.chuli(lineTxt);
             
         } catch (Exception e) {
             System.err.println("read errors :" + e);
         }*/
    	 
    	 
    	request.setCharacterEncoding("gbk");
		String pptelephone = request.getParameter("pptelephone");
		String ppassword = request.getParameter("ppassword");

		// 验证用户注册
		String reg_answer= tranManager.register(pptelephone , ppassword);
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