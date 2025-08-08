package com.spike293.vision.limelights;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class VisionManager {
    @Getter
    private static final List<Limelight> limelights = new ArrayList<>();

    public static void addLimelight(Limelight limelight) {
        limelights.add(limelight);
    }

    public static Limelight getLimelight(String id) {
        return limelights.stream()
                .filter(limelight -> limelight.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Limelight with id " + id + " not found"));
    }
}