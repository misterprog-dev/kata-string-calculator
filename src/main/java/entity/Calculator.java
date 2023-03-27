package entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static java.math.BigDecimal.ZERO;
import static java.util.Arrays.stream;

public class Calculator {
    private static final DecimalFormat df = new DecimalFormat("0");
    public static final String EMPTY_INPUT = "";
    public static final String ALL_SEPARATOR = ",|\n";
    public static final String ZERO_STRING = "0";

    String add(String number) {
        if(EMPTY_INPUT.equals(number.trim())) {
            return ZERO_STRING;
        }

        String[] numbers = number.split(ALL_SEPARATOR);

        return stream(numbers)
                .map(BigDecimal::new)
                .reduce(ZERO, BigDecimal::add).toString();
    }
}
