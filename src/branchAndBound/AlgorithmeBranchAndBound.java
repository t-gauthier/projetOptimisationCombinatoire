package branchAndBound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import model.Base;
import model.Model;

public class AlgorithmeBranchAndBound {

	private Model model;
	private SacADos sacADos;
	private int majorant;
	
	private SacADos resultat;
	private int compteurMajorants;
	private int compteurBranches;
	
	private String logs;

	public AlgorithmeBranchAndBound(Model model) {
		this.model = model;
		this.sacADos = new SacADos();
		this.majorant = -1;
		
		this.resultat = null;
		this.compteurMajorants = 0;
		this.compteurBranches = 0;
		
		this.logs = "";
		
		rechercheOptimale(sacADos, 0);
		
		//System.out.println(logs);
	}
	
	@SuppressWarnings("unchecked")
	public void rechercheOptimale(SacADos sacADos, int increment) {
		
		if (increment < model.getEntreprises().size()) {
			
			// Nom de l'entreprise recherchée
			String nomEntreprise = model.getEntreprises().get(increment);

			// Pour chaque base contenant l'entreprise 
			for (Base base : model.getEntreprisesBases().get(nomEntreprise)) {
					
				logs += "\n";
				logs += "********** INCREMENT : " + increment + " **********";
				logs += "---------- ENTREPRISE : " + nomEntreprise + " ----------";
				logs += "BASE -> " + base.getNomBase();
				
				SacADos nouveauSac = new SacADos();
				nouveauSac.setResultatsPartiels((HashMap<String, ArrayList<String>>)sacADos.getResultatsPartiels().clone());
				
				// et qui n'est pas dans la liste partielle
				if (!sacADos.getResultatsPartiels().containsKey(base.getNomBase())) {
					ArrayList<String> nouvelleListeEntreprises = new ArrayList<String>();
					nouvelleListeEntreprises.add(nomEntreprise);
					nouveauSac.ajouterEntree(base.getNomBase(), nouvelleListeEntreprises);
					nouveauSac.setCoutOptimal(sacADos.getCoutOptimal() + base.getCoutBase());
				} else {
					nouveauSac.ajouterEntreprise(base.getNomBase(), nomEntreprise);
					nouveauSac.setCoutOptimal(sacADos.getCoutOptimal());
				}
				
				logs += nouveauSac.toString();
				
				if(!nouveauSac.testResultatsPartiels(model.getEntreprises().size())) {
					if (nouveauSac.getCoutOptimal() <= majorant || majorant == -1) {
						rechercheOptimale(nouveauSac, increment + 1);	
					} else {
						compteurBranches++;
						logs += "---> BRANCHE";
					}
				} else {
					if (nouveauSac.getCoutOptimal() < majorant || majorant == -1) {
						majorant = nouveauSac.getCoutOptimal();
						compteurMajorants++;
						resultat = nouveauSac;
						logs += "---> NOUVEAU MAJORANT : " + majorant;
					}
				}
			}
		}
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public SacADos getSacADos() {
		return sacADos;
	}

	public void setSacADos(SacADos sacADos) {
		this.sacADos = sacADos;
	}

	public SacADos getResultat() {
		return resultat;
	}

	public void setResultat(SacADos resultat) {
		this.resultat = resultat;
	}

	public int getMajorant() {
		return majorant;
	}

	public void setMajorant(int majorant) {
		this.majorant = majorant;
	}
	
	public String afficherResultat() {
		String affichageResultat = "";
		affichageResultat += "Meilleur coût : " + resultat.getCoutOptimal() + "\n\n";
		
		affichageResultat += "Nombre de majorants : " + compteurMajorants + "\n";
		affichageResultat += "Nombre de branches coupées : " + compteurBranches + "\n\n";
		
		
		for (Entry<String, ArrayList<String>> entry : resultat.getResultatsPartiels().entrySet()) {
			affichageResultat += "\tBase" + entry.getKey() + "\n";
			for (String entreprise : entry.getValue()) {
				affichageResultat += "\t\t" + entreprise + "\n";
			}
			affichageResultat += "\n";
		}
		return affichageResultat;
	}
	
	public String getLogs() {
		return logs;
	}
	
}