package net.datebook.www;

import net.datebook.www.entities.*;
import net.datebook.www.interfaces.Repeatable;
import net.datebook.www.services.TaskService;
import net.datebook.www.utils.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static net.datebook.www.utils.Util.*;

public class Main {
    public static String message = null;
    public static Scanner scanner = new Scanner(System.in);
    public static TaskService taskService = new TaskService();

    public static void main(String[] args) {
        fillTasksList();

        boolean isShowMenu = true;

        System.out.println("Добро пожаловать в Ежедневник!");

        do {
            showMessage(message);
            message = ""; // Стираем сообщение, что бы не показывалось при следующем выводе
            System.out.println("_________________");
            System.out.println("Введите комманду:");
            System.out.println(" \"1\" - Добравить задачу;");
            System.out.println(" \"2\" - Показать задачу;");
            System.out.println(" \"3\" - Изменить задачу;");
            System.out.println(" \"4\" - Удалить задачу;");
            System.out.println(" \"5\" - Показать все задачи");
            System.out.println(" \"6\" - Показать задачи на дату");
            System.out.println(" \"7\" - Показать удаленные задачи;");
            System.out.println(" \"0\" - В начало.");

            switch (enterText()) {
                case "1" -> add();
                case "2" -> findById(-1);
                case "3" -> update(-1);
                case "4" -> deleteOrRestore(-1); // аргумент -1 для того чтобы запустить код по вводу ID в методе
                case "5" -> findAll(true);
                case "6" -> findAll(false);
                case "7" -> findDeletedTasks();
                case "0" -> isShowMenu = false;
                default -> System.out.println("Такой комманды нет. Выберете комманду из списка");
            }
        } while (isShowMenu);

    }

    private static void fillTasksList() {
        taskService.addOrUpdate(new Task("Задача 1", "Описание задачи 1", Relate.WORK, LocalDateTime.now(), Repeat.NONE));
        taskService.addOrUpdate(new Task("Задача 2", "Описание задачи 2", Relate.PERSON, LocalDateTime.now().plusDays(1), Repeat.EVERY_EAR));
        taskService.addOrUpdate(new Task("Задача 3", "Описание задачи 3", Relate.WORK, LocalDateTime.now().plusDays(3), Repeat.EVERY_DAY));
        taskService.addOrUpdate(new Task("Задача 4", "Описание задачи 4", Relate.PERSON, LocalDateTime.now().plusDays(4), Repeat.EVERY_WEEK));
        taskService.addOrUpdate(new Task("Задача 5", "Описание задачи 5", Relate.WORK, LocalDateTime.now().plusDays(1), Repeat.EVERY_DAY));
        taskService.addOrUpdate(new Task("Задача 6", "Описание задачи 6", Relate.PERSON, LocalDateTime.now().plusDays(3), Repeat.NONE));
        taskService.addOrUpdate(new Task("Задача 7", "Описание задачи 7", Relate.WORK, LocalDateTime.now().plusDays(5), Repeat.EVERY_MONTH));
    }

    private static void add() {
        String taskHeading;
        String taskDescription;
        Relate relate = null;
        Repeat repeat = null;
        LocalDateTime deadline = null; // Ставим NULL чтобы запустить цикл по валидации ввода дедлайна

        System.out.println("Добавление задачи \n -----------------");

        System.out.println("Введите заголовок задачи:");
        taskHeading = enterText();

        System.out.println("Введите описание задачи:");
        taskDescription = enterText();

        System.out.println("Личная(1) или рабочая(2)? (введите 1 или 2)");
        while (relate == null) {
            switch (enterText()) {
                case "1" -> relate = Relate.PERSON;
                case "2" -> relate = Relate.WORK;
                default -> System.out.println("Введите 1 или 2");
            }
        }

        System.out.println("Введите дату завершения задачи:");
        deadline = enterDeadline();

        System.out.println("Введите частоту повторения задачи:");
        System.out.println("1 - без повторения");
        System.out.println("2 - каждый день");
        System.out.println("3 - каждую неделю");
        System.out.println("4 - каждый месяц");
        System.out.println("5 - каждый год");

        Task newTask = null;

        while (repeat == null) {
            switch (enterText()) {
                case "1" -> {repeat = Repeat.NONE;  newTask = new Task(taskHeading, taskDescription, relate, deadline, repeat);}
                case "2" -> {repeat = Repeat.EVERY_DAY; newTask = new everyDayTask(taskHeading, taskDescription, relate, deadline, repeat);}
                case "3" -> {repeat = Repeat.EVERY_WEEK; newTask = new everyWeekTask(taskHeading, taskDescription, relate, deadline, repeat);}
                case "4" -> {repeat = Repeat.EVERY_MONTH; newTask = new everyMonthTask(taskHeading, taskDescription, relate, deadline, repeat);}
                case "5" -> {repeat = Repeat.EVERY_EAR; newTask = new everyYearTask(taskHeading, taskDescription, relate, deadline, repeat);}
                default -> System.out.println("Введите цифру из списка");
            }
        }

        message = taskService.addOrUpdate(newTask) ? "Задача была добавлена" : "Задача была обновлена";
    }

    private static void findById(int taskId) {
        taskId = taskId == -1 ? enterId() : taskId;
        Task task = taskService.findById(taskId);

        System.out.println("taskId = " + taskId);
        try {
            System.out.println(task.toString());
            taskMenu(task);
        } catch (NullPointerException e) {
            System.out.println("Такой задачи нет в списке");
        }

    }

    private static void update(int taskId) {
        taskId = taskId == -1 ? enterId() : taskId;
        Task changedTask = taskService.findById(taskId);

        try {
            System.out.println("Введите новое название:");
            changedTask.setHeading(enterText());

            System.out.println("Введите новое описание:");
            changedTask.setDescription(enterText());

            changedTask.setDeadLine(enterDeadline());

            if (!taskService.addOrUpdate(changedTask)) {
                showMessage("Задача успешно изменена!");
            } else {
                showMessage("Задача была добавлена :-о ");
            }

            findById(changedTask.getId());
        } catch (NullPointerException e) {
            System.out.println("Такой задачи нет в списке");
        }
    }

    private static void deleteOrRestore(int taskId) {
        taskId = taskId == -1 ? enterId() : taskId;

        if (taskService.findById(taskId).isDelete()) {
            if (taskService.restore(taskId)) {
                showMessage("Задача успешно восстановлена.");
            } else {
                showMessage("Не удалось восстановить задачу. Убедитесь в правильности введенного ID.");
                deleteOrRestore(-1);
            }
        } else {
            System.out.println("Точно удалить задачу с ID = " + taskId + "?");
            System.out.println("1 - да; 2 - нет");
            if (scanner.next().equals("1")) {
                if (taskService.delete(taskId)) {
                    showMessage("Задача успешно удалена.");
                } else {
                    showMessage("Не удалось удалить задачу. Убедитесь в правильности введенного ID.");
                    deleteOrRestore(-1);
                }
            }
        }
    }

    private static void findAll(boolean isAll) {
        var tasks = new ArrayList<Task>();

        if (isAll) {
            for (var task : taskService.getTasksList().values()) {
                if (!task.isDelete()) {
                    tasks.add(task);
                }
            }
            showMessage("Показаны все задачи:");
        } else {
            LocalDate date = enterDate();

            for (var task : taskService.getTasksList().values()) {
                if (date.isEqual(task.findNextDate(date).toLocalDate())) {
                    tasks.add(task);
                }
            }
        }
        tasks.sort(Comparator.comparing(Task::getDeadLine));
        showTasksList(tasks);
    }

    private static void findDeletedTasks() {

        var tasks = new ArrayList<Task>();

        for (var task : taskService.getTasksList().values()) {
            if (task.isDelete()) {
                tasks.add(task);
            }
        }

        showMessage("Показаны все удаленные задачи:");
        showTasksList(tasks);
    }

    private static void showTasksList(ArrayList<Task> tasks) {
        try {
            LocalDateTime date = tasks.get(0).getDeadLine();
            System.out.println("Показаны задачи на дату: " + tasks.get(0).getDeadLine().format(formatter));
            for (int i = 0; i < tasks.size(); i++) {
                if (i != 0) {
                    System.out.println("------------------");
                }
                if (!date.isEqual(tasks.get(i).getDeadLine())) {
                    System.out.println();
                    System.out.println("Задачи на число: " + tasks.get(i).getDeadLine().format(formatter));
                }
                date = tasks.get(i).getDeadLine();
                System.out.print((i + 1) + ") ");
                System.out.println(tasks.get(i).showShortTask());
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("На эту дату нет задач.");
        }

    }

    private static void taskMenu(Task task) {
        String input;
        boolean showMenu = true;

        while (showMenu) {
            System.out.println("____________");
            System.out.println("Меню задачи:");
            System.out.println(" \"1\" - Изменить задачу;");

            if (task.isDelete()) {
                System.out.println(" \"2\" - Восстановить удаленную задачу;");
            } else {
                System.out.println(" \"2\" - Удалить задачу;");
            }

            System.out.println(" \"3\" - Следующая дата задачи;");

            System.out.println(" \"0\" - Вернуться назад.");

            input = scanner.next();

            switch (input) {
                case "1" -> {update(task.getId());
                    showMenu = false;}
                case "2" -> {deleteOrRestore(task.getId());
                    showMenu = false;}
                case "3" -> {showMessage("Следующая дата задачи: " + task.findNextDate(LocalDate.now()).format(formatter));
                    showMenu = false;}
                case "0" -> showMenu = false;
                default -> System.out.println("Вы ввели комманду: " + input
                        + "такой комманды нет. Выберете комманду из списка");
            }
        }
    }

    private static LocalDateTime enterDeadline() {
        String inputDeadline = "";

        do { // цикл по вводу даты дедлайна с валидацией

            System.out.println("На какой день поставить задачу? (День.Месяц.Год Часы:Минуты) Пример: 31.01.2022 13:30");
            inputDeadline = enterText();

            if (inputDeadline.matches("[0-3]\\d\\.[0-1]\\d\\.[2-9]\\d\\d\\d [0-2]\\d:[0-5]\\d")) { //Проверем дату по регулярному выражению
                return LocalDateTime.parse(inputDeadline, Util.formatterWithTime); // переводим строку в дату
            } else {
                System.out.println("Поле должно быть в формате (День.Месяц.Год Часы:Минуты) и не может быть пустым");
            }

        } while (true); // выполняем цикл пока не введена правильно дата дедлайна
    }

    private static int enterId() {
        int taskId = -1;

        while (taskId == -1) {
            System.out.println("Введите ID задачи:");

            try {
                taskId = scanner.nextInt();
                if (taskId <= 0) {
                    System.out.println("Введите положительное число");
                    taskId = -1;
                }
            } catch (InputMismatchException e) {
                System.out.println("Введите число");
            }
        }
        return taskId;
    }

    private static String enterText() {
        while (true) {
            String input = scanner.nextLine();
            if (input != null && !input.isEmpty()) {
                return input;
            } else {
                System.out.println("Поле не может быть пустым");
            }
        }
    }

    private static LocalDate enterDate() {
        String inputDate = "";

        do { // цикл по вводу даты с валидацией

            System.out.println("На какой день показать задачи? (День.Месяц.Год) Пример: 31.01.2022");
            inputDate = enterText();

            if (inputDate.matches("[0-3]\\d\\.[0-1]\\d\\.[2-9]\\d\\d\\d")) { //Проверем дату по регулярному выражению
                return LocalDate.parse(inputDate, Util.formatter); // переводим строку в дату
            } else {
                System.out.println("Поле должно быть в формате (День.Месяц.Год) и не может быть пустым");
            }

        } while (true); // выполняем цикл пока не введена правильно дата
    }

}
