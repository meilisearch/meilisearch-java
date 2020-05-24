package meilisearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class Request {

    private Config config;

    protected Request(Config config) {
        this.config = config;
    }

    String get(String api) throws Exception {
        return this.get(api, "");
    }

    String get(String api, String param) throws Exception {

        StringBuilder urlBuilder = new StringBuilder(config.hostUrl + api);
        if (!param.equals("")) {
            urlBuilder.append(param);
        }

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection connection = connection(url, "GET", config.apiKey);
        connection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
        StringBuffer sb = new StringBuffer();
        String responsed;

        while ((responsed = br.readLine()) != null) {
            sb.append(responsed);
        }

        br.close();
        return sb.toString();
    }

    String post(String api, String params) throws Exception {
        System.out.println(params);
        URL url = new URL(config.hostUrl + api);

        HttpURLConnection connection = connection(url, "POST", config.apiKey);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(params.length()));
        connection.getOutputStream().write(params.getBytes("UTF-8"));
        connection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
        StringBuffer sb = new StringBuffer();
        String responsed;

        while ((responsed = br.readLine()) != null) {
            sb.append(responsed);
        }

        br.close();
        return sb.toString();
    }

    String put(String api, String params) throws Exception {
        System.out.println(params);
        URL url = new URL(config.hostUrl + api);

        HttpURLConnection connection = connection(url, "PUT", config.apiKey);
        connection.setDoOutput(true);
        connection.getOutputStream().write(params.getBytes());
        connection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
        StringBuffer sb = new StringBuffer();
        String responsed;

        while ((responsed = br.readLine()) != null) {
            sb.append(responsed);
        }

        br.close();
        return sb.toString();
    }

    String delete(String api) throws Exception {
        URL url = new URL(config.hostUrl + api);

        HttpURLConnection connection = connection(url, "DELETE", config.apiKey);
        connection.connect();
        return getResponseBuffer(connection.getInputStream());
    }

    private String getResponseBuffer(InputStream is) {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );
        StringBuffer sb = new StringBuffer();
        String responsed;
        try {
            while ((responsed = br.readLine()) != null) {
                sb.append(responsed);
            }

            br.close();
        } catch (IOException ioe) {
        } finally {
            return sb.toString();
        }
    }

    private HttpURLConnection connection(URL url, String requestMethod, String apiKey) throws IOException {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestMethod);
            con.setRequestProperty("Content-Type", "application/json");

            if (!apiKey.equals("")) {
                con.setRequestProperty("X-Meili-API-Key", apiKey);
            }

            return con;
        } catch (IOException ioe) {

        }

        return null;
    }
}
