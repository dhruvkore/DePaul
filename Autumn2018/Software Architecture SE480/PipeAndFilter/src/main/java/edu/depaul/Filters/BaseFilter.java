package edu.depaul.Filters;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class BaseFilter implements IFilter, Runnable{
    protected BlockingQueue<String> fromQueue;
    protected BlockingQueue<String>  toQueue;

    protected List<Long> processMessageTimes;

    private volatile boolean running = true;

    public BaseFilter(BlockingQueue<String>  FromQueue, BlockingQueue<String> ToQueue){
        this.fromQueue = FromQueue;
        this.toQueue = ToQueue;
        processMessageTimes = new ArrayList<Long>();
    }

    public String filter(String input) {
        return null;
    }

    public void run() {
        Long startTime = new Long(0);
        Long stopTime = new Long(0);

        while(running){
            if(!fromQueue.isEmpty()) {
                // Get from Queue
                startTime = System.currentTimeMillis();
                String incoming = fromQueue.poll();

                //Filter
                String output = filter(incoming);

                // If there is no to Queue
                if (toQueue != null && (output != null && !output.isEmpty())) {
                    // Output to new Queue (if applicable)
                    toQueue.add(output);
                    stopTime = System.currentTimeMillis();
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

    public float getAverageProcessTime(){
        return calcAverage(processMessageTimes);
    }

    private float calcAverage(List<Long> allTimes){
        Long sum = new Long(0);

        if(!allTimes.isEmpty()){
            for(Long l : allTimes){
                sum = sum + l;
            }

            return (float)sum / allTimes.size();
        }

        return sum;
    }
}
