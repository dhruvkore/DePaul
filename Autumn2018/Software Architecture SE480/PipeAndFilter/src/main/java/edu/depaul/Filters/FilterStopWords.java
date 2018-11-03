package edu.depaul.Filters;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class FilterStopWords extends BaseFilter {
    private Set<String> stopWords;

    public FilterStopWords(BlockingQueue<String>  FromQueue, BlockingQueue<String> ToQueue, Set<String> StopWords) {
        super(FromQueue, ToQueue);
        if(StopWords != null) {
            this.stopWords = StopWords;
        }
        else{
            stopWords = new HashSet<String>();
        }
    }

    @Override
    public String filter(String input) {
        if(input == null){
            return null;
        }

        if(stopWords.contains(input)){
            return "";
        }
        else {
            return input;
        }
    }
}
