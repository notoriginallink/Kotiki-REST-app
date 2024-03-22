package ru.tolstov;

import lombok.EqualsAndHashCode;
import lombok.Value;

public abstract class ServiceOperationResult {
    private ServiceOperationResult() { }
    public static final class Success extends ServiceOperationResult { }

    @EqualsAndHashCode(callSuper = true)
    @Value
    public static class Fail extends ServiceOperationResult {
        String message;
    }
}
