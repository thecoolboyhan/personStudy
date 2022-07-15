package server;

/**
 * 韩永发
 * http协议工具类， 主要提供响应头信息，这里我们只提供200和404的情况
 * @author hp
 * @Date 18:22 2022/7/14
 */
public class HttpProtocolUtil {

  /**
   * 为响应码200
   * @return
   */
  public static String getHttpHeader200(long contentLength){

       return "HTTP/1.1 200 OK+ \n"+
               "Content-Type: text/html;charset=utf-8 \n"+
               "Content-Length: "+contentLength+"\n"+
               "\r\n";
  }

  /**
   * 为响应码404提供返回头
   * @return
   */
  public static String getHttpHeader404(){
    String str404="<h1>404 not found</h1>";
       return "HTTP/1.1 404 NOT Found \n"+
               "Content-Type: text/html;charset=utf-8 \n"+
               "Content-Length: "+str404.getBytes().length+"\n"+
               "\r\n"+str404;
  }
}
