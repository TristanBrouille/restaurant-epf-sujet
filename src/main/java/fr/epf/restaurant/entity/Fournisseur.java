package fr.epf.restaurant.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String contact;

    private String email;

    public Fournisseur(Long id, String nom, String contact, String email) {
        this.id = id;
        this.nom = nom;
        this.contact = contact;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }
}
