# Usage LocalizeME
### Before you can use your localized text, you need to follow these steps:
* Configure change log files for your texts
* Initialize the storage by connecting it to your database
* Start using LocalizeMe in your project

### Create .lang files in your resources:

localization/ru-RU.lang
```lang
shop.exception.InsufficientFunds=У вас недостаточно средств для покупки товара :name
...
```
localization/en-US.lang
```lang
You don't have enough money to buy a :name product
...
```

### Create a new storage instance
```java
Storage storage = new RemoteLocalizationStorage(
    "com.mysql.jdbc.Driver", //JDBS Driver
    "org.hibernate.dialect.MySQLDialect", //SQL Dialect
    "jdbc:mysql://localhost:3306/your_db", //URL
    "root", //Username
    "root" //Password
);
```
### Import localization from files
```java
storage.readFromFile("localization/ru-RU.lang", "ru-RU");
storage.readFromFile("localization/en-US.lang", "en-US");
```
Get a factory instance with the desired language (it can be changed later)
```java
Factory lang = storage.buildFactory("en-US")
```
### There are two ways to get your strings:
### Single
> You are requesting one instance of the translated string from the database at a time
```java
String message = lang.getSingle("shop.success.SuccessfulPurchase")
        .withProperty("name", "Pizza")
        .build();

System.out.println(message);
```
### Group
> You are requesting multiple instances of translated strings from the database at the same time
```java
Group purchaseMessages = lang.createGroup()
        .withLanguage("ru-RU") // The group allows language change during formation
        .withString("shop.exception.InsufficientFunds")
        .withString("shop.success.SuccessfulPurchase")
        .withProperty("name", "Pizza")
        .build();

String messageError = purchaseMessages.get("shop.exception.InsufficientFunds")
        .build();
String messageSuccess = purchaseMessages.get("shop.success.SuccessfulPurchase")
        .build();

System.out.println(messageError);
System.out.println(messageSuccess);
```

# Example
https://github.com/ClausCode/JustGUI/blob/master/src/test/Example.java
