package InterfaceUtilisateur;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utilitaires.ligneDeCommande.*;
import inscriptions.*;



/***IMPLEMENTATION DU DIALOGUE EN LIGNE DE COMMANDE****/


public class Interface {

	static final Inscriptions inscript = Inscriptions.getInscriptions();

	static Menu getMenuPrincipal()
	{
		Menu menuPrincipal = new Menu ("Menu Principal");
		menuPrincipal.ajoute(getMenuCandidat());
		menuPrincipal.ajoute(getMenuCompetition());
		return menuPrincipal;
	}

	static Menu getMenuCandidat()
	{
		Menu candidat = new Menu ("CANDIDATS", "c");
		candidat.ajoute(getOptionAfficherCandidat());
		candidat.ajoute(getOptionCreerPersonne());
		candidat.ajoute(getOptionSupprimerCandidat());
		return candidat;
	}

	static Option getOptionAfficherCandidat()
	{
		Option afficherCandidats = new Option ("Afficher", "AC", getActionAfficherCandidat());
		return afficherCandidats;
	}

	static Action getActionAfficherCandidat()
	{
		return new Action ()
		{
			public void optionSelectionnee() 
			{
				for (inscriptions.Candidat c : inscript.getCandidats())
					System.out.println(c);
			}
		};
	}

	static Option getOptionCreerPersonne()
	{
		Option creationPersonne = new Option ("Creation", "C", getActionCreerPersonne());
		return creationPersonne;
	}

	static Action getActionCreerPersonne()
	{
		return new Action ()
		{
			public void optionSelectionnee()
			{
				String nom = utilitaires.EntreesSorties.getString("entrer le nom");
				String prenom = utilitaires.EntreesSorties.getString("entrer le prenom");
				String mail = utilitaires.EntreesSorties.getString("entrer l'adresse mail");
				inscript.createPersonne(nom, prenom, mail);
			}
		};
	}

	static Option getOptionSupprimerCandidat()
	{
		Option supprimerCandidat = new Liste<Personne>("Suppression d'une personne", "S",getActionListeSuppression());
		return supprimerCandidat;
	}

	static ActionListe<Personne> getActionListeSuppression()
	{
		return new ActionListe<Personne>()
				{

			// Retourne la liste des personnes formant le menu
			@Override
			public List<Personne> getListe()
			{
				System.out.println("génération de la liste");
				ArrayList<Personne> personnes = new ArrayList<>();
				for(Candidat candidat : inscript.getCandidats())
					if (candidat instanceof Personne)
						personnes.add((Personne)candidat);
				System.out.println("ok");
				return personnes;
			}
			// Exécutée automatiquement lorsqu'un élément de liste est sélectionné
			@Override
			public void elementSelectionne(int indice, Personne element)
			{
				System.out.println("vous avez selectionné" +element+ "à l'indice" +indice);
				element.delete();	
				System.out.println("le membre" + element.getNom() +"a bien été supprimé");

			}
				};
	}

	static Menu getMenuCompetition()
	{
		Menu competition = new Menu ("COMPETITIONS", "compet");
		competition.ajoute(getOptionAfficherCompetition());
		competition.ajoute(getOptionCreerCompetition());
		competition.ajoute(getOptionSupprimerCompetition());
		return competition;
	}

	static Option getOptionAfficherCompetition()
	{
		Option afficherCompetition = new Option ("Afficher", "ac", getActionAfficherCompetition());
		return afficherCompetition;
	}
	static Action getActionAfficherCompetition()
	{
		return new Action ()
		{

			@Override
			public void optionSelectionnee() 
			{
				for (inscriptions.Competition c : inscript.getCompetitions())
				{
					System.out.println( "Pour la compétiton\t------>\t" +c.toString()+ 
							"\n\nles inscriptions sont ouvertes?:\t----->\t" +c.ouvert() + 
							"\n\njusqu'au\t------>\t" +c.getDateCloture()+
							"\n\nelles se déroulent en Equipe?:\t----->\t" +c.estEnEquipe()+
							"\n\n");
				}
			}
		};
	}

	static Option getOptionCreerCompetition()
	{
		Option creationCompetition = new Option ("Nouvelle Compétition", "nc", getActionCreerCompetition());
		return creationCompetition;
	}

	static Action getActionCreerCompetition()
	{
		return new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				String nom = utilitaires.EntreesSorties.getString("entrer le nom");
				LocalDate date = LocalDate.parse(utilitaires.EntreesSorties.getString("entrer la date"));
				boolean enEquipe = (utilitaires.EntreesSorties.getString("en Equipe?")).matches("true|1|vrai|oui")?true: false;
				inscript.createCompetition(nom, date, enEquipe);
			}
		};
	}

	static Option getOptionSupprimerCompetition()
	{
		return new Liste<Competition>("Supprimer competition", "sc", getActionSupprimerCompetition());
	}

	static ActionListe<Competition> getActionSupprimerCompetition()
	{
		return new ActionListe<Competition>()
				{

			public List<Competition> getListe() 
			{
				List<Competition> compet = new ArrayList<Competition>();
				for (Competition competition : inscript.getCompetitions())
					compet.add(competition);
				return compet;
			}

			@Override
			public void elementSelectionne(int indice, Competition element) 
			{
				System.out.println("Vous avez sélectionnez" +element+ "à l'indice" +indice);
				element.delete();
				System.out.println("l'élément a été supprimé");

			}

				};
	}


	public static void main(String[] args) 
	{
		Menu menu = getMenuPrincipal();
		menu.start();
	}

}
