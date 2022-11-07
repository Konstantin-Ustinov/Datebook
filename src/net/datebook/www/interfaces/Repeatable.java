package net.datebook.www.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Repeatable {
    LocalDateTime findNextDate(LocalDate date);
}
