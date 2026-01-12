package com.spike293.vision;

import com.spike293.subsystem.SpikeSubsystem;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Vision extends SpikeSubsystem {
    private final VisionIO io;
    private final VisionIOInputsAutoLogged inputs;
    private final Supplier<Rotation2d> gyroSupplier;
    private final SwerveDrivePoseEstimator estimator;
    private final Supplier<SwerveModulePosition[]> positionSupplier;

    public Vision(Supplier<Rotation2d> gyroSupplier, Supplier<SwerveModulePosition[]> positionSupplier, SwerveDrivePoseEstimator estimator) {
        super("Vision");

        this.io = new VisionIOImpl();
        this.inputs = new VisionIOInputsAutoLogged();
        this.gyroSupplier = gyroSupplier;
        this.estimator = estimator;
        this.positionSupplier = positionSupplier;
    }

    @Override
    public void onPeriodic() {
        for (VisionEstimate estimate : inputs.estimates) {
            estimator.addVisionMeasurement(
                    estimate.poseEstimate(),
                    estimate.timestamp()
            );

            estimator.updateWithTime(System.currentTimeMillis(), gyroSupplier.get(), positionSupplier.get());
        }
    }

    @Override
    protected Runnable setupDataRefresher() {
        return useDataRefresher(inputs, io);
    }
}
