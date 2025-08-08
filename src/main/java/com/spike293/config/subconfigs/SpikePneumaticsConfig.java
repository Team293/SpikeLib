package com.spike293.config.subconfigs;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The configuration for the pneumatics subsystem.
 */
@Getter
@AllArgsConstructor
public class SpikePneumaticsConfig {
    @JsonProperty("min_pressure")
    private final int minPressure;

    @JsonProperty("max_pressure")
    private final int maxPressure;

    @JsonProperty("compressor_hub_id")
    private final int compressorHubId;

    @JsonProperty("use_pneumatics")
    private final boolean usePneumatics;

    @JsonProperty("module_type")
    private final PneumaticsModuleType moduleType;
}
