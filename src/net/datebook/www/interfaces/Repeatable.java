package net.datebook.www.interfaces;

import java.time.LocalDateTime;

public interface Repeatable {
    LocalDateTime findSoonDate();
}
