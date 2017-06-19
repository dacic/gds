package application.control;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;  
import application.Main;
import application.model.Barcode;
import application.model.InterfaceEan;
import application.model.SQLConnexion;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image; 

public class GestionBDDSql implements InterfaceEan {

	private Connection sqlConnnexion; //Autoclose
	private Main main;

	public GestionBDDSql() {
		main = Main.getMain();
		try {
			sqlConnnexion = new SQLConnexion(main.getPref()).connect();
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}

	@Override
	public void ajouterEan(final String ean, final String qty, final String desrcription) {
		
		int ancien = vérifierQtyAvant(ean);
		
		final int fqty = ancien + Integer.parseInt(qty);
		 
		feuVertSqlViewAjout();

		if (verifierSiExiste(ean)) {
			
			String sql = "UPDATE stock SET qty = ? WHERE ean = ?";
			try {
				PreparedStatement preparedStatement = sqlConnnexion.prepareStatement(sql);
				preparedStatement.setInt(1, fqty);
				preparedStatement.setString(2, ean);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
			}
		} else {
			String sql = "INSERT INTO stock(ean, description,qty) VALUES(?,?,?)";
			try {
				PreparedStatement preparedStatement = sqlConnnexion.prepareStatement(sql);
				preparedStatement.setString(1, ean);
				preparedStatement.setInt(3, fqty);
				preparedStatement.setString(2, desrcription);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
			}
		}
		
			verifierAvantApresQty(ean, ancien, fqty);
			feuSqlGrisViewAjout();
	}

	@Override
	public void suprimerEan(final String ean, final String qty) {
		feuVertSqlViewSup();
		final int ancien = vérifierQtyAvant(ean);
		 
		final int fqty =  ancien - Integer.parseInt(qty);
		 
		String sql = "UPDATE stock SET qty = ? WHERE ean = ?";

		if (verifierSiExiste(ean)) {
			
			try {
				PreparedStatement preparedStatement = sqlConnnexion.prepareStatement(sql);
				preparedStatement.setInt(1, fqty);
				preparedStatement.setString(2, ean);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
			}
		} else { 
			main.getAfficheurMsg().afficheErreur("Erreur", "article introuvable dans la bdd");
		}
		verifierAvantApresQty(ean, ancien, fqty);
	 
		feuSqlGrisViewSup();
	}

	public void modifierDesriptionEanSql(String ean, String newDescription) {
			feuVertSqlViewAjout();
			final String ancien = getDescription(ean);
		if (verifierSiExiste(ean)){
			if (!ancien.equals(newDescription)){
				String sql = "UPDATE stock SET description = ? WHERE ean = ?";

				if (verifierSiExiste(ean)) {
					
					try {
						PreparedStatement preparedStatement = sqlConnnexion.prepareStatement(sql);
						preparedStatement.setString(1, newDescription);
						preparedStatement.setString(2, ean);
						preparedStatement.executeUpdate();
					} catch (SQLException e) {
					}
				} else { 
					main.getAfficheurMsg().afficheErreur("Erreur", "article introuvable dans la bdd");
				}
			}
		}
		verifierAvantApresDescription(ean, ancien, newDescription);
		feuSqlGrisViewAjout();
	}

	private int vérifierQtyAvant(final String ean) {
		int qty = 0;

		String sql = "SELECT qty FROM stock WHERE ean='".concat(ean).concat("'");

		try {
			ResultSet rs = sqlConnnexion.createStatement().executeQuery(sql);
			rs.next();
			qty = rs.getInt(1);

		} catch (SQLException e) {

		}		
		return qty;
	}

	private boolean verifierSiExiste(String ean) {
		boolean existe = false;
		String sql = "SELECT ean FROM stock where ean=?";
		try {
			PreparedStatement preparedStatement = sqlConnnexion.prepareStatement(sql);
			preparedStatement.setString(1, ean);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				existe = true;
			} 
		} catch (SQLException e) {

		}
		return existe;
	}
	
	private String getDescription(final String ean) {
		String description = "";
		String sql = "SELECT descritpion FROM stock WHERE ean='".concat(ean).concat("'");

		try {
			ResultSet rs = sqlConnnexion.createStatement().executeQuery(sql);
			rs.next();
			description = rs.getString(1);

		} catch (SQLException e) {

		}
		return description;
	}
	
	
	private void verifierAvantApresQty(String ean, int ancien, int nouveau){
		int qty = -1;

		String sql = "SELECT qty FROM stock WHERE ean='".concat(ean).concat("'");

		try {
			ResultSet rs = sqlConnnexion.createStatement().executeQuery(sql);
			rs.next();
			qty = rs.getInt(1);

		} catch (SQLException e) {

		} 
		if (qty==nouveau && qty != ancien){
		
		} else {
			//TODO sql non synchro
		}
	}

	private void verifierAvantApresDescription(String ean, String ancien, String nouveau){
		String description = "";

		String sql = "SELECT descritpion FROM stock WHERE ean='".concat(ean).concat("'");

		try {
			ResultSet rs = sqlConnnexion.createStatement().executeQuery(sql);
			rs.next();
			description = rs.getString(1);

		} catch (SQLException e) {

		} 
		if (description.equals(nouveau)&&(!ancien.equals(description))){
		 
		} else {
			main.getAfficheurMsg().afficheErreur("Erreur", "erreur lors de le vérification de la base sql, quantité erroné\n merci d'effectuer un synchronisation complète");
		}
		
	}
	 
	
	private void feuVertSqlViewAjout() {
		try {
			if (!sqlConnnexion.isClosed()) {   
				main.getEanAjouterView().getSqlImage().setImage(new Image(getClass().getResource("/images/bddgreen.png").toExternalForm())); 
			
			}
		} catch (SQLException e1) { 
			main.getAfficheurMsg().afficheErreur("Erreur", "erreur lors de le vérification connection sql active");
		}	
	}
	
	private void feuSqlGrisViewAjout() { 
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	main.getEanAjouterView().getSqlImage().setImage(new Image(getClass().getResource("/images/bddgray.png").toExternalForm()));  
		            }
		        }, 
		       900
		);
	}
	private void feuVertSqlViewSup() {
		try {
			if (!sqlConnnexion.isClosed()) {   
				main.getEanSupprimerView().getSqlImqge().setImage(new Image(getClass().getResource("/images/bddgreen.png").toExternalForm())); 		
			}
		} catch (SQLException e1) { 
			main.getAfficheurMsg().afficheErreur("Erreur", "erreur lors de le vérification connection sql active");
		}	
	}
	
	private void feuSqlGrisViewSup() { 
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	main.getEanSupprimerView().getSqlImqge() .setImage(new Image(getClass().getResource("/images/bddgray.png").toExternalForm()));  
		            }
		        }, 
		       900
		);
	}
	
	public void modifierQtyMinCsvPourTous(String qtyMin, ProgressBar progressBar) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void enregistrerLaBase(Map<Barcode, Integer> baseArticles) {
		// TODO pas besoin enregistrement permanent 
	}
}
