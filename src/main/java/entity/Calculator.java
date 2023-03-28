package entity;

import exception.InvalidPositionException;
import exception.MissingNumberException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Set;

import static java.lang.Math.max;
import static java.math.BigDecimal.ZERO;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class Calculator {
    private static final String EMPTY_INPUT = "";
    private static final String COMMA_SEPARATOR = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    private static final Set<String> LIST_OF_SEPARATORS = Set.of(COMMA_SEPARATOR, NEW_LINE_SEPARATOR);
    private static final String ALL_SEPARATOR = COMMA_SEPARATOR + "|" + NEW_LINE_SEPARATOR;
    private static final String ZERO_STRING = "0";
    private static final String NEW_LINE_FOR_EXCEPTION = "\\n";

    String add(String number) throws InvalidPositionException, MissingNumberException {
        if(EMPTY_INPUT.equals(number.trim())) {
            return ZERO_STRING;
        }
        validateLastPosition(number);

        validateInput(number);

        String[] numbers = number.split(ALL_SEPARATOR);

        return  stream(numbers)
                .map(BigDecimal::new)
                .reduce(ZERO, BigDecimal::add).toString();
    }

    private static void validateLastPosition(String number) throws MissingNumberException {
        if(hasLastPositionSeparator(number)) {
            throw new MissingNumberException();
        }
    }

    private static boolean hasLastPositionSeparator(String number) {
        return !LIST_OF_SEPARATORS.stream()
                .filter(separator -> number.lastIndexOf(separator) == number.length() - 1)
                .collect(toList()).isEmpty();
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
