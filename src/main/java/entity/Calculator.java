package entity;

import exception.InvalidPositionException;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static java.lang.Math.max;
import static java.math.BigDecimal.ZERO;
import static java.util.Arrays.stream;

public class Calculator {
    private static final DecimalFormat df = new DecimalFormat("0");
    public static final String EMPTY_INPUT = "";
    private static final String COMMA_SEPARATOR = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    public static final String ALL_SEPARATOR = COMMA_SEPARATOR + "|" + NEW_LINE_SEPARATOR;
    public static final String ZERO_STRING = "0";
    public static final String NEW_LINE_FOR_EXCEPTION = "\\n";

    String add(String number) throws InvalidPositionException {
        if(EMPTY_INPUT.equals(number.trim())) {
            return ZERO_STRING;
        }

        validateInput(number);

        String[] numbers = number.split(ALL_SEPARATOR);

        return  stream(numbers)
                .map(BigDecimal::new)
                .reduce(ZERO, BigDecimal::add).toString();
    }

    private static void validateInput(String number) throws InvalidPositionException {
        int indexOfComma = number.indexOf(COMMA_SEPARATOR);
        int indexOfNewLine = number.indexOf(NEW_LINE_SEPARATOR);

        if (isSeparatorAligned(indexOfComma, indexOfNewLine)) {
            invalidMessageForSeparatorSuccessive(indexOfComma, indexOfNewLine);
        }
    }

    private static void invalidMessageForSeparatorSuccessive(int indexOfComma, int indexOfNewLine) throws InvalidPositionException {
        int position = max(indexOfComma, indexOfNewLine);
        String separator = indexOfComma == position ? COMMA_SEPARATOR : NEW_LINE_FOR_EXCEPTION;
        throw new InvalidPositionException("Number expected but '" + separator + "' found at position " + position + ".");
    }

    private static boolean isSeparatorAligned(int indexOfComma, int indexOfNewLine) {
        return indexOfComma + 1 == indexOfNewLine || indexOfNewLine + 1 == indexOfComma;
    }
}
