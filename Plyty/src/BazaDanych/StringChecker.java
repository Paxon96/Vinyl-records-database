package BazaDanych;

import java.util.regex.Pattern;

public class StringChecker {
    private String stringToCheck;

    public StringChecker(String stringToCheck) {
        this.stringToCheck = stringToCheck;
    }

    public boolean stringCheck()
    {
        if(Pattern.matches("[a-zA-Z óąęźżń&0-9]+",stringToCheck))
            return true;
        else
            return false;
    }
    public String getStringToCheck() {
        return stringToCheck;
    }

    public void setStringToCheck(String stringToCheck) {
        this.stringToCheck = stringToCheck;
    }
}
