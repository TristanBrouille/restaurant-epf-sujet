package fr.epf.restaurant.entity;

public class Plat {


    private final Long id;
    private final String nom;
    private final String description;
    private final Double prix;

    public Plat(Long id, String nom, String description,
                Double prix) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrix() {
        return prix;
    }
}
