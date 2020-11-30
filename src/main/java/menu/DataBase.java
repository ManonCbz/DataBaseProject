package menu;

import java.io.Serializable;
import java.util.ArrayList;

public class DataBase implements Serializable{
	
	// ======================= Attributs ======================== //
	
	private String nom;
	private ArrayList<Table> listeDesTables = new ArrayList<Table>();
	
	// ===================== Constructeurs ===================== //

	public DataBase(String nom) {
		this.nom = nom;
	}
	
	// =================== Getters & Setters =================== //

	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public ArrayList<Table> getListeDesTables() {
		return listeDesTables;
	}
	
	public void setListeDesTables(ArrayList<Table> listeDesTables) {
		this.listeDesTables = listeDesTables;
	}
	
	// ======================= Méthodes ======================= //
	
	// ================== CREATE ================== //

	public Table creerTable(String nom) {
		
		Table newTable = null;
		boolean doublon = false;
		
		for (Table t : this.listeDesTables) {
			
			if (nom.equalsIgnoreCase(t.getNom())) {
				doublon = true;
				break;
			}
		}
		
		if (doublon == false) {
			newTable = new Table(nom);
			this.listeDesTables.add(newTable);
		}
		else {
			System.out.println("Il y a déjà une table avec ce nom");
		}
		
		// Return pour pouvoir mettre la nouvelle table sous variable (si doublon retourne null)
		return newTable;
	}

	// =============== READ(SELECT) =============== //
	
	// (8) Affiche la structure de la base de données
	
	public void affichageStructure() {
		
		// Parcours les tables
		
		for (Table t : this.listeDesTables) {
			
			System.out.print("Table : " + t.getNom() + "(");
			
			// Parcours les colonnes
			
			for (int i = 0; i < t.getListeDeColonne().size(); i++) {
							
				Colonne colonne = t.getListeDeColonne().get(i);
				
				// Condition pour savoir quand afficher la virgule
				if (i > 0) {
					System.out.print(", " + colonne.getNom());
				}
				else {
					System.out.print(colonne.getNom());
				}
			}
			
			// Fin de ligne
			System.out.print(")\n");
		}
		
	}

}
