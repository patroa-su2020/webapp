package com.patro.SpringBootProject;

import com.patro.SpringBootProject.api.UserController;
import com.patro.SpringBootProject.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

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

    /**
     * when:  password is strong
     * then: assert false
     */
    @Test
    public void testIsValidPassword() {
        final String password = "Password$33";
        assertThat(userCont.isPasswordStrong(password)).isTrue();
    }

    /**
     * when:  password is weak
     * then: assert true
     */
    @Test
    public void testIsInvalidPassword() {
        final String password = "123456";
        assertThat(userCont.isPasswordStrong(password)).isFalse();
    }

    /**
     * when:  valid username
     * then: assert true
     */
    @Test
    public void isValidUsername() {
        final String username = "ankit.patro@northeastern.edu";
        assertThat(userCont.isValidEmail(username)).isTrue();
    }

    /**
     * when:  invalid username
     * then: assert false
     */
    @Test
    public void isInvalidUsername() {
        final String username = "ankit@gmail";
        assertThat(userCont.isValidEmail(username)).isFalse();
    }


}
