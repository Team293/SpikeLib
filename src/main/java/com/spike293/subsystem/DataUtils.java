package com.spike293.subsystem;

public class DataUtils {
    public static <I extends BaseInputClass, T extends BaseIO<I> & IORefresher> void createDataLogger(
            I inputs, T baseIO
    ) {
        SubsystemDataProcessor.createAndStartSubsystemDataProcessor(
                () -> {
                    synchronized (inputs) {
                        baseIO.updateInputs(inputs);
                    }
                },
                baseIO
        );
    }
}
