package test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;

public class AuthTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");

        // заполняем форму авторизации
        $("[data-test-id='login'] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] .input__control").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();

        // Проверяем, что авторизация прошла успешно
        $("h2").shouldHave(Condition.text("Личный кабинет"))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        // создаем незарегистрированного пользователя
        var notRegisteredUser = getUser("active");

        // заполняем форму авторизации
        $("[data-test-id='login'] .input__control").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] .input__control").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").click();

        // проверяем, что появилось сообщение об ошибке
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        //создаем заблокированного пользователя
        var blockedUser = getRegisteredUser("blocked");

        //заполняем форму авторизации
        $("[data-test-id='login'] .input__control").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] .input__control").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();

        //проверяем сообщение о блокировке пользователя
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        // создаем активного пользователя
        var registeredUser = getRegisteredUser("active");
        // генерируем неправильный логин
        var wrongLogin = getRandomLogin();

        // заполняем форму авторизации с неверным логином
        $("[data-test-id='login'] .input__control").setValue(wrongLogin); //неправильный логин
        $("[data-test-id='password'] .input__control").setValue(registeredUser.getPassword()); //правильный пароль
        $("[data-test-id='action-login']").click();

        // проверяем сообщение об ошибке
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        //создаем активного пользователя
        var registeredUser = getRegisteredUser("active");
        //генерируем неправильный пароль
        var wrongPassword = getRandomPassword();

        //заполняем форму авторизации с НЕВЕРНЫМ паролем
        $("[data-test-id='login'] .input__control").setValue(registeredUser.getLogin()); //правильный логин
        $("[data-test-id='password'] .input__control").setValue(wrongPassword); //неправильный пароль
        $("[data-test-id='action-login']").click();

        //проверяем сообщение об ошибке
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

}
