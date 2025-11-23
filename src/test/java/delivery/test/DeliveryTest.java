package delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import delivery.data.DataGenerator;
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
    @BeforeAll
    static void setUpAll(){
        SelenideLogger.addListener("allure",new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll(){
        SelenideLogger.removeListener("allure");
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
        successNotification.should(Condition.visible).shouldHave(text(date))
                .shouldHave(text("Встреча успешно запланирована на "));;
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE).setValue(date);
        submitButton.click();
        $("[data-test-id=replan-notification]")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        replaneNotificationButton.should(Condition.visible).click();
        successNotification.should(Condition.visible).shouldHave(text(date))
                .shouldHave(text("Встреча успешно запланирована на "));
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

    @Test
    @DisplayName("should Not Send Form With Invalid Name")
    void shouldNotSendFormWithInvalidNamesEn() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(DataGenerator.generateCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(DataGenerator.invalidNamesEn());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidName.should(Condition.visible)
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    @DisplayName("should Not Send Form With Invalid Names With Special Chars")
    void shouldNotSendFormWithInvalidNamesWithSpecialChars() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(DataGenerator.generateCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(DataGenerator.invalidNamesWithSpecialChars());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidName.should(Condition.visible)
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    @DisplayName("should Not Send Form With invalid Names With Nums")
    void shouldNotSendFormWithInvalidNamesWithNums() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(DataGenerator.generateCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(DataGenerator.invalidNamesWithNums());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        invalidName.should(Condition.visible)
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    @DisplayName("should Auto Correct With invalid Long Phones")
    void shouldAutoCorrectWithInvalidLongPhones() {
        var PhoneNumber = DataGenerator.invalidLongPhones();
        var validUser = DataGenerator.Registration.generateUser("ru");
        String formPhone = DataGenerator.formatter(PhoneNumber);
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(PhoneNumber);
        phoneInput.shouldHave(Condition.exactValue(formPhone));
    }

    @Test
    @DisplayName("should Auto Correct With Invalid Long Phones With Chars")
    void shouldAutoCorrectWithInvalidLongPhonesWithChars() {
        var PhoneNumber = DataGenerator.invalidLongPhonesWithChars();
        var validUser = DataGenerator.Registration.generateUser("ru");
        String formPhone = DataGenerator.formatter(PhoneNumber);
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(PhoneNumber);
        phoneInput.shouldHave(Condition.exactValue(formPhone));
    }

    @Test
    @DisplayName("should Auto Correct With Invalid Long Phones With Special Chars")
    void shouldAutoCorrectWithInvalidLongPhonesWithSpecialChars() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var PhoneNumber = DataGenerator.invalidLongPhonesWithSpecialChars();
        String formPhone = DataGenerator.formatter(PhoneNumber);
        cityInput.setValue(validUser.getCity());
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate(3));
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(PhoneNumber);
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

    @Test
    @DisplayName("should Not Send Form With Invalid City With Nums")

    void shouldNotSendFormWithInvalidCityWithNums() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(DataGenerator.invalidCitiesWithNums());
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
    @DisplayName("should Not Send Form With Invalid City With Spec Chars")
    void shouldNotSendFormWithInvalidCityWithSpecChars() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(DataGenerator.invalidCitiesWithSpecChars());
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
    @DisplayName("should Not Send Form With Invalid City")
    void shouldNotSendFormWithInvalidCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        cityInput.setValue(DataGenerator.generateInvalidCity());
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

    @Test
    @DisplayName("should Send Form Search City With Two Symbols")
    void shouldSendFormSearchCityWithTwoSymbols() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var city = DataGenerator.generateCity();
        var twoSymbolsOfCity = city.substring(0,2);
        String date = DataGenerator.generateDate(3);
        cityInput.setValue(twoSymbolsOfCity);
        $$("div.popup div.menu-item").find(Condition.text(city)).click();
        dateInput.press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE).setValue(date);
        nameInput.setValue(validUser.getName());
        phoneInput.setValue(validUser.getPhone());
        agreementCheckBox.click();
        submitButton.click();
        successNotification.shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(text(date))
                .shouldHave(text("Встреча успешно запланирована на "));
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
                .shouldHave(text("Встреча успешно запланирована на "));
    }
}
