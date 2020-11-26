package menu;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Programme {

	public static void main(String[] args) {
		
		ArrayList<User> listeUtilisateurs = new ArrayList<User>();
		ArrayList<DataBase> listeBasesDeDonnees = new ArrayList<DataBase>();
		
		// Utilisateur test
		User u1 = new User("test", "test", "test");
		listeUtilisateurs.add(u1);
		
		Scanner scan = new Scanner(System.in);
		int choice = 0;
		
		User utilisateurConnecte = null;
		
		do {
			utilisateurConnecte = log(scan, listeUtilisateurs);			
		}
		while (utilisateurConnecte == null);

		menu(scan, listeBasesDeDonnees);
		
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

			// Patterns pour vérifier les commandes

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
			
			// Conditions qui lance chaque commande -> Finir les contrôles (vérifications) et appeler les fonctions)
				
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
			// Si la commande est autre mais pas de DB utilisé
			if (dbUtilisee == null && (m3.find() == true || m4.find() == true || m5.find() == true || m6.find() == true || m7.find() == true)) {
				commandeOk = true;
				System.out.println("   Vous devez d'abord selectionner une base de données");
			}
			
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
			else if (!commande.equals("QUITTER") && commandeOk == false){
				System.out.println("   Commande incorrecte");
			}
			

		}
		while (!commande.equals("QUITTER"));
		
		System.out.println("+--------------------------------------------------------+");
		System.out.println("Au revoir");
	}
	
	// ======================================================================================== //
	
	public static DataBase selectDB(String saisie, ArrayList<DataBase> listeBaseDeDonnnees) {
		
		DataBase db = null;
		
        String[] etape0 = saisie.split("USE ");
        String[] etape1 = etape0[1].split("\\;");
        String nomDB = etape1[0];
        
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
	
	public static void createDB(String saisie, ArrayList<DataBase> listeBaseDeDonnnees) {
		
		boolean doublon = false;
		
        String[] etape0 = saisie.split("CREATE DATABASE ");
        String[] etape1 = etape0[1].split("\\;");
        String nomDB = etape1[0];
        
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

	public static void createTable(String saisie, DataBase db) {
		
		boolean doublon = false;
		        
        // On retire CREATE TABLE
		
        String[] etape0 = saisie.split("CREATE TABLE ");
        
        // On retire la premiere parenthèse "("
        
        String[] etape1 = etape0[1].split("\\(");
        String nomTable = etape1[0];
        
        // On retire la deuxième parenthèse ")"
        
        String[] etape2 = etape1[1].split("\\)");
        
        // On retire les virgules et les espaces ", " et on obtient une liste de String
        
        String [] liste = etape2[0].split(", ");
        
        ArrayList <String> listeDeColonne = new ArrayList <String> ();
        
        for (int i = 0; i < liste.length; i++) {
            listeDeColonne.add(liste[i]);
        }
        
        for (Table t : db.getListeDesTables()) {
        	if (t.getNom().equals(nomTable)) {
        		doublon = true;
        		System.out.println("   Cette table existe déjà");
        		break;
        	}
        }
        
        if (doublon == false) {
        	Table table = db.creerTable(nomTable);
        	table.creerColonne(listeDeColonne);
    		System.out.println("   La table " + nomTable + " a bien été crée");
        }
    }
	
	public static void insertInto(String saisie, DataBase db) {
		
        String[] etape0 = saisie.split("INSERT INTO ");
        
        // On retire la premiere parenthèse "("
        
        String[] etape1 = etape0[1].split(" VALUES\\(");
        String nomTable = etape1[0];
        
        // On retire la deuxième parenthèse ")"
        
        String[] etape2 = etape1[1].split("\\)");
        
        // On retire les virgules et les espaces ", " et on obtient une liste de String
        
        String [] liste = etape2[0].split(", ");
        
        ArrayList <String> listeDeValeurs = new ArrayList <String> ();
        
        for (int i = 0; i < liste.length; i++) {
        	listeDeValeurs.add(liste[i]);
        }
        
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

	public static void selectAll(String saisie, DataBase db) {
        
		String[] etape0 = saisie.split("SELECT \\* FROM ");
        
        // On retire la premiere parenthèse "("
        
        String[] etape1 = etape0[1].split(";");
        String nomTable = etape1[0];
        
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
	
	public static void updateSyntaxe1(String saisie, DataBase db) {
		//"UPDATE nomTable SET nom_du_champ = 'nouvelle valeur';"
		System.out.println("\nSaisie: " +saisie);
		String[] etape0 = saisie.split(";"); // vire le ';'
		String[] etape1 = etape0[0].split("UPDATE ");//vire le UPDATE
		etape0 = etape1[1].split(" SET "); // split au SER
		String nomTable = etape0[0]; // Récupère nom de table
		etape1 = etape0[1].split(" = ");// split au =
		String nomDeColonne= etape1[0]; // Récupère nom de colonne
		etape0=etape1[1].split("'"); // split au '
		String nouvelleValeur = etape0[1]; //Récupère valeur

		System.out.println("nom de la table: " +nomTable + "\nnomDeColonne: " + nomDeColonne+ "\nnouvelle Valeur: "+nouvelleValeur);
		
		for (Table t : db.getListeDesTables()) {
						
			if (t.getNom().equals(nomTable)) {
				
				System.out.println(t.getNom());
				
				ArrayList<String> listeNomDeColonne = new ArrayList<String>();
				ArrayList<String> listeValeurs = new ArrayList<String>();
				
				listeNomDeColonne.add(nomDeColonne);
				listeValeurs.add(nouvelleValeur);
				
				t.modifierContenuColonne(listeNomDeColonne, listeValeurs);

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
		String nomTable = etape0[0]; // Récupère nom de la table
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
		//Vérifie que les deux noms de colonne son identique:
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