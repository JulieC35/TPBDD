
public enum tables {
	//Nos objets initialisés
	Cours("Cours"),
	Creneaux("Crenaux"),
	Enseignant("Enseignant"),
	Etudiant("Etudiant"),
	Horaire("Horaire"),
	Matiere("Matiere"),
	Parcours("Parcours"),
	Salle("Salle");
	
	private String m_name;
	
	tables(String name){
		m_name=name;
	}

	public String getString() {
		return m_name;
	}

	public void setname(String name) {
		this.m_name = name;
	}
	
	public static tables getEnum(String table) {
		switch(table) {
		case "Cours" : return Cours;
		case "Crenaux" : return Creneaux;
		case "Enseignant" : return Enseignant;
		case "Etudiant" : return Etudiant;
		case "Horaire"  : return Horaire;
		case "Matiere"  : return Matiere;
		case "Parcours" : return Parcours;
		case "Salle" : return Salle;
		default : return null;
		}
		
	}
}
