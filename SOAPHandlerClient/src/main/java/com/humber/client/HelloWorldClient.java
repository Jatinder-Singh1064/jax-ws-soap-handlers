package com.humber.client;

import com.humber.ws.HelloWorldService;
import com.humber.ws.HelloWorldServiceImplService;

public class HelloWorldClient {
	public static void main(String[] args) {
		HelloWorldServiceImplService service = new HelloWorldServiceImplService();
		HelloWorldService serviceInterface = service.getHelloWorldServiceImplPort();
		String response = serviceInterface.sayHelloWorld();
		System.out.println(response);
	}

}
