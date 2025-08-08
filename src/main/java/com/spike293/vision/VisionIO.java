package com.spike293.vision;

import com.spike293.subsystem.BaseIO;
import com.spike293.subsystem.BaseInputClass;
import org.littletonrobotics.junction.AutoLog;

public interface VisionIO extends BaseIO<VisionIO.VisionIOInputs> {
    @AutoLog
    class VisionIOInputs extends BaseInputClass {
        public VisionEstimate[] estimates;
        public String[] connectedLimelightIds;
    }
}
