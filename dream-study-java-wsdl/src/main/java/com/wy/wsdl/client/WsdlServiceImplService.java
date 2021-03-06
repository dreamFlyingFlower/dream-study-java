package com.wy.wsdl.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "WsdlServiceImplService", targetNamespace = "http://server.wsdl.wy.com/", wsdlLocation = "http://localhost:5502/wsdlService?wsdl")
public class WsdlServiceImplService extends Service {

	private final static URL WSDLSERVICEIMPLSERVICE_WSDL_LOCATION;

	private final static WebServiceException WSDLSERVICEIMPLSERVICE_EXCEPTION;

	private final static QName WSDLSERVICEIMPLSERVICE_QNAME = new QName("http://server.wsdl.wy.com/",
			"WsdlServiceImplService");

	static {
		URL url = null;
		WebServiceException e = null;
		try {
			url = new URL("http://localhost:5502/wsdlService?wsdl");
		} catch (MalformedURLException ex) {
			e = new WebServiceException(ex);
		}
		WSDLSERVICEIMPLSERVICE_WSDL_LOCATION = url;
		WSDLSERVICEIMPLSERVICE_EXCEPTION = e;
	}

	public WsdlServiceImplService() {
		super(__getWsdlLocation(), WSDLSERVICEIMPLSERVICE_QNAME);
	}

	public WsdlServiceImplService(WebServiceFeature... features) {
		super(__getWsdlLocation(), WSDLSERVICEIMPLSERVICE_QNAME, features);
	}

	public WsdlServiceImplService(URL wsdlLocation) {
		super(wsdlLocation, WSDLSERVICEIMPLSERVICE_QNAME);
	}

	public WsdlServiceImplService(URL wsdlLocation, WebServiceFeature... features) {
		super(wsdlLocation, WSDLSERVICEIMPLSERVICE_QNAME, features);
	}

	public WsdlServiceImplService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public WsdlServiceImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
		super(wsdlLocation, serviceName, features);
	}

	/**
	 * 
	 * @return returns WsdlServiceImpl
	 */
	@WebEndpoint(name = "WsdlServiceImplPort")
	public WsdlServiceImpl getWsdlServiceImplPort() {
		return super.getPort(new QName("http://server.wsdl.wy.com/", "WsdlServiceImplPort"), WsdlServiceImpl.class);
	}

	/**
	 * 
	 * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure
	 *        on the proxy. Supported features not in the <code>features</code>
	 *        parameter will have their default values.
	 * @return returns WsdlServiceImpl
	 */
	@WebEndpoint(name = "WsdlServiceImplPort")
	public WsdlServiceImpl getWsdlServiceImplPort(WebServiceFeature... features) {
		return super.getPort(new QName("http://server.wsdl.wy.com/", "WsdlServiceImplPort"), WsdlServiceImpl.class,
				features);
	}

	private static URL __getWsdlLocation() {
		if (WSDLSERVICEIMPLSERVICE_EXCEPTION != null) {
			throw WSDLSERVICEIMPLSERVICE_EXCEPTION;
		}
		return WSDLSERVICEIMPLSERVICE_WSDL_LOCATION;
	}
}