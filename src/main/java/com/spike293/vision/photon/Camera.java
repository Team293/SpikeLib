package com.spike293.vision.photon;

import com.spike293.vision.VisionEstimate;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;
import lombok.Data;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import java.util.List;
import java.util.Optional;

@Data
public class Camera {
    private final String id;
    private final Transform3d cameraToRobot;
    private transient final PhotonCamera photonCamera;
    private transient final PhotonPoseEstimator photonPoseEstimator;

    public Camera(String id, Transform3d cameraToRobot) {
        this.id = id;
        this.cameraToRobot = cameraToRobot;
        this.photonCamera = new PhotonCamera(id);

        this.photonPoseEstimator = new PhotonPoseEstimator(
                VisionManager.getLayout(),
                PhotonPoseEstimator.PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
                this.cameraToRobot
        );

        this.photonPoseEstimator.setMultiTagFallbackStrategy(PhotonPoseEstimator.PoseStrategy.CLOSEST_TO_REFERENCE_POSE);
    }

    public Optional<VisionEstimate> getVisionEstimate() {
        List<PhotonPipelineResult> results = photonCamera.getAllUnreadResults();
        Optional<EstimatedRobotPose> lastPose = Optional.empty();
        PhotonPipelineResult lastResult = null;

        for (PhotonPipelineResult result : results) {
            lastResult = result;
            lastPose = photonPoseEstimator.update(result, Optional.empty(), Optional.empty());
        }

        if (lastResult == null || lastPose.isEmpty()) {
            return Optional.empty();
        }

        EstimatedRobotPose est = lastPose.get();
        PhotonPipelineResult result = lastResult;

        var targets = result.getTargets();
        int tagCount = targets.size();
        int[] targetIds = targets.stream()
                .mapToInt(PhotonTrackedTarget::getFiducialId)
                .toArray();

        var bestTarget = result.getBestTarget();
        double tx = 0.0;
        double ty = 0.0;
        double ta = 0.0;
        if (bestTarget != null) {
            tx = bestTarget.getYaw();
            ty = bestTarget.getPitch();
            ta = bestTarget.getArea();
        }

        double tagSpan = 0.0;
        double avgTagArea = 0.0;
        double avgTagDist = 0.0;
        if (!targets.isEmpty()) {
            double minYaw = Double.POSITIVE_INFINITY;
            double maxYaw = Double.NEGATIVE_INFINITY;
            double sumArea = 0.0;
            double sumDist = 0.0;
            int distCount = 0;

            for (var t : targets) {
                double yaw = t.getYaw();
                minYaw = Math.min(minYaw, yaw);
                maxYaw = Math.max(maxYaw, yaw);
                sumArea += t.getArea();

                var camToTag = t.getBestCameraToTarget();
                double dist = camToTag.getTranslation().getNorm();
                sumDist += dist;
                distCount++;
            }

            tagSpan = maxYaw - minYaw;
            avgTagArea = sumArea / targets.size();
            avgTagDist = distCount > 0 ? sumDist / distCount : 0.0;
        }

        double timestamp = result.getTimestampSeconds();

        Pose2d pose2d = est.estimatedPose.toPose2d();

        VisionEstimate estimate = new VisionEstimate(
                tx,
                ty,
                ta,
                tagCount,
                timestamp,
                targetIds,
                tagSpan,
                avgTagArea,
                avgTagDist,
                pose2d,
                this.id
        );

        return Optional.of(estimate);
    }
}
