package edu.depaul.Filters;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class FilterNonalphabeticWords extends BaseFilter {
    private volatile boolean running = true;
    public int i = 0;

    public FilterNonalphabeticWords(BlockingQueue<String> FromQueue, BlockingQueue<String> ToQueue) {
        super(FromQueue, ToQueue);
    }

    @Override
    public String filter(String input) {

        if(input == null){
            return null;
        }
        else if(input.length() < 2){
            return input;
        }

        increment();

        for(int i = 0; i < input.length() - 2; i ++){
            if(Character.toLowerCase(input.charAt(i)) > Character.toLowerCase(input.charAt(i))){
                return "";
            }
        }
        return input;
    }
}
