package com.spike293.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spike293.config.subconfigs.SpikeAutoConfig;
import com.spike293.config.subconfigs.SpikeLoggingConfig;
import com.spike293.config.subconfigs.SpikePneumaticsConfig;
import com.spike293.config.subconfigs.vision.SpikeVisionConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * SpikeLibConfig is a configuration class for the SpikeLib library.
 * It contains various sub-configurations such as robot mode, logging, auto, pneumatics
 */
@Getter
@AllArgsConstructor
public class SpikeLibConfig {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static SpikeLibConfig LOADED_CONFIG = null;

    @JsonProperty("robot_mode")
    private final RobotMode robotMode;

    @JsonProperty("logging")
    private final SpikeLoggingConfig loggingConfig;

    @JsonProperty("auto")
    private final SpikeAutoConfig autoConfig;

    @JsonProperty("pneumatics")
    private final SpikePneumaticsConfig pneumaticsConfig;

    @JsonProperty("vision")
    private final SpikeVisionConfig visionConfig;

    /**
     * Gets the already loaded config or tries to load it if it doesn't exist.
     * @return SpikeLibConfig object with loaded config values
     */
    public static SpikeLibConfig getOrLoadConfig() {
        if (LOADED_CONFIG != null) { // If loaded config already exists, return it
            return LOADED_CONFIG;
        }

        try {
            var file = new File("src/main/deploy/spike-config.json"); // Tries to load file
            var config = MAPPER.readValue(file, SpikeLibConfig.class); // Attempts to read json files values
            LOADED_CONFIG = config;

            return config; // Return config
        } catch (Exception e) {
            // If fails to load, raise error
            throw new RuntimeException(
                    "Failed to load config file. Please make sure that the file exists and the values are fully populated.",
                    e
            );
        }
    }
}
