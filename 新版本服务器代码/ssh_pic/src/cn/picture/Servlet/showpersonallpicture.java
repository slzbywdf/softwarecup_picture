package cn.picture.Servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import cn.picture.service.tranManager;

/**
 * Servlet implementation class showpersonallpicture
 */
@WebServlet("/showpersonallpicture")
public class showpersonallpicture extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public showpersonallpicture() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void service(HttpServletRequest request ,HttpServletResponse response)
    		throws IOException , ServletException
	{
    	try{
    		// 获取系统的业务逻辑组件
    		request.setCharacterEncoding("utf-8");
    		tranManager tranManager = (tranManager)getCtx().getBean("tranManager");
    		String pptelephone=request.getParameter("pptelephone");
    		String res=tranManager.showpersonallpicture(pptelephone);
    		// 返回用户信息
    		JSONObject jsonObj= new JSONObject();
    		jsonObj.put("showpersonallpicture", res);
    		response.setContentType("text/html; charset=UTF-8");
    		// 输出响应
    		response.getWriter().println(jsonObj.toString());
        	}catch(Exception e){
        		e.printStackTrace();
        	}
		
		
	}

}
