package server;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

/**
 * 韩永发
 *
 * @Date 16:21 2022/7/15
 */
public class RequestProcessor extends Thread {

  private Socket socket;
  private Map<String, HttpServlet> servletMap;

  public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
    this.socket=socket;
    this.servletMap=servletMap;
  }

  @Override
  public void run() {
    try {
      InputStream inputStream = socket.getInputStream();
      //封装Request对象和Response对象
      Request request = new Request(inputStream);
      Response response = new Response(socket.getOutputStream());
      if (servletMap.get(request.getUrl()) == null) {
        response.outputHtml(request.getUrl());
      } else {
        //动态资源servlet，请求动态资源servlet
        HttpServlet httpServlet = servletMap.get(request.getUrl());
        httpServlet.service(request, response);
      }
      socket.close();
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
