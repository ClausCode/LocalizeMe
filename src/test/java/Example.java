import com.clauscode.localization.group.Group;
import com.clauscode.localization.store.RemoteLocalizationStorage;
import com.clauscode.localization.store.Storage;

public class Example {
    private static Storage storage;

    public static void main(String[] args) {
        storage = new RemoteLocalizationStorage(
                "com.mysql.jdbc.Driver",
                "org.hibernate.dialect.MySQLDialect",
                "jdbc:mysql://localhost:3306/catalyst_db",
                "root",
                "root"
        );
        storage.readFromFile("localization/ru-RU.lang", "ru-RU");
        storage.readFromFile("localization/en-US.lang", "en-US");
        getSingle();
        getGroup();
    }

    public static void getGroup() {
        Group lang = storage.buildFactory("en-US")
                .createGroup()
                .withString("shop.exception.InsufficientFunds")
                .withProperty("name", "Кактус")
                .build();

        String messageError = lang.get("shop.exception.InsufficientFunds")
                .build();

        System.out.println(messageError);
    }

    public static void getSingle() {
        String message = storage.buildFactory("ru-RU")
                .getSingle("shop.exception.InsufficientFunds")
                .withProperty("name", "Кактус")
                .build();

        System.out.println(message);
    }
}
