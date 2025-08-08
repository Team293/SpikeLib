package com.spike293.vision;

import com.spike293.subsystem.SpikeSubsystem;
import edu.wpi.first.math.geometry.Pose2d;

import java.util.function.Consumer;

public class Vision extends SpikeSubsystem {
    private final VisionIO io;
    private final VisionIOInputsAutoLogged inputs;
    private final Consumer<Pose2d> positionCallback;

    public Vision(Consumer<Pose2d> positionCallback) {
        super("Vision");

        this.io = new VisionIOImpl();
        this.inputs = new VisionIOInputsAutoLogged();
        this.positionCallback = positionCallback;
    }

    @Override
    public void onPeriodic() {

    }

    @Override
    protected Runnable setupDataRefresher() {
        return useDataRefresher(inputs, io);
    }
}
