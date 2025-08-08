package com.spike293.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Subsystem;

public interface PathPlannerSwerve extends Subsystem {
    void driveRobotRelative(ChassisSpeeds chassisSpeeds, SwerveRequestContainer requests);
    void resetPose(Pose2d pose2d);
    Pose2d getPose();
    ChassisSpeeds getChassisSpeeds();
}
