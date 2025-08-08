package com.spike293.pneumatics.subsystem;

import com.spike293.pneumatics.CompressorStatus;
import com.spike293.subsystem.SpikeSubsystem;

public class Pneumatics extends SpikeSubsystem {
    private final PneumaticsIO io;
    private final PneumaticsIOInputsAutoLogged inputs;

    public Pneumatics() {
        super("Pneumatics");

        io = new PneumaticsIOCompressor();
        inputs = new PneumaticsIOInputsAutoLogged();
    }

    @Override
    protected Runnable setupDataRefresher() {
        return useDataRefresher(inputs, io);
    }

    public void enableCompressor() {
        io.setCompressorStatus(CompressorStatus.ENABLED);
    }

    public void disableCompressor() {
        io.setCompressorStatus(CompressorStatus.DISABLED);
    }

    public double getPressure() {
        return inputs.compressorPressurePsi;
    }

    public boolean isCompressorEnabled() {
        return inputs.compressorEnabled;
    }
}
