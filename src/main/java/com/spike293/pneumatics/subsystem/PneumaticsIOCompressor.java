package com.spike293.pneumatics.subsystem;

import com.spike293.config.SpikeLibConfig;
import com.spike293.config.subconfigs.SpikePneumaticsConfig;
import com.spike293.pneumatics.CompressorStatus;
import edu.wpi.first.wpilibj.PneumaticHub;

public class PneumaticsIOCompressor implements PneumaticsIO {
    private static final SpikePneumaticsConfig config = SpikeLibConfig.getOrLoadConfig().getPneumaticsConfig();

    private final PneumaticHub hub;

    public PneumaticsIOCompressor() {
        this.hub = new PneumaticHub(config.getCompressorHubId());
    }

    @Override
    public void setCompressorStatus(CompressorStatus compressorStatus) {
        if (config.isUsePneumatics()) {
            switch (compressorStatus) {
                case ENABLED -> hub.enableCompressorAnalog(
                        config.getMinPressure(),
                        config.getMaxPressure()
                );
                case DISABLED -> hub.disableCompressor();
            }
        }
    }

    @Override
    public void updateInputs(PneumaticsIOInputs inputs) {
        inputs.compressorEnabled = hub.getCompressor();
        inputs.compressorPressurePsi = hub.getPressure(0);
    }
}
