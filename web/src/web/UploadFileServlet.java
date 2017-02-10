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
		
		//��ǰ�ϴ�����Ϣ���Ƿ����ļ�����
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if(isMultipart){
			//�õ��������ʵ·��
			String realpath = req.getSession().getServletContext().getRealPath("/files");
//			System.out.println(realpath);
			File dir = new File(realpath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			//�õ��ļ�����
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			try {
				List<FileItem> items = upload.parseRequest(req);
				//�����ϴ�����Ϣ
				for(FileItem item : items){
					if(item.isFormField()){//�����һ��ı���Ϣ
						String name = item.getFieldName();//�õ��������������
						String value = item.getString("UTF-8");//�õ�����ֵ
						System.out.println(name+"-->"+value);
					}else{//���ļ������д����ʵ·����
						System.out.println("�ļ�����-->"+item.getName());
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
