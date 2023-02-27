package com.meilisearch.sdk.http;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import lombok.Getter;

@Getter
public class URLBuilder {
    private StringBuilder routes, params;

    public URLBuilder() {
        routes = new StringBuilder();
        params = new StringBuilder();
    }

    public URLBuilder(String rootRoute) {
        routes = new StringBuilder(rootRoute);
        params = new StringBuilder();
    }

    public URLBuilder addSubroute(String route) {
        routes.append("/");
        routes.append(route);
        return this;
    }

    public URLBuilder addParameter(String parameter, String value) {
        if (value != "") {
            addSeparator();
            params.append(parameter);
            params.append("=");
            params.append(value);
        }
        return this;
    }

    public URLBuilder addParameter(String parameter, int value) {
        if (value > -1) {
            addSeparator();
            params.append(parameter);
            params.append("=");
            params.append(value);
        }
        return this;
    }

    public URLBuilder addParameter(String parameter, String[] value) {
        if (value != null && value.length > 0) {
            addSeparator();
            params.append(parameter);
            params.append("=");
            params.append(formatArrayParameters(value));
        }
        return this;
    }

    public URLBuilder addParameter(String parameter, int[] value) {
        if (value != null && value.length > 0) {
            addSeparator();
            params.append(parameter);
            params.append("=");
            params.append(formatArrayParameters(value));
        }
        return this;
    }

    public URLBuilder addParameter(String parameter, Date value) {
        if (value != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            addSeparator();
            params.append(parameter);
            params.append("=");
            params.append(formatter.format(value));
        }
        return this;
    }

    public URLBuilder addQuery(String query) {
        this.params.append(query);
        return this;
    }

    private URLBuilder addSeparator() {
        if (params.length() > 0) {
            params.append("&");
        } else {
            params.append("?");
        }
        return this;
    }

    private String formatArrayParameters(String[] fields) {
        return String.join(",", fields);
    }

    private String formatArrayParameters(int[] fields) {
        String[] arr = Arrays.stream(fields).mapToObj(String::valueOf).toArray(String[]::new);
        return String.join(",", arr);
    }

    public String getURL() {
        return routes.toString() + params.toString();
    }
}
