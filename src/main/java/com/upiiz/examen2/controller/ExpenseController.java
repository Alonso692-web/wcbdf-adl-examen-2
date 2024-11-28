package com.upiiz.examen2.controller;

import com.upiiz.examen2.entities.Expense;
import com.upiiz.examen2.responses.CustomResponse;
import com.upiiz.examen2.services.ExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v1/expenses")
@Tag(
        name = "Expenses"
)
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<CustomResponse<List<Expense>>> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        Link allExpensesLink = linkTo(ExpenseController.class).withSelfRel();
        List<Link> links = List.of(allExpensesLink);
        try {
            expenses = expenseService.getAllExpenses();
            if (!expenses.isEmpty()) {
                CustomResponse<List<Expense>> response = new CustomResponse<>(1, "Gastos encontrados", expenses, links);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse<>(0, "Gastos no encontrados", expenses, links));
            }
        } catch (Exception e) {
            CustomResponse<List<Expense>> response = new CustomResponse<>(500, "Error interno de servidor", expenses, links);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Expense>> getExpenseById(@PathVariable Long id) {
        Optional<Expense> expense = null;
        CustomResponse<Expense> response = null;
        Link allExpensesLink = linkTo(ExpenseController.class).withSelfRel();
        List<Link> links = List.of(allExpensesLink);
        try {
            expense = expenseService.getExpenseById(id);
            if (expense.isPresent()) {
                response = new CustomResponse<>(1, "Gasto encontrado", expense.get(), links);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new CustomResponse<>(0, "Gasto no encontrado", null, links);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response = new CustomResponse<>(500, "Error interno de servidor", null, links);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<CustomResponse<Expense>> crearExpense(@RequestBody Expense expense) {
        Link allExpensesLink = linkTo(ExpenseController.class).withSelfRel();
        List<Link> links = List.of(allExpensesLink);
        try {
            Expense expense1 = expenseService.createExpense(expense);
            if (expense1 != null) {
                CustomResponse<Expense> response = new CustomResponse<>(1, "Gasto creado", expense1, links);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse<>(0, "Gasto no encontrado", expense1, links));
            }
        } catch (Exception e) {
            CustomResponse<Expense> response = new CustomResponse<>(500, "Error interno de servidor", null, links);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Expense>> updateExpense(@RequestBody Expense expense, @PathVariable Long id) {
        Link allExpensesLink = linkTo(ExpenseController.class).withSelfRel();
        List<Link> links = List.of(allExpensesLink);
        try {
            expense.setExpenseId(id);
            if (!expenseService.getExpenseById(id).equals("")) {
                Expense expenseEntity = expenseService.updateExpense(expense);
                CustomResponse<Expense> response = new CustomResponse<>(1, "Gasto actualizado", expenseEntity, links);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                CustomResponse<Expense> response = new CustomResponse<>(0, "Gasto no encontrado", null, links);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            CustomResponse<Expense> response = new CustomResponse<>(500, "Error interno de servidor", null, links);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Expense>> deleteExpenseById(@PathVariable Long id) {
        Optional<Expense> expenseEntity = null;
        CustomResponse<Expense> response = null;
        Link allExpensesLink = linkTo(ExpenseController.class).withSelfRel();
        List<Link> links = List.of(allExpensesLink);

        try {
            expenseEntity = expenseService.getExpenseById(id);
            if (expenseEntity.isPresent()) {
                expenseService.deleteExpense(id);
                response = new CustomResponse<>(1, "Gasto eliminado", null, links);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new CustomResponse<>(0, "Gasto no encontrado", null, links);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response = new CustomResponse<>(500, "Error interno de servidor", null, links);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}