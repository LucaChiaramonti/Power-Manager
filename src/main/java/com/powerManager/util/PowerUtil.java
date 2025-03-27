package com.powerManager.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PowerUtil {
    public static final  String descriptionRegex = "<div id=\"description\"><p>(.*?)<\\/p>";
    public static final String powerFirstClassRegex = "<h6>Level<\\/h6><p>(.*?)<a class=\"addcustom\"";
    public static final String powerClassRegex = "<\\/a>, (.*?)<a";
    private static Log _log = LogFactory.getLog(HtmlUtil.class);

    public static Integer getPowerLevel(String powerCost) {
       return (Integer.parseInt(powerCost) / 2) + 1;
    }


    public static String extractDescription(String powerDescription) {
        return HtmlUtil.extractText(powerDescription, descriptionRegex);
    }

    public static String extractName(String powerResponse ) {

        return HtmlUtil.extractText(powerResponse, "\">([^<]+)</a></h1>");
    }

    public static String extractCost(String powerResponse) {
        return HtmlUtil.extractText(powerResponse,  "<h6>Cost<\\/h6><p>(.*?)<\\/p>");
    }

    public static List<String> buildPowerCostList(String powerCostFound) {
        if(powerCostFound.contains(", XP")) {
            powerCostFound = powerCostFound.replace(", XP", "");
        }
        List<String> powerCostList = Arrays.asList(powerCostFound.split("or"));
        powerCostList.replaceAll(String::trim);
        return powerCostList;
    }

    public static  Map<String, Long>   extractClassList(String powerResponse) {

        Map<String, Long> classLevelMap = HtmlUtil.extractLevelClassAsMap(powerResponse, powerClassRegex);

        _log.info("Map count: " + classLevelMap.size());
        classLevelMap.forEach((k, v) -> _log.info("Class = " + k + ", Level = " + v));

        return classLevelMap;
    }
}
