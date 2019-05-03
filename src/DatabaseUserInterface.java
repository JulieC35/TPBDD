
import java.awt.BorderLayout;


import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import java.util.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * This is a skeleton for realizing a very simple database user interface in java. 
 * The interface is an Applet, and it implements the interface ActionListener. 
 * If the user performs an action (for example he presses a button), the procedure actionPerformed 
 * is called. Depending on his actions, one can implement the database connection (disconnection), 
 * querying or insert. 
 * 
 * @author zmiklos
 *
 * */
public class DatabaseUserInterface extends java.applet.Applet implements ActionListener {

	private TextField mStat, m1, m2, m3, m4, m5, tfTable;
	JButton tcourrante;
	TextArea mRes;
	JPanel panStatus, pChampsQuery;
	JMenuBar menuBar = new JMenuBar();
    JMenu menuBase = new JMenu("Base");
    JMenu menu = new JMenu("Requetes");
    
    //Pour selectionner la table dans Base - Insert
    JMenuItem itemCours, itemCreneaux, itemEnseignant, itemEtudiant, itemHoraire, itemMatiere, itemParcours, itemSalle;
    
  
  

    //Sous menus de Base
    JMenuItem itemConnect = new JMenuItem("Connect");
    JMenuItem itemDisconnect = new JMenuItem("Disconnect");
    JMenuItem itemQuery = new JMenuItem("Query");
    JMenuItem itemInsert = new JMenuItem("Insert");
    JMenu menuInsert = new JMenu("Insert");
    
   
    // Requete 
    JMenuItem item1 = new JMenuItem("Liste de tous les étudiants");
    JMenuItem item2 = new JMenuItem("Requete aléatoire");
    JMenuItem EDT_etudiant = new JMenuItem("EDT pour un etudiant à un jour donné");
    JMenuItem EDT_parcours = new JMenuItem("EDT pour un parcours");
    JMenuItem EDT_prof_crenaux = new JMenuItem("EDT pour un prof à un jour donné");
    JMenuItem HORAIRE_prochainCours = new JMenuItem ("Horaire du prochain cours d'une matiére");
    JMenuItem ENS_matiere = new JMenuItem ("Nom des enseignants pour une matiére");
    JMenuItem EDT_salle = new JMenuItem ("EDT d'une salle");
    JMenuItem TP_occupation_salle = new JMenuItem ("Temps d’occupation d’une salle sur 1 jours");
    JMenuItem TP_moy_occupation_tp = new JMenuItem ("Temps d’occupation moyen des salles de tp sur 1 jours");
    JMenuItem SALLE_libre = new JMenuItem("Liste des salles non-occupées");
    

    
    
    private static final long serialVersionUID = 1L; 
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://mysql.istic.univ-rennes1.fr/base_17006900";
	static final String USER = "user_17006900";
	static final String PASS = "Forest35";
	Connection conn = null;
	Statement stmt = null;
	enum tables {Cours, Crenaux, Enseignant, Etudiant, Horaire, Matiere, Parcours, Salle};
	tables currentTable;


	/**
	 * This procedure is called when the Applet is initialized.
	 * 
	 */
	public void init ()
	{    
		setSize(1500, 500);
		
		initComponent();
		
		addListeners();
		
		addToApplet();

		setStatus("Waiting for user actions.");
		
		//connectToDatabase();
	}

	protected void initComponent() {
		
		//Status
		mStat = new TextField(100);
		mStat.setEditable(false);
		panStatus = new JPanel();
		

		//Champs de texte
		m1 = new TextField(100);
		m2 = new TextField(100);
		m3 = new TextField(100);
		m4 = new TextField(100);
		m5 = new TextField(100);
		tfTable = new TextField(100);
		tfTable.setName("Table courante");
		tcourrante = new JButton("Changer");
		pChampsQuery = new JPanel();

		//m1.setText("Name (e.g. John Smith) - Please enter here!");  //According to the database schema
		//m2.setText("Age (e.g. 23)  - Please enter here!"); //According to the database schema
		//m3.setText("Color of the eye (e.g. green) - Please enter here!");  //According to the database schema
		tfTable.setText("Table to insert (e.g. Etudiant) - Please enter here!"); 

		m1.setText("Champ 1");  //According to the database schema
		m2.setText("Champ 2"); //According to the database schema
		m3.setText("Champ 3");  //According to the database schema
		m4.setText("Champ 4"); 
		
		
		//Champs résultat
		mRes = new TextArea(10,100);
		mRes.setEditable(false);
		mRes.setText("Query results");

		//item Base - Insert
		itemCours = new JMenuItem("Cours");
		itemCreneaux = new JMenuItem("Creneaux");
		itemEnseignant = new JMenuItem("Enseignant");
		itemEtudiant = new JMenuItem("Etudiant");
		itemHoraire = new JMenuItem("Horaire");
		itemMatiere = new JMenuItem("Matiere");
		itemParcours = new JMenuItem("Parcours");
		itemSalle = new JMenuItem("Salle");
		
		
	}
	
	protected void addListeners() {
		
		//Base
		itemConnect.addActionListener(this);
		itemDisconnect.addActionListener(this);
		itemQuery.addActionListener(this);
		itemInsert.addActionListener(this);
		//Base - Insert
		menuInsert.addActionListener(this);
		itemEtudiant.addActionListener(this);
		tcourrante.addActionListener(this);
		
		//Requetes
		item1.addActionListener(this);
		item2.addActionListener(this);
		
		
		//Ajout item requete Romain	
        EDT_etudiant.addActionListener(this);
        EDT_parcours.addActionListener(this); 
        EDT_prof_crenaux.addActionListener(this);
        HORAIRE_prochainCours.addActionListener(this);
        ENS_matiere.addActionListener(this);
        EDT_salle.addActionListener(this);
        TP_occupation_salle.addActionListener(this);
        TP_moy_occupation_tp.addActionListener(this);
        SALLE_libre.addActionListener(this);
	}
	
	protected void addToApplet() {
		
		setLayout(new BorderLayout());
		
		//Ajout des tables 
		menuInsert.add(itemCours);
		menuInsert.addSeparator();
	    menuInsert.add(itemCreneaux);
	    menuInsert.addSeparator();
	    menuInsert.add(itemEnseignant);
	    menuInsert.addSeparator();
	    menuInsert.add(itemEtudiant);
	    menuInsert.addSeparator();
	    menuInsert.add(itemHoraire);
	    menuInsert.addSeparator();
	    menuInsert.add(itemMatiere);
	    menuInsert.addSeparator();
	    menuInsert.add(itemParcours);
	    menuInsert.addSeparator();
	    menuInsert.add(itemSalle);
		
		//Ajout requete menu romain
		menu.add(this.EDT_etudiant);
	    menu.add(this.EDT_parcours);
	    menu.add(this.EDT_prof_crenaux);
		menu.add(this.TP_occupation_salle);
		menu.add(this.TP_moy_occupation_tp);
		menu.add(this.HORAIRE_prochainCours);
		menu.add(this.EDT_salle);
		menu.add(this.ENS_matiere);
		menu.add(this.SALLE_libre);
		menu.add(item1);
	    menu.addSeparator();
	    menu.add(item2);
	    
	    
	    menuBase.add(itemConnect);
	    menuBase.addSeparator();
	    menuBase.add(itemDisconnect);
	    menuBase.addSeparator();
	    menuBase.add(itemQuery);
	    menuBase.addSeparator();
	    menuBase.add(itemInsert);
	    
		menuBar.add(menuBase);
	    menuBar.add(menu);
	    
	    panStatus.add(menuBar);
		add(panStatus, BorderLayout.NORTH);
		
		pChampsQuery.add(mStat);
		pChampsQuery.add(m1);
		pChampsQuery.add(m2);
		pChampsQuery.add(m3);
		pChampsQuery.add(m4);
		pChampsQuery.add(m5);
		pChampsQuery.add(tfTable);
		pChampsQuery.add(tcourrante);

		
		pChampsQuery.add(mRes);

		
		add(pChampsQuery);
		
	}

	/**
	 * This procedure is called upon a user action.
	 * 
	 *  @param event The user event. 
	 */
	public void actionPerformed(ActionEvent event)
	{

		// Extract the relevant information from the action (i.e. which button is pressed?)
		Object cause = event.getSource();
		
		mRes.setText("");

		// Act depending on the user action
		// Button CONNECT
		if (cause == itemConnect)
		{
			connectToDatabase();
		} else

		// Button DISCONNECT
		if (cause == itemDisconnect)
		{
			disconnectFromDatabase();
		} else

		//Button QUERY
		if (cause == itemQuery)
		{
			queryDatabase();
		} else

		//Button INSERT
		if (cause == itemInsert)
		{
			insertDatabase();
		} else 
			
		if(cause == itemEtudiant) {
			insertDatabase();
			/*m2.setText("Prenom");
			m3.setText("idEtudiant");
			currentTable = tables.Etudiant;*/
		} else
		
		if(cause == tcourrante) {
			setCurrentTable(tfTable.getText());
		}
		
		
		else 
			
		//Button Lister les étudiants
		if (cause == item1) {
			String req = "select * from Etudiant;";
			m1.setText(req);
			if(tfTable.getText().toLowerCase() == "etudiant") {
				currentTable = tables.Etudiant;
			} 
			queryDatabase();
		}
		
		
		else if(cause == this.EDT_etudiant) {
			request_EDT_etudiant();
		}
		
		else if(cause == this.EDT_parcours) {
			request_parcours();
		}
		
		else if(cause == this.EDT_prof_crenaux) {
			request_EDT_prof_crenaux();
		}
		else if (cause == this.TP_occupation_salle) {
			request_TP_occupation_salle();
		}
		else if (cause == TP_moy_occupation_tp) {
			request_TP_moy_occupation_tp();
		}
		else if (cause == EDT_salle) {
			request_EDT_salle();
		}
		else if (cause == HORAIRE_prochainCours) {
			request_HORAIRE_prochainCours();
		}
		else if (cause == ENS_matiere) {
			request_ENS_matiere();
		}
		else if (cause== SALLE_libre) {
			request_SALLE_libre();
		}
		else {
			System.out.println("button : " + cause.toString());
		}
	}


	/**
	 * Set the status text. 
	 * 
	 * @param text The text to set. 
	 */
	private void setStatus(String text){
		mStat.setText("Status: " + text);
	}
	
	private void setCurrentTable(String text) {
		currentTable = tables.valueOf(text);
		tfTable.setText(currentTable + "");
		switch(currentTable) {
		case Etudiant :
			m1.setText("Nom");
			m2.setText("Prénom");
			m3.setText("idEtudiant");
			m4.setText("idParcours");
			m5.setText("");
			break;
		case Enseignant :
			m1.setText("Nom");
			m2.setText("Prénom");
			m3.setText("idEnseignant");
			m4.setText("");
			m5.setText("");
			break;
			
		case Cours :
			m1.setText("Nom matière");
			m2.setText("idEnseignant");
			m3.setText("idSalle");
			m4.setText("idHoraire");
			m5.setText("idCours");			
			break;
			
		case Crenaux : 
			m1.setText("Nom du créneau");
			m2.setText("id");
			m3.setText("");
			m4.setText("");
			m5.setText("");
			break;
			
		case Horaire :
			m1.setText("Jour (sous la forme 2016-31-04)");
			m2.setText("idCreneau");
			m3.setText("idHoraire");
			m4.setText("");
			m5.setText("");
			break;
			
		case Matiere : 
			m1.setText("Nom de la matière");
			m2.setText("idParcours");
			m3.setText("");
			m4.setText("");
			m5.setText("");
			break;
			
		case Parcours :
			m1.setText("Nom");
			m2.setText("idParcours");
			m3.setText("");
			m4.setText("");
			m5.setText("");
			break;
			
		case Salle :
			m1.setText("Nom");
			m2.setText("TP (1 pour oui, 0 pour non)");
			m3.setText("idSalle");
			m4.setText("");
			m5.setText("");
			break;
			
		default : 
		}
		
			
			
	}

	/**
	 * Procedure, where the database connection should be implemented. 
	 */
	private void connectToDatabase(){
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			
			setStatus("Connected to the database");
		} catch(Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();
			setStatus("Connection failed");
		}
	}


	/**
	 * Procedure, where the database connection should be implemented. 
	 */
	private void disconnectFromDatabase(){
		try{
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
			setStatus("Disconnected from the database");
			mRes.setText("");
		} catch(Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();
			setStatus("Disconnection failed");
		}
	}

	/**
	 * Execute a query and display the results. Implement the database querying and the 
	 * display of the results here 
	 */
	private void queryDatabase(){
		String requete = m1.getText();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(requete);
			setStatus("Querying the database");
			ResultSetMetaData rsmd = rs.getMetaData();
			int nbCol = rsmd.getColumnCount();
			for(int i=1; i <= nbCol; i++) {
				mRes.append(rsmd.getColumnName(i) + "	|	");
			}
			mRes.append("\n");
			mRes.append("\n");
			while(rs.next()) {
				
				for(int i=1; i <= nbCol; i++) {
					mRes.append(rs.getString(i) + "	|	");
					
				}
				mRes.append("\n");
				
			}
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mRes.append(e.getMessage());
		}
	}

	/**
	 * Insert tuples to the database. 
	 */
	private void insertDatabase(){
		try{
			String name = m1.getText();
			String prenom = m2.getText();
			String id = m3.getText();
			setStatus("Inserting to the database");
			if(currentTable == tables.Etudiant) {
				insertEtudiant();
				
			} else 
			if(currentTable == tables.Salle) {
				stmt = conn.createStatement();
				String requete = "insert into Salle(idSalle, Nom, TP) values(" + m3.getText() + ", \"" + m1.getText() + "\", " + m2.getText() + ");";
				System.out.println(requete);
				stmt.execute(requete);
				
			}else if(currentTable == tables.Parcours){
				insertParcours();
			}
			else if(currentTable == tables.Matiere){
				insertMatiere();	
			}else if(currentTable == tables.Horaire){
				insertHoraire();	
			}
			else if(currentTable == tables.Enseignant){
				insertEnseignant();	
			}
			else if(currentTable == tables.Crenaux){
				insertCreneaux();	
			}
			else if(currentTable == tables.Cours){
				insertCours();	
			}
			setStatus("Insertion ok");
			
		} catch(Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();
			setStatus("Insertion failed");
		}

	}
	
	private void insertEtudiant() throws Exception {
		String name = m1.getText();
		String prenom = m2.getText();
		String id = m3.getText();
		String idP = m4.getText();
		stmt = conn.createStatement();
		String requete = "insert into Etudiant(nom, prenom, idEtudiant, idParcours) values(\"" + name + "\",\"" + prenom + "\"," + id +", " + idP + ");";
		System.out.println(requete);
		stmt.execute(requete);
		
	}
	
	private void insertParcours() throws Exception {
		String name = m1.getText();
		String id = m2.getText();
		stmt = conn.createStatement();
		String requete = "insert into Parcours(Nom,idParcours) values(\"" + name+ "\"," + id + ");";
		System.out.println(requete);
		stmt.execute(requete);
		
	}
	
	private void insertMatiere() throws Exception {
		String name = m1.getText();
		String id = m2.getText();
		stmt = conn.createStatement();
		String requete = "insert into Matiere(Nom,idParcours) values(\"" + name+ "\"," + id + ");";
		System.out.println(requete);
		stmt.execute(requete);
		
	}
	
	private void insertHoraire() throws Exception {
		String idH = m3.getText();
		String jour = m1.getText();
		String idC = m2.getText();
		stmt = conn.createStatement();
		String requete = "insert into Horaire(idHoraire, Jour, idCrenaux) values(" + idH + ",\"" + jour + "\"," + idC + ");";
		System.out.println(requete);
		stmt.execute(requete);
		
	}
	
	private void insertEnseignant() throws Exception {
		String id = m3.getText();
		String name = m1.getText();
		String prenom = m2.getText();
		stmt = conn.createStatement();
		String requete = "insert into Enseignant(idEnseignant, Nom, Prenom) values(" + id + ",\"" + name+ "\",\"" + prenom + "\");";
		System.out.println(requete);
		stmt.execute(requete);
		
	}
	
	private void insertCreneaux() throws Exception {
		String name = m1.getText();
		String id = m2.getText();
		stmt = conn.createStatement();
		String requete = "insert into Crenaux(Nom,idCrenaux) values(\"" + name + "\"," + id + ");";
		System.out.println(requete);
		stmt.execute(requete);
		
	}
	
	private void insertCours() throws Exception {
		String idC = m5.getText();
		String idH = m4.getText();
		String idS = m3.getText();
		String mat = m1.getText();
		String idEns = m2.getText();
		stmt = conn.createStatement();
		String requete = "insert into Cours(idCours, idHoraire, idSalle, matiere, idEnseignant) values(" + idC + ","+ idH + ","+ idS + ",\""+ mat + "\"," + idEns + ");";
		System.out.println(requete);
		stmt.execute(requete);
		
	}
	
	
	//Les requetes de ROMAIN :)
	
	private void request_EDT_etudiant() {
		String id= m1.getText();
		String date=m2.getText();
		String request = "SELECT Matiere.nom, Horaire.Jour, Crenaux.Nom, Cours.idSalle"
				       + " FROM Etudiant JOIN Matiere on Etudiant.idParcours = Matiere.idParcours join Cours on Cours.matiere = Matiere.nom join Horaire on Cours.idHoraire=Horaire.idHoraire join Crenaux on Crenaux.idCrenaux = Horaire.idCrenaux "
				       + " Where idEtudiant =" + id 
				       + " AND Horaire.jour=\""+ date+"\";";
		m1.setText(request);
		m2.setText("");
		queryDatabase();
		
	}
	
	private void request_parcours() {
		String nom_parcours= m1.getText();
		
		
		String request = "SELECT Matiere.nom, Horaire.Jour, Crenaux.Nom, Cours.idSalle"
				       + " FROM Cours JOIN Matiere on Cours.matiere = Matiere.nom JOIN Parcours on Parcours.idParcours = Matiere.idParcours JOIN Horaire on Cours.idHoraire=Horaire.idHoraire JOIN Crenaux on Crenaux.idCrenaux = Horaire.idCrenaux "
				       + " Where Parcours.Nom = \""+ nom_parcours+"\";";
		m1.setText(request);
		m2.setText("");
		queryDatabase();
		
	}
	
	private void request_TP_occupation_salle() {
		String idSalle=m1.getText();
		String jour=m2.getText();
		
		String request = "SELECT count(idCrenaux)*2 FROM Cours NATURAL JOIN Horaire NATURAL JOIN Crenaux "
				         + " WHERE idSalle="+idSalle+" AND Horaire.Jour=\""+jour+"\";";
		m1.setText(request);
		m2.setText("");
		queryDatabase();
		
	}
    
	private void request_TP_moy_occupation_tp() {
		String request = "SELECT AVG(heures.H) FROM (SELECT count(idCrenaux)*2 as H FROM Cours NATURAL JOIN Horaire NATURAL JOIN Crenaux WHERE Horaire.Jour='2019-04-05' GROUP BY Cours.idSalle) as heures;";
		m1.setText(request);
		queryDatabase();
	}
	
	private void request_EDT_salle() {
		String idSalle=m1.getText();
		String jour=m2.getText();
		String request = "SELECT Horaire.Jour, Crenaux.Nom, Cours.matiere FROM Cours NATURAL JOIN Horaire NATURAL JOIN Crenaux WHERE Cours.idSalle="+idSalle
				         +" AND Horaire.Jour=\""+jour+"\";";
		m1.setText(request);
		m2.setText("");
		queryDatabase();
	}
	
	private void request_EDT_prof_crenaux() {
		String id= m1.getText();
		String jour=m2.getText();
		String request="SELECT  Cours.matiere,Horaire.Jour,Crenaux.Nom FROM Cours NATURAL JOIN Horaire NATURAL JOIN Crenaux WHERE Cours.idEnseignant="
				+id +" AND Horaire.Jour=\""+jour+"\";";
		m1.setText(request);
		m2.setText("");
		queryDatabase();
	}
	
	private void request_HORAIRE_prochainCours() {
		String cours= m1.getText();
		String request ="SELECT MAX(Crenaux.Nom),Horaire.Jour "+ 
				"FROM Cours NATURAL JOIN Crenaux NATURAL JOIN Horaire " + 
				"WHERE Cours.matiere='"+cours+"' ORDER BY Horaire.Jour,Crenaux.Nom;";
		m1.setText(request);
		m2.setText("");
		queryDatabase();
	}
	
	private void request_ENS_matiere() {
		String matiere= m1.getText();
		String request = "SELECT Enseignant.Nom , Enseignant.Prenom FROM Enseignant NATURAL JOIN Cours WHERE Cours.matiere='"+matiere+"';";
		m1.setText(request);
		m2.setText("");
		queryDatabase();
	}
	
	private void request_SALLE_libre() {
		String crenaux = m2.getText();
		String horaire = m1.getText();
		String request = "SELECT Salle.idSalle FROM Salle WHERE idSalle NOT IN(SELECT idSalle FROM Cours NATURAL JOIN Horaire WHERE Horaire.Jour='"+horaire+"' AND Horaire.idCrenaux="+crenaux+");";
		m1.setText(request);
		m2.setText("");
		queryDatabase();
	}

}
