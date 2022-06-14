package com.humber.endpoint;

import javax.xml.ws.Endpoint;

import com.humber.ws.HelloWorldServiceImpl;

public class HelloWorldPublisher {

	public static void main(String[] args) {
		Endpoint endpoint = Endpoint.create(new HelloWorldServiceImpl());
		endpoint.publish("http://localhost:8080/ws/soapHandler");
	}
}
