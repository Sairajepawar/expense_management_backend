package com.sairaj.expense.tracker.repository;

import com.sairaj.expense.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
