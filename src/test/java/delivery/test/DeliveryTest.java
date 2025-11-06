package delivery.test;

import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import delivery.data.DataGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.Keys;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }



    @Test
   // @CsvFileSource(files = "src/test/resources/shouldSuccessfulPlanAndReplanMeeting.csv", numLinesToSkip = 1)
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date ] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(text("Запланировать")).click();
        $("[data-test-id=success-notification]").should(Condition.visible)
                .shouldHave(text(DataGenerator.generateDate()));
        $("[data-test-id=date ] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $$("button").find(text("Запланировать")).click();
        $("[data-test-id=replan-notification] button").should(Condition.visible).click();
        $("[data-test-id=success-notification]").should(Condition.visible)
                .shouldHave(text(DataGenerator.generateDate()));
    }

    @Test
    @DisplayName("should Not Replan Meeting")
    void shouldNotReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date ] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(text("Запланировать")).click();
        $("[data-test-id=success-notification]").should(Condition.visible)
                .shouldHave(text(DataGenerator.generateDate()));
        $("[data-test-id=date ] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $$("button").find(text("Запланировать")).click();
        $("[data-test-id=date] span.input_invalid").should(Condition.visible)
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    //адаптированные тесты из прошлого ДЗ в качестве регресса.

    @ParameterizedTest
    @MethodSource("delivery.data.DataGenerator#invalidNames")
    void shouldNotSendFormWithInvalidName(String invalidNames) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(DataGenerator.generateCity());
        $("[data-test-id=date] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $("[data-test-id=name] input").setValue(invalidNames);
        $$("[data-test-id=phone] input").find(Condition.visible).setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=name].input_invalid").should(Condition.visible);
        $$("[data-test-id=name].input_invalid ").find(Condition.visible)
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @ParameterizedTest
    @MethodSource("delivery.data.DataGenerator#invalidPhones")
    void shouldAutoCorrectWithInvalidPhone(String phone) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        String formPhone = DataGenerator.formatter(phone);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=phone] input.input__control").shouldHave(Condition.exactValue(formPhone));
    }

    @Test
    void shouldNotSendFormWithShortPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue("+" + DataGenerator.shortPhoneGenerator());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=phone].input_invalid ").shouldBe(Condition.visible)
                .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678"));
    }

    @ParameterizedTest
    @MethodSource("delivery.data.DataGenerator#invalidCities")
    void shouldNotSendFormWithInvalidCity(String city) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(city);
        $("[data-test-id=date] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=city].input_invalid").should(Condition.visible)
                .shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotSendFormWithInvalidDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=date] span.input_invalid").should(Condition.visible)
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotSendFormWithInvalidDateInFuture() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE).setValue(DataGenerator.generateDate("Future"));
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=date] span.input_invalid").should(Condition.visible)
                .shouldHave(text("Неверно введена дата"));
    }

    @Test
    void shouldNotSendFormWithEmptyName() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $("[data-test-id=name] input").setValue("  ");
        $("[data-test-id=phone] input").setValue("+79114359999");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=name].input_invalid").should(Condition.visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendFormWithEmptyPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue("  ");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=phone].input_invalid").should(Condition.visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendFormWithEmptyCheckbox() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=agreement].input_invalid").should(Condition.visible);
    }

    @Test
    void shouldNotSendFormWithEmptyCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=date] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(DataGenerator.generateDate());
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=city].input_invalid").should(Condition.visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendFormWithEmptyDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE).setValue(" ");
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=date] span.input_invalid").should(Condition.visible)
                .shouldHave(text("Неверно введена дата"));
    }

    @Test
    void shouldNotSendFormWithUnCorrectDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=date] span.input_invalid").should(Condition.visible)
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/shouldSearchCityWithTwoSymbols.csv")
    void shouldSendFormSearchCityWithTwoSymbols(String querySymbols, String cityToFind) {
        var validUser = DataGenerator.Registration.generateUser("ru");
        String date = DataGenerator.generateDate();
        $("[data-test-id=city] input").setValue(querySymbols);
        $$("div.popup div.menu-item").find(Condition.text(cityToFind)).click();
        $("[data-test-id=date] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(date);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=success-notification]").shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(text(date))
                .shouldHave(text("Успешно"));
    }

    @Test
    void shouldAddDateInCalendar() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        LocalDate dayNextWeek = LocalDate.now().plusDays(7);
        String newDay = Integer.toString(dayNextWeek.getDayOfMonth());
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] button").click();
        if (LocalDate.now().plusDays(3).getMonthValue() != dayNextWeek.getMonthValue()) {
            $(" div[data-step='1']").click();
        }
        $$("td.calendar__day").find(Condition.text(newDay)).click();
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=success-notification]").shouldBe(Condition.visible,Duration.ofSeconds(15))
                .shouldHave(text(newDay))
                .shouldHave(text("Успешно"));
    }
}
