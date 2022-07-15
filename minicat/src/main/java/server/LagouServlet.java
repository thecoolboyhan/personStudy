package server;

import java.io.IOException;

/**
 * 韩永发
 *
 * @Date 21:45 2022/7/14
 */
public class LagouServlet extends HttpServlet{
  @Override
  public void doGet(Request request, Response response) {
    String content="<h1>LagouServlet get</h1>";
    try {
      response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void doPost(Request request, Response response) {
    String content="<h1>LagouServlet post</h1>";
    try {
      response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void init() throws Exception {
    
  }

  @Override
  public void destory() throws Exception {

  }
}
