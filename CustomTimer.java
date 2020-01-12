package org.usfirst.frc2797.Robot2019v2;

import edu.wpi.first.wpilibj.Utility;

public class CustomTimer{
    private static long startTime;
    private static double accumulatedTime;
    private static boolean running; 

    public static void delay (final double seconds){
        try{
            Thread.sleep((long) (seconds* 1000));
            running = false; 
        }   catch (final InterruptedException e) {
        }
    }

    @Deprecated
    public static long getUsClock(){
        return Utility.getFPGATime();
    }

    static long getMsClock(){
        return getUsClock() /1000;
    }

    public CustomTimer(){
        reset();
    }

    public static synchronized double get(){
        if(running) {
            return ((double) ((getMsClock()-startTime) + accumulatedTime)) /1000;
        } else {
            return accumulatedTime;
        }
    }

    public synchronized void reset(){
        accumulatedTime = 0;
        startTime = getMsClock();    
    }

    public static synchronized void start(){
        startTime = getMsClock();
        running = true;
    }

    public static synchronized void stop(){
        final double temp = get();
        accumulatedTime += temp;
        running = false; 
    }

    public static boolean isRunning(){
        return running;
    }

}