package org.asasna.chat.common.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User user;
    int userId;
    String name, email, phone, password, country, bio, imageUrl;
    boolean verified, chatBotOption;
    Gender gender;
    UserStatus status;
    UserTest(){
        userId = -1;
        name = "Elsayed Nabil";
        email = "sayed@gmail.com";
        phone = "01279425232";
        password = "123456789";
        country = "Egypt";
        bio = "I'm A Software Engineer";
        imageUrl = "sayed.jpg";
        status = UserStatus.ONLINE;
        gender = Gender.Male;
        user = new User(userId, name , phone, email, password, gender, country, null, bio, status, imageUrl, verified, chatBotOption);
    }
    @Test
    void getId() {
            Assertions.assertEquals(userId, user.getId(), "Range Exception");
    }

    @Test
    void getName() {
        Assertions.assertEquals(name, user.getName());
        Assertions.assertNotNull(name);
    }

    @Test
    void getPhone() {
        Assertions.assertEquals(phone, user.getPhone());
        Assertions.assertNotNull(phone);
    }

    @Test
    void getEmail() {
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertNotNull(email);
    }

    @Test
    void getPassword() {
        Assertions.assertEquals(password, user.getPassword());
        Assertions.assertNotNull(password);
    }

    @Test
    void getGender() {
        Assertions.assertEquals(gender, user.getGender());
        Assertions.assertNotNull(gender);
    }

    @Test
    void getCountry() {
        Assertions.assertEquals(country, user.getCountry());
    }

    @Test
    void getDateOfBirth() {
        Assertions.assertEquals(null, user.getDateOfBirth());
    }

    @Test
    void getBio() {
        Assertions.assertEquals(bio, user.getBio());
    }

    @Test
    void getStatus() {
        Assertions.assertEquals(status, user.getStatus());
    }

    @Test
    void getImageURL() {
        Assertions.assertEquals(imageUrl, user.getImageURL());
    }

    @Test
    void isVerified() {
        Assertions.assertEquals(verified, user.isVerified());
    }

    @Test
    void isChatbotOption() {
        Assertions.assertEquals(chatBotOption, user.isChatbotOption());
    }
}