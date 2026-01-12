package com.spike293.config.subconfigs.vision;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The configuration for the vision subsystem.
 */
@Getter
@AllArgsConstructor
public class SpikeVisionConfig {
    @JsonProperty("min_tag_count_validation")
    private final double minTagCountValidation;

    @JsonProperty("max_latency_sec_validation")
    private final double maxLatencySecValidation;

    @JsonProperty("min_tag_span_validation")
    private final double minTagSpanValidation;

    @JsonProperty("min_avg_tag_area_validation")
    private final double minAvgTagAreaValidation;

    @JsonProperty("max_avg_tag_dist_meters_validation")
    private final double maxAvgTagDistMetersValidation;

    @JsonProperty("limelights")
    private final CameraConfigObject[] limelights;

    @JsonProperty("default_pipeline_index")
    private final int defaultPipelineIndex;
}
