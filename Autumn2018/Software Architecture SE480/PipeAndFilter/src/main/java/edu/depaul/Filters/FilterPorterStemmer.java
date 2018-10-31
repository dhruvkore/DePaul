package edu.depaul.Filters;

import opennlp.tools.stemmer.PorterStemmer;

import java.util.Queue;

//Uses PorterFilter Library from OpenNPL library
public class FilterPorterStemmer extends BaseFilter {
    private volatile boolean running = true;
    private PorterStemmer porterStemmer;
    public int i = 0;

    public FilterPorterStemmer(Queue<String> FromQueue, Queue<String> ToQueue) {
        super(FromQueue, ToQueue);
        porterStemmer = new PorterStemmer();
    }

    @Override
    public String filter(String input) {
        if(input == null) {
            return null;
        }

        increment();

        return porterStemmer.stem(input);
    }
}
