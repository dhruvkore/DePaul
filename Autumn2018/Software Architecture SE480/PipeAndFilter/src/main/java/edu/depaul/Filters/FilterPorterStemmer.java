package edu.depaul.Filters;

import opennlp.tools.stemmer.PorterStemmer;

import java.util.concurrent.BlockingQueue;

//Uses PorterFilter Library from OpenNPL library
public class FilterPorterStemmer extends BaseFilter {
    private PorterStemmer porterStemmer;

    public FilterPorterStemmer(BlockingQueue<String> FromQueue, BlockingQueue<String> ToQueue) {
        super(FromQueue, ToQueue);
        porterStemmer = new PorterStemmer();
    }

    @Override
    public String filter(String input) {
        if(input == null) {
            return null;
        }

        //Uses OpenNPL opensourced library for stemming; jumping -> jump, watery -> water
        return porterStemmer.stem(input);
    }
}
