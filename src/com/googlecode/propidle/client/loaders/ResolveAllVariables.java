package com.googlecode.propidle.client.loaders;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.googlecode.propidle.properties.Properties.toPairs;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.util.regex.Pattern.compile;

public class ResolveAllVariables implements Callable<Properties> {

    private final Callable<Properties> loader;
    private final Pattern TOKEN_EXTRACTION_PATTERN = compile("\\$\\{.*?\\}");
    private Properties properties;

    public static ResolveAllVariables resolveAllProperties(Callable<Properties> properties) {
        return new ResolveAllVariables(properties);
    }

    public ResolveAllVariables(Callable<Properties> loader) {
        this.loader = loader;
    }

    @Override
    public Properties call() throws Exception {
        properties = loader.call();
        return toPairs(properties).fold(properties, resolveProperties());
    }

    private Callable2<Properties, Pair<String, String>, Properties> resolveProperties() {
        return new Callable2<Properties, Pair<String, String>, Properties>() {
            @Override
            public Properties call(Properties properties, Pair<String, String> keyValue) throws Exception {
                properties.setProperty(keyValue.first(), resolveValue(keyValue.second(), new HashSet<String>()));
                return properties;
            }
        };
    }

    private String resolveValue(String value, Set<String> alreadyResolvedTokens) {
        return alreadyResolvedTokens.contains(value) ? value :
                extractTokensFrom(value).map(resolveToken(alreadyResolvedTokens)).fold(value, replaceTokens());
    }

    private Callable1<String, Pair<String, String>> resolveToken(final Set<String> resolved) {
        return new Callable1<String, Pair<String, String>>() {
            @Override
            public Pair<String, String> call(String token) throws Exception {
                resolved.add(wrapToken(token));
                return pair(token, resolveValue(properties.getProperty(token, wrapToken(token)), resolved));
            }
        };
    }

    private Callable2<String, Pair<String, String>, String> replaceTokens() {
        return new Callable2<String, Pair<String, String>, String>() {
            @Override
            public String call(String value, Pair<String, String> tokenValue) throws Exception {
                return value.replace(wrapToken(tokenValue.first()), tokenValue.second());
            }
        };
    }

    private Sequence<String> extractTokensFrom(String token) {
        Matcher matcher = TOKEN_EXTRACTION_PATTERN.matcher(token);
        Set<String> tokenSet = new HashSet<String>();
        while (matcher.find()) {
            tokenSet.add(unwrapToken(matcher.group()));
        }
        return sequence(tokenSet);
    }

    private static String unwrapToken(String token) {
        return token.replace("${", "").replace("}", "");
    }

    private static String wrapToken(String token) {
        return "${" + token + "}";
    }
}

