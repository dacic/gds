package application.model;

import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.postgresql.util.PSQLException;
import application.Main;

public class SQLConnexion {

	private Connection conn;
	private String url = "localhost";
	private String bdd = "gds";
	private String user = "postgres";
	private String pass = "postgres";
	private int port = 5432;  
	private Main main;

	public SQLConnexion(Preferences pref) {
		main = Main.getMain();
		this.url = pref.getSqlurl();
		this.bdd = pref.getSqlbdd();
		this.user = pref.getSqluser();
		this.pass = pref.getSqlpass();
		this.port = pref.getPort();
	}

	public Connection connect() throws SQLException {

		try {
			Class.forName("org.postgresql.Driver").newInstance();

		} catch (ClassNotFoundException cnfe) {
			main.getAfficheurMsg().afficheErreur("Error sql: ", cnfe.getMessage());
		} catch (InstantiationException ie) {
			main.getAfficheurMsg().afficheErreur("Error sql: ", ie.getMessage());
		} catch (IllegalAccessException iae) {
			main.getAfficheurMsg().afficheErreur("Error sql: ", iae.getMessage());
		}
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://" + url + ":" + port + "/" + bdd, user, pass);
		} catch (PSQLException psql) {
			main.getAfficheurMsg().afficheErreur("Error sql: ", psql.getMessage());
		} catch (SQLException sql) {
			main.getAfficheurMsg().afficheErreur("Error sql: ", sql.getMessage());
		}

		return conn;
	}

	public Connection getConnection() throws SQLException, ClassNotFoundException {
		if (conn != null && !conn.isClosed())
			return conn;

		connect();

		return conn;
	}

	public Boolean testerSiBddExiste() { 
		Boolean bddExiste = false; 
			try { 
				ResultSet rs = conn.getMetaData().getCatalogs();
				if (rs.next()) { 
					bddExiste = true;
				} 
			} catch (SQLException e) {
			}
		 
		return bddExiste;

	}
}
