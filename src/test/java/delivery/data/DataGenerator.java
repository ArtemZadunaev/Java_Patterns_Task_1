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

    public static String generateDate() {
        return LocalDate.now().plusDays(new Random().nextInt(400) + 3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateDate(String Future) {
        return LocalDate.now().plusYears(new Random().nextInt(1000) + 100).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
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
    static List<String> invalidNames() {
        Faker faker = new Faker(new Locale("en"));
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                names.add(faker.name().firstName());
            } else if (i % 3 == 0) {
                names.add(faker.internet().emailAddress());
            } else {
                names.add(Integer.toString(faker.number().randomDigit()));
            }

        }
        return names;
    }

    static List<String> invalidPhones() {
        Faker faker = new Faker(new Locale("en"));
        List<String> phones = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                phones.add(faker.number().digits(5) + faker.regexify("[!@#$%&*()_+\\-=]{1}")
                        + faker.number().digits(10));
            } else if (i % 3 == 0) {
                phones.add(faker.number().digits(5) + faker.name().prefix() + faker.number().digits(10));
            } else {
                phones.add(faker.number().digits(15));
            }

        }
        return phones;
    }

    public static String formatter(String toFormat) {
        String tmpString1 = toFormat.replaceAll("\\D", "");
        if (tmpString1.length() > 12) {
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

    private static String generateInvalidCity() {
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

    static List<String> invalidCities() {
        Faker faker = new Faker(new Locale("en"));
        List<String> invalidCities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                invalidCities.add(generateInvalidCity() + faker.regexify("[!@#$%&*()_+\\-=]{1}"));

            } else if (i % 3 == 0) {
                invalidCities.add(generateInvalidCity() + faker.regexify("[1234567890]{1}"));
            } else {
                invalidCities.add(generateInvalidCity());
            }

        }
        return invalidCities;
    }


    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
