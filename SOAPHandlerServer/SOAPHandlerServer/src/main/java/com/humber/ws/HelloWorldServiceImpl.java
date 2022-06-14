package com.humber.ws;

import javax.jws.HandlerChain;
import javax.jws.WebService;

@WebService(endpointInterface="com.humber.ws.HelloWorldService")
@HandlerChain(file="../handler/handler-chain.xml")
public class HelloWorldServiceImpl implements HelloWorldService {

	public String sayHelloWorld() {
		return "Hello World";
	}

}
