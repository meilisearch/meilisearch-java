package com.meilisearch.sdk.http;

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

    public String getURL() {
        return routes.toString() + params.toString();
    }
}
