package com.company.data;

import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig {

//    @Value("${app.data.defaultHost}")
    private String defaultHost = "http://localhost:";
//    @Value("${app.data.defaultPort}")
    private int defaultPort = 8080;
    private String host;
    private int port;

    public ApplicationConfig() {
        this.host = defaultHost;
        this.port = defaultPort;
    }

//    public ApplicationConfig(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
