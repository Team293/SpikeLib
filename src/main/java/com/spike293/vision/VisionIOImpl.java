package com.spike293.vision;

import com.spike293.vision.photon.Camera;
import com.spike293.vision.photon.PoseValidator;
import com.spike293.vision.photon.VisionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VisionIOImpl implements VisionIO {
    @Override
    public void updateInputs(VisionIOInputs inputs) {
        List<Camera> cameras = VisionManager.getCameras();
        List<VisionEstimate> estimates = new ArrayList<>();

        cameras.forEach(camera -> {
            Optional<VisionEstimate> estimate = camera.getVisionEstimate();

            if (estimate.isEmpty()) {
                return;
            }

            if (PoseValidator.isPoseValid(estimate.get())) {
                // pose is valid; add it to the list
                estimates.add(estimate.get());
            }
        });

        inputs.estimates = estimates.toArray(new VisionEstimate[0]);
        inputs.connectedCameraIds = cameras.stream().map(Camera::getId).toArray(String[]::new);
    }
}
