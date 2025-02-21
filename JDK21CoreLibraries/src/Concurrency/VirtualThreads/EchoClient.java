package Concurrency.VirtualThreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        if(args.length!=2){
            System.out.println(args.length);
            for (String arg : args) {
                System.out.println(arg);
            }
            System.out.println("Usage: EchoClient <host> <port>");
            System.exit(1);
        }
        String hostName=args[0];
        int port=Integer.parseInt(args[1]);
        try(
                Socket echoSocket = new Socket(hostName,port);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()))
        ){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while((userInput=bufferedReader.readLine())!=null){
                out.println(userInput);
                System.out.println("echo:"+in.readLine());
                if (userInput.equals("bye")) break;
            }
        }
    }
}
