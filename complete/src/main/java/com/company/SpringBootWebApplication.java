package com.company;

import com.company.data.UserQueries;
import com.company.data.model.ApplicationConfig;
import com.company.data.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@SpringBootApplication
@EnableJdbcHttpSession
public class SpringBootWebApplication extends SpringBootServletInitializer implements ServletContextListener {

    private static final String systemHash = ApplicationConfig.getSystemHash();
    private static final String adminHash = ApplicationConfig.getAdminHash();

    public static void main(String[] args) {

        SpringApplication.run(SpringBootWebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootWebApplication.class);
    }

    @Override
    public final void contextInitialized(final ServletContextEvent sce) {
        User system = UserQueries.getUserByName("System");
        User admin = UserQueries.getUserByName("Admin1");

        if (system == null) {

            User newSystem = new User(
                    "System",
                    systemHash,
                    "no-reply@mywatchlist.com",
                    true,
                    true,
                    0);

            int createdSystem = UserQueries.insertAdmin(newSystem);

            if (createdSystem == 1) {
                System.out.println("System user created");
            }

            if (admin == null) {

                User newAdmin = new User(
                        "Admin1",
                        adminHash,
                        "admin@mywatchlist.com",
                        true,
                        true,
                        0);

                int createdAdmin = UserQueries.insertAdmin(newAdmin);

                if (createdAdmin == 1) {
                    System.out.println("Admin user created");
                }

            } else {
                System.out.println("Admin user found");
            }

        } else {
            System.out.println("System user found");
        }
    }
}
