package com.company.business;

import com.company.data.UserQueries;
import com.company.data.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserBusinessTest {

    @Before
    public void setUp() {

        User user = new User(
                "TestUser",
                "TestPassword",
                "no-reply@testmail.com",
                false,
                true,
                1);

        UserQueries.insertAdmin(user);
    }

    @After
    public void tearDown() {

        User user = UserQueries.getUserByName("TestUser");

        if (user != null) {
            UserQueries.deleteUserById(user.getId());
        }
    }

    @Test
    public void verifyRegistrationTest() {

        User user = UserQueries.getUserByName("TestUser");

        if (user != null) {
            Assert.assertEquals(UserBusiness.verifyRegistration(user.getToken()), Boolean.TRUE);
        }
    }

    @Test
    public void requestLinkTest() {

        Assert.assertEquals(UserBusiness.requestLink("no-reply@testmail.com"), Boolean.TRUE);

    }

    @Test
    public void getUserByEmailTest() {

        User user = UserBusiness.getUserByEmail("no-reply@testmail.com");

        if (user != null) {
            Assert.assertEquals(user.getName(), "TestUser");
        }

    }
}