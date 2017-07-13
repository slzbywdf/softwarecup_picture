package cn.picture.Servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import cn.picture.service.tranManager;

//图片上传
@WebServlet(name="upfile",urlPatterns="/upfile")
public class pictureupServlet extends BaseServlet {
	public void service(HttpServletRequest request ,HttpServletResponse response)
    		throws IOException , ServletException
	{
		request.setCharacterEncoding("UTF-8");
		//System.out.println("4545");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart){
			String realpath = request.getSession().getServletContext().getRealPath("/image");
			// 获取系统的业务逻辑组件
			tranManager tranManager = (tranManager)getCtx().getBean("tranManager");
			//System.out.print(realpath);
			File dir =new File(realpath);
			if(!dir.exists()) dir.mkdirs();
			FileItemFactory factory =new DiskFileItemFactory();
			ServletFileUpload upload =new ServletFileUpload(factory);
			upload.setHeaderEncoding("ISO8859_1");//设计utf-8出现乱码
			response.setContentType("text/html; charset=UTF-8");
			try{
				List<FileItem> items =(List<FileItem>) upload.parseRequest(new ServletRequestContext(request));
				for(FileItem item:items){
					String name1 = item.getFieldName();
					//如果获取的表单信息是普通的文本信息。即通过页面表单形式传递来的字符串。
					if(!name1.equals("upload")){
						//获取用户具体输入的字符串，  
	                    String value = item.getString();  
	                    request.setAttribute(name1, value);  
					}
					//如果传入的是非简单字符串，而是图片，音频，视频等二进制文件。  
					else{
						String fileName=item.getName();
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						fileName =new String(fileName.getBytes("ISO8859_1"), "UTF-8");
						item.write(new File(dir,fileName));
						//System.out.print(newStr);
						//保存图片信息
						if(!tranManager.picup("image\\"+fileName,fileName))
							response.getWriter().println(fileName+"上传失败，请重命名图片名！！");
						else
							response.getWriter().println(fileName+"上传服务器成功！");
					}
				}
				//response.getWriter().println("1");
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			
		}
		
	}
}
