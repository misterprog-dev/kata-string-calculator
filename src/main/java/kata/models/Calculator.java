package kata.models;

import kata.exception.InvalidPositionException;
import kata.exception.MissingNumberException;
import kata.exception.MultipleDelimiterException;
import kata.exception.NegativeNumberException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.String.join;
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
    public static final String NEGATIVE_NUMBER_DELIMITER = ", ";
    public static final String EMPTY_STRING = "";


    String add(String number) throws InvalidPositionException, MissingNumberException, MultipleDelimiterException, NegativeNumberException {
        if (EMPTY_INPUT.equals(number.trim())) {
            return ZERO_STRING;
        }

        return stream(extractNumbers(number))
                .map(BigDecimal::new)
                .reduce(ZERO, BigDecimal::add).toString();
    }

    private static String[] extractNumbers(String number) throws MissingNumberException, InvalidPositionException, MultipleDelimiterException, NegativeNumberException {
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

    private static void validation(String delimiter, String finalNumber) throws MissingNumberException, InvalidPositionException, MultipleDelimiterException, NegativeNumberException {
        validateLastPosition(delimiter, finalNumber);
        validateMultipleDelimiter(delimiter, finalNumber);
        validateMultipleErrors(delimiter, finalNumber);
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

    private static void validateMultipleDelimiter(String delimiter, String number) throws MultipleDelimiterException {
        if (hasDelimiterDefinedAndNotDefault(delimiter)) {
            Optional<String> invalidSeparator = stream(number.split(delimiter)).filter(n -> isNotDelimiterAndNotANumber(delimiter, n)).findFirst();

            stream(number.split(delimiter))
                    .filter(n -> isNotDelimiterAndNotANumber(delimiter, n))
                    .findAny();

            if (invalidSeparator.isPresent()) {
                throw new MultipleDelimiterException("'" + delimiter + "' expected but '" + invalidSeparator.get() + "' found at position " + number.indexOf(invalidSeparator.get()) + ".");
            }
        }
    }

    private static boolean hasDelimiterDefinedAndNotDefault(String delimiter) {
        return delimiter != null && !LIST_OF_DEFAULT_SEPARATORS.contains(delimiter);
    }

    private static void validateMultipleErrors(String delimiter, String number) throws InvalidPositionException, NegativeNumberException {
        boolean isNegativeAndMultipleSep = checkNegativeAndMultipleNumber(delimiter, number);
        boolean isSuccessiveNumber = checkOnlySuccessiveNumbers(number, isNegativeAndMultipleSep);
        checkOnlyNegativeNumbers(delimiter, number, isSuccessiveNumber);
    }

    private static boolean checkNegativeAndMultipleNumber(String delimiter, String number) throws InvalidPositionException {
        if (!getNegativesNumbers(number, delimiter).isEmpty() && getLastSeparator(number) != null) {
            throw new InvalidPositionException(getNegativeErrorMessage(delimiter, number) + "\n" + getSuccessiveSepErrorsMessage(number));
        }
        return false;
    }

    private static boolean checkOnlySuccessiveNumbers(String number, boolean isNegativeAndMultipleSep) throws InvalidPositionException {
        if (!isNegativeAndMultipleSep && getLastSeparator(number) != null) {
            throw new InvalidPositionException(getSuccessiveSepErrorsMessage(number).replace("\n", NEW_LINE_FOR_EXCEPTION));
        }
        return false;
    }

    private static void checkOnlyNegativeNumbers(String delimiter, String number, boolean isSuccessiveNumber) throws NegativeNumberException {
        if (!isSuccessiveNumber && !getNegativesNumbers(number, delimiter).isEmpty()) {
            throw new NegativeNumberException(getNegativeErrorMessage(delimiter, number));
        }
    }

    private static String getFinalDelimiter(String delimiter) {
        String result = REGEX_FOR_DEFAULT_SEPARATORS;

        if (PIPE_SEPARATOR.equals(delimiter)) {
            result = REGEX_PIPE_SEPARATOR;
        }

        if (delimiter != null && !delimiter.equals(PIPE_SEPARATOR)) {
            result = delimiter;
        }

        return result;
    }


    private static String getSuccessiveSepErrorsMessage(String number) {
        if (getLastSeparator(number) != null) {
            return "Number expected but '" + right(getLastSeparator(number), 1) +
                    "' found at position " + (number.indexOf(getLastSeparator(number)) + 1) + ".";
        }
        return EMPTY_STRING;
    }

    private static String getNegativeErrorMessage(String delimiter, String number) {
        if (!getNegativesNumbers(number, delimiter).isEmpty()) {
            return "Negative not allowed : " + join(NEGATIVE_NUMBER_DELIMITER, getNegativesNumbers(number, delimiter));
        }
        return EMPTY_STRING;
    }

    private static boolean isNotDelimiterAndNotANumber(String delimiter, String currentNumber) {
        return !currentNumber.equals(delimiter) && !isNumeric(currentNumber);
    }

    private static String getLastSeparator(String number) {
        Pattern pattern = Pattern.compile(REGEX_FOR_ALIGN_SEPARATOR);
        Matcher matcher = pattern.matcher(number);

        if (matcher.find()) {
            return number.substring(matcher.start(), matcher.end());
        }

        return null;
    }

    private static List<String> getNegativesNumbers(String number, String delimiter) {
        return stream(number.split(getFinalDelimiter(delimiter)))
                .filter(n -> !n.isEmpty())
                .filter(n -> Double.parseDouble(n) < 0).collect(toList());
    }
}
