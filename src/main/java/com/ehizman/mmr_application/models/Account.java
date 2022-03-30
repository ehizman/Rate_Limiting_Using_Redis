package com.ehizman.mmr_application.models;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "auth_id")
    private String authId;
    @Column(name = "username")
    private String username;

}
