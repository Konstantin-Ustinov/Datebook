package net.datebook.www.utils;

import java.time.format.DateTimeFormatter;

public class Util {
    public enum Relate {PERSON, WORK}
    public enum Repeat {NONE, EVERY_DAY, EVERY_WEEK, EVERY_MONTH, EVERY_EAR}
    public static DateTimeFormatter formatterWithTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static void showMessage(String message) {
        if (message != null && !message.isEmpty()) {
            for (int i = 0; i < message.length() + 4; i++) {
                System.out.print("-");
            }
            System.out.println("\n| " + message + " |");

            for (int i = 0; i < message.length() + 4; i++) {
                System.out.print("-");
            }
            System.out.print("\n");
        }
    }
}
