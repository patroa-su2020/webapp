package com.patro.SpringBootProject;

import com.patro.SpringBootProject.api.UserController;
import com.patro.SpringBootProject.model.User;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validation;
import javax.xml.validation.Validator;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class UserTest {
    private static Validator validator;

    private UserController userCont = new UserController();

    public User setTestUser() {
        User u = new User();
        Optional<User> ou = Optional.ofNullable(u);
        User user = ou.get();
        user.setFirstName("Ankit");
        user.setLastName("Patro");
        user.setUsername("ankitpatro@gmail.com");
        user.setPassword("passS$123");

        return user;
    }

    @Test //Testing password strength
    public void validatePassword() {
        final String password1 = "123456";
        assertThat(userCont.isPasswordStrong(password1)).isFalse();
        final String password2 = "Password$33";
        assertThat(userCont.isPasswordStrong(password2)).isTrue();
    }


}
