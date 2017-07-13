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

import cn.picture.domain.hw_Picture;
import cn.picture.service.tranManager;

/**
 * Servlet implementation class showpersonallpicture
 */
@WebServlet("/P_showallpicture")
public class P_showallpicture extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public P_showallpicture() {
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
    		List<hw_Picture> res=tranManager.showallpicture();
    		Iterator<hw_Picture> it =res.iterator();
    		String cont="";
    		while(it.hasNext()){
    			hw_Picture temp =it.next();
    			cont=cont+temp.getPic_passway()+";";
    		}
    		if(!cont.isEmpty())
    			cont=cont.substring(0,cont.length()-1);
    		// 返回图片
    		JSONObject jsonObj= new JSONObject();
    		jsonObj.put("showallpicture", cont);
    		response.setContentType("text/html; charset=UTF-8");
    		// 输出响应
    		response.getWriter().println(jsonObj.toString());
        	}catch(Exception e){
        		e.printStackTrace();
        	}
	}
}
