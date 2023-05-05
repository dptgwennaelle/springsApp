public class BooktoAdd extends Book{
    String auteurNom;
    String auteurPrenom;
    String categorie;
    String resume;
    int idAuteur;
    int idEditeur;
    int idCategorie;

    public BooktoAdd(){
    }

    public BooktoAdd(String auteurNom, String auteurPrenom,String edition,String categorie,String resume,int idAuteur, int idEditeur,int idCategorie){
        super();
        this.auteurNom = auteurNom;
        this.auteurPrenom = auteurPrenom;
        this.categorie = categorie;
        this.resume = resume;
        this.idAuteur = idAuteur;
        this.idCategorie = idCategorie;
        this.idEditeur = idEditeur;

    }

    public String getAuteurNom() {
        return auteurNom;
    }

    public void setAuteurNom(String auteurNom) {
        this.auteurNom = auteurNom;
    }

    public String getAuteurPrenom() {
        return auteurPrenom;
    }

    public void setAuteurPrenom(String auteurPrenom) {
        this.auteurPrenom = auteurPrenom;
    }

    @Override
    public String getEdition() {
        return edition;
    }

    @Override
    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
