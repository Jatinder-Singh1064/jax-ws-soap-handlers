package com.humber.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface HelloWorldService {
	
	@WebMethod
	public String sayHelloWorld();

}
