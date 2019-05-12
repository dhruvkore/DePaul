package Quotes;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@WebService
public class QuoteService {
    private static List<String> quotes;
    private static Random rand = new Random();

    public QuoteService(){
        quotes = new ArrayList<String>();
        quotes.add("Initial String");
    }

    @WebMethod
    public String getQuote(){
        int randNum = rand.nextInt(quotes.size());
        return quotes.get(randNum);
    }

    @WebMethod
    public void addQuote(String quote){
        quotes.add(quote);
    }
}
