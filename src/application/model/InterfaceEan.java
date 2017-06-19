package application.model;

import java.util.Map;

public interface InterfaceEan {
	
	void ajouterEan(final String ean, final String quantite, final String desrcription);
	void suprimerEan(final String barcode, final String quantite);
	void enregistrerLaBase(final Map<Barcode, Integer> baseArticles);
}
