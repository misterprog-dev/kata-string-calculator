package entity;

public class Calculator {
    String add(String number) {
        if("".equals(number.trim())) {
            return "0";
        }
        return number;
    }
}
