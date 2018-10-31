package edu.depaul.Filters;

import javafx.scene.paint.Stop;
import sun.jvm.hotspot.utilities.MessageQueue;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class FilterStopWords extends BaseFilter {
    private Set<String> stopWords;
    private volatile boolean running = true;
    public int i = 0;

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

        increment();

        if(stopWords.contains(input)){
            return "";
        }
        else {
            //System.out.println("Without Stop " + input);
            return input;
        }
    }
}
