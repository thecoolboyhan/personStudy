package org.example.springboot;

import org.example.springboot.classLoadChange.PluginClassLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.String;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;

@SpringBootApplication
public class Application {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		SpringApplication.run(Application.class, args);
//		Solution solution = new Solution();
//		System.out.println(solution.peopleAwareOfSecret(6, 2, 4));

	}

}


class Solution {
	public int peopleAwareOfSecret(int n, int delay, int forget) {
		int[] dp = new int[n];
		dp[0]=1;
		for(int i=1;i<n;i++){
			int f=i-forget>=0?dp[i-forget]:0;
			int d=i-delay>=0?dp[i-delay]-f:0;
			dp[i]=dp[i-1]+d-f;
		}
		return dp[n-1];
	}
}
