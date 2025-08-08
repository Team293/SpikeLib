package com.spike293.vision;

import edu.wpi.first.math.geometry.Pose2d;

public record VisionEstimate(
        double tx,
        double ty,
        double ta,
        int tagCount,
        double pipelineIndex,
        double latencyMs,
        double timestamp,
        int[] targetIds,
        double tagSpan,
        double avgTagArea,
        double avgTagDist,
        Pose2d poseEstimate,
        String limelightId
) {}