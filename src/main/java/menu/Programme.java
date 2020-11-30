package menu;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Programme {

	public static void main(String[] args) {
		
		// Listes de donn�es de notre application
		
		ArrayList<User> listeUtilisateurs = new ArrayList<User>();
		ArrayList<DataBase> listeBasesDeDonnees = new ArrayList<DataBase>();
		
		// Cr�ation d'un utilisateur test
		
		User u1 = new User("test", "test", "test");
		listeUtilisateurs.add(u1);
		
		// D�but du programme
		
		Scanner scan = new Scanner(System.in);
		
		// V�rifie si un utilisateur est connect� avec log(), si ok menu();
				
		User utilisateurConnecte = null;
		
		do {
			utilisateurConnecte = log(scan, listeUtilisateurs);			
		}
		while (utilisateurConnecte == null);

		menu(scan, listeBasesDeDonnees);
		
		System.out.println("+--------------------------------------------------------+");
		System.out.println("Au revoir");
				
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
	
	// Menu d'affichage qui v�rifie les patterns & appel les m�thodes de saisie
	
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

			// ============= Patterns pour v�rifier les commandes saisies par l'utilisateur ============= //

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
			
			// ============= Conditions qui lance chaque commande & leurs m�thodes ============= //

			// ===== Pour la base de donn�e : 
			
			// Si la commande est : CREATE DATABASE
			if (m1.find() == true) {
				createDB(commande, listeBaseDeDonnnees);
				commandeOk = true;
			}
			// Si la commande est : USE DATABASE
			else if (m2.find() == true) {
				dbUtilisee = selectDB(commande, listeBaseDeDonnnees);
				commandeOk = true;
				if (dbUtilisee != null) {
					System.out.println("SQL - " + dbUtilisee.getNom() + " > \n");
				}
			}
		
			// ===== Si la commande est correcte mais que l'utilisateur n'a pas s�lectionn� de DB
			
			if (dbUtilisee == null && (m3.find() == true || m4.find() == true || m5.find() == true || m6.find() == true || m7.find() == true || m8.find() == true)) {
				commandeOk = true;
				System.out.println("   Vous devez d'abord selectionner une base de donn�es");
			}
			
			// ===== Si la commande est correcte et qu'une db est s�lectionn� (Appel des m�thodes de saisies)
			
			// Si la commande est CREATE TABLE
			if (dbUtilisee != null && m3.find() == true) {
				commandeOk = true;
				createTable(commande, dbUtilisee);
				dbUtilisee.affichageStructure();
			}
			
			// Si la commande est INSERT INTO
			else if (dbUtilisee != null && m4.find() == true) {
				commandeOk = true;
				insertInto(commande, dbUtilisee);
			}
			
			// Si la commande est UPDATE
			else if (dbUtilisee != null && m5.find() == true) {
				commandeOk = true;
				updateSyntaxe1(commande, dbUtilisee);
			}
			
			// Si la commande est UPDATE WHERE
			else if (dbUtilisee != null && m6.find() == true) {
				commandeOk = true;
				updateSyntaxe3(commande, dbUtilisee);
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
			
			// Si la commande ne correspond � aucun des patterns ni a la commande de sortie
			else if (!commande.equals("QUITTER") && commandeOk == false){
				System.out.println("   Commande incorrecte");
			}
			

		} // Commande de sortie, pour quitter l'application
		while (!commande.equals("QUITTER"));
	}
	
	// ======================================================================================== //

	// M�thode de saisie pour USE nomDB;
	// Retourne la base de donn�e (menu() -> syntaxe m2 (USE ..))
	// Elle servira � verifier si une base de donn�es est s�lectionn�
	
	public static DataBase selectDB(String saisie, ArrayList<DataBase> listeBaseDeDonnnees) {
		
		DataBase db = null;
		
		// Split des elements � retirer de la saisie
		
        String[] etape0 = saisie.split("USE ");
        String[] etape1 = etape0[1].split("\\;");
        String nomDB = etape1[0];
        
        // Si la base de donn�e existe
        
        for (DataBase d : listeBaseDeDonnnees) {
        	if (d.getNom().equals(nomDB)) {
        		db = d;
        		break;
        	}
        }
        
		if (db == null) {
			System.out.println("   Cette base de donn�es n'existe pas");
		}
		
		return db;
	}
	
	// M�thode de saisie pour CREATE DATABASE nomDB;
	// V�rifie si doublon & Ajoute la base de donn�es dans la liste de db du programme
	
	public static void createDB(String saisie, ArrayList<DataBase> listeBaseDeDonnnees) {
		
		boolean doublon = false;
		
		// Split des elements � retirer de la saisie
		
        String[] etape0 = saisie.split("CREATE DATABASE ");
        String[] etape1 = etape0[1].split("\\;");
        String nomDB = etape1[0];
        
        // V�rifie si la base de donn�e existe d�j�
        
        for (DataBase d : listeBaseDeDonnnees) {
        	if (d.getNom().equals(nomDB)) {
        		doublon = true;
        		System.out.println("   Cette base de donn�e existe d�j�");
        		break;
        	}
        }
        
        if (doublon == false) {
        	DataBase db = new DataBase(nomDB);
    		listeBaseDeDonnnees.add(db);
    		System.out.println("   La base de donn�es " + nomDB + " a bien �t� cr�e");
        }
	}
	
	// M�thode de saisie pour CREATE TABLE nomTable(colonne1, colonne2);
	// Appel les m�thodes de Database.java & Table.java pour cr�er la table et les colonnes (si pas de doublon)
	
	public static void createTable(String saisie, DataBase db) {
		
		boolean doublon = false;
		
		// Split des elements � retirer de la saisie et mise en variable/liste des elements � conserver
		
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
        
        // V�rifie si la table n'a pas de doublon
        
        for (Table t : db.getListeDesTables()) {
        	if (t.getNom().equals(nomTable)) {
        		doublon = true;
        		System.out.println("   Cette table existe d�j�");
        		break;
        	}
        }
        
        // Appel de la fonction creerTable & creerColonne (Class Database.java & Table.java)
        
        if (doublon == false) {
        	Table table = db.creerTable(nomTable);
        	table.creerColonne(listeDeColonne, nomFichier);
    		System.out.println("   La table " + nomTable + " a bien �t� cr�e");
        }
    }
	
	// M�thode de saisie pour INSERT INTO nomTable VALUES('colonne1', 'colonne2');
	// Appel la m�thode ajoutLigne() de Table.java pour ajouter des valeurs � la table concern�e
	
	public static void insertInto(String saisie, DataBase db) {
		
		// Split des elements � retirer de la saisie et mise en variable/liste des elements � conserver
		
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
        
        // V�rifie que la table existe & appel de la m�thode ajoutLigne() de Table.java
        
		boolean tableIsInDB = false;
        
        for (Table t : db.getListeDesTables()) {
        	if (t.getNom().equals(nomTable)) {
        		tableIsInDB = true;
        		t.ajoutLigne(listeDeValeurs);
        		System.out.println("   Les donn�es sont enregistr�es");
        		break;
        	}
        }
        
        if (tableIsInDB == false) {
        	System.out.println("   Cette table n'existe pas");
        }       
	}
	
	// M�thode de saisie pour SELECT * FROM nomTable;
	// Appel la m�thode affichageDeDonnees() de Table.java pour afficher toutes les donn�es sous forme de tableau
	
	public static void selectAll(String saisie, DataBase db) {
		
		// Split des elements � retirer de la saisie
        
		String[] etape0 = saisie.split("SELECT \\* FROM ");
                
        String[] etape1 = etape0[1].split(";");
        String nomTable = etape1[0];
        
        // V�rifie si la table existe & appel la m�thode affichageDeDonnees() de Table.java
        
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
	
	// ====================== En cours de traitement ====================== //
	
	// M�thode de saisie pour SELECT nom_du_champ FROM nomTable;
	//
	
	public static void selectFrom(String saisie, DataBase db) {
		
		// Split des elements � retirer de la saisie et mise en variable/liste des elements � conserver
		
        String[] etape0 = saisie.split("SELECT ");
        
        String[] etape1 = etape0[1].split(" FROM ");
        String nomColonne = etape1[0];
                
        String[] etape2 = etape1[1].split(";");
        String nomTable = etape2[0];
        
        System.out.println("nomColonne : " + nomColonne + " nomTable : " + nomTable);
        
        ArrayList <String> listeDeColonne = new ArrayList <String> ();
        listeDeColonne.add(nomColonne);
        
        // V�rifie si la table existe & appel la m�thode affichageColonne() de Table.java
        
		boolean tableIsInDB = false;
     
        for (Table t : db.getListeDesTables()) {
        	if (t.getNom().equals(nomTable)) {
        		tableIsInDB = true;
        		t.affichageColonne(listeDeColonne);
        		break;
        	}
        }
        
        if (tableIsInDB == false) {
        	System.out.println("   Cette table n'existe pas");
        }
		
	}
		
	public static void updateSyntaxe1(String saisie, DataBase db) {
		//"UPDATE nomTable SET nom_du_champ = 'nouvelle valeur';"
		System.out.println("\nSaisie: " +saisie);
		String[] etape0 = saisie.split(";"); // vire le ';'
		String[] etape1 = etape0[0].split("UPDATE ");//vire le UPDATE
		etape0 = etape1[1].split(" SET "); // split au SER
		String nomTable = etape0[0]; // R�cup�re nom de table
		etape1 = etape0[1].split(" = ");// split au =
		String nomDeColonne= etape1[0]; // R�cup�re nom de colonne
		etape0=etape1[1].split("'"); // split au '
		String nouvelleValeur = etape0[1]; //R�cup�re valeur

		System.out.println("nom de la table: " +nomTable + "\nnomDeColonne: " + nomDeColonne+ "\nnouvelle Valeur: "+nouvelleValeur);
		
		for (Table t : db.getListeDesTables()) {
						
			if (t.getNom().equals(nomTable)) {
				
				System.out.println(t.getNom());
				
				ArrayList<String> listeNomDeColonne = new ArrayList<String>();
				ArrayList<String> listeValeurs = new ArrayList<String>();
				
				listeNomDeColonne.add(nomDeColonne);
				listeValeurs.add(nouvelleValeur);
				
				t.modifierContenuColonne(listeNomDeColonne, listeValeurs);
				System.out.println(listeNomDeColonne.toString());
				break;
			}
			
		}
		
	}

	public static void updateSyntaxe2(String saisie, DataBase db) {
		//"UPDATE nomTable SET nom_du_champ = 'nouvelle valeur', nom_du_champ1 = 'nouvelle valeur1' ;"
		System.out.println("\nSaisie: " +saisie);
		String[] etape0 = saisie.split(";");//Retire le ';'
		String[] etape1 = etape0[0].split("UPDATE ");//Retire le UPDATE
		etape0 = etape1[1].split("SET "); // split au SET
		String nomTable = etape0[0]; // R�cup�re nom de la table
		etape1 = etape0[1].split(", ");//split au ", "
		ArrayList <String> listeDeColonne = new ArrayList <String> (); // stockage des noms de colonnes 
		ArrayList <String> listeDeValeur = new ArrayList <String> (); // stockage des valeurs
		for (int i=0; i<etape1.length; i++) {//parcours le split
			String[] temp0 = etape1[i].split(" = ");// split au =
			listeDeColonne.add(temp0[0]); // met le nom dans sa liste
			String[] temp1 = temp0[1].split("'"); // split au '
			listeDeValeur.add(temp1[1]); // met la valeur dans sa liste
		}
		System.out.println("Nom de la table: " +nomTable);
		System.out.println(listeDeColonne.toString());
		System.out.println(listeDeValeur.toString());
		
		for (Table t : db.getListeDesTables()) {
			
			if (t.getNom().equals(nomTable)) {
				
				System.out.println(t.getNom());
				
				t.modifierContenuColonne(listeDeColonne, listeDeValeur);

				break;
			}
		}
	}

	public static void updateSyntaxe3(String saisie, DataBase db) {
		//"UPDATE nomTable SET nom_du_champ = 'nouvelle valeur' WHERE nom_du_champ = 'ancienne valeur';"
		System.out.println("\nSaisie :" +saisie);
		String[] etape0 = saisie.split(";");//vire le ';'
		String[] etape1 = etape0[0].split("UPDATE ");//vire le UPDATE
		etape0 = etape1[1].split("SET ");//vire le SET
		String nomTable = etape0[0]; // get nom de la table
		etape1 = etape0[1].split("WHERE");//split au WHERE
		etape0 = etape1[0].split(" = ");;//split au " = "
		String nomDeColonne = etape0[0];
		String nouvelleValeur = etape0[1];
		String[] etape2 = etape1[1].split(" = ");;//split au " = "
		String ancienneValeur = etape2[1];
		etape0=nouvelleValeur.split("'");
		nouvelleValeur = etape0[1];
		etape0=ancienneValeur.split("'");
		ancienneValeur = etape0[1];
		//V�rifie que les deux noms de colonne son identique:
		if ( !(nomDeColonne.equals(etape2[0])) ) {
			System.out.println("ERREUR Les colonnes sont differentes!");
		}
		System.out.println("Nom de la table: " +nomTable +"\nNom de Colonne: "+nomDeColonne+ "\nNouvelle Valeur: "+nouvelleValeur + "\nAncienne Valeur: " +ancienneValeur);
	
		for (Table t : db.getListeDesTables()) {
			
			if (t.getNom().equals(nomTable)) {
				
				System.out.println(t.getNom());
			
				t.modifierContenuCible(nomDeColonne, ancienneValeur, nouvelleValeur);
				
				break;
			}
		}
		
	}	
	
	public static void deleteSyntaxe1(String saisie, DataBase db) {
		//"DELETE FROM  nomTable ;"
		System.out.println("\nSaisie :" +saisie);
		String[] etape0 = saisie.split(";");//vire le ';'
		String[] etape1 = etape0[0].split("DELETE FROM ");//vire le "DELETE FROM "
		String nomTable = etape1[1]; // get nom de la table
		System.out.println("Nom de la table: " + nomTable);
		
		for (Table t : db.getListeDesTables()) {
			
			if (t.getNom().equals(nomTable)) {
				
				System.out.println(t.getNom());
			
				t.supprimerEnsembleLigne();
				
				break;
			}
		}
	}
	
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
}