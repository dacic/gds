package application.model;

import java.io.Serializable;

/**
 * Constructor Objet Preferences avec les options de l'application
 * @author DACIC Enis NFA019
 */
public class Preferences implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean utiliserEffetSonore; 
	private boolean afficherMessageAide;
	private boolean utiliserBaseSql=false;
	private String sqlurl = "localhost";
	private String sqlbdd = "gds";
	private String sqluser = "postgres";
	private String sqlpass = "postgres";
	private int port=5432;
	private String clef;


	public Preferences(Boolean utiliserEffetSonore, Boolean emplacementEanObligatoire, Boolean afficherMessageAide,
			Boolean utiliserBaseSql, String clef) {
		this.utiliserEffetSonore = utiliserEffetSonore; 
		this.afficherMessageAide = afficherMessageAide;
		this.utiliserBaseSql = utiliserBaseSql;
		this.clef = clef;
	}

	public boolean isUtiliserEffetSonore() {
		return utiliserEffetSonore;
	}

	public void setUtiliserEffetSonore(boolean utiliserEffetSonore) {
		this.utiliserEffetSonore = utiliserEffetSonore;
	}
 

	public boolean isAfficherMessageAide() {
		return afficherMessageAide;
	}

	public void setAfficherMessageAide(boolean afficherMessageAide) {
		this.afficherMessageAide = afficherMessageAide;
	}

	public boolean isUtiliserBaseSql() {
		return utiliserBaseSql;
	}

	public void setUtiliserBaseSql(boolean utiliserBaseSql) {
		this.utiliserBaseSql = utiliserBaseSql;
	}

	public String getSqlurl() {
		return sqlurl;
	}

	public void setSqlurl(String sqlurl) {
		this.sqlurl = sqlurl;
	}

	public String getSqlbdd() {
		return sqlbdd;
	}

	public void setSqlbdd(String sqlbdd) {
		this.sqlbdd = sqlbdd;
	}

	public String getSqluser() {
		return sqluser;
	}

	public void setSqluser(String sqluser) {
		this.sqluser = sqluser;
	}

	public String getSqlpass() {
		return Crypter.decrypt(sqlpass, clef);
	}

	public void setSqlpass(String sqlpass) {	 
		this.sqlpass = Crypter.encrypt(sqlpass, clef);
	}

	public String getClef() { 
		return this.clef;
	}

	public void setClef(String clef) {
		this.clef = clef;
	}
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
