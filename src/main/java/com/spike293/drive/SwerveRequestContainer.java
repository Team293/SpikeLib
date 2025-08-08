package com.spike293.drive;

import com.ctre.phoenix6.swerve.SwerveModule;
import com.ctre.phoenix6.swerve.SwerveRequest;

public class SwerveRequestContainer {

    public SwerveRequestContainer(double maxSpeed, double maxAngularRate) {
        drive = new SwerveRequest.FieldCentric()
                .withDeadband(maxSpeed * 0.1).withRotationalDeadband(maxAngularRate * 0.1) // Add a 10% deadband
                .withDriveRequestType(SwerveModule.DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors

        brake = new SwerveRequest.SwerveDriveBrake();
        point = new SwerveRequest.PointWheelsAt();
    }

    /* Setting up bindings for necessary control of the swerve drive platform */
    public final SwerveRequest.FieldCentric drive;

    public final SwerveRequest.SwerveDriveBrake brake;
    public final SwerveRequest.PointWheelsAt point;
}

