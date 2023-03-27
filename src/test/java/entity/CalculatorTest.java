package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void should_return_zero_when_empty_input() {
        //GIVEN
        Calculator calculator = new Calculator();

        //WHEN
        String sum = calculator.add("");

        //THEN
        assertEquals(sum, "0");
    }
}