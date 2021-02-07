package application;

import java.sql.Time;

public abstract class TimeHandler {
    private static int delta_timeMS;
    private static float delta_time;
    private static long last_time;
    public static int frameCounter = -1;
    private static TimeHandler instance;
    public static void setLast_time(long last_time) {
        TimeHandler.last_time = last_time;
    }
    public static int getFrameCounter() {
        return frameCounter;
    }
    public static float getDelta_time() {
        return delta_time;
    }

    public static void update() {
        frameCounter++;
        long time = System.nanoTime();
        delta_timeMS = (int) ((time - last_time) / 1000000);
        delta_time = ((float) delta_timeMS) / 1000;
        last_time = time;

    }

}
