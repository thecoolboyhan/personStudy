package io.helidon.examples;

import io.helidon.microprofile.server.Server;

public final class Main {
//	空类，不能被实例化
	private Main(){
	}
	
	public static void main(String[] args) {
		Server startServer = startServer();
		System.out.println("http://localhost:"+startServer.port()+"/greet");
	}
	
	static Server startServer() {
		return Server.create().start();
	}
}
