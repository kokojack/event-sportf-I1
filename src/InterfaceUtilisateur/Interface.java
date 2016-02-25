package InterfaceUtilisateur;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import utilitaires.ligneDeCommande.*;
import inscriptions.*;

/*** IMPLEMENTATION DU DIALOGUE EN LIGNE DE COMMANDE ****/

public class Interface {

	static final Inscriptions inscript = Inscriptions.getInscriptions();

	/**
	 * constitue la page d'acceuil de l'application en regroupant les 3
	 * principaux menus -candidat, competitions et inscription
	 */
	static Menu getMenuPrincipal() {
		Menu menuPrincipal = new Menu("Menu Principal");
		// menuPrincipal.ajoute(getMenuInscription());
		menuPrincipal.ajoute(getOptionAfficherInscription());
		menuPrincipal.ajoute(getMenuCandidat());
		menuPrincipal.ajoute(getMenuCompetition());
		menuPrincipal.ajouteQuitter("Q");
		return menuPrincipal;
	}

	static Menu getMenuCandidat() {
		Menu candidat = new Menu("CANDIDATS", "CA");
		candidat.ajoute(getMenuPersonne());
		candidat.ajoute(getMenuEquipe());
		candidat.ajouteRevenir("P");
		candidat.ajouteQuitter("Q");
		return candidat;
	}

	static Menu getMenuPersonne() {
		Menu personne = new Menu("Les Personnes", "LP");
		personne.ajoute(getOptionAfficherPersonne());
		personne.ajoute(getMenuModifierPersonne());
		personne.ajoute(getOptionCreerPersonne());
		personne.ajoute(getOptionSupprimerPersonne());
		personne.ajouteRevenir("P");
		personne.ajouteQuitter("Q");
		return personne;
	}

	static Menu getMenuEquipe() {
		Menu equipe = new Menu("Les Equipes", "LE");
		equipe.ajoute(getOptionAfficherEquipe());
		equipe.ajoute(getMenuModifierPersonneEquipe());
		equipe.ajoute(getOptionCreerEquipe());
		equipe.ajoute(getOptionSupprimerEquipe());
		equipe.ajouteRevenir("P");;
		equipe.ajouteQuitter("Q");;
		return equipe;
	}

	static Option getOptionAfficherPersonne() {
		Option afficherCandidats = new Option("Afficher les personnes", "AP",
				getActionAfficherPersonne());
		return afficherCandidats;
	}

	static Action getActionAfficherPersonne() {
		return new Action() {
			public void optionSelectionnee() {
				int i = 0;
				int count = 1;
				for (inscriptions.Candidat c : inscript.getCandidats()) {
					if (c instanceof Personne) {
						System.out.println(count + "- " + c.getNom() + " "
								+ ((Personne) c).getPrenom());
						System.out.println("\tCOMPETITIONS:"
								+ c.getCompetitions() + "\n");
						System.out.println("\tEQUIPES:"
								+ NomEquipes((Personne) c) + "\n");
						count++;
						i++;
					}
				}
				if (i == 0)
					System.out.println("\tAucune personne enregistrée!!\n");
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
				int i = 0;
				int count = 1;
				for (inscriptions.Candidat c : inscript.getCandidats()) {

					if (c instanceof Equipe) {
						System.out.println(count + "- " + c.getNom());
						count++;
						i++;
					}
				}
				if (i == 0)
					System.out.println("\tAucune Equipe enregistrée!\n");
			}
		};
	}

	static Option getOptionCreerPersonne() {
		Option creationPersonne = new Option("Créer une personne", "CP",
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
				if (nom.isEmpty() || prenom.isEmpty() || mail.isEmpty()) {
					System.out.println("\tDûMENT REMPLIR LES CHAMPS!!!");
					getMenuPersonne().start();
				} else
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
				//LocalDate date = LocalDate.parse(utilitaires.EntreesSorties.getString("entrer la date de clôture"));
				inscript.createEquipe(nom);
			}
		};
	}

	static Menu getMenuModifierPersonne() {
		Menu modifierPersonne = new Menu("Modifier Une Personne", "MP");
		modifierPersonne.ajoute(getOptionListerPersonne());
		modifierPersonne.ajouteRevenir("P");
		modifierPersonne.ajouteQuitter("Q");
		// modifierEquipe.ajoute(getOptionSupprimerPersonneAequipe());
		return modifierPersonne;
	}

	static Option getOptionListerPersonne() {
		Option ListePersonne = new Liste<Personne>("Liste Des Personnes", "LP",
				getActionListeModification());
		return ListePersonne;
	}

	static ActionListe<Personne> getActionListeModification() {
		return new ActionListe<Personne>() {

			@Override
			public List<Personne> getListe() {
				ArrayList<Personne> personnes = new ArrayList<>();
				for (Candidat candidat : inscript.getCandidats())
					if (candidat instanceof Personne)
						personnes.add((Personne) candidat);
				if (personnes.isEmpty()) {
					System.out.println("\tACUNE PERSONNE ENREGISTREE!!\n");
					getMenuEquipe().start();
				} else
					System.out
							.println("\tchoisir une personne en saisissant son numéro\n");
				return personnes;
			}

			@Override
			public void elementSelectionne(int indice, Personne element) {
				indice = indice + 1;
				System.out
						.println("\tVous avez selectionné le membre "
								+ NomCandidat(element) + " à l'indice "
								+ indice + "\n");
				Menu modifMembre = getModifierMembre(element);
				modifMembre.start();

			}

		};
	}

	static Menu getModifierMembre(Personne membre) {
		Menu modifierMembre = new Menu("Modifier un membre");
		modifierMembre.ajoute(getOptionModifierNom(membre));
		modifierMembre.ajoute(getOptionModifierPrenom(membre));
		modifierMembre.ajoute(getOptionModifierMail(membre));
		modifierMembre.ajouteRevenir("P");
		modifierMembre.ajouteQuitter("Q");
		return modifierMembre;
	}

	static Option getOptionModifierNom(Personne membre) {
		Option modificationNom = new Option("Modifier son nom", "MN",
				getActionModifierNom(membre));
		return modificationNom;
	}

	static Action getActionModifierNom(Personne membre) {
		return new Action() {

			@Override
			public void optionSelectionnee() {
				String newLastName = utilitaires.EntreesSorties
						.getString("Entrer le nouveau nom");
				membre.setNom(newLastName);
				System.out.println("\tLe nom a bien été modifié!\n");
			}

		};
	}

	static Option getOptionModifierPrenom(Personne membre) {
		Option modificationNom = new Option("Modifier son prénom", "MP",
				getActionModifierPrenom(membre));
		return modificationNom;
	}

	static Action getActionModifierPrenom(Personne membre) {
		return new Action() {

			@Override
			public void optionSelectionnee() {
				String newFirstName = utilitaires.EntreesSorties
						.getString("Entrer le nouveau prénom");
				membre.setPrenom(newFirstName);
				System.out.println("\tLe prénom a bien été modifié!\n");
			}

		};
	}

	static Option getOptionModifierMail(Personne membre) {
		Option modificationNom = new Option("Modifier son mail", "MM",
				getActionModifierMail(membre));
		return modificationNom;
	}

	static Action getActionModifierMail(Personne membre) {
		return new Action() {

			@Override
			public void optionSelectionnee() {
				String newMail = utilitaires.EntreesSorties
						.getString("Entrer le nouveau mail");
				membre.setMail(newMail);
				System.out.println("\tLe mail a bien été modifié!\n");
			}

		};
	}

	static Menu getMenuModifierPersonneEquipe() {
		Menu modifierEquipe = new Menu("Modifier l'équipe", "ME");
		modifierEquipe.ajoute(getOptionListerEquipe());
		modifierEquipe.ajouteRevenir("P");
		modifierEquipe.ajouteQuitter("Q");
		return modifierEquipe;
	}

	static Option getOptionListerEquipe() {
		Option ListeEquipes = new Liste<Equipe>("Lister les Equipes", "LE",
				getActionListeEquipe());
		return ListeEquipes;
	}

	static ActionListe<Equipe> getActionListeEquipe() {
		return new ActionListe<Equipe>() {

			@Override
			public List<Equipe> getListe() {
				ArrayList<Equipe> equipe = new ArrayList<>();
				for (Candidat candidat : inscript.getCandidats())
					if (candidat instanceof Equipe)
						equipe.add((Equipe) candidat);
				if (equipe.isEmpty()) {
					System.out.println("\tAucune Equipe enregistrée!!\n");
					getMenuEquipe().start();
				}
				System.out
						.println("\tChoisir l'équipe en saisissant son numéro\n");
				return equipe;
			}

			@Override
			public void elementSelectionne(int indice, Equipe element) {
				Menu modifMembre = getModifierMembresEquipe(element);
				modifMembre.start();

			}

		};
	}

	static Menu getModifierMembresEquipe(Equipe equipe) {
		Menu modifierEquipe = new Menu("Modifier les membres d'une equipe");
		modifierEquipe.ajoute(getOptionEnleverPersonne(equipe));
		modifierEquipe.ajoute(getOptionAjouterPersonne(equipe));
		modifierEquipe.ajouteRevenir("P");
		modifierEquipe.ajouteQuitter("Q");
		return modifierEquipe;
	}

	static Option getOptionEnleverPersonne(Equipe equipe) {
		Option enleverPersonne = new Liste<>("Enlever une personne", "EP",
				getActionEnleverPersonne(equipe));
		return enleverPersonne;
	}

	static ActionListe<Personne> getActionEnleverPersonne(final Equipe equipe) {
		return new ActionListe<Personne>() {

			@Override
			public List<Personne> getListe() {
				ArrayList<Personne> liste = new ArrayList<Personne>(
						equipe.getMembres());
				if (liste.isEmpty()) {
					System.out.println("\tAucune personne dans l'équipe\n");
					getMenuModifierPersonneEquipe().start();
				}
				return liste;
			}

			@Override
			public void elementSelectionne(int indice, Personne element) {
				equipe.remove(element);
				System.out.println("Le membre a bien été enlevé de l'équipe");
			}
		};
	}

	static Option getOptionAjouterPersonne(Equipe equipe) {
		Option ajouterPersonne = new Liste<>("ajouter une personne", "AP",
				getActionAjouterPersonne(equipe));
		return ajouterPersonne;
	}

	static ActionListe<Personne> getActionAjouterPersonne(final Equipe equipe) {
		return new ActionListe<Personne>() {

			@Override
			public List<Personne> getListe() {
				ArrayList<Personne> personnes = new ArrayList<>();
				for (Candidat candidat : inscript.getCandidats())
					if (candidat instanceof Personne)
						personnes.add((Personne) candidat);
				if (personnes.isEmpty()) {
					System.out.println("\tAucun membre enregistré!!\n");
					getMenuModifierPersonneEquipe().start();
				}
				return personnes;
			}

			@Override
			public void elementSelectionne(int indice, Personne element) {
				equipe.add(element);
			}
		};
	}

	static Option getOptionSupprimerPersonne() {
		Option supprimerCandidat = new Liste<Personne>(
				"Supprimer une personne", "SP", getActionListeSuppression());
		return supprimerCandidat;
	}

	static ActionListe<Personne> getActionListeSuppression() {
		return new ActionListe<Personne>() {

			@Override
			public List<Personne> getListe() {
				ArrayList<Personne> personnes = new ArrayList<>();
				for (Candidat candidat : inscript.getCandidats())
					if (candidat instanceof Personne)
						personnes.add((Personne) candidat);
				return personnes;
			}

			@Override
			public void elementSelectionne(int indice, Personne element) {
				System.out.println("\tvous avez selectionné " + element
						+ " à l'indice\n" + indice);
				element.delete();
				System.out.println("\tle membre " + element.getNom()
						+ " a bien été supprimé\n");

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
				ArrayList<Equipe> ListeEquipe = new ArrayList<Equipe>();
				for (Candidat c : inscript.getCandidats())
					if (c instanceof Equipe)
						ListeEquipe.add((Equipe) c);
				if (ListeEquipe.isEmpty()) {
					System.out.println("\tAucune Equipe Enregistrée\n");
					getMenuEquipe().start();
				}
				return ListeEquipe;
			}

			@Override
			public void elementSelectionne(int indice, Equipe element) {
				System.out.println("\tVous avez sélectionné l'élément"
						+ element + "à l'indice " + indice + "\n");
				element.delete();
				System.out.println(element + " a bien été supprimée!\n");

			}

		};
	}

	// TODO
	static Menu getMenuCompetition() {
		Menu competition = new Menu("COMPETITIONS", "CO");
		competition.ajoute(getOptionAfficherCompetition());
		competition.ajoute(getOptionCreerCompetition());
		competition.ajoute(getOptionSupprimerCompetition());
		competition.ajoute(getMenuInscrireCandidat());
		competition.ajouteRevenir("P");
		competition.ajouteQuitter("Q");
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
				String ch;
				String stat;
				int count = 1;
				for (inscriptions.Competition c : inscript.getCompetitions()) {
					if (c.ouvert())
						ch = "ouvertes";
					else
						ch = "fermées";
					if (c.estEnEquipe())
						stat = "équipe";
					stat = "individuel";
					System.out.println("\t" + count + "-La compétiton\t"
							+ c.toString() + "\n\t\tles inscriptions sont\t"
							+ ch + "\n\t\t\t\tjusqu'à la date:\t"
							+ c.getDateCloture()
							+ "\n\t\telles se déroulent en\t" + stat + "\n\n");
					count++;
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
				if (compet.isEmpty()) {
					System.out.println("\tAucune Compétition Enregistrée!!\n");
					getMenuCompetition().start();
				}
				return compet;
			}

			@Override
			public void elementSelectionne(int indice, Competition element) {
				indice++;
				System.out.println("\tVous avez sélectionnez la compétition "
						+ element + "à l'indice " + indice + "\n");
				element.delete();
				System.out.println("\tL'élément a été supprimé\n");

			}

		};
	}

	static Menu getMenuInscrireCandidat() {
		Menu inscription = new Menu("Inscrire un candidat", "IC");
		inscription.ajoute(getOptionListerCompetitions());
		inscription.ajouteRevenir("P");
		inscription.ajouteQuitter("Q");
		return inscription;
	}

	static Liste<Competition> getOptionListerCompetitions() {
		Liste<Competition> ListeCompetitions = new Liste<Competition>(
				"Liste des compétitions", "LC", getActionListerCompetitions());
		return ListeCompetitions;
	}

	static ActionListe<Competition> getActionListerCompetitions() {
		return new ActionListe<Competition>() {

			@Override
			public List<Competition> getListe() {
				ArrayList<Competition> Comp = new ArrayList<Competition>();
				for (Competition co : inscript.getCompetitions())
					Comp.add(co);
				if (Comp.isEmpty())
					System.out.println("Aucune Competition pour le moment");
				return Comp;
			}

			@Override
			public void elementSelectionne(int indice, Competition element) {
				if (!(element.getCandidats().isEmpty()))
					System.out
							.println("\tListe des inscripts à la compétition "
									+ element.getNom() + "\n");
				int count = 1;
				for (Candidat c : element.getCandidats()) {
					System.out.println("\t\t" + count + "- " + c.getNom()
							+ "\n");
					count++;
				}
				if (element.estEnEquipe()) {
					System.out
							.println("\tCette competition se déroule en équipe, veuillez choisir l'équipe à inscrire\n");
					MenuInscrireEquipe(element).start();
				} else {
					System.out
							.println("\tCette competition  se déroule individuellement, veuillez choisir la personne à inscrire\n");
					MenuInscrirePersonne(element).start();
				}
			}
		};
	}

	static Menu MenuInscrirePersonne(Competition c) {
		Menu inscrireP = new Menu("Inscrire une personne", "IP");
		inscrireP.ajoute(getOptionListePersonne(c));
		inscrireP.ajouteRevenir("P");
		inscrireP.ajouteQuitter("Q");
		return inscrireP;
	}

	static Liste<Personne> getOptionListePersonne(Competition compet) {
		Liste<Personne> listepersonne = new Liste<Personne>(
				"Liste des Personnes", "LP", getActionListePersonne(compet));
		return listepersonne;
	}

	static ActionListe<Personne> getActionListePersonne(Competition comp) {
		return new ActionListe<Personne>() {

			@Override
			public List<Personne> getListe() {
				ArrayList<Personne> pers = new ArrayList<Personne>();
				for (Candidat p : inscript.getCandidats())
					if (p instanceof Personne
							&& !(comp.getCandidats().contains(p)))
						pers.add((Personne) p);
				if (pers.isEmpty())
					System.out.println("Aucune personne enregistrée");
				return pers;
			}

			@Override
			public void elementSelectionne(int indice, Personne element) {
				if (comp.add(element)) {
					System.out.println("\tLe membre " + element.getNom()
							+ " est maintenant inscrit à la compétition "
							+ comp.getNom() + "\n");
					getMenuCompetition().start();
				}

			}

		};
	}

	static Menu MenuInscrireEquipe(Competition c) {
		Menu inscrireE = new Menu("Inscrire une équipe", "IE");
		inscrireE.ajoute(getOptionListeEquipe(c));
		inscrireE.ajouteRevenir("P");
		inscrireE.ajouteQuitter("Q");
		return inscrireE;
	}

	static Option getOptionListeEquipe(Competition comp) {
		Liste<Equipe> equipes = new Liste<Equipe>("Listes Equipes", "LE",
				getActionListerEquipe(comp));
		return equipes;
	}

	static ActionListe<Equipe> getActionListerEquipe(Competition comp) {
		return new ActionListe<Equipe>() {

			@Override
			public List<Equipe> getListe() {
				ArrayList<Equipe> equi = new ArrayList<Equipe>();
				for (Candidat p : inscript.getCandidats())
					if (p instanceof Equipe
							&& !(comp.getCandidats().contains(p)))
						equi.add((Equipe) p);
				if (equi.isEmpty())
					System.out.println("Aucune Equipe enregistrée");
				return equi;
			}

			@Override
			public void elementSelectionne(int indice, Equipe element) {
				if (comp.add(element)) {
					System.out.println("\tL'équipe " + element.getNom()
							+ " est maintenant inscrite à la compétition "
							+ comp.getNom() + "\n");

				}
			}
		};
	}

	// static Menu getMenuInscription() {
	// Menu Inscription = new Menu("INSCRIPTIONS", "I");
	// Inscription.ajoute(getOptionAfficherInscription());
	// Inscription.ajoute(getOptionPrecedent());
	// return Inscription;
	// }

	static Option getOptionAfficherInscription() {
		Option afficherInscription = new Option("Afficher les inscriptions",
				"AI", getActionAfficherInscriptions());
		return afficherInscription;
	}

	static Action getActionAfficherInscriptions() {
		return new Action() {

			@Override
			public void optionSelectionnee() {
				int count = 1;
				for (Candidat p : inscript.getCandidats())
					if (p instanceof Personne) {
						System.out
								.println(count
										+ "- Le membre: <<"
										+ p.getNom()
										+ ">> \n\test inscrit à la (aux)compétition(s): <<"
										+ NomCompetitions((Personne) (p))
										+ " >>, \n\test membre de l' (des) équipe(s): <<"
										+ NomEquipes((Personne) (p)) + ">>\n\n");
						count++;
					}
				for (Candidat e : inscript.getCandidats())
					if (e instanceof Equipe) {
						System.out
								.println(count
										+ "- L'équipe: <<"
										+ e.getNom()
										+ ">> \n\test inscrite à la (aux) compétition(s): <<"
										+ NomCompetitions(e)
										+ ">> \n\tses Membres sont: "
										+ NomMembres((Equipe) (e)) + "\n\n");
						count++;
					}

			}
		};
	}

	public static String NomEquipes(Personne c) {
		String msg = "";
		for (Equipe e : c.getEquipes())
			msg = msg + e.getNom() + "; ";
		return msg;
	}

	public static String NomMembres(Equipe e) {
		String ch = " ";
		for (Personne p : ((Equipe) e).getMembres())
			ch = ch + p.getNom() + p.getPrenom() + p.getMail();
		return ch;
	}

	public static String NomCompetitions(Candidat e) {
		String ch = " ";
		for (Competition p : e.getCompetitions())
			ch = ch + p.getNom() + ";";
		return ch;
	}

	public static String NomCandidat(Candidat e) {
		if (e instanceof Personne)
			return ((Personne) (e)).getNom() + " "
					+ ((Personne) (e)).getPrenom();
		return ((Equipe) (e)).getNom();
	}

	public static void main(String[] args) {
		Menu menu = getMenuPrincipal();
		menu.start();
	}

}
