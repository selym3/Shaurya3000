package robot;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.implot.ImPlot;
import robot.util.StopWatch;
import robot.util.TimedSeries;

public class Motor implements Simmable {
    
    private final int id;

    private final StopWatch timer;

    private final double kS;
    private final double kV;
    private final double kA;

    private TimedSeries timedSeries;
    private double velocity;

    private double voltage;
    
    public Motor(int id, double kS, double kV, double kA) {
        this.id = id;
        timer = new StopWatch();

        this.kS = kS;
        this.kV = kV;
        this.kA = kA;

        timedSeries = new TimedSeries(5.0);
        velocity = 0.0;

        voltage = 0.0;
    }
    
    public Motor(int id) {
        this(id, 0.2, 1.7, 0.5);
    }

    @Override
    public void run() {
        double dt = timer.reset();
        // V = kS * sgn(velocity) + kV * velocity + kA * acceleration
        // (V - (kS * sgn(velocity) + kV * velocity)) / kA = acceleration
        double accel = (voltage - (kS * Math.signum(velocity) + kV * velocity)) / kA;
        velocity += accel * dt;
    }

    @Override
    public void push() {

        ImGui.begin("motor-" + id);

        ImGui.text("velocity: " + velocity);

        float[] f = new float[] { (float)voltage };
        ImGui.sliderFloat("voltage: ", f, -12, +12);
        voltage = f[0];

        timedSeries.push(velocity);
        // ImPlot.setNext
        if(ImPlot.beginPlot("velocity vs time", "time", "velocity", new ImVec2(-1, -1))) {
            ImPlot.plotLine("velocity", timedSeries.getTimes(), timedSeries.getValues(), timedSeries.size(), 0);
            ImPlot.endPlot();
        }


        ImGui.end();
    }

}
