package com.spike293;

import com.spike293.config.SpikeLibConfig;
import com.spike293.config.subconfigs.SpikeLoggingConfig;
import com.spike293.controller.SpikeController;
import com.spike293.telemetry.LoggedTracer;
import edu.wpi.first.wpilibj.Threads;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.function.Supplier;

public class SpikeRobot<C extends SpikeContainer> extends LoggedRobot {
    private static final SpikeLibConfig config = SpikeLibConfig.getOrLoadConfig();
    private static final SpikeLoggingConfig loggingConfig = config.getLoggingConfig();

    private static final boolean IS_PRACTICE = loggingConfig.isPracticeMode();
    private static final String LOG_DIRECTORY = loggingConfig.getLogFilePath();
    private static final long MIN_FREE_SPACE = IS_PRACTICE
            ? 100000000
            : // 100 MB
            1000000000; // 1 GB

    private Command autonomousCommand;
    private C spikeContainer;

    private final Supplier<C> containerSupplier;

    public SpikeRobot(Class<C> spikeContainerClass) {
        this.containerSupplier = () -> {
            try {
                return spikeContainerClass.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to instantiate container class: " + spikeContainerClass.getName(), e);
            }
        };
    }

    @Override
    public void robotInit() {
        setupLogger();
        setupReceiver();

        Logger.start();

        this.spikeContainer = containerSupplier.get();
    }

    @Override
    public void robotPeriodic() {
        LoggedTracer.reset();

        try {
            Threads.setCurrentThreadPriority(true, 4);
            double commandSchedulerStart = Timer.getTimestamp();
            CommandScheduler.getInstance().run();
            double commandSchedulerEnd = Timer.getTimestamp();
            LoggedTracer.record("Commands");
            SmartDashboard.putNumber(
                    "Logged Robot/Loop Cycle Time Milliseconds",
                    (commandSchedulerEnd - commandSchedulerStart) * 1000.0);
            Threads.setCurrentThreadPriority(false, 0);
        } catch (Exception e) {
            SmartDashboard.putString("Logged Robot/Latest Error", e.getMessage());
        }
    }

    @Override
    public void autonomousInit() {
        this.autonomousCommand = this.spikeContainer.getAutoCommand();

        if (this.autonomousCommand != null) {
            CommandScheduler.getInstance().schedule(this.autonomousCommand);
        }
    }

    @Override
    public void teleopInit() {
        if (this.autonomousCommand != null) {
            this.autonomousCommand.cancel();
        }
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    private void setupLogger() {
        // setup logging directory
        var dir = new File(LOG_DIRECTORY);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        // ensure we have enough free space on the logging directory
        if (dir.getFreeSpace() < MIN_FREE_SPACE) {
            System.err.println("Not enough free space on logging directory. Attempting to clear space.");

            // delete old logs
            var files = dir.listFiles();
            if (files != null) {
                // sorted by name, which will order by creation date
                files = Arrays.stream(files).sorted().toArray(File[]::new);

                long bytesToDelete = MIN_FREE_SPACE - dir.getFreeSpace();

                // delete files until we have enough free space
                for (File file : files) {
                    // ensure we only delete .wpilog files
                    if (file.getName().endsWith(".wpilog")) {
                        try {
                            bytesToDelete -= Files.size(file.toPath());
                        } catch (IOException e) {
                            System.err.println("Failed to get size of file: " + file.getName());
                            continue; // skip this file
                        }

                        if (file.delete()) {
                            System.out.println("Deleted file: " + file.getName());
                        } else {
                            System.err.println("Failed to delete file: " + file.getName());
                        }

                        // stop once we have enough free space
                        if (bytesToDelete <= 0) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private void setupReceiver() {
        switch (config.getRobotMode()) {
            case REAL -> {
                Logger.addDataReceiver(new WPILOGWriter(LOG_DIRECTORY));
                Logger.addDataReceiver(new NT4Publisher());
            }
            case SIM -> Logger.addDataReceiver(new NT4Publisher());
            case REPLAY -> {
                setUseTiming(false);
                String logPath = LogFileUtil.findReplayLog();
                Logger.setReplaySource(new WPILOGReader(logPath));
                Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
            }
        }
    }
}