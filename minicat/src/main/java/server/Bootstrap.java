package server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 韩永发
 * minicat 的启动入口
 *
 * @author hp
 * @Date 18:08 2022/7/14
 */
public class Bootstrap {
  //端口号
  private int port = 8080;

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  /**
   * minicat启动需要初始化展开的一些操作
   */
  public void start() throws Exception {
    //加载解析相关的配置
    loadServlet();
    //定义一个线程池
    int corePoolSize=10;
    int maximumPoolSize=50;
    long keepAliveTime=100;
    TimeUnit unit=TimeUnit.SECONDS;
    BlockingQueue<Runnable> workQueue=new ArrayBlockingQueue<>(50);
    ThreadFactory threadFactory=Executors.defaultThreadFactory();
    RejectedExecutionHandler handler=new ThreadPoolExecutor.AbortPolicy();
    ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue,threadFactory,handler);
    

    ServerSocket serverSocket = new ServerSocket(port);
    System.out.println("minicat Start on port:" + port);
    //完成minicat 1.0版本   ,localhost:8080，返回一个固定的字符串    "Hello minicat"
//    while (true){
//      //监听这个端口
//      Socket socket = serverSocket.accept();
//      //接收到请求
//      OutputStream outputStream = socket.getOutputStream();
//      String data="Hello Minicat!!";
//      String responseText =HttpProtocolUtil.getHttpHeader200(data.getBytes().length)+data;
//      outputStream.write(responseText.getBytes());
//      socket.close();
//    }

    /**
     * 2.0版本
     * 封装Request和Response对象，返回html静态资源文件
     */
//     while (true){
//       Socket socket=serverSocket.accept();
//       InputStream inputStream = socket.getInputStream();
//       //封装Request对象和Response对象
//       Request request = new Request(inputStream);
//       Response response=new Response(socket.getOutputStream());
//       response.outputHtml(request.getUrl());
//       socket.close();
//     }

    /**
     * 3.0版本
     * 请求动态资源（Servlet)
     */
//    while (true) {
//      Socket socket = serverSocket.accept();
//      InputStream inputStream = socket.getInputStream();
//      //封装Request对象和Response对象
//      Request request = new Request(inputStream);
//      Response response = new Response(socket.getOutputStream());
//      if (servletMap.get(request.getUrl()) == null) {
//        response.outputHtml(request.getUrl());
//      } else {
//        //动态资源servlet，请求动态资源servlet
//        HttpServlet httpServlet = servletMap.get(request.getUrl());
//        httpServlet.service(request, response);
//      }
//      socket.close();
//    }


    //多线程改造   不使用线程池
//    while (true) {
//      Socket socket = serverSocket.accept();
//      RequestProcessor requestProcessor = new RequestProcessor(socket, servletMap);
//      requestProcessor.start();
//    }

    //使用线程池 进行多线程改造
    while (true) {
      Socket socket = serverSocket.accept();
      RequestProcessor requestProcessor = new RequestProcessor(socket, servletMap);
      threadPoolExecutor.execute(requestProcessor);
    }
  }

  private Map<String, HttpServlet> servletMap = new HashMap<>();

  /**
   * 加载解析web.xml 初始化Servlet
   */
  private void loadServlet() {
    InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
    SAXReader saxReader = new SAXReader();
    try {
      Document document = saxReader.read(resourceAsStream);
      Element rootElement = document.getRootElement();
      List<Element> list = rootElement.selectNodes("//servlet");
      for (int i = 0; i < list.size(); i++) {
        Element element = list.get(i);
        Element servletNameElement = (Element) element.selectSingleNode("servlet-name");
        String ServletName = servletNameElement.getStringValue();
        Element servletClassElement = (Element) element.selectSingleNode("servlet-class");
        String ServletClass = servletClassElement.getStringValue();

        //根据Servlet-name的值找到url-pattern
        Element ServletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + ServletName + "']");
        String urlPattern = ServletMapping.selectSingleNode("url-pattern").getStringValue();
        servletMap.put(urlPattern, (HttpServlet) Class.forName(ServletClass).newInstance());
      }
    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Bootstrap bootstrap = new Bootstrap();
    try {
      //启动minicat
      bootstrap.start();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
