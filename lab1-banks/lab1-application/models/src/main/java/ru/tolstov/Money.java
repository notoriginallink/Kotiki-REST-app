package ru.tolstov;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Money {
    private Double value;
    public Money(double value) {
        this.value = value;
    }
    public Money(Money other) {
        this.value = other.getValue();
    }
    public void increase(double value) {
        this.value += value;
    }
    public void increase(Money other) {
        this.value += other.getValue();
    }

    public void decrease(double value) {
        this.value -= value;
    }
    public void decrease(Money other) {
        this.value -= other.getValue();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
