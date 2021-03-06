
package client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.3.1-SNAPSHOT
 * Generated source version: 2.2
 * 
 */
@WebService(name = "QuoteService", targetNamespace = "http://Quotes/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface QuoteService {


    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getQuote", targetNamespace = "http://Quotes/", className = "client.GetQuote")
    @ResponseWrapper(localName = "getQuoteResponse", targetNamespace = "http://Quotes/", className = "client.GetQuoteResponse")
    @Action(input = "http://Quotes/QuoteService/getQuoteRequest", output = "http://Quotes/QuoteService/getQuoteResponse")
    public String getQuote();

    /**
     * 
     * @param arg0
     */
    @WebMethod
    @RequestWrapper(localName = "addQuote", targetNamespace = "http://Quotes/", className = "client.AddQuote")
    @ResponseWrapper(localName = "addQuoteResponse", targetNamespace = "http://Quotes/", className = "client.AddQuoteResponse")
    @Action(input = "http://Quotes/QuoteService/addQuoteRequest", output = "http://Quotes/QuoteService/addQuoteResponse")
    public void addQuote(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

}
