package ru.tolstov.accounts;

import lombok.Data;
import lombok.EqualsAndHashCode;

public abstract class AccountOperationResult {
    private AccountOperationResult() { }
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static final class Success extends AccountOperationResult {
        private final Account account;
    }
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static final class Fail extends AccountOperationResult {
        private final String message;
        public Fail(String message) {
            this.message = message;
        }
    }
}

