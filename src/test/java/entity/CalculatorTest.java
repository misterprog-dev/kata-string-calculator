package entity;

import exception.InvalidPositionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void should_return_zero_when_empty_input() throws InvalidPositionException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("");

        // Then
        assertEquals(sum, "0");
    }

    @Test
    void should_return_number_when_one_input_number() throws InvalidPositionException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("1");

        // Then
        assertEquals(sum, "1");
    }

    @Test
    void should_return_sum_when_two_input_number() throws InvalidPositionException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("1.1,2.2");

        // Then
        assertEquals(sum, "3.3");
    }

    @Test
    void should_return_sum_when_many_input_number() throws InvalidPositionException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("1.1,2.2,3,4,2.3");

        // Then
        assertEquals(sum, "12.6");
    }

    @Test
    void should_return_sum_when_new_line_separator() throws InvalidPositionException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("1\n2,3");

        // Then
        assertEquals(sum, "6");
    }

    @Test
    void should_return_exception_for_invalid_position() {
        // Given
        Calculator calculator = new Calculator();

        // When

        Exception exception = assertThrows(InvalidPositionException.class, () ->
            calculator.add("175.2,\n35")
        );
        // Then
        assertEquals(exception.getMessage(), "Number expected but '\\n' found at position 6.");
    }
}