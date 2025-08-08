package com.spike293.pneumatics.subsystem;

import com.spike293.pneumatics.CompressorStatus;
import com.spike293.subsystem.BaseIO;
import com.spike293.subsystem.BaseInputClass;
import org.littletonrobotics.junction.AutoLog;

public interface PneumaticsIO extends BaseIO<PneumaticsIO.PneumaticsIOInputs> {
    @AutoLog
    class PneumaticsIOInputs extends BaseInputClass {
        public boolean compressorEnabled;
        public double compressorPressurePsi;
    }

    void setCompressorStatus(CompressorStatus compressorStatus);
}
