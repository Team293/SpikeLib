package com.spike293.vision;

import com.spike293.subsystem.SpikeSubsystem;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Vision extends SpikeSubsystem {
    private final VisionIO io;
    private final VisionIOInputsAutoLogged inputs;
    private final BiConsumer<Pose2d, Double> poseConsumer;

    public Vision(BiConsumer<Pose2d, Double> poseConsumer) {
        super("Vision");

        this.io = new VisionIOImpl();
        this.inputs = new VisionIOInputsAutoLogged();
        this.poseConsumer = poseConsumer;
    }

    @Override
    public void onPeriodic() {
        for (VisionEstimate estimate : inputs.estimates) {
            poseConsumer.accept(
                    estimate.poseEstimate(),
                    estimate.timestamp()
            );
        }
    }

    @Override
    protected Runnable setupDataRefresher() {
        return useDataRefresher(inputs, io);
    }
}
