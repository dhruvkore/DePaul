package edu.depaul.Filters;

import sun.jvm.hotspot.utilities.MessageQueue;
import sun.plugin2.message.Message;

import java.util.Queue;

public class BaseFilter implements IFilter, Runnable{
    protected Queue<String> fromQueue;
    protected Queue<String>  toQueue;
    private int i = 0;

    private volatile boolean running = true;

    public BaseFilter(Queue<String>  FromQueue, Queue<String> ToQueue){
        this.fromQueue = FromQueue;
        this.toQueue = ToQueue;
    }

    public String filter(String input) {
        return null;
    }

    public void run() {
        while(running){
            if(!fromQueue.isEmpty()) {
                String output = filter(fromQueue.poll());

                // If there is no to Queue
                if (toQueue != null && (output != null && !output.isEmpty())) {
                    toQueue.add(output);
                } else {

                    try {
                        System.out.println("Sleeping: " + i);
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //To only be used by Main method to stop this thread
    public void stopRunning(){
        running = false;
    }

    public void print(){
        System.out.println("Base Filter :D");
    }

    public void increment(){
        i = i + 1;
    }

    public void printI(){
        System.out.println(i);
    }
}
