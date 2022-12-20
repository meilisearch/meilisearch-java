package com.meilisearch.sdk.http;

public class URLBuilder {
    private StringBuilder routes, params;

    public URLBuilder() {
        routes = new StringBuilder();
        params = new StringBuilder();
    }

    URLBuilder(String host) {}

    public void addSubroute(String route) {
        routes.append("/");
        routes.append(route);
    }

    public void addParameter(String parameter, String value) {
        if (value != "") {
            addSeparator();
            params.append(parameter);
            params.append("=");
            params.append(value);
        }
    }

    public void addParameter(String parameter, int value) {
        if (value > -1) {
            addSeparator();
            params.append(parameter);
            params.append("=");
            params.append(value);
        }
    }

    public void addParameter(String parameter, String[] value) {
        if (value != null && value.length > 0) {
            addSeparator();
            params.append(parameter);
            params.append("=");
            params.append(formatArrayParameters(value));
        }
    }

    void addSeparator() {
        if (params.length() > 0) {
            params.append("&");
        } else {
            params.append("?");
        }
    }

    String formatArrayParameters(String[] fields) {
        return String.join(",", fields);
    }

    public String getURL() {
        return routes.toString() + params.toString();
    }
}
