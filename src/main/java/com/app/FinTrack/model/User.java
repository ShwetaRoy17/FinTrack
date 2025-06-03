package com.app.FinTrack.model;

import javax.annotation.processing.Generated;
import java.util.List;

@Entity
public class User {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;

    private List<Transaction> transactions;
}
