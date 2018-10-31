package edu.depaul.pipeandfilterbooks;

import edu.depaul.Filters.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterBook_PipeAndFilter {
    public static void main(String[] args) {
        File bookFile = new File("alice30.txt");
        File stopWordsFile = new File("stopwords.txt");
        HashSet<String> stopWords = new HashSet<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(stopWordsFile));

            String st;
            while((st = br.readLine()) != null){
                stopWords.add(st);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Queue<String> startMQ = new LinkedList<String>();
        Queue<String> stopwordsToNonAlphabetMQ = new LinkedList<String>();
        Queue<String> nonAlphbetToStemMQ = new LinkedList<String>();
        Queue<String> StemToWordCountMQ = new LinkedList<String>();

        BaseFilter stopWordsFilter = new FilterStopWords(startMQ, stopwordsToNonAlphabetMQ, stopWords);
        BaseFilter nonAlphabetFilter = new FilterNonalphabeticWords(stopwordsToNonAlphabetMQ, nonAlphbetToStemMQ);
        BaseFilter porterStemmerFilter = new FilterPorterStemmer(nonAlphbetToStemMQ, StemToWordCountMQ);
        BaseFilter wordCountFilter = new FilterFrequencyCount(StemToWordCountMQ, null); // To is null

        Thread t1 = new Thread(stopWordsFilter);
        Thread t2 = new Thread(nonAlphabetFilter);
        Thread t3 = new Thread(porterStemmerFilter);
        Thread t4 = new Thread(wordCountFilter);

        Pattern pattern = Pattern.compile("[\\w']+"); //Regex to match words and ignore punctuation

        //Start all Filter Threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        int w = 0;

        //Get file and start populating Start Message Queue
        try {
            BufferedReader br = new BufferedReader(new FileReader(bookFile));

            String st;
            while((st = br.readLine()) != null){
                //Gets words
                Matcher matcher = pattern.matcher(st);
                while(matcher.find()){
                    startMQ.add(st.substring(matcher.start(), matcher.end()));
                    w = w + 1;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Parsed book.");

        //Wait for queues to be empty after book has been read
        try {
            while (startMQ.peek() != null) {
                Thread.sleep(1000);
            }
            while (stopwordsToNonAlphabetMQ.peek() != null) {
                Thread.sleep(1000);
            }
            while (nonAlphbetToStemMQ.peek()  != null) {
                Thread.sleep(1000);
            }
            while (StemToWordCountMQ.peek()  != null) {
                Thread.sleep(1000);
            }
            Thread.sleep(5000);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }



        //Stop All Filter Threads
        stopWordsFilter.stopRunning();
        nonAlphabetFilter.stopRunning();
        porterStemmerFilter.stopRunning();
        wordCountFilter.stopRunning();

        //Print Word Counts
        wordCountFilter.print();
        stopWordsFilter.printI();
        nonAlphabetFilter.printI();
        porterStemmerFilter.printI();
        wordCountFilter.printI();

        System.out.println("Total Words: " + w);
    }
}
