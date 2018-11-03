package edu.depaul.pipeandfilterbooks;

import edu.depaul.Filters.*;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterBook_PipeAndFilterTest {

    @Test
    public void Alice30() {
        System.out.println();
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

        BlockingQueue<String> startMQ = new LinkedBlockingQueue<String>();
        BlockingQueue<String> stopwordsToNonAlphabetMQ = new LinkedBlockingQueue<String>();
        BlockingQueue<String> nonAlphbetToStemMQ = new LinkedBlockingQueue<String>();
        BlockingQueue<String> StemToWordCountMQ = new LinkedBlockingQueue<String>();

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

        //Get file and start populating Start Message Queue
        try {
            BufferedReader br = new BufferedReader(new FileReader(bookFile));

            String st;
            while((st = br.readLine()) != null){
                //Gets words
                Matcher matcher = pattern.matcher(st);
                while(matcher.find()){
                    startMQ.add(st.substring(matcher.start(), matcher.end()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Parsed book. alice30.txt");

        //Wait for queues to be empty after book has been read
        try {
            while (startMQ.peek() != null) {
                //Thread.sleep(1000);
            }
            while (stopwordsToNonAlphabetMQ.peek() != null) {
                //Thread.sleep(1000);
            }
            while (nonAlphbetToStemMQ.peek()  != null) {
                //Thread.sleep(1000);
            }
            while (StemToWordCountMQ.peek()  != null) {
                //Thread.sleep(1000);
            }
            //Thread.sleep(5000);
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
    }

    @Test
    public void Kjbible() {
        System.out.println();
        File bookFile = new File("kjbible.txt");
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

        BlockingQueue<String> startMQ = new LinkedBlockingQueue<String>();
        BlockingQueue<String> stopwordsToNonAlphabetMQ = new LinkedBlockingQueue<String>();
        BlockingQueue<String> nonAlphbetToStemMQ = new LinkedBlockingQueue<String>();
        BlockingQueue<String> StemToWordCountMQ = new LinkedBlockingQueue<String>();

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

        //Get file and start populating Start Message Queue
        try {
            BufferedReader br = new BufferedReader(new FileReader(bookFile));

            String st;
            while((st = br.readLine()) != null){
                //Gets words
                Matcher matcher = pattern.matcher(st);
                while(matcher.find()){
                    startMQ.add(st.substring(matcher.start(), matcher.end()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Parsed book. kjbible.txt");

        //Wait for queues to be empty after book has been read
        try {
            while (startMQ.peek() != null) {
                //Thread.sleep(1000);
            }
            while (stopwordsToNonAlphabetMQ.peek() != null) {
                //Thread.sleep(1000);
            }
            while (nonAlphbetToStemMQ.peek()  != null) {
                //Thread.sleep(1000);
            }
            while (StemToWordCountMQ.peek()  != null) {
                //Thread.sleep(1000);
            }
            //Thread.sleep(5000);
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
    }

    @Test
    public void USdeclar() {
        System.out.println();
        File bookFile = new File("usdeclar.txt");
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

        BlockingQueue<String> startMQ = new LinkedBlockingQueue<String>();
        BlockingQueue<String> stopwordsToNonAlphabetMQ = new LinkedBlockingQueue<String>();
        BlockingQueue<String> nonAlphbetToStemMQ = new LinkedBlockingQueue<String>();
        BlockingQueue<String> StemToWordCountMQ = new LinkedBlockingQueue<String>();

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

        //Get file and start populating Start Message Queue
        try {
            BufferedReader br = new BufferedReader(new FileReader(bookFile));

            String st;
            while((st = br.readLine()) != null){
                //Gets words
                Matcher matcher = pattern.matcher(st);
                while(matcher.find()){
                    startMQ.add(st.substring(matcher.start(), matcher.end()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Parsed book. usdeclar.txt");

        //Wait for queues to be empty after book has been read
        try {
            while (startMQ.peek() != null) {
                //Thread.sleep(1000);
            }
            while (stopwordsToNonAlphabetMQ.peek() != null) {
                //Thread.sleep(1000);
            }
            while (nonAlphbetToStemMQ.peek()  != null) {
                //Thread.sleep(1000);
            }
            while (StemToWordCountMQ.peek()  != null) {
                //Thread.sleep(1000);
            }
            //Thread.sleep(5000);
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
    }
}