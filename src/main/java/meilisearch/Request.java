package meilisearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

class Request {

    private Config config;

    Request(Config config) {
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

    private HttpURLConnection connection(URL url, String requestMethod, String apiKey) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(requestMethod);
        con.setRequestProperty("Content-Type", "application/json");

        if (!apiKey.equals("")) {
            con.setRequestProperty("X-Meili-API-Key", apiKey);
        }

//        con.setRequestProperty("Content-Encoding", "gzip");
//        con.setRequestProperty("Accept-Encoding", "gzip, deflate");

        return con;
    }
}
