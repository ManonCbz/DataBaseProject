package menu;

import java.util.ArrayList;

public class Table {
	
	// ======================= Attributs ======================== //

	private String nom;
	private int index = 0;
	private ArrayList<Colonne> listeDeColonne = new ArrayList<Colonne>();
	private String nomFichier = "";
		
	// ===================== Constructeurs ===================== //
	
	public Table(String nom, String nomFichier) {
		this.nom = nom;
		this.nomFichier = this.nomFichier.concat(nomFichier);
	}
	
	// =================== Getters Z& Setters =================== //

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public ArrayList<Colonne> getListeDeColonne() {
		return listeDeColonne;
	}

	public void setListeDeColonne(ArrayList<Colonne> listeDeColonne) {
		this.listeDeColonne = listeDeColonne;
	}

	// ======================= Méthodes ======================= //
	
	public void creerColonne(ArrayList<String> listeNomColonne) {
		
		Colonne newColonne = null;
		boolean doublon = false;
		for (String nom : listeNomColonne) {
			
			for (Colonne c : this.listeDeColonne) {
				
				if (nom.equalsIgnoreCase(c.getNom())) {
					doublon = true;
					break;
				}
			}
			if (doublon == false) {
				newColonne = new Colonne(nom);
				this.listeDeColonne.add(newColonne);
			}
			else {
				System.out.println("Il y a déjà une colonne avec ce nom, celle-ci ne sera pas ajouté");
			}	
		}
	}

	//Retourne Vrai si une colonne avec ce nom existe
	public boolean colonneExiste (String nom) {
		boolean retour = false;
		for (Colonne cible: listeDeColonne) {
			if (cible.getNom().equals(nom)) {
				retour = true;
			}
		}
		return retour;
	}//FAIT
	
	
	// 	Retourne une colonne
	// Vide
	public Colonne recupColonne(String nom) { ///J'ai ajouté "boolean colonneExiste (String nom)" ^^^^ pour vérifier que la colonne recherché exite, a utilisé dans DB avant cette méthode
		for ( Colonne cible : listeDeColonne) {
		 if (cible.getNom().equals(nom)) {
			 return cible;
		 }
		}
		System.out.println("Probleme colonne non existante");
		return new Colonne ("Vide");
	} //FAIT
	
	// ================== CREATE ================== //
	
	// (2 - Syntaxe 1) Ajoute une nouvelle ligne dans la table et toutes les colonnes
	// Vide
	public void ajoutLigne(ArrayList<String> listeValeurs) { 
		//compte la progression dans listeValeurs
		int compteProgres = 0;
		//Verifie que le nombre de données fourni est == au nombre de colonnes
		if (listeValeurs.size() == this.listeDeColonne.size()) {
		// Si oui, ajout du contenu pris dans chaque colonne en utilisant l'index de la table pour valeur de ligne
			for (Colonne cible: this.listeDeColonne) {
				cible.addLigneContenu(index, listeValeurs.get(compteProgres));
				compteProgres++;
			}
			// une fois une ligne ajouté, on incremente l'index pour eviter les doublons de lignes;
			this.index++;	
		//Si non, un message d'erreur est affiché.
		} else System.out.println("Nombre de nouvelle valeurs entrées incorrecte");
		
	} //FAIT*********************

	// =============== READ(SELECT) =============== //
	
	// (3 - Syntaxe 1) Affichage de toutes les données de la table
	// Vide
	public void affichageDeDonnees() {
		int compteLigne = 0;
		//Entete avec les colonnes
		this.methodeAfficheLigne();
		for (int i = 0; i< this.listeDeColonne.size(); i++) {
		this.methodeAfficheNom(i);
		}
		this.methodeAfficheLigne();
		for (int j = 0; j <this.index; j++) {
			for (int i = 0; i< this.listeDeColonne.size(); i++) {
				this.methodeAfficheContenu(i, j);
			}
		}
		this.methodeAfficheLigne();
		// affichage des noms de colonnes en entétes:
	} //A FAIRE*********************
	
	// (3 - Syntaxe 2) Affiche les données concernées par la liste pour certaines colonnes
	// Vide
	public void affichageColonne(ArrayList<String> listeDeColonne) {
		int compteLigne = 0;
		//	ArrayList <Colonne> temp = this.getListeDeColonne()
		// affichage des noms de colonnes en entétes:
			//VERSION DISCUTER AFFICHAGE
		}
	//A FAIRE*********************
	
	// ================== UPDATE ================== //
	
	// (4 - Syntaxe 1 & 2) Toutes les lignes de cette colonne sont modifier par le nouveau contenu
	// Vide
	public void modifierContenuColonne(ArrayList<String> nomDeColonne, ArrayList<String> nouveauContenu) {//TABLE DANS TABLE????????
		
		for (int i = 0; i < nomDeColonne.size(); i++) {
			System.out.println(nomDeColonne.get(i));
			System.out.println(nouveauContenu.get(i));
		}
	
	}//A DISCUTER
	
	// (4 - Syntaxe 3) Toutes les lignes de cette colonne qui contiennent une valeur précise sont modifier par le nouveau contenu
	// Vide
	public void modifierContenuCible(String nomDeColonne, String nouveauContenu, String nomCible) {
	
		System.out.println(nomDeColonne + " " + nouveauContenu + " " + nomCible);
		
	}
	// ================== DELETE ================== //

	// (5 - Syntaxe 1) Toutes les lignes de toutes les colonnes sont supprimées (reste le nom des colonnes)
	// Vide
	public void supprimerEnsembleLigne() {
		for (Colonne cible : this.listeDeColonne){
			cible.vidage();
		}//On vide toutes les colonnes une par une
		this.index = 0;
		// Remise à 0 de l'index des lignes maintenant que les colonnes sont vides.
	}
	
	// (5 - Syntaxe 2) Toutes les lignes qui contiennent la valeur enregistrée sont supprimées
	// Vide
	public void supprimerLigneCible(String nomCible, String nomDeColonne) {
		//On selectionne la colonne avec le bon nom
		Colonne aModifier = new Colonne ("vide");
		for (Colonne cible : this.listeDeColonne) {
			if (cible.getNom().equals(nomDeColonne)) {// VERIFIER QUE DEUX COLONNES N'ONT PAS LE MEME NOM?????????
				aModifier = cible;
			}
		//On recupere une liste de ligne à supprimer et on la parcour pour supprimer ces lignes une par une dans chaque colonnes
		ArrayList <Integer> indexASupprimer = aModifier.returnIndexASupprimer(nomCible);
		for (Colonne colonneCible : this.listeDeColonne) {
			colonneCible.supprimerCible(indexASupprimer);
			}			
		} 
	}
	
	public void methodeAfficheLigne () { // affiche 12 * par colonne
		int nombreDeColonne = this.listeDeColonne.size();
		System.out.print("+");
		for (int i=0; i<nombreDeColonne; i++) {
			System.out.print("----------+");
		}
		System.out.print("\n");
	}
	
	public void methodeAfficheNom (int colonneCible) { // affiche le nom de la commande
		if (colonneCible == 0) {// si c'est la premiere colonne on ajoute le '|'
			System.out.print("|");
		} 
		String nom = this.listeDeColonne.get(colonneCible).getNom();
		System.out.print(nom);
		for (int i = 0; i< (12 -( 2+nom.length() ) ); i++ ) {
			System.out.print(" ");
		}
		System.out.print("|");
		if ( colonneCible == (this.listeDeColonne.size()-1)){
			System.out.print("\n");	
		}
	}
	
	public void methodeAfficheContenu (int colonneCible, int ligneCible) {// affiche le contenu de la lign cible dans la colonne cible
		String affiche = this.listeDeColonne.get(colonneCible).getContenu(ligneCible);
		if (affiche != null) {
			if (colonneCible == 0) {// si c'est la premiere colonne on ajoute le '|'
				System.out.print("|");
			} 
			System.out.print(affiche);
			for (int i = 0; i< (12 -( 2+affiche.length() ) ); i++ ) {
				System.out.print(" ");
			}
			System.out.print("|");
			if ( colonneCible == (this.listeDeColonne.size()-1)){
				System.out.print("\n");	
			}
			}
		
	}
	
}
