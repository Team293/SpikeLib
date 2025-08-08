package com.spike293.telemetry;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LoggedTracer {
    private LoggedTracer() {}

    private static double startTime = -1.0;

    public static void reset() {
        startTime = Timer.getFPGATimestamp();
    }

    public static void record(String epochName) {
        double now = Timer.getFPGATimestamp();
        SmartDashboard.putNumber(
                "Logged Tracer/" + epochName + " Milliseconds", Units.secondsToMilliseconds(now - startTime));
        startTime = now;
    }
}