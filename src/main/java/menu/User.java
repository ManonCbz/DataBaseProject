package menu;

import java.io.Serializable;

public class User implements Serializable{
	
	// ======================= Attributs ======================== //
	
	private String nom;
	private String login;
	private String motDePasse;
	
	// ===================== Constructeurs ===================== //
	
	public User(String nom, String login, String motDePasse) {
		this.nom = nom;
		this.login = login;
		this.motDePasse = motDePasse;
	}

	// =================== Getters & Setters =================== //

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	
	// ======================= Méthodes ======================= //
	
	
}
