package delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataGenerator {
    private DataGenerator() {
    }

    //Faker faker = new Faker(new Locale("ru"));

    public static String generateDate(int shift) {
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateCity() {
        String[] city = {"Абакан", "Биробиджан", "Великий Новгород", "Волгоград", "Иваново", "Калининград", "Кострома",
                "Курск", "Магадан", "Мурманск", "Омск", "Петрозаводск", "Петропавловск-Камчатский", "Санкт-Петербург",
                "Севастополь", "Ставрополь", "Томск", "Хабаровск", "Элиста", "Ярославль", "Луганск", "Донецк"};
        Random randomizer = new Random();
        int randomNum = randomizer.nextInt(city.length - 1);
        return city[randomNum];
    }

    public static String generateName(Faker faker) {
        return faker.name().firstName().toUpperCase() + " " + faker.name().lastName().toUpperCase();

    }

    public static String generatePhone(Faker faker) {
        return faker.phoneNumber().phoneNumber();
    }

    public static class Registration {
        private static Faker faker;

        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return new UserInfo(generateCity(), generateName(faker), generatePhone(faker));
        }
    }

    //дополнительные методы
    public static String invalidNamesEn() {
        Faker faker = new Faker(new Locale("en"));
        return faker.name().firstName().toUpperCase();
    }

    public static String invalidNamesWithSpecialChars() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.regexify(faker.name().fullName().toUpperCase() + "[!@#$%&*()_+\\=]{1}");
    }

    public static String invalidNamesWithNums() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().fullName().toUpperCase() + Integer.toString(faker.number().randomDigit());
    }

    public static String invalidLongPhones() {
        Faker faker = new Faker(new Locale("en"));
          return faker.number().digits(15);
    }

    public static String invalidLongPhonesWithChars() {
        Faker faker = new Faker(new Locale("en"));
     return  faker.number().digits(5) + faker.name().prefix().toUpperCase() + faker.number().digits(7);
    }

    public static String invalidLongPhonesWithSpecialChars() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.number().digits(5)
                    + faker.regexify("[!@#$%&*()_+\\-=]{1}")
                    + faker.number().digits(7);
    }

    public static String formatter(String toFormat) {
        String tmpString1 = toFormat.replaceAll("\\D", "");
        if (tmpString1.length() >= 12) {
            tmpString1 = tmpString1.substring(0, 11);
        }
        tmpString1 = MessageFormat.format(
                "+{0} {1} {2} {3} {4}",
                tmpString1.substring(0, 1),
                tmpString1.substring(1, 4),
                tmpString1.substring(4, 7),
                tmpString1.substring(7, 9),
                tmpString1.substring(9)
        );
        return tmpString1;
    }

    public static String shortPhoneGenerator() {
        return new Faker(new Locale("en")).number().digits(10);
    }

    public static String generateInvalidCity() {
        String[] cities = {
                "Алушта", "Великие Луки", "Гатчина", "Дербент", "Ейск", "Зеленогорск", "Канск", "Кинешма",
                "Котлас", "Лысьва", "Мичуринск", "Можайск", "Новоалтайск", "Онега", "Полевской", "Сарапул",
                "Сегежа", "Сосновый Бор",
                "Тихорецк", "Шуя"
        };
        Random randomizer = new Random();
        int randomNum = randomizer.nextInt(cities.length - 1);
        return cities[randomNum];
    }

    public static String invalidCitiesWithSpecChars() {
        Faker faker = new Faker(new Locale("en"));
           return generateCity() + faker.regexify("[!@#$%&*()_+\\=]{1}");
    }

    public static String invalidCitiesWithNums() {
        Faker faker = new Faker(new Locale("en"));
        return  generateInvalidCity() + faker.regexify("[1234567890]{1}");

    }
    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
