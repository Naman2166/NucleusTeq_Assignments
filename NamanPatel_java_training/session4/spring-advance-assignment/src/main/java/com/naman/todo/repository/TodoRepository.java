package com.naman.todo.repository;

import com.naman.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository for Todo entity to perform database operations
// it extends JpaRepository to use build-in CRUD methods
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
