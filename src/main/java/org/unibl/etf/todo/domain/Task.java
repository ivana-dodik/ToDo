package org.unibl.etf.todo.domain;

import java.time.LocalDateTime;

public record Task(int id, String name, LocalDateTime due, Priority priority, boolean completed) {
}
