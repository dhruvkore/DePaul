package Quotes;

import javax.xml.ws.Endpoint;

public class QuotePublisher {
    public static void main(String[] args){
        final String url = "http://localhost:8888/quotes";
        System.out.println("Publishing QuoteService at endpoint " + url);
        Endpoint.publish(url, new QuoteService());
    }
}
