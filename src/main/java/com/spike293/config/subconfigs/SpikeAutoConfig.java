package com.spike293.config.subconfigs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The pathplanner configuration
 */
@Getter
@AllArgsConstructor
public class SpikeAutoConfig {
    @JsonProperty("translation_p")
    private final double translationP;

    @JsonProperty("translation_i")
    private final double translationI;

    @JsonProperty("translation_d")
    private final double translationD;


    @JsonProperty("rotation_p")
    private final double rotationP;

    @JsonProperty("rotation_i")
    private final double rotationI;

    @JsonProperty("rotation_d")
    private final double rotationD;
}
