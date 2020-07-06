package com.company.data.model;

import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig {

//    @Value("${app.data.defaultHost}")
    private String defaultHost = "http://localhost:";
//    @Value("${app.data.defaultPort}")
    private int defaultPort = 8080;
    private String host;
    private int port;

    private static final String systemHash = "$2a$16$1ZGiIJmasE9wW50nDQi3J.UDUgRjsnoRZZEP6eSNiZABghiI//w4G";

//    @Autowired
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

    public static String getSystemHash() {
        return systemHash;
    }
}
