package net.datebook.www.entities;

import net.datebook.www.interfaces.Repeatable;
import net.datebook.www.utils.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class everyMonthTask extends Task implements Repeatable {
    public everyMonthTask(String heading, String description, Util.Relate relate, LocalDateTime deadLine, Util.Repeat repeat) {
        super(heading, description, relate, deadLine, repeat);
    }

    @Override
    public LocalDateTime findNextDate(LocalDate date) {

        while (getDeadLine().toLocalDate().isBefore(date)) {
            setDeadLine(getDeadLine().plusMonths(1));
        }

        return getDeadLine();
    }
}
