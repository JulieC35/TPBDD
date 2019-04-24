
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
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
	TextArea mRes;
	private Button b1, b2, b3, b4;
	JPanel pBut, panStatus, pChampsQuery;
	JMenuBar menuBar = new JMenuBar();
	JMenuBar menuInsertBar = new JMenuBar();
    JMenu menuBase = new JMenu("Base");
    JMenu menu = new JMenu("Requetes");
    JMenu menuInsert = new JMenu("INSERT");
    JMenuItem itemCours, itemCreneaux, itemEnseignant, itemEtudiant, itemHoraire, itemMatiere, itemParcours, itemSalle;
    JMenuItem item1 = new JMenuItem("Liste de tous les étudiants");
    JMenuItem item2 = new JMenuItem("Requete aléatoire");
    JMenuItem itemConnect = new JMenuItem("Connect");
    JMenuItem itemDisconnect = new JMenuItem("Disconnect");
    JMenuItem itemQuery = new JMenuItem("Query");
	
	
	private static final long serialVersionUID = 1L; 
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://mysql.istic.univ-rennes1.fr/base_17006900";
	static final String USER = "user_17006900";
	static final String PASS = "Forest35";
	Connection conn = null;
	Statement stmt = null;
	enum tables {Cours, Creneaux, Enseignant, Etudiant, Horaire, Matiere, Parcours, Salle};
	tables currentTable;


	/**
	 * This procedure is called when the Applet is initialized.
	 * 
	 */
	public void init ()
	{    
		/**
		 * Definition of text fields
		 */
		//m1 = new TextField(80);
		//m1.setText("What are you going to do when the light is:");
		//m1.setEditable(false);
		setSize(1500, 500);
		
		initComponent();
		
		addToApplet();

		currentTable = tables.Etudiant;

		setStatus("Waiting for user actions.");
	}

	protected void initComponent() {
		mStat = new TextField(150);
		mStat.setEditable(false);
		m1 = new TextField(150);
		m2 = new TextField(150);
		m3 = new TextField(150);
		m4 = new TextField(150);
		m5 = new TextField(150);
		tfTable = new TextField(150); 
		mRes = new TextArea(10,150);
		mRes.setEditable(false);
		//mTittle.setEditable(false);
		//mTittle.setText("Application de gestion d'emploi du temps");


		pBut = new JPanel();
		panStatus = new JPanel();
		pChampsQuery = new JPanel();
		/**
		 * First we define the buttons, then we add to the Applet, finally add and ActionListener 
		 * (with a self-reference) to capture the user actions.  
		 */
		b1 = new Button("CONNECT");
		b2 = new Button("DISCONNECT");
		b3 = new Button("QUERY");
		b4 = new Button("INSERT");
		
		itemCours = new JMenuItem("Cours");
		itemCreneaux = new JMenuItem("Creneaux");
		itemEnseignant = new JMenuItem("Enseignant");
		itemEtudiant = new JMenuItem("Etudiant");
		itemHoraire = new JMenuItem("Horaire");
		itemMatiere = new JMenuItem("Matiere");
		itemParcours = new JMenuItem("Parcours");
		itemSalle = new JMenuItem("Salle");
		
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		menuInsert.addActionListener(this);
		//menuInsertBar.addAncestorListener(this);
		itemEtudiant.addActionListener(this);
		item1.addActionListener(this);
		item2.addActionListener(this);
		itemConnect.addActionListener(this);
		itemDisconnect.addActionListener(this);
		itemQuery.addActionListener(this);

		
		m1.setText("Name (e.g. John Smith) - Please enter here!");  //According to the database schema
		m2.setText("Age (e.g. 23)  - Please enter here!"); //According to the database schema
		m3.setText("Color of the eye (e.g. green) - Please enter here!");  //According to the database schema
		tfTable.setText("Table to query (e.g. Etudiant) - Please enter here!"); 
		
		mRes.setText("Query results");
	}
	
	protected void addToApplet() {
		
		setLayout(new BorderLayout());
		
		pBut.setLayout(new BoxLayout(pBut, BoxLayout.PAGE_AXIS));
		pBut.add(b1) ;
		pBut.add(b2) ;
		pBut.add(b3) ;
		//pBut.add(b4);
		
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
	    menuInsertBar.add(menuInsert);
		//pBut.add(menuInsertBar);
		
		//add(pBut, BorderLayout.WEST);
		
		menu.add(item1);
	    menu.addSeparator();
	    menu.add(item2);
	    
	    
	    menuBase.add(itemConnect);
	    menuBase.addSeparator();
	    menuBase.add(itemDisconnect);
	    menuBase.addSeparator();
	    menuBase.add(itemQuery);
	    menuBase.addSeparator();
	    menuBase.add(menuInsert);
	    
	    
	    
		menuBar.add(menuBase);
	    menuBar.add(menu);
	    
	    panStatus.add(menuBar);
		//panStatus.add(mStat);
		add(panStatus, BorderLayout.NORTH);
		//add(new Label("Input fields: ", Label.CENTER));
		
		pChampsQuery.add(mStat);
		pChampsQuery.add(m1);
		pChampsQuery.add(m2);
		pChampsQuery.add(m3);
		pChampsQuery.add(m4);
		pChampsQuery.add(m5);
		pChampsQuery.add(tfTable);
		/*add(m1);
		add(m2);
		add(m3);*/
		
		pChampsQuery.add(mRes);
		
		/*add(new Label("Query results: ", Label.CENTER));
		add(mRes);*/
		
		add(pChampsQuery);//, BorderLayout.CENTER);
		
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
		if (cause == b1 || cause == itemConnect)
		{
			connectToDatabase();
		} else

		// Button DISCONNECT
		if (cause == b2 || cause == itemDisconnect)
		{
			disconnectFromDatabase();
		} else

		//Button QUERY
		if (cause == b3 || cause == itemQuery)
		{
			queryDatabase();
		} else

		//Button INSERT
		if (cause == menuInsertBar || cause == menuInsert)
		{
			insertDatabase();
		} else 
			
		if(cause == itemEtudiant) {
			m2.setText("Prenom");
			m3.setText("idEtudiant");
			currentTable = tables.Etudiant;
		}
		
		
		
		else 
			
		//Button Lister les étudiants
		if (cause == item1) {
			String req = "select * from " + currentTable + ";";
			m1.setText(req);
			if(tfTable.getText().toLowerCase() == "etudiant") {
				currentTable = tables.Etudiant;
			} 
			queryDatabase();
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
			while(rs.next()) {
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				int idEns = rs.getInt("idEtudiant");
				mRes.append("nom: " + nom + "			|	");
				mRes.append("prénom : " + prenom + "			|	");
				mRes.append("id : " + idEns + "\n");
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
			
			if(currentTable == tables.Etudiant) {
				stmt = conn.createStatement();
				String requete = "insert into etudiant(nom, prenom, idEtudiant) values(\"" + name + "\",\"" + prenom + "\"," + id +");";
				System.out.println(requete);
				stmt.execute(requete);
			} else {
				stmt = conn.createStatement();
				String requete = "insert into personne(age, nom, couleuryeux) values(" + prenom + ",\"" + name + "\",\"" + id +"\");";
				System.out.println(requete);
				stmt.execute(requete);
			}
			
			setStatus("Inserting to the database");
			
		} catch(Exception e){
			System.err.println(e.getMessage());
			setStatus("Insertion failed");
		}

	}


}
