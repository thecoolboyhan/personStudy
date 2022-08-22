package grss.hash;

import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;

public class ConsistentHashNoVirtual {
    public static void main(String[] args) {
        String[] tomcatServers=new String[]{"123.111.0.0","123.101.3.1","111.20.35.2","123.98.26.3"};

        SortedMap<Integer,String> hashServerMap=new TreeMap<>();
        for (String tomcatServer : tomcatServers) {
            int serverHash = Math.abs(tomcatServer.hashCode());
            hashServerMap.put(serverHash,tomcatServer);
        }

        //针对客户端ip求hash值
        String[] clients=new String[]{"10.78.12.3","113.25.63.1","126.12.3.8"};
        for (String client : clients) {
            int clientHash = Math.abs(client.hashCode());
            //根据客户端的ip找到哪个服务器节点来处理
            SortedMap<Integer, String> integerStringSortedMap = hashServerMap.tailMap(clientHash);
            if(integerStringSortedMap.isEmpty()){
                //去hash环上的第一台服务器
                Integer integer = hashServerMap.firstKey();
                System.out.println("======客户端:"+client+"====服务器："+hashServerMap.get(integer));
            }else {
                Integer integer = integerStringSortedMap.firstKey();
                System.out.println("======客户端:"+client+"====服务器："+hashServerMap.get(integer));
            }

        }
    }
}
