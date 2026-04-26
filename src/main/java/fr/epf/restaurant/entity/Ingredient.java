package fr.epf.restaurant.entity;

public class Ingredient {

    private final Long id;
    private final String nom;
    private final String unite;
    private Double stockActuel;
    private Double seuilAlerte;

    public Ingredient(Long id, String nom, String unite, Double stockActuel, Double seuilAlerte) {
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

    public Double getStockActuel() {
        return stockActuel;
    }

    public Double getSeuilAlerte() {
        return seuilAlerte;
    }

    public void setStockActuel(Double stockActuel) {
        this.stockActuel = stockActuel;
    }
}
