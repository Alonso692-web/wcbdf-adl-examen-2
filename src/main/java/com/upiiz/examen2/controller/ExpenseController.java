package com.upiiz.examen2.controller;

import com.upiiz.examen2.entities.Expense;
import com.upiiz.examen2.services.ExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@Tag(
        name = "Expenses"
)
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    //@PreAuthorize("hasAuthority('READ')")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MODERATOR', 'EDITOR', 'DEVELOPER', 'ANALYST')")
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAuthority('READ')")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MODERATOR', 'EDITOR', 'DEVELOPER', 'ANALYST')")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        Expense expense = expenseService.getExpenseById(id);
        if (expense == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(expense);
    }

    @PostMapping
    //@PreAuthorize("hasAuthority('CREATE') or hasAuthority('CREATE-USER' or hasAuthority('WRITE'))")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    public Expense createExpense(@RequestBody Expense expense) {
        return expenseService.createExpense(expense);
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAuthority('UPDATE')")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'EDITOR', 'DEVELOPER')")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expenseDetails) {
        Expense updatedExpense = expenseService.updateExpense(id, expenseDetails);
        if (updatedExpense == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthority('DELETE')")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER','ANALYST')")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}