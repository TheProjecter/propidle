package com.googlecode.propidle.util.matchers;

import static com.googlecode.totallylazy.Sequences.sequence;

/*
Because who wants to use xml in Java?
 */
public class HtmlRegexes {
    public static String tr(String... regexesOfContents) {
        return "<tr.*?>\\s*" + concat(regexesOfContents) + "\\s*</tr>";
    }

    public static String td(String contents) {
        return "<td.*?>" + contents + "</td>\\s*";
    }

    public static String li(String contents) {
        return "<li.*?>.*?" + contents + ".*?</li>";
    }

    public static String img(String href) {
        return "<img.*?src=\"" + href + "\".*?>";
    }

    public static String anchor(String href) {
        return anchor(href, href);
    }

    public static String anchor(String href, String text) {
        return "<a.*? href=\"" + href + "\".*?>" + text + "</a>";
    }

    public static String input(String name, String value) {
        return "<input.*? name=\"" + name + ".*? value=\"" + value ;
    }

    public static String div(String id, String content) {
        return "<div.*? id=\"" + id + "\".*?>" + content + "</div>";
    }

    public static String concat(String... strings) {
        return sequence(strings).toString("");
    }
}
