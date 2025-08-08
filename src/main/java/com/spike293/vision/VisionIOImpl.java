package com.spike293.vision;

import com.spike293.vision.limelights.Limelight;
import com.spike293.vision.limelights.PoseValidator;
import com.spike293.vision.limelights.VisionManager;

import java.util.ArrayList;
import java.util.List;

public class VisionIOImpl implements VisionIO {
    @Override
    public void updateInputs(VisionIOInputs inputs) {
        List<Limelight> limelights = VisionManager.getLimelights();
        List<VisionEstimate> estimates = new ArrayList<>();

        limelights.forEach(limelight -> {
            VisionEstimate estimate = limelight.getVisionEstimate();

            if (PoseValidator.isPoseValid(estimate)) {
                // pose is valid; add it to the list
                estimates.add(limelight.getVisionEstimate());
            }
        });

        inputs.estimates = estimates.toArray(new VisionEstimate[0]);
        inputs.connectedLimelightIds = limelights.stream().map(Limelight::getId).toArray(String[]::new);
    }
}
