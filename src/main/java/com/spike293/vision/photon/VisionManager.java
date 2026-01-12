package com.spike293.vision.photon;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VisionManager {
    @Getter
    private static final List<Camera> cameras = new ArrayList<>();

    @Getter
    private static final AprilTagFieldLayout layout;

    static {
        try {
            layout = AprilTagFieldLayout.loadFromResource(AprilTagFields.kDefaultField.m_resourceFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addCamera(Camera camera) {
        cameras.add(camera);
    }

    public static Camera getCamera(String id) {
        return cameras.stream()
                .filter(camera -> camera.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Camera with id " + id + " not found"));
    }
}
