package net.datebook.www.entities;

import net.datebook.www.interfaces.Repeatable;
import net.datebook.www.utils.Util.Relate;
import net.datebook.www.utils.Util.Repeat;
import static net.datebook.www.utils.Util.formatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;


public class Task implements Repeatable{
    public static int globalId;
    private int id;
    private String heading;
    private String description;
    private Relate relate;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime deadLine;
    private boolean isDelete;
    private Repeat repeat;

    public Task(String heading, String description, Relate relate, LocalDateTime deadLine, Repeat repeat) {
        this.heading = (heading != null && !heading.isEmpty()) ? heading : "Не указано";
        this.description = (description != null && !description.isEmpty()) ? description : "Не указано";
        this.relate = relate != null ? relate : Relate.PERSON;
        this.deadLine = deadLine != null ? deadLine : LocalDateTime.now();
        this.repeat = repeat != null ? repeat : Repeat.NONE;
        globalId++;
        this.id = globalId;
    }

    public int getId() {
        return id;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDeleteTrue() {
        isDelete = true;
    }

    public void setDeleteFalse() {
        isDelete = false;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setHeading(String heading) {
        this.heading = (heading != null && !heading.isEmpty()) ? heading : "Не указано";
    }

    public void setDescription(String description) {
        this.description = (description != null && !description.isEmpty()) ? description : "Не указано";
    }

    public void setRelate(Relate relate) {
        this.relate = relate != null ? relate : Relate.PERSON;
    }

    public void setDeadLine(LocalDateTime deadLine) {
        this.deadLine = deadLine != null ? deadLine : LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ID задачи: " + id +
                "\nЗаголовок: " + heading +
                "\nОписание: " + description +
                "\nДата создания: " + createdDate.atZone(ZoneId.systemDefault()).format(formatter) +
                "\nДата выполнения: " + deadLine.format(formatter) +
                "\nЗадача " + relate +
                "\nПовторяемость: " + repeat;
    }

    public String showShortTask() {
        return "ID задачи: " + id +
                "\nЗаголовок: " + heading +
                "; Дата выполнения: " + deadLine.format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && isDelete == task.isDelete && heading.equals(task.heading) && description.equals(task.description) && relate == task.relate && createdDate.equals(task.createdDate) && deadLine.equals(task.deadLine) && repeat == task.repeat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, heading, description, relate, createdDate, deadLine, isDelete, repeat);
    }

    @Override
    public LocalDateTime findNextDate(LocalDate date) {

        return getDeadLine();
    }
}
