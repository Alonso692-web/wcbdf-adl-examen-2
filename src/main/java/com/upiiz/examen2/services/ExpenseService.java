package com.upiiz.examen2.services;

import com.upiiz.examen2.entities.Expense;

import java.util.List;

public interface ExpenseService {
    List<Expense> getAllExpenses();

    Expense getExpenseById(Long id);

    Expense createExpense(Expense expense);

    Expense updateExpense(Long id, Expense expenseDetails);

    void deleteExpense(Long id);
}