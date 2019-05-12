import client.QuoteServiceService;
import client.QuoteService;

public class QuoteClient {
    public static void main(String[] args){
        QuoteServiceService service = new QuoteServiceService();
        QuoteService port = service.getQuoteServicePort();

        System.out.println(port.getQuote());
        System.out.println("---");


        port.addQuote("This is a second Quote");
        port.addQuote("This is a third Quote");
        port.addQuote("This is a fourth Quote");
        port.addQuote("This is a fifth Quote");

        for(int i = 0; i < 10; i++) {
            System.out.println(port.getQuote());
        }
        System.out.println("---");

        port.addQuote("This is a sixth Quote");

        for(int i = 0; i < 10; i++) {
            System.out.println(port.getQuote());
        }
    }
}
