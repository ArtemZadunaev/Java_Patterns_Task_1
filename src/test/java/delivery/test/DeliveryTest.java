package delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import delivery.data.DataGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    SelenideElement cityInput = $("[data-test-id=city] input");
    SelenideElement dateInput = $("[data-test-id=date ] input");
    SelenideElement nameInput = $("[data-test-id=name] input");
    SelenideElement phoneInput = $("[data-test-id=phone] input");
    SelenideElement agreementCheckBox = $("[data-test-id=agreement]");
    SelenideElement submitButton = $$("button").find(text("Запланировать"));
    SelenideElement successNotification = $("[data-test-id=success-notification]");
    SelenideElement replaneNotificationButton = $("[data-test-id=replan-notification] button");
    //InvalidStatement
    SelenideElement invalidDate = $("[data-test-id=date] span.input_invalid");
    SelenideElement invalidName = $("[data-test-id=name].input_invalid");
    SelenideElement invalidPhone = $("[data-test-id=phone].input_invalid ");
    SelenideElement invalidCity = $("[data-test-id=city].input_invalid");
    SelenideElement invalidAgreement = $("[data-test-id=agreement].input_invalid");

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }


    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        String date = DataGenerator.generateDate(3);
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE).setValue(date);
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        successNotification.should(Condition.visible).shouldHave(text(date));
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE).setValue(date);
        submitButton.click();
        replaneNotificationButton.should(Condition.visible).click();
        successNotification.should(Condition.visible).shouldHave(text(date));
    }

    @Test
    @DisplayName("should Not Replan Meeting")
    void shouldNotReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        String validDate = DataGenerator.generateDate(3);
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE).setValue(validDate);
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        successNotification.should(Condition.visible).shouldHave(text(validDate));
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE).setValue(DataGenerator.generateDate(2));
        submitButton.click();
        invalidDate.should(Condition.visible).shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @ParameterizedTest
    @DisplayName("should Not Send Form With Invalid Name")
    @MethodSource("delivery.data.DataGenerator#invalidNamesEn")
    void shouldNotSendFormWithInvalidNamesEn(String invalidNames) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(DataGenerator.generateCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(invalidNames);
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidName.should(Condition.visible)
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @ParameterizedTest
    @DisplayName("should Not Send Form With Invalid Names With Special Chars")
    @MethodSource("delivery.data.DataGenerator#invalidNamesWithSpecialChars")
    void shouldNotSendFormWithInvalidNamesWithSpecialChars(String invalidNames) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(DataGenerator.generateCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(invalidNames);
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidName.should(Condition.visible)
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @ParameterizedTest
    @DisplayName("should Not Send Form With invalid Names With Nums")
    @MethodSource("delivery.data.DataGenerator#invalidNamesWithNums")
    void shouldNotSendFormWithInvalidNamesWithNums(String invalidNames) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(DataGenerator.generateCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(invalidNames);
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidName.should(Condition.visible)
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @ParameterizedTest
    @DisplayName("should Auto Correct With invalid Long Phones")
    @MethodSource("delivery.data.DataGenerator#invalidLongPhones")
    void shouldAutoCorrectWithInvalidLongPhones(String phone) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        String formPhone = DataGenerator.formatter(phone);
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(phone);
        phoneInput.shouldHave(Condition.exactValue(formPhone));
    }

    @ParameterizedTest
    @DisplayName("should Auto Correct With Invalid Long Phones With Chars")
    @MethodSource("delivery.data.DataGenerator#invalidLongPhonesWithChars")
    void shouldAutoCorrectWithInvalidLongPhonesWithChars(String phone) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        String formPhone = DataGenerator.formatter(phone);
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(phone);
        phoneInput.shouldHave(Condition.exactValue(formPhone));
    }

    @ParameterizedTest
    @DisplayName("should Auto Correct With Invalid Long Phones With Special Chars")
    @MethodSource("delivery.data.DataGenerator#invalidLongPhonesWithSpecialChars")
    void shouldAutoCorrectWithInvalidLongPhonesWithSpecialChars(String phone) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        String formPhone = DataGenerator.formatter(phone);
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(phone);
        phoneInput.shouldHave(Condition.exactValue(formPhone));
    }

    @Test
    @DisplayName("should Not Send Form With Short Phone")
    void shouldNotSendFormWithShortPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue("+" + DataGenerator.shortPhoneGenerator());
        agreementCheckBox.click();
        submitButton.click();
        invalidPhone.shouldBe(Condition.visible)
                .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678"));
    }

    @ParameterizedTest
    @DisplayName("should Not Send Form With Invalid City With Nums")
    @MethodSource("delivery.data.DataGenerator#invalidCitiesWithNums")
    void shouldNotSendFormWithInvalidCityWithNums(String city) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(city);
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidCity.should(Condition.visible)
                .shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @ParameterizedTest
    @DisplayName("should Not Send Form With Invalid City With Spec Chars")
    @MethodSource("delivery.data.DataGenerator#invalidCitiesWithSpecChars")
    void shouldNotSendFormWithInvalidCityWithSpecChars(String city) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(city);
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidCity.should(Condition.visible)
                .shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @ParameterizedTest
    @DisplayName("should Not Send Form With Invalid City")
    @MethodSource("delivery.data.DataGenerator#invalidCities")
    void shouldNotSendFormWithInvalidCity(String city) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(city);
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidCity.should(Condition.visible)
                .shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    @DisplayName("should Not Send Form With Invalid Date")
    void shouldNotSendFormWithInvalidDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(2));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidDate.should(Condition.visible)
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    @DisplayName("should Not Send Form With Invalid Date In Future")
    void shouldNotSendFormWithInvalidDateInFuture() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE).setValue(DataGenerator.generateDate(400));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidDate.shouldBe(Condition.visible).shouldHave(text("Неверно введена дата"));
    }

    @Test
    @DisplayName("should Not Send Form With Empty Name")
    void shouldNotSendFormWithEmptyName() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue("  ");
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidName.should(Condition.visible).shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("should Not Send Form With Empty Phone")
    void shouldNotSendFormWithEmptyPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue("  ");
        agreementCheckBox.click();
        submitButton.click();
        invalidPhone.should(Condition.visible).shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("should Not Send Form With Empty Checkbox")
    void shouldNotSendFormWithEmptyCheckbox() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        submitButton.click();
        invalidAgreement.should(Condition.visible);
    }

    @Test
    @DisplayName("should Not Send Form With Empty City")
    void shouldNotSendFormWithEmptyCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidCity.should(Condition.visible).shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("should Not Send Form With Empty Date")
    void shouldNotSendFormWithEmptyDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE).setValue(" ");
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidDate.should(Condition.visible).shouldHave(text("Неверно введена дата"));
    }

    @Test
    @DisplayName("should Not Send Form With Un Correct Date")
    void shouldNotSendFormWithUnCorrectDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidDate.should(Condition.visible).shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @ParameterizedTest
    @DisplayName("should Send Form Search City With Two Symbols")
    @CsvFileSource(files = "src/test/resources/shouldSearchCityWithTwoSymbols.csv")
    void shouldSendFormSearchCityWithTwoSymbols(String querySymbols, String cityToFind) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        String date = DataGenerator.generateDate(3);
        cityInput.setValue(querySymbols);
        $$("div.popup div.menu-item").find(Condition.text(cityToFind)).click();
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE).setValue(date);
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        successNotification.shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(text(date))
                .shouldHave(text("Успешно"));
    }

    @Test
    @DisplayName("should Send Form Search City With Two Symbols")
    void shouldAddDateInCalendar() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        LocalDate dayNextWeek = LocalDate.now().plusDays(7);
        String newDay = Integer.toString(dayNextWeek.getDayOfMonth());
        cityInput.setValue(validUser.getCity());
        $("[data-test-id=date] button").click();
        if (LocalDate.now().plusDays(3).getMonthValue() != dayNextWeek.getMonthValue()) {
            $(" div[data-step='1']").click();
        }
        $$("td.calendar__day").find(Condition.text(newDay)).click();
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        successNotification.shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(text(newDay))
                .shouldHave(text("Успешно"));
    }
}
