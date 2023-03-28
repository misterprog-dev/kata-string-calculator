package entity;

import exception.InvalidPositionException;
import exception.MissingNumberException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.math.BigDecimal.ZERO;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBetween;

public class Calculator {
    private static final String EMPTY_INPUT = "";
    private static final String COMMA_SEPARATOR = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String DELIMITER_BEGIN = "//";
    private static final String DELIMITER_END = "\n";
    private static final Set<String> LIST_OF_SEPARATORS = Set.of(COMMA_SEPARATOR, NEW_LINE_SEPARATOR);
    private static final String ALL_SEPARATOR = COMMA_SEPARATOR + "|" + NEW_LINE_SEPARATOR;
    private static final String ZERO_STRING = "0";
    private static final String NEW_LINE_FOR_EXCEPTION = "\\n";

    String add(String number) throws InvalidPositionException, MissingNumberException {
        if (EMPTY_INPUT.equals(number.trim())) {
            return ZERO_STRING;
        }

        return stream(extractNumbers(number))
                .map(BigDecimal::new)
                .reduce(ZERO, BigDecimal::add).toString();
    }

    private static String[] extractNumbers(String number) throws MissingNumberException, InvalidPositionException {
        String delimiter = getDelimiter(number);
        String finalNumber = getFinalNumber(number);
        validation(delimiter, finalNumber);

        if (delimiter != null) {
            return finalNumber.split(delimiter);
        }

        return finalNumber.split(ALL_SEPARATOR);
    }

    private static String getDelimiter(String number) {
        if (number.startsWith(DELIMITER_BEGIN)) {
            return substringBetween(number, DELIMITER_BEGIN, DELIMITER_END);
        }
        return null;
    }

    private static String getFinalNumber(String number) {
        if (number.startsWith(DELIMITER_BEGIN)) {
            return substringAfter(number, DELIMITER_END);
        }
        return number;
    }

    private static void validation(String delimiter, String finalNumber) throws MissingNumberException, InvalidPositionException {
        validateLastPosition(delimiter, finalNumber);
        validateInput(delimiter, finalNumber);
    }

    private static void validateLastPosition(String delimiter, String number) throws MissingNumberException {
        if (hasLastPositionSeparator(delimiter, number)) {
            throw new MissingNumberException();
        }
    }

    private static boolean hasLastPositionSeparator(String delimiter, String number) {
        List<String> listOfAllSeparators = new ArrayList<>(LIST_OF_SEPARATORS);
        listOfAllSeparators.add(delimiter);
        return !listOfAllSeparators.stream()
                .filter(Objects::nonNull)
                .filter(separator -> number.lastIndexOf(separator) == number.length() - 1)
                .collect(toList()).isEmpty();
    }

    private static void validateInput(String delimiter, String number) throws InvalidPositionException {
        int indexOfComma = number.indexOf(COMMA_SEPARATOR);
        int indexOfNewLine = number.indexOf(NEW_LINE_SEPARATOR);

        if (delimiter == null && isSeparatorAligned(indexOfComma, indexOfNewLine)) {
            invalidMessageForSeparatorSuccessive(indexOfComma, indexOfNewLine);
        }
    }

    private static boolean isSeparatorAligned(int indexOfComma, int indexOfNewLine) {
        return indexOfComma + 1 == indexOfNewLine || indexOfNewLine + 1 == indexOfComma;
    }

    private static void invalidMessageForSeparatorSuccessive(int indexOfComma, int indexOfNewLine) throws InvalidPositionException {
        int position = max(indexOfComma, indexOfNewLine);
        String separator = indexOfComma == position ? COMMA_SEPARATOR : NEW_LINE_FOR_EXCEPTION;
        throw new InvalidPositionException("Number expected but '" + separator + "' found at position " + position + ".");
    }
}
