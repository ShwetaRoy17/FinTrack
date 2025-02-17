package com.app.FinTrack.controllers;

import com.app.FinTrack.entities.Expense;
import com.app.FinTrack.entities.User;
import com.app.FinTrack.repos.ExpenseRepository;
import com.app.FinTrack.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
@PreAuthorize("hasRole('USER')")
public class ExpenseController {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense, @AuthenticationPrincipal User user) {
        expense.setUser(user);
        return ResponseEntity.ok(expenseRepository.save(expense));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Expense>> listExpenses(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(expenseRepository.findByUserId(user.getId()));
    }
}