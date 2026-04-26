package fr.epf.restaurant.entity;

public class Fournisseur {

    private final Long id;
    private final String nom;
    private final String contact;
    private final String email;

    public Fournisseur(Long id, String nom, String contact,
                       String email) {
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
