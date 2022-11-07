package net.datebook.www.entities;

import net.datebook.www.interfaces.Repeatable;
import net.datebook.www.utils.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class everyYearTask extends Task implements Repeatable {
    public everyYearTask(String heading, String description, Util.Relate relate, LocalDateTime deadLine, Util.Repeat repeat) {
        super(heading, description, relate, deadLine, repeat);
    }

    @Override
    public LocalDateTime findNextDate(LocalDate date) {

        while (getDeadLine().toLocalDate().isBefore(date)) {
            setDeadLine(getDeadLine().plusYears(1));
        }

        return getDeadLine();
    }
}
