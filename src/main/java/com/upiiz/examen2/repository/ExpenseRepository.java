package com.upiiz.examen2.repository;

import com.upiiz.examen2.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}