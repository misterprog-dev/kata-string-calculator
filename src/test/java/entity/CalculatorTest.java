package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void should_return_zero_when_empty_input() {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("");

        // Then
        assertEquals(sum, "0");
    }

    @Test
    void should_return_number_when_one_input_number() {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("1");

        // Then
        assertEquals(sum, "1");
    }

    @Test
    void should_return_sum_when_two_input_number() {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("1.1,2.2");

        // Then
        assertEquals(sum, "3.3");
    }

    @Test
    void should_return_sum_when_many_input_number() {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("1.1,2.2,3,4,2.3");

        // Then
        assertEquals(sum, "12.6");
    }
}