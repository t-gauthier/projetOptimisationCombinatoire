package ihm;

import branchAndBound.AlgorithmeBranchAndBound;
import model.Model;
import readers.Lecteur;

public class Main {

	public static void main(String[] args) {
		String cheminListeBases = "./bin/docs/listesBases/lb1.txt";
		String cheminListeEntreprises = "./bin/docs/listesEntreprises/le1.txt";
		Lecteur lecteur = new Lecteur(cheminListeBases, cheminListeEntreprises);
		Model model = new Model();
		model.setBases(lecteur.lireBases());
		model.setEntreprises(lecteur.lireEntreprises());
		//model.afficher();
		AlgorithmeBranchAndBound algoBB = new AlgorithmeBranchAndBound(model);
	}

}
