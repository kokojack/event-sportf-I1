package InterfaceUtilisateur;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator.OfDouble;

import utilitaires.ligneDeCommande.*;
import inscriptions.*;

/*** IMPLEMENTATION DU DIALOGUE EN LIGNE DE COMMANDE ****/

public class Interface {

	static final Inscriptions inscript = Inscriptions.getInscriptions();

	static Menu getMenuPrincipal() {
		Menu menuPrincipal = new Menu("Menu Principal");
		menuPrincipal.ajoute(getMenuInscription());
		menuPrincipal.ajoute(getMenuCandidat());
		menuPrincipal.ajoute(getMenuCompetition());
		menuPrincipal.ajoute(getOptionQuitter());
		return menuPrincipal;
	}

	static Option getOptionQuitter() {
		Option quitter = new Option("QUITTER", "Q", getActionQuitter());
		return quitter;
	}

	static Action getActionQuitter() {
		return new Action() {

			@Override
			public void optionSelectionnee() {

			}

		};
	}

	static Menu getMenuCandidat() {
		Menu candidat = new Menu("CANDIDATS", "CA");
		candidat.ajoute(getOptionAfficherPersonne());
		candidat.ajoute(getOptionAfficherEquipe());
		candidat.ajoute(getModifierEquipe());
		;
		candidat.ajoute(getOptionCreerPersonne());
		candidat.ajoute(getOptionCreerEquipe());
		candidat.ajoute(getOptionSupprimerPersonne());
		candidat.ajoute(getOptionSupprimerEquipe());
		candidat.ajoute(getOptionPrecedent());
		return candidat;
	}

	static Option getOptionPrecedent() {
		Option retour = new Option("PRECEDENT", "P",
				getActionPrecedentCandidat());
		return retour;
	}

	static Action getActionPrecedentCandidat() {
		return new Action() {

			@Override
			public void optionSelectionnee() {
				getMenuPrincipal().start();
			}

		};
	}

	static Option getOptionAfficherPersonne() {
		Option afficherCandidats = new Option("Afficher les personnes", "AC",
				getActionAfficherPersonne());
		return afficherCandidats;
	}

	static Action getActionAfficherPersonne() {
		return new Action() {
			public void optionSelectionnee() {
				for (inscriptions.Candidat c : inscript.getCandidats())
					if (c instanceof Personne)
						System.out.println(c);
			}
		};
	}

	static Option getOptionAfficherEquipe() {
		Option afficherCandidats = new Option("Afficher les équipes", "AE",
				getActionAfficherEquipe());
		return afficherCandidats;
	}

	static Action getActionAfficherEquipe() {
		return new Action() {
			public void optionSelectionnee() {
				for (inscriptions.Candidat c : inscript.getCandidats())
					if (c instanceof Equipe)
						System.out.println(c);
			}
		};
	}

	static Option getOptionCreerPersonne() {
		Option creationPersonne = new Option("Creation une personne", "CP",
				getActionCreerPersonne());
		return creationPersonne;
	}

	static Action getActionCreerPersonne() {
		return new Action() {
			public void optionSelectionnee() {
				String nom = utilitaires.EntreesSorties
						.getString("entrer le nom");
				String prenom = utilitaires.EntreesSorties
						.getString("entrer le prenom");
				String mail = utilitaires.EntreesSorties
						.getString("entrer l'adresse mail");
				inscript.createPersonne(nom, prenom, mail);
			}
		};
	}

	static Option getOptionCreerEquipe() {
		Option creationEquipe = new Option("Créer une Equipe", "CE",
				getActionCreerEquipe());
		return creationEquipe;
	}

	static Action getActionCreerEquipe() {
		return new Action() {
			public void optionSelectionnee() {
				String nom = utilitaires.EntreesSorties
						.getString("entrer le nom");
				inscript.createEquipe(nom);
			}
		};
	}

	static Menu getModifierEquipe() {
		Menu modifierEquipe = new Menu("MODIFIER LES EQUIPES", "ME");
		modifierEquipe.ajoute(getOptionListerEquipe());
		modifierEquipe.ajoute(getOptionPrecedent());
		//modifierEquipe.ajoute(getOptionSupprimerPersonneAequipe());
		return modifierEquipe;
	}

	static Option getOptionListerEquipe() {
		Option ListeEquipe = new Liste<Equipe>("Lister les équipes", "LE", getActionListeModification());
		return ListeEquipe;
	}
	
	static ActionListe<Equipe> getActionListeModification()
	{
		return new ActionListe<Equipe>(){

			@Override
			public List<Equipe> getListe() {
				System.out.println("génération de la liste des équipes");
				ArrayList<Equipe> equipes = new ArrayList<>();
				for (Candidat candidat : inscript.getCandidats())
					if (candidat instanceof Equipe)
						equipes.add((Equipe)candidat);
				System.out.println("ok");
				return equipes;
			}

			@Override
			public void elementSelectionne(int indice, Equipe element) {
				System.out.println("vous avez selectionné l'équipe " + element
						+ "à l'indice " + indice);
				Menu modifEquipe = getModifierMembresEquipe(element);
				modifEquipe.start();
				
				
				
				
			}
			
		};
	}

	static Menu getModifierMembresEquipe(Equipe equipe){
		Menu modifierEquipe = new Menu("Modifier les membres d'une equipe");
		modifierEquipe.ajoute(getOptionEnleverPersonne(equipe));
		modifierEquipe.ajoute(getOptionAjouterPersonne(equipe));
		modifierEquipe.ajoute(getOptionPrecedent());
		return modifierEquipe;
	}
	
	static Option getOptionEnleverPersonne(Equipe equipe){
		Option enleverPersonne = new Liste<>("Enlever une personne", "EP", getActionEnleverPersonne(equipe));
		return enleverPersonne;
	}
	
	static ActionListe<Personne> getActionEnleverPersonne(final Equipe equipe){
		return new ActionListe<Personne>(){
			
			
			@Override
			public List<Personne> getListe() {
				return new ArrayList<Personne>(equipe.getMembres());
			}
			
			@Override
			public void elementSelectionne(int indice, Personne element) {
				equipe.remove(element);
			}};
	}
	
	static Option getOptionAjouterPersonne(Equipe equipe){
		Option ajouterPersonne = new Liste<>("ajouter une personne", "AP", getActionAjouterPersonne(equipe));
		return ajouterPersonne;
	}
	
	static ActionListe<Personne> getActionAjouterPersonne(final Equipe equipe){
		return new ActionListe<Personne>(){
			
			
			@Override
			public List<Personne> getListe() {
				ArrayList<Personne> personnes = new ArrayList<>();
				for (Candidat candidat : inscript.getCandidats())
					if (candidat instanceof Personne)
						personnes.add((Personne) candidat);
				System.out.println("ok");
				return personnes;
			}
			
			@Override
			public void elementSelectionne(int indice, Personne element) {
				equipe.add(element);
			}};
	}


	static Option getOptionSupprimerPersonne() {
		Option supprimerCandidat = new Liste<Personne>(
				"Supprimer une personne", "SP", getActionListeSuppression());
		return supprimerCandidat;
	}

	static ActionListe<Personne> getActionListeSuppression() {
		return new ActionListe<Personne>() {

			// Retourne la liste des personnes formant le menu
			@Override
			public List<Personne> getListe() {
				System.out.println("génération de la liste");
				ArrayList<Personne> personnes = new ArrayList<>();
				for (Candidat candidat : inscript.getCandidats())
					if (candidat instanceof Personne)
						personnes.add((Personne) candidat);
				System.out.println("ok");
				return personnes;
			}

			// Exécutée automatiquement lorsqu'un élément de liste est
			// sélectionné
			@Override
			public void elementSelectionne(int indice, Personne element) {
				System.out.println("vous avez selectionné" + element
						+ "à l'indice" + indice);
				element.delete();
				System.out.println("le membre" + element.getNom()
						+ "a bien été supprimé");

			}
		};
	}

	static Option getOptionSupprimerEquipe() {
		Option supprimerEquipe = new Liste<Equipe>("Supprimer une Equipe",
				"SE", getActionListeSupprimerEquipe());
		return supprimerEquipe;
	}

	static ActionListe<Equipe> getActionListeSupprimerEquipe() {
		return new ActionListe<Equipe>() {

			@Override
			public List<Equipe> getListe() {
				System.out.println("Génération de la liste");
				ArrayList<Equipe> ListeEquipe = new ArrayList<Equipe>();
				for (Candidat c : inscript.getCandidats())
					if (c instanceof Equipe)
						ListeEquipe.add((Equipe) c);
				System.out.println("ok");
				return ListeEquipe;
			}

			@Override
			public void elementSelectionne(int indice, Equipe element) {
				System.out.println("Vous avez sélectionné l'élément" + element
						+ "à l'indice" + indice);
				element.delete();
				System.out.println(element + "a bien été supprimé!");

			}

		};
	}

	static Menu getMenuCompetition() {
		Menu competition = new Menu("COMPETITIONS", "CO");
		competition.ajoute(getOptionAfficherCompetition());
		competition.ajoute(getOptionCreerCompetition());
		competition.ajoute(getOptionSupprimerCompetition());
		competition.ajoute(getOptionPrecedent());
		return competition;
	}

	static Option getOptionAfficherCompetition() {
		Option afficherCompetition = new Option("Afficher les compétitions",
				"AC", getActionAfficherCompetition());
		return afficherCompetition;
	}

	static Action getActionAfficherCompetition() {
		return new Action() {

			@Override
			public void optionSelectionnee() {
				for (inscriptions.Competition c : inscript.getCompetitions()) {
					System.out.println("Pour la compétiton\t------>\t"
							+ c.toString()
							+ "\n\nles inscriptions sont ouvertes?:\t----->\t"
							+ c.ouvert() + "\n\njusqu'au\t------>\t"
							+ c.getDateCloture()
							+ "\n\nelles se déroulent en Equipe?:\t----->\t"
							+ c.estEnEquipe() + "\n\n");
				}
			}
		};
	}

	static Option getOptionCreerCompetition() {
		Option creationCompetition = new Option("Nouvelle Compétition", "NC",
				getActionCreerCompetition());
		return creationCompetition;
	}

	static Action getActionCreerCompetition() {
		return new Action() {
			@Override
			public void optionSelectionnee() {
				String nom = utilitaires.EntreesSorties
						.getString("entrer le nom");
				LocalDate date = LocalDate.parse(utilitaires.EntreesSorties
						.getString("entrer la date"));
				boolean enEquipe = (utilitaires.EntreesSorties
						.getString("en Equipe?")).matches("true|1|vrai|oui") ? true
						: false;
				inscript.createCompetition(nom, date, enEquipe);
			}
		};
	}

	static Option getOptionSupprimerCompetition() {
		return new Liste<Competition>("Supprimer une competition", "SC",
				getActionSupprimerCompetition());
	}

	static ActionListe<Competition> getActionSupprimerCompetition() {
		return new ActionListe<Competition>() {

			public List<Competition> getListe() {
				List<Competition> compet = new ArrayList<Competition>();
				for (Competition competition : inscript.getCompetitions())
					compet.add(competition);
				return compet;
			}

			@Override
			public void elementSelectionne(int indice, Competition element) {
				System.out.println("Vous avez sélectionnez" + element
						+ "à l'indice" + indice);
				element.delete();
				System.out.println("l'élément a été supprimé");

			}

		};
	}

	static Menu getMenuInscription() {
		Menu Inscription = new Menu("INSCRIPTIONS", "I");
		Inscription.ajoute(getOptionAfficherInscription());
		Inscription.ajoute(getOptionPrecedent());
		return Inscription;
	}

	static Option getOptionAfficherInscription() {
		Option afficherInscription = new Option("Afficher les inscriptions",
				"AI", getActionAfficherInscriptions());
		return afficherInscription;
	}

	static Action getActionAfficherInscriptions() {
		return new Action() {

			@Override
			public void optionSelectionnee() {
				System.out.println(inscript);

			}
		};
	}

	public static void main(String[] args) {
		Menu menu = getMenuPrincipal();
		menu.start();
	}

}
