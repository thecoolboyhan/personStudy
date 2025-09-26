package org.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.String;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
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
