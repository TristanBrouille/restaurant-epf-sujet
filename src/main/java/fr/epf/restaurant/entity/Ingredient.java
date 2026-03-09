package fr.epf.restaurant.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String unite;
    private double stockActuel;
    private double seuilAlerte;

    public Ingredient(Long id, String nom, String unite, double stockActuel, double seuilAlerte) {
        this.id = id;
        this.nom = nom;
        this.unite = unite;
        this.stockActuel = stockActuel;
        this.seuilAlerte = seuilAlerte;
    }

    public Ingredient(Long id, String nom, String unite) {
        this.id = id;
        this.nom = nom;
        this.unite = unite;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getUnite() {
        return unite;
    }

    public double getStockActuel() {
        return stockActuel;
    }

    public double getSeuilAlerte() {
        return seuilAlerte;
    }

    public void setStockActuel(double stockActuel) {
        this.stockActuel = stockActuel;
    }
}
