
package com.wy.wsdl.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.wy.wsdl.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetUsernameResponse_QNAME = new QName("http://server.wsdl.wy.com/", "getUsernameResponse");
    private final static QName _GetUsername_QNAME = new QName("http://server.wsdl.wy.com/", "getUsername");
    private final static QName _GetUserResponse_QNAME = new QName("http://server.wsdl.wy.com/", "getUserResponse");
    private final static QName _GetUser_QNAME = new QName("http://server.wsdl.wy.com/", "getUser");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.wy.wsdl.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetUserResponse }
     * 
     */
    public GetUserResponse createGetUserResponse() {
        return new GetUserResponse();
    }

    /**
     * Create an instance of {@link GetUser }
     * 
     */
    public GetUser createGetUser() {
        return new GetUser();
    }

    /**
     * Create an instance of {@link GetUsernameResponse }
     * 
     */
    public GetUsernameResponse createGetUsernameResponse() {
        return new GetUsernameResponse();
    }

    /**
     * Create an instance of {@link GetUsername }
     * 
     */
    public GetUsername createGetUsername() {
        return new GetUsername();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUsernameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.wsdl.wy.com/", name = "getUsernameResponse")
    public JAXBElement<GetUsernameResponse> createGetUsernameResponse(GetUsernameResponse value) {
        return new JAXBElement<GetUsernameResponse>(_GetUsernameResponse_QNAME, GetUsernameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUsername }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.wsdl.wy.com/", name = "getUsername")
    public JAXBElement<GetUsername> createGetUsername(GetUsername value) {
        return new JAXBElement<GetUsername>(_GetUsername_QNAME, GetUsername.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.wsdl.wy.com/", name = "getUserResponse")
    public JAXBElement<GetUserResponse> createGetUserResponse(GetUserResponse value) {
        return new JAXBElement<GetUserResponse>(_GetUserResponse_QNAME, GetUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.wsdl.wy.com/", name = "getUser")
    public JAXBElement<GetUser> createGetUser(GetUser value) {
        return new JAXBElement<GetUser>(_GetUser_QNAME, GetUser.class, null, value);
    }

}
