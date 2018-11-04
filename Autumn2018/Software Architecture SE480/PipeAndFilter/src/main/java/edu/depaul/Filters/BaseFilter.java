package edu.depaul.Filters;

import org.junit.rules.Stopwatch;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class BaseFilter implements IFilter, Runnable{
    protected BlockingQueue<String> fromQueue;
    protected BlockingQueue<String>  toQueue;

    protected List<Float> processMessageTimes;

    private volatile boolean running = true;

    public BaseFilter(BlockingQueue<String>  FromQueue, BlockingQueue<String> ToQueue){
        this.fromQueue = FromQueue;
        this.toQueue = ToQueue;
        processMessageTimes = new ArrayList<Float>();
    }

    public String filter(String input) {
        return null;
    }

    public void run() {
        float startTime = new Long(0);
        float stopTime = new Long(0);

        while(running){
            if(!fromQueue.isEmpty()) {
                // Get from Queue
                String incoming = fromQueue.poll();

                //Filter
                String output = filter(incoming);

                // If there is no to Queue
                if (toQueue != null && (output != null && !output.isEmpty())) {
                    // Output to new Queue (if applicable)
                    toQueue.add(output);
                    processMessageTimes.add(stopTime - startTime);
                }
            }
        }
    }

    //To only be used by Main method to stop this thread
    public void stopRunning(){
        running = false;
    }

    public void print(){
        System.out.println("Base Filter Print :D This Filter has not defined a print.");
    }

    public double getAverageProcessTime(){
        return calcAverage(processMessageTimes);
    }

    private double calcAverage(List<Float> allTimes){
        float sum = 0;

        if(!allTimes.isEmpty()){
            for(float l : allTimes){
                sum = sum + l;
            }

            return (double)sum / allTimes.size();
        }

        return sum;
    }
}
