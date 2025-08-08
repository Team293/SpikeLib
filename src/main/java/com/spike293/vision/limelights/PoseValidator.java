package com.spike293.vision.limelights;

import com.spike293.config.SpikeLibConfig;
import com.spike293.config.subconfigs.vision.SpikeVisionConfig;
import com.spike293.vision.VisionEstimate;
import edu.wpi.first.wpilibj.Timer;

public class PoseValidator {
    public static final SpikeVisionConfig config = SpikeLibConfig.getOrLoadConfig().getVisionConfig();

    public static boolean isPoseValid(VisionEstimate estimate) {
        if (estimate == null) return false;

        if (estimate.tagCount() < config.getMinTagCountValidation()) return false;

        double currentTime = Timer.getFPGATimestamp();
        double age = currentTime - estimate.timestamp();
        if (age > config.getMaxLatencySecValidation()) return false;

        if (estimate.tagSpan() < config.getMinTagSpanValidation()) return false;

        if (estimate.avgTagArea() < config.getMinAvgTagAreaValidation()) return false;

        if (estimate.avgTagDist() > config.getMaxAvgTagDistMetersValidation()) return false;

        return true;
    }

}
