package app.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Use regular expression to parse String text.
 */
public class ParseByRegex {
    /**
     * Use regular expression to parse String text, given text, pattern, and group number.
     * @param text
     * @param regex
     * @param groupNum
     * @return
     */
    public String parseByRegex(String text, String regex, int groupNum) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()) {
            if (matcher.group(0) != null) {
                return matcher.group(groupNum);
            }
        }
        return null;
    }
}