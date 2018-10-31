package edu.depaul.Filters;


import java.util.*;
import java.util.concurrent.BlockingQueue;

// Maintains count of each word; Doesn't really filter anything
public class FilterFrequencyCount extends BaseFilter {
    private HashMap<String, Integer> wordCounts;
    public int i = 0;

    public FilterFrequencyCount(BlockingQueue<String> FromQueue, BlockingQueue<String> ToQueue) {
        super(FromQueue, ToQueue);
        wordCounts = new HashMap<String, Integer>();
    }

    @Override
    public String filter(String input){
        if(input == null){
            return null;
        }

        increment();

        if(wordCounts.containsKey(input)){
            wordCounts.put(input, wordCounts.get(input) + 1);
        }
        else{
            wordCounts.put(input, 1);
        }
        return null;
    }

    @Override
    public void print(){
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(wordCounts.entrySet());

        // Sorts List alphabetically
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getKey().compareTo(o2.toString()) ;
            }
        });

        // Sorts by most frequent word; Decending order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue() ;
            }
        });

        System.out.println("Word | Count");
        for(int i = 0; i < 10; i++){
            Map.Entry<String, Integer> wordToCount = list.get(i);
            System.out.println(wordToCount.getKey() + " | " + wordToCount.getValue());
        }
        System.out.println("Number of words: " + list.size());
    }
}
