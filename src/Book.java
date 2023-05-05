public class Book {
    protected String nom;
    protected String edition;
    protected String ISBN;
    protected int page;

    public Book() {
        // Constructeur par défaut
    }

    public Book(String nom, String edition, String ISBN, int page) {
        this.nom = nom;
        this.edition = edition;
        this.ISBN = ISBN;
        this.page = page;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "Titre : " + nom + ", Editeur : " + edition + ", ISBN : " + ISBN + ", Pages : " + page;
    }
}