package com.company;

import com.company.data.UserQueries;
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
    private static final String adminHash = ApplicationConfig.getAdminHash();

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootWebApplication.class);
    }

    public static void main(String[] args) {

        SpringApplication.run(SpringBootWebApplication.class, args);
        User system = UserQueries.getUserByName("System");
        User admin = UserQueries.getUserByName("Admin1");

        if (system == null) {

            User user = new User(
                    "System",
                    systemHash,
                    "no-reply@mywatchlist.com",
                    true,
                    true,
                    0);

            int createdUsers = UserQueries.insertAdmin(user);
            if (createdUsers == 1) {
                System.out.println("System user created");
            }
        } else {
            System.out.println("System user found");
        }

        if (admin == null) {

            User user = new User(
                    "Admin1",
                    adminHash,
                    "admin@mywatchlist.com",
                    true,
                    true,
                    0);

            int createdUsers = UserQueries.insertAdmin(user);
            if (createdUsers == 1) {
                System.out.println("Admin user created");
            }
        } else {
            System.out.println("Admin user found");
        }
    }
}
