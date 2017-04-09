# HttpDemo
    Android应用中有关Http请求、解析的Demo，及WebView的基本用法    
    
##HttpUrlConnectionDemo演示了最基本的GET、POST请求
1. 使用Bmob的云端逻辑做为服务器端
2. 请求结果以Json格式返回
    
##UploadDemo演示了模拟表单的文件上传功能
1. 使用本地Tomcat做为服务器端，需要搭建
2. 从设备选择一个文件，再上传到服务器端
    
##web项目是文件上传的服务器端
1. 部署在Tomcat之后用浏览器访问http://localhost:端口/web/upload.jsp可进行测试
2. Android应用上传的服务器路径：http://IP:端口/web/servlet/UploadFileServlet
    
##DownloadDemo演示了文件下载的功能
1. 下载网络图片
2. 下载Html
3. 下载文件（保存在SD卡根路径）
    
##AsyncTaskDemo演示了AsyncTask的基本用法
1. Model app 演示了主线程阻塞会导致ANR
2. Module UpdateProgressBar模拟网络访问，利用AsyncTask更新进度条
3. Module DownloadPicture使用LruCache、AsyncTask实现批量图片的加载与内存缓存
    
##XML演示了三种XML文件的解析方法
1. SAX解析
2. DOM解析
3. PULL解析
    
##Json演示了Json的构建与解析
1. JSONArray、JSONObject构建与解析Json
2. JsonReader解析Json
3. Gson构建与解析Json（注解、容错机制）
    
##WebViewDemo演示了WebView的基本用法
1. Model app 演示了WebView、WebViewClient、WebSettings等用法
2. Module WebChromClient 演示了WebChromClient的基本用法
3. Module HtmlUI 演示了将服务器网页作为界面，展示联系人列表的应用
4. Module VideoPlayer 演示了使用WebView播放H5视频，及全屏方案
    
##webview项目是WebViewDemo的服务器端
部署在Tomcat之后用浏览器访问http://localhost:端口/webview/xxx.html可进行测试
    