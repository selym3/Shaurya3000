package robot.util;

import java.util.ArrayDeque;
import java.util.Deque;

public class TimedSeries {
    
    private static class TimedValue {
        public final double time;
        public final double value;

        public TimedValue(double time, double value) {
            this.time = time;
            this.value = value;
        }

        public boolean hasExpired(double now, double window) {
            return now - time > window;
        }
    }

    private final StopWatch timer;

    private final double window;

    private final Deque<TimedValue> values;

    public TimedSeries(double window) {
        timer = new StopWatch();
        
        this.window = window;
    
        values = new ArrayDeque<>();
    }

    private boolean hasExpiredValue() {
        if (values.size() == 0) return false;
        return values.peekFirst().hasExpired(timer.getTime(), window);
    }

    public void push(double value) {
        while (hasExpiredValue()) {
            values.removeFirst();
        }
        values.add(new TimedValue(timer.getTime(), value));
    }

    public double[] getTimes() {
        double[] d = new double[values.size()];
        int top = 0;

        for (var item : values) {
            d[top++] = item.time;
        }
        return d;
    }

    public double[] getValues() {
        double[] d = new double[values.size()];
        int top = 0;

        for (var item : values) {
            d[top++] = item.value;
        }
        return d;
    }

    public int size() {
        return values.size();
    }

}
