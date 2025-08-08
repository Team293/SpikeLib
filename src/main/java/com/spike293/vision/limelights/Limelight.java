package com.spike293.vision.limelights;

import com.spike293.vision.VisionEstimate;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;
import lombok.Data;

import java.util.Arrays;

@Data
public class Limelight {
    private final String id;
    private final Transform3d limelightToRobot;
    private final double pipelineIndex;

    public Limelight(String id, Transform3d limelightToRobot, double pipelineIndex) {
        this.id = id;
        this.limelightToRobot = limelightToRobot;
        this.pipelineIndex = pipelineIndex;

        // apply config
        LimelightHelpers.setPipelineIndex(id, (int) pipelineIndex);
        LimelightHelpers.setCameraPose_RobotSpace(
                id,
                limelightToRobot.getTranslation().getX(),
                limelightToRobot.getTranslation().getY(),
                limelightToRobot.getTranslation().getZ(),
                limelightToRobot.getRotation().getX(),
                limelightToRobot.getRotation().getY(),
                limelightToRobot.getRotation().getZ()
        );
    }

    public final VisionEstimate getVisionEstimate() {
        LimelightHelpers.PoseEstimate poseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(id);

        double tx = LimelightHelpers.getTX(id);
        double ty = LimelightHelpers.getTY(id);
        double ta = LimelightHelpers.getTA(id);
        int tagCount = poseEstimate.tagCount;
        double pipelineIndex = LimelightHelpers.getCurrentPipelineIndex(id);
        double latencyMs = poseEstimate.latency;
        double timestamp = poseEstimate.timestampSeconds;
        int[] tagIds = Arrays.stream(poseEstimate.rawFiducials).mapToInt(f -> f.id).toArray();
        double tagSpan = poseEstimate.tagSpan;
        double avgTagArea = poseEstimate.avgTagArea;
        double avgTagDist = poseEstimate.avgTagDist;
        Pose2d pose = poseEstimate.pose;

        return new VisionEstimate(
                tx,
                ty,
                ta,
                tagCount,
                pipelineIndex,
                latencyMs,
                timestamp,
                tagIds,
                tagSpan,
                avgTagArea,
                avgTagDist,
                pose,
                id
        );
    }
}
