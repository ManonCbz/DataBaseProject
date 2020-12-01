package menu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Programme {

	public static void main(String[] args) {
		
		// Listes de données de notre application
		
		ArrayList<User> listeUtilisateurs = deserializeUser();
		ArrayList<DataBase> listeBasesDeDonnees = deserializeDB();
		
		// Début du programme
		
		Scanner scan = new Scanner(System.in);
		
		// Vérifie si un utilisateur est connecté avec log(), si ok menu();
				
		User utilisateurConnecte = null;
		
		do {
			utilisateurConnecte = connexion(scan, listeUtilisateurs);
		}
		while (utilisateurConnecte == null);
		
		menu(scan, listeBasesDeDonnees);
		
		System.out.println("+--------------------------------------------------------+");
		System.out.println("Au revoir");
		
	}
	
	public static User connexion(Scanner scan, ArrayList<User> listeUtilisateurs) {
		
		int choice = 0;
		User user = null;
		boolean erreur = true;
		
		do {
			System.out.println(""
					+ "+--------------------------------------------------------+\n"
					+ "|                       Connexion                        |\n"
					+ "+--------------------------------------------------------+\n");
			
			System.out.println("  1 - Se connecter | 2 - S'inscrire");
			choice = scan.nextInt();

			switch (choice) {
				case 1 :
					user = log(scan, listeUtilisateurs);
					break;
				case 2 :
					user = inscription(scan, listeUtilisateurs);
					break;
			}
			
			
			System.out.println(""
					+ "                                                          \n"
					+ "+--------------------------------------------------------+\n");
			
		}
		while (user == null);
		
		return user;
	}
	
	// Fonction de connexion, retourne un utilisateur 
	
	public static User log(Scanner scan, ArrayList<User> listeUtilisateurs) {
		int choice = 0;
		User user = null;
		boolean erreur = true;
		
		do {
			System.out.println(""
					+ "+--------------------------------------------------------+\n"
					+ "|                       Connexion                        |\n"
					+ "+--------------------------------------------------------+\n");
			
			System.out.println("  Login : ");
			String login = scan.next();
			
			System.out.println("  Mot de passe : ");
			String motDePasse = scan.next();
			
			for (User u : listeUtilisateurs) {
				
				if (u.getLogin().equals(login) && u.getMotDePasse().equals(motDePasse)) {
					user = u;
					erreur = false;
				}
				
			}
			
			if (erreur == true) {
				System.out.println("  Les identifiants sont incorrects\n");
			}
			
			System.out.println(""
					+ "                                                          \n"
					+ "+--------------------------------------------------------+\n");
			
		}
		while (choice != 0);
		
		return user;
	}
	
	// Fonction inscription .. nouvel utilisateur
	
	public static User inscription(Scanner scan, ArrayList<User> listeUtilisateurs) {
		
		int choice = 0;

		User user = null;
		
		do {
			System.out.println(""
					+ "+--------------------------------------------------------+\n"
					+ "|                      Inscription                       |\n"
					+ "+--------------------------------------------------------+\n");
			
			
			System.out.println("  Nom : ");
			String nom = scan.next();
			
			System.out.println("  Login (Pseudo) : ");
			String login = scan.next();
			
			for (User u : listeUtilisateurs) {
				if(login.equals(u.getLogin())) {
					do {
						System.out.println("   Ce pseudo est déjà pris\nPseudo : ");
						login = scan.next();
					}
					while (login.equals(u.getLogin()));								
				}
			}
			
			System.out.println("  Mot de passe : ");
			String motDePasse = scan.next();
			
			user = new User(nom, login, motDePasse);
			listeUtilisateurs.add(user);
			
			serializeUser(listeUtilisateurs);
			
			System.out.println(""
					+ "                                                          \n"
					+ "+--------------------------------------------------------+\n");
			
		}
		while (choice != 0 || user == null);
		
		return user;
	}
	
	// Menu d'affichage qui vérifie les patterns & appel les méthodes de saisie
	
	public static void menu(Scanner scan, ArrayList<DataBase> listeBaseDeDonnnees) {

		String commande = "";
		DataBase dbUtilisee = null;
		
		System.out.println(""
				+ "+--------------------------------------------------------+\n"
				+ "|                          Menu                          |\n"
				+ "+--------------------------------------------------------+\n");


		System.out.println("   Commandes :\n   (taper QUITTER pour fermer l'application)\n");
		scan.nextLine();
		
		do {
			commande = scan.nextLine();
			boolean commandeOk = false;

			// ============= Patterns pour vérifier les commandes saisies par l'utilisateur ============= //

			Pattern p1 = Pattern.compile("^CREATE DATABASE \\w+;$");
			Matcher m1 = p1.matcher(commande);

			Pattern p2 = Pattern.compile("^USE \\w+;$");
			Matcher m2 = p2.matcher(commande);

			Pattern p3 = Pattern.compile("^CREATE TABLE \\w+\\(\\w+(\\, \\w+)*\\);$");
			Matcher m3 = p3.matcher(commande);

			Pattern p4 = Pattern.compile("^INSERT INTO \\w+ VALUES\\('[a-zA-Z0-9 ]+'(, '[a-zA-Z0-9 ]+')*\\);$");
			Matcher m4 = p4.matcher(commande);

			Pattern p5 = Pattern.compile("^UPDATE \\w+ SET \\w+ = '[a-zA-Z0-9 ]+'(, \\w+ = '[a-zA-Z0-9 ]+')*;$");
			Matcher m5 = p5.matcher(commande);

			Pattern p6 = Pattern.compile("^UPDATE \\w+ SET \\w+ = '[a-zA-Z0-9 ]+'(, \\w+ = '[a-zA-Z0-9 ]+')* WHERE \\w+ = '[a-zA-Z0-9 ]+';$");
			Matcher m6 = p6.matcher(commande);

			Pattern p7 = Pattern.compile("^SELECT \\* FROM \\w+;$");
			Matcher m7 = p7.matcher(commande);
			
			Pattern p8 = Pattern.compile("^SELECT \\w+(, [a-zA-Z0-9 ]+)* FROM \\w+;$");
			Matcher m8 = p8.matcher(commande);
			
			Pattern p9 = Pattern.compile("^DELETE FROM \\w+;$");
			Matcher m9 = p9.matcher(commande);
			
			Pattern p10 = Pattern.compile("^DELETE FROM \\w+ WHERE \\w+ = '\\w+';$");
			Matcher m10 = p10.matcher(commande);
			
			Pattern p11 = Pattern.compile("^SHOW TABLES;$");
			Matcher m11 = p11.matcher(commande);
			
			Pattern p12 = Pattern.compile("^SELECT \\* FROM \\w+ Order by \\w+ ASC;$");
			Matcher m12 = p12.matcher(commande);
			
			// ============= Conditions qui lance chaque commande & leurs méthodes ============= //

			// ===== Pour la base de donnée : 
			
			// Si la commande est : CREATE DATABASE
			if (m1.find() == true) {
				createDB(commande, listeBaseDeDonnnees);
				commandeOk = true;
				serializeDB(listeBaseDeDonnnees);
			}
			// Si la commande est : USE DATABASE
			else if (m2.find() == true) {
				dbUtilisee = selectDB(commande, listeBaseDeDonnnees);
				commandeOk = true;
				if (dbUtilisee != null) {
					System.out.println("SQL - " + dbUtilisee.getNom() + " > \n");
				}
			}
		
			// ===== Si la commande est correcte mais que l'utilisateur n'a pas sélectionné de DB
			
			if (dbUtilisee == null && (m3.find() == true || m4.find() == true || m5.find() == true || m6.find() == true || m7.find() == true || m8.find() == true || m9.find() == true || m10.find() == true || m11.find() == true || m12.find() == true)) {
				commandeOk = true;
				System.out.println("   Vous devez d'abord selectionner une base de données");
			}
			
			// ===== Si la commande est correcte et qu'une db est sélectionné (Appel des méthodes de saisies)
			
			// Si la commande est CREATE TABLE
			if (dbUtilisee != null && m3.find() == true) {
				commandeOk = true;
				createTable(commande, dbUtilisee);
				serializeDB(listeBaseDeDonnnees);
			}
			
			// Si la commande est INSERT INTO
			else if (dbUtilisee != null && m4.find() == true) {
				commandeOk = true;
				insertInto(commande, dbUtilisee);
				serializeDB(listeBaseDeDonnnees);
			}
			
			// Si la commande est UPDATE
			else if (dbUtilisee != null && m5.find() == true) {
				commandeOk = true;
				updateSyntaxe1(commande, dbUtilisee);
				serializeDB(listeBaseDeDonnnees);
			}
			
			// Si la commande est UPDATE WHERE
			else if (dbUtilisee != null && m6.find() == true) {
				commandeOk = true;
				updateSyntaxe3(commande, dbUtilisee);
				serializeDB(listeBaseDeDonnnees);
			}
			
			//Si la commande est SELECT * FROM
			else if (dbUtilisee != null && m7.find() == true) {
				commandeOk = true;
				selectAll(commande, dbUtilisee);
			}
			
			// Si la commande est SELECT colonne FROM nomTable
			else if (dbUtilisee != null && m8.find() == true) {
				commandeOk = true;
				selectFrom(commande, dbUtilisee);
			}
			
			// Si la commande est DELETE FROM nomTable
			else if (dbUtilisee != null && m9.find() == true) {
				commandeOk = true;
				deleteSyntaxe1(commande, dbUtilisee);
				serializeDB(listeBaseDeDonnnees);
			}
			
			// Si la commande est DELETE FROM nomTable WHERE nom_du_champ = 'valeur'
			else if (dbUtilisee != null && m10.find() == true) {
				commandeOk = true;
				deleteSyntaxe2(commande, dbUtilisee);
				serializeDB(listeBaseDeDonnnees);
			}
			
			// Si la commande est SHOW TABLES;
			else if (dbUtilisee != null && m11.find() == true) {
				commandeOk = true;
				dbUtilisee.affichageStructure();
			}
			
			// Si la commande est SELECT * FROM nomTable Order by nom_du_champ ASC;
			else if (dbUtilisee != null && m12.find() == true) {
				commandeOk = true;
				selectFrom(commande, dbUtilisee);
			}

			
			// Si la commande ne correspond à aucun des patterns ni a la commande de sortie
			else if (!commande.equals("QUITTER") && commandeOk == false){
				System.out.println("   Commande incorrecte");
			}
						

		} // Commande de sortie, pour quitter l'application
		while (!commande.equals("QUITTER"));
	}
	
	// ======================================================================================== //

	// Méthode de saisie pour USE nomDB;
	// Retourne la base de donnée (menu() -> syntaxe m2 (USE ..))
	// Elle servira à verifier si une base de données est sélectionné
	
	public static DataBase selectDB(String saisie, ArrayList<DataBase> listeBaseDeDonnnees) {
		
		DataBase db = null;
		
		// Split des elements à retirer de la saisie
		
        String[] etape0 = saisie.split("USE ");
        String[] etape1 = etape0[1].split("\\;");
        String nomDB = etape1[0];
        
        // Si la base de donnée existe
        
        for (DataBase d : listeBaseDeDonnnees) {
        	if (d.getNom().equals(nomDB)) {
        		db = d;
        		break;
        	}
        }
        
		if (db == null) {
			System.out.println("   Cette base de données n'existe pas");
		}
		
		return db;
	}
	
	// Méthode de saisie pour CREATE DATABASE nomDB;
	// Vérifie si doublon & ajoute la base de données dans la liste de db du programme
	
	public static void createDB(String saisie, ArrayList<DataBase> listeBaseDeDonnnees) {
		
		boolean doublon = false;
		
		// Split des elements à retirer de la saisie
		
        String[] etape0 = saisie.split("CREATE DATABASE ");
        String[] etape1 = etape0[1].split("\\;");
        String nomDB = etape1[0];
        
        // Vérifie si la base de donnée existe déjà
        
        for (DataBase d : listeBaseDeDonnnees) {
        	if (d.getNom().equals(nomDB)) {
        		doublon = true;
        		System.out.println("   Cette base de donnée existe déjà");
        		break;
        	}
        }
        
        if (doublon == false) {
        	DataBase db = new DataBase(nomDB);
    		listeBaseDeDonnnees.add(db);
    		System.out.println("   La base de données " + nomDB + " a bien été crée");
        }
     }
	
	// Méthode de saisie pour CREATE TABLE nomTable(colonne1, colonne2);
	// Appel les méthodes de Database.java & Table.java pour créer la table et les colonnes (si pas de doublon)
	
	public static void createTable(String saisie, DataBase db) {
		
		boolean doublon = false;
		
		// Split des elements à retirer de la saisie et mise en variable/liste des elements à conserver
		
        String[] etape0 = saisie.split("CREATE TABLE ");
        
        String nomFichier= etape0[1];
        nomFichier = nomFichier.replace(',', ';');
        
        String[] etape1 = etape0[1].split("\\(");
        String nomTable = etape1[0];
                
        String[] etape2 = etape1[1].split("\\)");
                
        String [] liste = etape2[0].split(", ");
        
        ArrayList <String> listeDeColonne = new ArrayList <String> ();
        
        for (int i = 0; i < liste.length; i++) {
            listeDeColonne.add(liste[i]);
        }
        
        // Vérifie si la table n'a pas de doublon
        
        for (Table t : db.getListeDesTables()) {
        	if (t.getNom().equals(nomTable)) {
        		doublon = true;
        		System.out.println("   Cette table existe déjà");
        		break;
        	}
        }
        
        // Appel de la fonction creerTable & creerColonne (Class Database.java & Table.java)
        
        if (doublon == false) {
        	Table table = db.creerTable(nomTable);
        	table.creerColonne(listeDeColonne, nomFichier);
    		System.out.println("   La table " + nomTable + " a bien été crée");
        }
    }
	
	// Méthode de saisie pour INSERT INTO nomTable VALUES('colonne1', 'colonne2');
	// Appel la méthode ajoutLigne() de Table.java pour ajouter des valeurs à la table concernée
	
	public static void insertInto(String saisie, DataBase db) {
		
		// Split des elements à retirer de la saisie et mise en variable/liste des elements à conserver
		
        String[] etape0 = saisie.split("INSERT INTO ");
                
        String[] etape1 = etape0[1].split(" VALUES\\(");
        String nomTable = etape1[0];
                
        String[] etape2 = etape1[1].split("\\)");
                        
        String [] liste = etape2[0].split(", ");        
        
        ArrayList <String> listeDeValeurs = new ArrayList <String> ();
        
        for (int i = 0; i < liste.length; i++) {
        	String[] temp = liste[i].split("'");
        	listeDeValeurs.add(temp[1]);
        }
        
        // Vérifie que la table existe & appel de la méthode ajoutLigne() de Table.java
        
		boolean tableIsInDB = false;
        
        for (Table t : db.getListeDesTables()) {
        	if (t.getNom().equals(nomTable)) {
        		tableIsInDB = true;
        		t.ajoutLigne(listeDeValeurs);
        		System.out.println("   Les données sont enregistrées");
        		break;
        	}
        }
        
        if (tableIsInDB == false) {
        	System.out.println("   Cette table n'existe pas");
        }       
	}
	
	// Méthode de saisie pour SELECT * FROM nomTable;
	// Appel la méthode affichageDeDonnees() de Table.java pour afficher toutes les données sous forme de tableau
	
	public static void selectAll(String saisie, DataBase db) {
		
		// Split des elements à retirer de la saisie
        
		String[] etape0 = saisie.split("SELECT \\* FROM ");
                
        String[] etape1 = etape0[1].split(";");
        String nomTable = etape1[0];
        
        // Vérifie si la table existe & appel la méthode affichageDeDonnees() de Table.java
        
		boolean tableIsInDB = false;
        
        for (Table t : db.getListeDesTables()) {
        	if (t.getNom().equals(nomTable)) {
        		tableIsInDB = true;
        		t.affichageDeDonnees();
        		break;
        	}
        }
        
        if (tableIsInDB == false) {
        	System.out.println("   Cette table n'existe pas");
        }
        
	}
	
	// Méthode de saisie pour DELETE FROM nomTable;
	// Appel la méthode supprimerEnsembleLigne() de Table.java pour effacer toutes les données
	
	public static void deleteSyntaxe1(String saisie, DataBase db) {

		// Split des elements à retirer de la saisie

		String[] etape0 = saisie.split(";");
		String[] etape1 = etape0[0].split("DELETE FROM ");
		String nomTable = etape1[1];
		
        // Vérifie si la table existe & appel la méthode supprimerEnsembleLigne() de Table.java
		
		boolean tableIsInDB = false;
		
		for (Table t : db.getListeDesTables()) {
			
			if (t.getNom().equals(nomTable)) {
				t.supprimerEnsembleLigne();
				tableIsInDB = true;
				break;
			}
		}
		
        if (tableIsInDB == false) {
        	System.out.println("   Cette table n'existe pas");
        }
	}
	
	// Méthode de saisie pour UPDATE nomTable SET nom_du_champ = 'nouvelle valeur';
	// Appel la méthode modifierContenuColonne() de Table.java pour modifier toutes les données demandé

	public static void updateSyntaxe1(String saisie, DataBase db) {

		// Split des elements à retirer de la saisie
		String[] etape0 ;
		String[] etape1 ;
		String nomTable = "";
		ArrayList <String> listeDeColonne = new ArrayList <String> ();
		ArrayList <String> listeDeValeur = new ArrayList <String> ();
		int verite = saisie.indexOf(',');
		
		if (verite == -1) {
			
			etape0 = saisie.split(";");
			etape1 = etape0[0].split("UPDATE ");
			etape0 = etape1[1].split(" SET ");
			nomTable = etape0[0];
			etape1 = etape0[1].split(" = ");
			listeDeColonne.add(etape1[0]);
			etape0=etape1[1].split("'");
			listeDeValeur.add(etape0[1]);
			
		}else {
				
				etape0 = saisie.split(";");//vire le ';'
				etape1 = etape0[0].split("UPDATE ");//vire le UPDATE
				etape0 = etape1[1].split(" SET "); // split au SET
				nomTable = etape0[0]; // recupere nom de la table
				etape1 = etape0[1].split(", ");//split au ", "
				for (int i=0; i<etape1.length; i++) {//parcoure le split
					String[] temp0 = etape1[i].split(" = ");// split au =
					listeDeColonne.add(temp0[0]); // met le nom dans ça liste
					String[] temp1 = temp0[1].split("'"); // split au '
					listeDeValeur.add(temp1[1]); // met la valeur dans ça liste}
					}
				
				}
		
		// Vérifie si la table existe & appel la méthode modifierContenuColonne() de Table.java
		
		boolean tableIsInDB = false;
		
		for (Table t : db.getListeDesTables()) {
			if (t.getNom().equals(nomTable)) {
				tableIsInDB = true;
				
				ArrayList<String> listeNomDeColonne = new ArrayList<String>();
				ArrayList<String> listeValeurs = new ArrayList<String>();
				for (String temp : listeDeColonne) {
					listeNomDeColonne.add(temp);
					}
				for (String temp : listeDeValeur) {
					listeValeurs.add(temp);
				}
				t.modifierContenuColonne(listeNomDeColonne, listeValeurs);
				if (verite == -1)
					{
					System.out.println("   La colonne a bien été mise à jour");
					}
				else System.out.println("   Les colonnes ont bien été mise à jour");
				}
			
		}
		
        if (tableIsInDB == false) {
        	System.out.println("   Cette table n'existe pas!");
        }
		
	}

	// Méthode de saisie pour UPDATE nomTable SET nom_du_champ = 'nouvelle valeur' WHERE nom_du_champ = 'ancienne valeur';
	// Appel la méthode modifierContenuCible() de Table.java pour modifier toutes les données demandé
	
	public static void updateSyntaxe3(String saisie, DataBase db) {
		//"UPDATE nomTable SET nom_du_champ = 'nouvelle valeur' WHERE nom_du_champ = 'ancienne valeur';"
		String[] etape0 = saisie.split(";");//vire le ';'
		String[] etape1 = etape0[0].split("UPDATE ");//vire le UPDATE
		etape0 = etape1[1].split(" SET ");//vire le SET
		String nomTable = etape0[0]; // get nom de la table
		etape1 = etape0[1].split("WHERE ");//split au WHERE
		etape0 = etape1[0].split(" = ");;//split au " = "
		String nomDeColonne = etape0[0];
		String nouvelleValeur = etape0[1];
		String[] etape2 = etape1[1].split(" = ");;//split au " = "
		String ancienneValeur = etape2[1];
		etape0=nouvelleValeur.split("'");
		nouvelleValeur = etape0[1];
		etape0=ancienneValeur.split("'");
		ancienneValeur = etape0[1];
		
		//Vérifie que les deux noms de colonne sont identique:
		if ( !(nomDeColonne.equals(etape2[0])) ) {
			System.out.println("ERREUR Les colonnes sont differentes!");
			}
	
		for (Table t : db.getListeDesTables()) {
			
			if (t.getNom().equals(nomTable)) {
				t.modifierContenuCible(nomDeColonne, nouvelleValeur, ancienneValeur);
				System.out.println("  Les données ont bien été mise à jour");
				break;
			}
		}
	}

	// ====================== En cours de traitement ====================== //
		
	// Méthode de saisie pour SELECT nom_du_champ FROM nomTable;
	// Appel la méthode affichageDeColonne() de Table.java pour afficher les colonnes demandées
	
	// ***** saisie (ok une colonne, ko + d'1) *****
	// ***** fonctions table ko (IndexOutOfBoundsException) *****

	public static void selectFrom(String saisie, DataBase db) {
		
		//verifie s'il y a des virgules et plusiers colonnes cibles
		
		ArrayList <String> listeDeColonne = new ArrayList <String> ();
		String[] etape0;
		String[] etape1;
		String[] etape2;
		String nomTable = "";
		if (saisie.indexOf(',')==-1) {
			// Split des elements à retirer de la saisie et mise en variable/liste des elements à conserver
			
			etape0 = saisie.split("SELECT ");
			etape1 = etape0[1].split(" FROM ");
			listeDeColonne.add(etape1[0]);
		} else {
		
			etape0 = saisie.split("SELECT ");
			etape1 = etape0[1].split(" FROM ");
			etape2 = etape1[0].split(", ");
			for (int i=0; i<(etape2.length); i++) {
				listeDeColonne.add(etape2[i]);
			}
		}
			etape2 = etape1[1].split(";");
			nomTable = etape2[0];
			
			System.out.println("nomColonne : " + listeDeColonne.toString()+ " nomTable : " + nomTable);

			// Vérifie si la table et la colonne existent & appel la méthode affichageColonne() de Table.java
			
			boolean tableIsInDB = false;
			
			for (Table t : db.getListeDesTables()) {
				if (t.getNom().equals(nomTable)) {
					tableIsInDB = true;
					for (String nomColonne : listeDeColonne) {
					if (t.colonneExiste(nomColonne)) {
						t.affichageColonne(listeDeColonne);	
					} else System.out.println("Colonne "+nomColonne +" n'existe pas!");
					break;
					}
				}
			}
			
			if (tableIsInDB == false) {
				System.out.println("   Une des tables demandées n'existe pas!");
			}
		
	}
		
	// ***** fonctions table ko (Pas de modification) ***** 
	
	public static void deleteSyntaxe2(String saisie, DataBase db) {
		
		//"DELETE FROM nomTable WHERE nom_du_champ = 'valeur';"
		System.out.println("\nSaisie :" +saisie);
		String[] etape0 = saisie.split(";");//vire le ';'
		String[] etape1 = etape0[0].split("DELETE FROM ");//vire le "DELETE FROM "
		etape0 = etape1[1].split("WHERE ");//split au WHERE
		String nomTable = etape0[0]; // get nom de la table
		etape1 = etape0[1].split(" = ");//split au =
		String nomDeColonne = etape1[0];
		etape0 = etape1[1].split("'");
		String valeur = etape0[1];
		
		System.out.println("Nom de la table: " +nomTable+ "\nNom de colonne: " +nomDeColonne + "\nValeur: "+valeur);
	
		for (Table t : db.getListeDesTables()) {
			
			if (t.getNom().equals(nomTable)) {
				
				System.out.println(t.getNom());
			
				t.supprimerLigneCible(valeur, nomDeColonne);;
				
				break;
			}
		}
	}

	
	// ====================== Serialize / Deserialize ====================== //
	
	static void serializeDB(ArrayList<DataBase> liste) {
	    
		try {
	        
			FileOutputStream fos = new FileOutputStream("C:\\Users\\MDenh\\Documents\\txt\\DataBases.txt");
	        ObjectOutputStream outputStream = new ObjectOutputStream(fos);
	        
	        outputStream.writeObject(liste);
	        outputStream.close();
	        
	    }
	    catch (IOException e) {
	    	
	        e.getMessage();
	        
	    }
	}
	
	static void serializeUser(ArrayList<User> liste) {
	    
		try {
	        
			FileOutputStream fos = new FileOutputStream("C:\\Users\\MDenh\\Documents\\txt\\Users.txt");
	        ObjectOutputStream outputStream = new ObjectOutputStream(fos);
	        
	        outputStream.writeObject(liste);
	        outputStream.close();
	        
	    }
	    catch (IOException e) {
	    	
	        e.getMessage();
	        
	    }
	}
	
	static ArrayList<DataBase> deserializeDB() {
		
	    ArrayList<DataBase> listeDB = new ArrayList<>();
	    
	    try {
	           FileInputStream fichier = new FileInputStream("C:\\Users\\MDenh\\Documents\\txt\\DataBases.txt");
	           ObjectInputStream objet = new ObjectInputStream(fichier);
	            
	           listeDB = (ArrayList) objet.readObject();
	            
	           objet.close();
	           fichier.close();
	           
	      } 
	          catch (IOException | ClassNotFoundException ex) {
	           System.err.println(ex);
	      }
	      return listeDB;
	}  
	
	static ArrayList<User> deserializeUser() {
		
	    ArrayList<User> listeUser = new ArrayList<>();
	    
	    try {
	           FileInputStream fichier = new FileInputStream("C:\\Users\\MDenh\\Documents\\txt\\Users.txt");
	           ObjectInputStream objet = new ObjectInputStream(fichier);
	            
	           listeUser = (ArrayList) objet.readObject();
	            
	           objet.close();
	           fichier.close();
	           
	      } 
	          catch (IOException | ClassNotFoundException ex) {
	           System.err.println(ex);
	      }
	      return listeUser;
	}  
}