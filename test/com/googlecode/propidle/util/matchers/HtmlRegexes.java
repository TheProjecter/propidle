package com.googlecode.propidle.util.matchers;

import static com.googlecode.totallylazy.Sequences.sequence;

import static java.lang.String.format;

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
        return format("<input.*? name=\"%s\".*? value=\"%s\"", name, value);
    }

    public static String div(String id, String content) {
        return format("<div.*? id=\"%s\".*?>%s</div>", id, content);
    }

    public static String option(String value, String text) {
        return format("<option.*? value=\"%s\".*?>%s</option>", value, text);
    }

    public static String concat(String... strings) {
        return sequence(strings).toString("");
    }
}
