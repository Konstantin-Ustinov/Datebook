package net.datebook.www.services;

import net.datebook.www.entities.Task;

import java.util.HashMap;
import java.util.Map;

public class TaskService {
    private Map<Integer, Task> tasksList = new HashMap<>();

    // Возвращает true если задача добавлена и false если задача обновлена
    public boolean addOrUpdate(Task newTask) {
        return tasksList.put(newTask.getId(), newTask) == null;
    }

    // Возвращает null если задачи под таким id нет в списке. Либо возвращает задачу.
    public Task findById(int id) {
        return tasksList.get(id);
    }

    // Возвращает true если задача успешно восстановлена
    public boolean restore(int id) {
        tasksList.get(id).setDeleteFalse();
        return !tasksList.get(id).isDelete();
    }

    // Возвращает true если задача успешно удалена
    public boolean delete(int id) {
        tasksList.get(id).setDeleteTrue();
        return tasksList.get(id).isDelete();
    }

    public Map<Integer, Task> getTasksList() {
        return tasksList;
    }
}
