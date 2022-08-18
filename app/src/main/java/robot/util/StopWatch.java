package robot.util;

public final class StopWatch {
    
    private static long getRawTime() {
        return System.nanoTime();
    }

    private static double toSeconds(long raw) {
        return raw / 1_000_000_000.0;
    }

    private long lastTime;

    public StopWatch() {
        lastTime = getRawTime();
    }

    public double reset() {
        long time = getRawTime();
        long delta = Math.max(1, time - lastTime);
        lastTime = time;
        return toSeconds(delta);
    }

    public double getTime() {
        return toSeconds(Math.max(1, getRawTime() - lastTime));
    }

}
