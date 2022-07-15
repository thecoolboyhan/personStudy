package server;

import java.io.IOException;
import java.io.InputStream;

/**
 * 韩永发
 *根据inputStream来分装
 * @Date 18:52 2022/7/14
 */
public class Request {
  private String method;//请求方式：get post
  private String url; //  /,/index.html

  private InputStream inputStream;//输入流，其他属性从输入流中取出来

  public Request(InputStream inputStream) throws IOException {
    this.inputStream = inputStream;
    //从输入流中获取请求信息
    //输入流的数据长度
    int count=0;
    while (count==0){
      count=inputStream.available();
    }
    int available = inputStream.available();
    byte[] bytes=new byte[available];
    inputStream.read(bytes);
    String inputString =new String(bytes);
    //获取第一行的请求头信息
    String firstLineStr = inputString.split("\\n")[0];
    String[] strings = firstLineStr.split(" ");
    this.method=strings[0];
    this.url=strings[1];
    System.out.println("=========>>>>>method"+method);
    System.out.println("=========>>>>>url"+url);

  }

  public Request() {
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  public void setInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }
}
