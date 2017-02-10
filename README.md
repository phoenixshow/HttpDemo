# HttpDemo
Android应用中有关Http请求的Demo
<br/>
##HttpUrlConnectionDemo演示了最基本的GET、POST请求
1、使用Bmob的云端逻辑做为服务器端<br/>
2、请求结果以Json格式返回<br/>
<br/>
##UploadDemo演示了模拟表单的文件上传功能
1、使用本地Tomcat做为服务器端，需要搭建<br/>
2、从设备选择一个文件，再上传到服务器端<br/>
<br/>
##web项目是文件上传的服务器端
1、部署之后用浏览器访问http://localhost:端口/web/upload.jsp可进行测试<br/>
2、Android应用上传的服务器路径：http://IP:端口/web/servlet/UploadFileServlet<br/>
<br/>