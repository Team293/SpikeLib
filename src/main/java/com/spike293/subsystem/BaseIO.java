package com.spike293.subsystem;

public interface BaseIO<I extends BaseInputClass> {
    void updateInputs(I inputs);
}
