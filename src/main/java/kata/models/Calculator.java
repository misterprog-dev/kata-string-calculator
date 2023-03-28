package kata.models;

import kata.exception.InvalidPositionException;
import kata.exception.MissingNumberException;
import kata.exception.MultipleDelimiterException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.math.BigDecimal.ZERO;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.apache.commons.lang3.StringUtils.*;

public class Calculator {
    private static final String EMPTY_INPUT = "";
    private static final String COMMA_SEPARATOR = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String PIPE_SEPARATOR = "|";
    private static final String DELIMITER_BEGIN = "//";
    private static final String DELIMITER_END = "\n";
    private static final Set<String> LIST_OF_DEFAULT_SEPARATORS = Set.of(COMMA_SEPARATOR, NEW_LINE_SEPARATOR);
    private static final String ZERO_STRING = "0";
    private static final String NEW_LINE_FOR_EXCEPTION = "\\n";
    public static final String REGEX_FOR_ALIGN_SEPARATOR = "(,|\\n){2}";
    private static final String REGEX_PIPE_SEPARATOR = "\\|";
    private static final String REGEX_FOR_DEFAULT_SEPARATORS = COMMA_SEPARATOR + "|" + NEW_LINE_SEPARATOR;


    String add(String number) throws InvalidPositionException, MissingNumberException, MultipleDelimiterException {
        if (EMPTY_INPUT.equals(number.trim())) {
            return ZERO_STRING;
        }

        return stream(extractNumbers(number))
                .map(BigDecimal::new)
                .reduce(ZERO, BigDecimal::add).toString();
    }

    private static String[] extractNumbers(String number) throws MissingNumberException, InvalidPositionException, MultipleDelimiterException {
        String delimiter = getDelimiter(number);
        String finalNumber = getFinalNumber(number);
        validation(delimiter, finalNumber);
        return finalNumber.split(getFinalDelimiter(delimiter));
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

    private static void validation(String delimiter, String finalNumber) throws MissingNumberException, InvalidPositionException, MultipleDelimiterException {
        validateLastPosition(delimiter, finalNumber);
        validateSeparatorSuccessive(finalNumber);
        validateMultipleDelimiter(delimiter, finalNumber);
    }

    private static String getFinalDelimiter(String delimiter) {
        String result = REGEX_FOR_DEFAULT_SEPARATORS;

        if(PIPE_SEPARATOR.equals(delimiter)) {
            result = REGEX_PIPE_SEPARATOR;
        }

        if (delimiter != null && !delimiter.equals(PIPE_SEPARATOR)) {
            result = delimiter;
        }

        return result;
    }

    private static void validateLastPosition(String delimiter, String number) throws MissingNumberException {
        if (hasLastPositionSeparator(delimiter, number)) {
            throw new MissingNumberException();
        }
    }

    private static boolean hasLastPositionSeparator(String delimiter, String number) {
        return !concat(LIST_OF_DEFAULT_SEPARATORS.stream(), Stream.of(delimiter))
                .filter(Objects::nonNull)
                .filter(separator -> endsWith(number, separator))
                .collect(toList()).isEmpty();
    }

    private static void validateSeparatorSuccessive(String number) throws InvalidPositionException {
        int indexOfComma = number.indexOf(COMMA_SEPARATOR);
        int indexOfNewLine = number.indexOf(NEW_LINE_SEPARATOR);
        if (isSeparatorAligned(number)) {
            invalidMessageForSeparatorSuccessive(indexOfComma, indexOfNewLine);
        }
    }

    private static void validateMultipleDelimiter(String delimiter, String number)  throws MultipleDelimiterException {
        if (!LIST_OF_DEFAULT_SEPARATORS.contains(delimiter)) {

            Optional<String> invalidSeparator = stream(number.split(delimiter)).filter(n -> !isNumeric(n)).findFirst();
            if (invalidSeparator.isPresent()) {
                throw new MultipleDelimiterException("'" + delimiter + "' expected but " + invalidSeparator.get() + " found at position " + number.indexOf(invalidSeparator.get()) + ".");
            }
        }
    }

    private static boolean isSeparatorAligned(String number) { //(int indexOfComma, int indexOfNewLine
        Pattern pattern = Pattern.compile(REGEX_FOR_ALIGN_SEPARATOR);
        return pattern.matcher(number).find();
    }

    private static void invalidMessageForSeparatorSuccessive(int indexOfComma, int indexOfNewLine) throws InvalidPositionException {
        int position = max(indexOfComma, indexOfNewLine);
        String separator = indexOfComma == position ? COMMA_SEPARATOR : NEW_LINE_FOR_EXCEPTION;
        throw new InvalidPositionException("Number expected but '" + separator + "' found at position " + position + ".");
    }
}
