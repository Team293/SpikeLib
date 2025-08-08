package com.spike293.config.subconfigs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The logging configuration.
 */
@Getter
@AllArgsConstructor
public class SpikeLoggingConfig {
    @JsonProperty("is_practice_mode")
    private final boolean isPracticeMode;

    @JsonProperty("log_file_path")
    private final String logFilePath;
}
