package server;

/**
 * 韩永发
 *
 * @Date 21:40 2022/7/14
 */
public interface Servlet {
  void init() throws Exception;
  void destory() throws Exception;
  void service(Request request,Response response)throws Exception;
}
