package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 韩永发
 *
 * @Date 21:12 2022/7/14
 */
public class StaticResourceUtil {
  /**
   * 获取静态资源的绝对路径
   * @param path
   * @return
   */
  public static String getAbsolutePath(String path){
    String absolutePath=StaticResourceUtil.class.getResource("/").getPath();
    return absolutePath.replaceAll("\\\\","/")+path;
  }

  /**
   * 读取静态资源文件输入流，通过输出流输出
   * @param inputStream
   * @param outputStream
   */
  public static void outputStaticResource(InputStream inputStream,OutputStream outputStream) throws IOException {
    int count=0;
    while (count==0){
      count=inputStream.available();
    }
    int resourceSize=count;
    //htp请求头
    outputStream.write(HttpProtocolUtil.getHttpHeader200(resourceSize).getBytes());
    //先读取内容，再输出具体内容
    long written =0; //已经读取的内容长度
    int byteSize=1024;  //计划每次缓冲的长度
    while (written<resourceSize){
      if (written+byteSize>resourceSize){
        //最后以此剩余未读取的大小
        byteSize= (int) (resourceSize-written);
      }
      byte[] bytes=new byte[byteSize];
      inputStream.read(bytes);
      outputStream.write(bytes);
      written+=byteSize;
    }

  }
}
