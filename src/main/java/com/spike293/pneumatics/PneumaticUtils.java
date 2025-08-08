package com.spike293.pneumatics;

import com.spike293.config.SpikeLibConfig;
import com.spike293.config.subconfigs.SpikePneumaticsConfig;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class PneumaticUtils {
    private static final SpikePneumaticsConfig config = SpikeLibConfig.getOrLoadConfig().getPneumaticsConfig();

    public static DoubleSolenoid createDoubleSolenoid(int forwardChannel, int reverseChannel) {
        return new DoubleSolenoid(config.getModuleType(), forwardChannel, reverseChannel);
    }

    public static Solenoid createSingleSolenoid(int channel) {
        return new Solenoid(config.getModuleType(), channel);
    }
}
