package com.spike293.config.subconfigs.vision;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The configuration for a Limelight.
 */
@Getter
@AllArgsConstructor
public class LimelightConfigObject {
    private final String id;
    private final double translationX;
    private final double translationY;
    private final double translationZ;
    private final double rotationX;
    private final double rotationY;
    private final double rotationZ;
}
