package grss.hash;

/**
 * 普通hash
 */
public class GeneralHash {
    public static void main(String[] args) {
        //定义客户端IP
        String[] clients=new String[]{"10.78.12.3","113.25.63.1","126.12.3.8"};
        //定义服务器数量
        int ServerCount=3;
        //路由计算
        for(String client :clients){
            int hashcode = Math.abs(client.hashCode());
            int index = hashcode % ServerCount;
            System.out.println("客户端"+client+"被路由到服务器编号"+index);
        }

    }
}
