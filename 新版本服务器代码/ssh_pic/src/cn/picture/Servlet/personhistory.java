package cn.picture.Servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import cn.picture.service.tranManager;

/**
 * Servlet implementation class personhistory
 * 用来获取用户历史图片及标签（以及是否完成）
 */
@WebServlet("/personhistory")
public class personhistory extends BaseServlet  {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public personhistory() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void service(HttpServletRequest request ,HttpServletResponse response)
    		throws IOException , ServletException
	{
    	try{
    		request.setCharacterEncoding("utf-8");
		// 获取系统的业务逻辑组件
		tranManager tranManager = (tranManager)getCtx().getBean("tranManager");
		String pptelephone=request.getParameter("pptelephone");
		String res=tranManager.getpersonhistorybyphone(pptelephone);
		// 返回用户信息
		JSONObject jsonObj= new JSONObject();
		jsonObj.put("personhistory", res);
		response.setContentType("text/html; charset=UTF-8");
		// 输出响应
		response.getWriter().println(jsonObj.toString());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		
	}

}
