package web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import web.bean.RespMsg;

import com.google.gson.GsonBuilder;

public class UploadFileServlet extends HttpServlet {

	private static final long serialVersionUID = 2799736659961743659L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		String message = "fail";
		
		//当前上传的信息中是否有文件类型
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if(isMultipart){
			//得到保存的真实路径
			String realpath = req.getSession().getServletContext().getRealPath("/files");
//			System.out.println(realpath);
			File dir = new File(realpath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			//得到文件工厂
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			try {
				List<FileItem> items = upload.parseRequest(req);
				//遍历上传的信息
				for(FileItem item : items){
					if(item.isFormField()){//如果是一般的表单信息
						String name = item.getFieldName();//得到请求参数的名称
						String value = item.getString("UTF-8");//得到参数值
						System.out.println(name+"-->"+value);
					}else{//是文件对象就写到真实路径下
						System.out.println("文件对象-->"+item.getName());
						item.write(new File(dir, System.currentTimeMillis()+
							item.getName().substring(item.getName().lastIndexOf("."))));
					}
				}
				message = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		RespMsg msg = new RespMsg(200, message);
		String json = new GsonBuilder().create().toJson(msg);
		System.out.println(json);
		out.println(json);
		out.flush();
		out.close();
	}

}
