package com.company;

import com.company.data.PostgreSystemQueries;
import com.company.data.model.ApplicationConfig;
import com.company.data.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
@EnableJdbcHttpSession
public class SpringBootWebApplication extends SpringBootServletInitializer {

    private static final String systemHash = ApplicationConfig.getSystemHash();

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootWebApplication.class);
    }

    public static void main(String[] args) throws Exception {

        SpringApplication.run(SpringBootWebApplication.class, args);
        User user = PostgreSystemQueries.getUserByName("System");

        if (user == null) {

            User systemUser = new User(
                    "System",
                    systemHash,
                    "no-reply@mywatchlist.com",
                    true,
                    true,
                    0);

            int createdUsers = PostgreSystemQueries.insertUser(systemUser);
            if (createdUsers == 1) {
                System.out.println("System user created");
            }
        } else {
            System.out.println("System user found");
        }
    }
}
