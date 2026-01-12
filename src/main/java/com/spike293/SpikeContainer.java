package com.spike293;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.spike293.config.subconfigs.SpikeAutoConfig;
import com.spike293.config.SpikeLibConfig;
import com.spike293.config.subconfigs.SpikePneumaticsConfig;
import com.spike293.config.subconfigs.vision.CameraConfigObject;
import com.spike293.config.subconfigs.vision.SpikeVisionConfig;
import com.spike293.drive.PathPlannerSwerve;
import com.spike293.drive.SwerveRequestContainer;
import com.spike293.pneumatics.subsystem.Pneumatics;
import com.spike293.vision.photon.Camera;
import com.spike293.vision.photon.VisionManager;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import lombok.Getter;

public abstract class SpikeContainer {
    private static final SpikePneumaticsConfig pneumaticsConfig = SpikeLibConfig.getOrLoadConfig().getPneumaticsConfig();
    private static final SpikeAutoConfig autoConfig = SpikeLibConfig.getOrLoadConfig().getAutoConfig();
    private static final SpikeVisionConfig visionConfig = SpikeLibConfig.getOrLoadConfig().getVisionConfig();

    @Getter
    protected SwerveRequestContainer driveRequests;

    private SendableChooser<Command> autoChooser;

    protected Pneumatics pneumatics;

    public SpikeContainer() {
        configureBindings();

        if (pneumaticsConfig.isUsePneumatics()) {
            this.pneumatics = new Pneumatics();
        }

        initializeCameras();
    }

    protected void setupDrivetrain(PathPlannerSwerve swerve, double maxSpeed, double maxAngularRate) {
        this.driveRequests = new SwerveRequestContainer(maxSpeed, maxAngularRate);

        // setup path planner
        RobotConfig robotConfig;

        try {
            robotConfig = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            System.err.println("Failed to load path planner config.");
            return;
        }

        AutoBuilder.configure(
                swerve::getPose,
                swerve::resetPose,
                swerve::getChassisSpeeds,
                (speeds, f) -> swerve.driveRobotRelative(speeds, this.driveRequests),
                new PPHolonomicDriveController(
                        new PIDConstants(
                                autoConfig.getTranslationP(),
                                autoConfig.getTranslationI(),
                                autoConfig.getTranslationD()
                        ), // translation
                        new PIDConstants(
                                autoConfig.getRotationP(),
                                autoConfig.getRotationI(),
                                autoConfig.getRotationD()
                        ) // rotation
                ),
                robotConfig,
                () -> {
                    var alliance = DriverStation.getAlliance();
                    return alliance.filter(value -> value == DriverStation.Alliance.Red).isPresent();
                },
                swerve
        );

        this.autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    private void initializeCameras() {
        for (CameraConfigObject configCamera : visionConfig.getLimelights()) {
            Transform3d cameraToRobot = new Transform3d(
                    configCamera.getTranslationX(),
                    configCamera.getTranslationY(),
                    configCamera.getTranslationZ(),
                    new Rotation3d(
                            configCamera.getRotationX(),
                            configCamera.getRotationY(),
                            configCamera.getRotationZ()
                    )
            );

            Camera camera = new Camera(
                    configCamera.getId(),
                    cameraToRobot
            );

            VisionManager.addCamera(camera);
        }
    }

    public Command getAutoCommand() {
        return autoChooser.getSelected();
    }

    protected abstract void configureBindings();
}
