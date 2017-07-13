package cn.picture.Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import cn.picture.service.tranManager;

//用于客户端上传标签

@WebServlet(urlPatterns="/tagup")
public class tagupServlet extends BaseServlet  {

    public void service(HttpServletRequest request ,HttpServletResponse response)
    		throws IOException , ServletException
	{
    	try{
		// 获取系统的业务逻辑组件
    	request.setCharacterEncoding("utf-8");
		tranManager tranManager = (tranManager)getCtx().getBean("tranManager");
		String pptelephone=request.getParameter("pptelephone");
		String picid=request.getParameter("picid");
		String pictag=request.getParameter("pictag");
		int f=-1;
		if(pictag!=null)
			f = tranManager.tagup(pptelephone, picid, pictag);
		// 返回用户信息
		JSONObject jsonObj= new JSONObject();
		jsonObj.put("tagup", f);
		response.setContentType("text/html; charset=UTF-8");
		// 输出响应
		response.getWriter().println(jsonObj.toString());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		
	}
}
