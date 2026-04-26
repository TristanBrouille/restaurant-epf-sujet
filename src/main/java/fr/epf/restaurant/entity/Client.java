package fr.epf.restaurant.entity;

public class Client {

    private final Long id;
    private final String nom;
    private final String prenom;
    private final String email;
    private final String telephone;

    public Client(Long id, String nom, String prenom,
                  String email, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }
}
