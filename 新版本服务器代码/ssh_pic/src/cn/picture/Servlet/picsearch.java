package cn.picture.Servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.picture.service.tranManager;

/**
 * Servlet implementation class picsearch
 */
@WebServlet("/picsearch")
public class picsearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public picsearch() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private ApplicationContext ctx;
    private HashMap<String,HashMap<Integer,Integer>> allpic = new HashMap<String,HashMap<Integer,Integer>>();
    private tranManager tranManager;
	public void init(ServletConfig config)throws ServletException
	{
		super.init(config);
		// 获取Web应用中的ApplicationContext实例
		ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		tranManager = (tranManager)ctx.getBean("tranManager");
		//初始化拥有某tag的，图片地址集
		allpic =tranManager.P_search_init();
		//System.out.print(b);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		String pra = request.getParameter("search");//输入参数为search
		String phone =request.getParameter("phone");//获得用户
		//获得用户打标签的图片记录
		String[] mytag = tranManager.search_tag_person(phone);
		//System.out.println(mytag.size());
		Iterator<Entry<String,HashMap<Integer,Integer>>> all =allpic.entrySet().iterator();
		Set<Integer> res = new HashSet<Integer>();
		String result="";
		while(all.hasNext()){
			Entry<String,HashMap<Integer,Integer>> temp= all.next();
			String fg =temp.getKey();
			if(fg.indexOf(pra)>=0||fg.equalsIgnoreCase(pra)){
				HashMap<Integer,Integer> mymap = temp.getValue();
				Iterator<Entry<Integer,Integer>> itt=mymap.entrySet().iterator();
				while(itt.hasNext()){
					int pid = itt.next().getKey();
					if(!res.contains(pid)){
						res.add(pid);
						String picd =String.valueOf(pid);
						int i;
						for(i=0;i<mytag.length;i++){
							if(picd.equals(mytag[i])){
								break;
							}
						}
						if(i==mytag.length){//表明该用户没有打过该图片
							result=result+pid+","+tranManager.getpssbypicid(pid)+",";
							String label = tranManager.gettagpush(pid, phone);
							if(label==null||label.isEmpty()){
								result=result+"null;";
							}else
								result=result+label+";";
						}
					}
				} 
			}
		}
		if(result!=null&&!result.isEmpty()){
			result=result.substring(0,result.length()-1);
		}
		System.out.println(result);
		response.setContentType("text/html; charset=UTF-8");
		try
		{
			// 把验证的userId封装成JSONObject
			JSONObject jsonObj = new JSONObject().put("result" , result);
			 //输出响应
			response.getWriter().println(jsonObj.toString());
		}
		catch (JSONException ex)
		{
			ex.printStackTrace();
		}
	}

}
