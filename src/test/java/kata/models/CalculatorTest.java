package kata.models;

import kata.exception.InvalidPositionException;
import kata.exception.MissingNumberException;
import kata.exception.MultipleDelimiterException;
import kata.exception.NegativeNumberException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void should_return_zero_when_empty_input() throws InvalidPositionException, MissingNumberException, MultipleDelimiterException, NegativeNumberException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("");

        // Then
        assertEquals(sum, "0");
    }

    @Test
    void should_return_number_when_one_input_number() throws InvalidPositionException, MissingNumberException, MultipleDelimiterException, NegativeNumberException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("1");

        // Then
        assertEquals(sum, "1");
    }

    @Test
    void should_return_sum_when_two_input_number() throws InvalidPositionException, MissingNumberException, MultipleDelimiterException, NegativeNumberException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("1.1,2.2");

        // Then
        assertEquals(sum, "3.3");
    }

    @Test
    void should_return_sum_when_many_input_number() throws InvalidPositionException, MissingNumberException, MultipleDelimiterException, NegativeNumberException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("1.1,2.2,3,4,2.3");

        // Then
        assertEquals(sum, "12.6");
    }

    @Test
    void should_return_sum_when_new_line_separator() throws InvalidPositionException, MissingNumberException, MultipleDelimiterException, NegativeNumberException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("1\n2,3");

        // Then
        assertEquals(sum, "6");
    }

    @ParameterizedTest
    @ValueSource(strings = {"175.2,\n35", "175.2,\n\n35"})
    void should_return_exception_for_invalid_position(String number) {
        // Given
        Calculator calculator = new Calculator();

        // When

        Exception exception = assertThrows(InvalidPositionException.class, () ->
            calculator.add(number)
        );
        // Then
        assertEquals(exception.getMessage(), "Number expected but '\\n' found at position 6.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,3,", "1,", "2,3,4,,"})
    void should_return_exception_for_missing_last_position(String number) {
        // Given
        Calculator calculator = new Calculator();

        // When

        Exception exception = assertThrows(MissingNumberException.class, () ->
                calculator.add(number)
        );
        // Then
        assertEquals(exception.getMessage(), "Number expected but EOF found.");
    }

    @Test
    void should_add_custom_pipe_separator() throws InvalidPositionException, MissingNumberException, MultipleDelimiterException, NegativeNumberException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("//|\n1|2|3");

        // Then
        assertEquals(sum, "6");
    }

    @Test
    void should_add_custom_sep_separator() throws InvalidPositionException, MissingNumberException, MultipleDelimiterException, NegativeNumberException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add("//sep\n2sep3");

        // Then
        assertEquals(sum, "5");
    }

    @ParameterizedTest
    @ValueSource(strings = {"//;\n1;2", "//|\n1|2", "//sep\n2sep1"})
    void should_add_custom_separator(String number) throws InvalidPositionException, MissingNumberException, MultipleDelimiterException, NegativeNumberException {
        // Given
        Calculator calculator = new Calculator();

        // When
        String sum = calculator.add(number);

        // Then
        assertEquals(sum, "3");
    }


    @ParameterizedTest
    @ValueSource(strings = {"//|\n1|2,3"})
    void should_return_exception_for_multiple_delimiters_after_delimiter_defined(String number) {
        // Given
        Calculator calculator = new Calculator();

        // When
        Exception exception = assertThrows(MultipleDelimiterException.class, () ->
                calculator.add(number)
        );
        // Then
        assertEquals(exception.getMessage(), "'|' expected but ',' found at position 3.");
    }

    @Test
    void should_return_exception_for_one_negative_value() {
        // Given
        Calculator calculator = new Calculator();

        // When
        Exception exception = assertThrows(NegativeNumberException.class, () ->
                calculator.add("-1,2")
        );
        // Then
        assertEquals(exception.getMessage(), "Negative not allowed : -1");
    }

    @Test
    void should_return_exception_for_multi_negative_value() {
        // Given
        Calculator calculator = new Calculator();

        // When
        Exception exception = assertThrows(NegativeNumberException.class, () ->
                calculator.add("2,-4,-5")
        );
        // Then
        assertEquals(exception.getMessage(), "Negative not allowed : -4, -5");
    }

    @Test
    void should_return_exception_for_multi_errors_value() {
        // Given
        Calculator calculator = new Calculator();

        // When
        Exception exception = assertThrows(InvalidPositionException.class, () ->
                calculator.add("-1,,2")
        );

        // Then
        assertEquals(exception.getMessage(), "Negative not allowed : -1\nNumber expected but ',' found at position 3.");
    }
}