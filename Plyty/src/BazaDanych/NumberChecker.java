package BazaDanych;

import java.util.regex.Pattern;

public class NumberChecker {
    private String numberToCheck;

    public NumberChecker(String stringToCheck) {
        this.numberToCheck = stringToCheck;
    }

    public boolean stringCheck()
    {
        if(Pattern.matches("[0-9]+",numberToCheck))
            return true;
        else
            return false;
    }

    public String getNumberToCheck() {
        return numberToCheck;
    }

    public void setNumberToCheck(String numberToCheck) {
        this.numberToCheck = numberToCheck;
    }
}

