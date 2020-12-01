package menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
	
public class Colonne implements Serializable{
	
	// ======================= Attributs ======================== //
	
	private String nom;
	private HashMap<Integer, String> ligne = new HashMap<Integer, String>();
	
	// ===================== Constructeurs ===================== //
	
	public Colonne(String nom) {
		this.nom = nom;
	}
	
	// =================== Getters & Setters =================== //

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public HashMap<Integer, String> getLigne() {
		return ligne;
	}

	public void setContenu(HashMap<Integer, String> ligne) {
		this.ligne = ligne;
	}
	
	public int getLenght () {
		return this.ligne.size();	
	}
	
	//Replace tous le contenu d'une colonne par une valeur
	public void setallContenu (String nouveauContenu) {
		for (Integer cible : ligne.keySet()) {
			ligne.replace(cible, nouveauContenu);
		}
	}
	
	// ======================= Méthodes ======================= //
	
	// Retourne le contenu d'une colonne
	
	public String getContenu(int index) {
		
		String txt = "";
		
		if (this.ligne.containsKey(index)) {
			 txt = this.ligne.get(index);
		}
		else {
			txt = "";
		}
		
		return txt;
	}

	// ================== CREATE ================== //
	
	// (2 - Syntaxe 1) Ajoute une nouvelle ligne dans la colonne (Voir table)
	
	public void addLigneContenu(int index, String contenu) {
		
		this.ligne.put(index, contenu);
		
	}
	
	// =============== READ(SELECT) =============== //
	
	
	// ================== UPDATE ================== //


	// (4 - Syntaxe 3) Modifie les lignes de la colonne index par un nouveau contenu lorsque celles-ci avaient pour valeur l'ancien contenu
	
	public void setContenu(String ancienContenu, String nouveauContenu) {
		
		for (Entry<Integer, String> ligne : ligne.entrySet()) {
			
			if (ligne.getValue().equalsIgnoreCase(ancienContenu)) {
				
				ligne.setValue(nouveauContenu);
				
			}
		}
		
	}

	// ================== DELETE ================== //

	
	// Outil pour DELETE (5 - Syntaxe 1) Retourne l'index des lignes à supprimer dans la fonction déclaré dans la classe Table
	
	public ArrayList<Integer> returnIndexASupprimer(String cible) {
		
		// Liste d'index
		ArrayList<Integer> indexASupprimer = new ArrayList<Integer>();
		
		// Boucle sur l'hashmap contenu de la colonne (ligne)
		for (Entry<Integer, String> ligne : ligne.entrySet()) {
			
			// Si une ligne contient la valeur demandé
			if (ligne.getValue().equalsIgnoreCase(cible)) {
				
				// Ajoute l'index de cette ligne dans la liste à supprimer
				indexASupprimer.add(ligne.getKey());
				
			}
		}
		
		return indexASupprimer;
	}
	
	// Supprime les index a supprimer sur la colonne
	
	public void supprimerCible(ArrayList<Integer> index) {
	
		for (int i : index) {
			this.ligne.remove(i);
		}
		
	}
	
	// Supprime l'ensemble de la colonne
	
	public void vidage() {
		
		HashMap<Integer, String> newListe = new HashMap<Integer, String>();
		
		setContenu(newListe);
		
	}
}
