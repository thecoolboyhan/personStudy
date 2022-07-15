package server;

/**
 * 韩永发
 *
 * @Date 21:42 2022/7/14
 */
public abstract class HttpServlet implements Servlet {
  public abstract void doGet(Request request,Response response);
  public abstract void doPost(Request request,Response response);

  @Override
  public void service(Request request, Response response) throws Exception {
    if ("GET".equalsIgnoreCase(request.getMethod())){
      doGet(request,response);
    } else {
      doPost(request,response);
    }
  }
}
