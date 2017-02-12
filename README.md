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
##DownloadDemo演示了文件下载的功能
1、下载网络图片<br/>
2、下载Html<br/>
3、下载文件（保存在SD卡根路径）<br/>
<br/>
##AsyncTaskDemo演示了AsyncTask的基本用法
1、Model app 演示了主线程阻塞会导致ANR<br/>
2、Module UpdateProgressBar模拟网络访问，利用AsyncTask更新进度条<br/>
<br/>
##XML演示了三种XML文件的解析方法
1、SAX解析<br/>
2、DOM解析<br/>
3、PULL解析<br/>
<br/>