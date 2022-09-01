module test {
	exports com.wy;
	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires spring.context;
	requires spring.beans;
	requires spring.core;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires lombok;
	requires java.net.http;
	requires jmh.core;
//	requires jdk.incubator.httpclient;
}