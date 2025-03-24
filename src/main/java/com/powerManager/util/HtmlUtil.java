package com.powerManager.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
    public static final String powerFirstClassRegex = "<h6>Level<\\/h6><p>(.*?)<a class=\"addcustom\"";
    public static final String powerClassRegex = "<\\/a>, (.*?)<a";
    public static final String powerNameRegex = "(?<=href=\\\"\\/powers\\/)(.*)(?=\\\" title=)";

    private static Log _log = LogFactory.getLog(HtmlUtil.class);
    public static String extractText(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    public static Map<String, Long> extractLevelClassAsMap(String text, String regex) {

        String firstClass = HtmlUtil.extractText(text, powerFirstClassRegex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        String levelString = firstClass.replaceAll( "[^0-9]+", "");
        Long level = 0L;
        if(isNumeric(levelString)) {
            level = Long.parseLong(levelString);
        }
        String className = firstClass.replaceAll("[0-9]+" , "");
        Map<String, Long> map = new HashMap<>();
        map.put(className, level);

        while(matcher.find()) {
            String classLevel = matcher.group();
            classLevel = classLevel.replaceAll("</a>, ", "").replaceAll(" <a", "");
            Pattern levelPattern = Pattern.compile("\\d+(?!.*\\d)");
            Matcher levelMatcher = levelPattern.matcher(classLevel);

            if (levelMatcher.find()) {
                levelString = levelMatcher.group();
                if(isNumeric(levelString)) {
                    level = Long.parseLong(levelString);
                }
                //remove all the number characters and the last space
                className = classLevel.replaceAll("[0-9]+", "");
                if(className.endsWith(" ")) {
                    className = className.substring(0, className.length() - 1);
                }
                map.put(className, level);
            }


        }

        return map;
    }

    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}