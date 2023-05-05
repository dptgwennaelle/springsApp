import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class addBook extends JDialog {
    User utilisateur = new User();
    private JTextField tfTitre;
    private JTextField tfNom;
    private JTextField tfEdition;
    private JTextField tfISBN;
    private JTextField tfCategorie;
    private JTextField tfNombrePage;
    private JTextField tfResume;
    private JButton validerButton;
    private JPanel addBookPanel;
    private JTextField tfPrenom;
    BooktoAdd newBook;
    public addBook(JFrame parent, User utilisateur){
        super();
        this.utilisateur = utilisateur;
        // titre de la fenêtre
        setTitle("Accueil");
        // définit le composant d'affichage à utiliser comme contenu de cette fenêtre,
        //ici, c'est le composant "loginPanel"
        setContentPane(addBookPanel);
        // taille minimale
        setMinimumSize(new Dimension(600, 600));
        // défini la fenêtre en mode modal (pour empêcher l'utilisateur
        // d'interagir avec l'application tant qu'il n'est pas connecté)
        setModal(true);
        // position par rapport à la fenêtre parente
        setLocationRelativeTo(parent);
        // action à réaliser lorsqu'on ferme la fenêtre
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        validerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newBook = addBook();
                bookLister bookLister = new bookLister(null, utilisateur);

                dispose();
            }
        });
        setVisible(true);
    }

    public addBook newbook;

    public BooktoAdd addBook() {
        // nouveau livre pour le moment vide
        newBook = null;
        // nouvelle connexion à la base de données
        Connexion connexion = new Connexion();
        // instanciation d'une variable booléenne permettant d'afficher ou non
        // une erreur due à l'écriture des infos par l'utilisateur
        boolean error = false;

        // si le champ de texte ISBN n'est pas vide
        if (tfISBN.getText().length()!=0){
            // récupère la valeur rentrée et intègre celle-ci dans la variable ISBN
            String ISBN = tfISBN.getText();
            try {
                // préparation de la requête SQL
                String sql = "select ISBN from exemplaire where ISBN = ?;";
                // envoi de la requête et de son paramètre
                ResultSet resultSet = connexion.executeQuery(sql, ISBN);
                // s'il y a bien un résultat en retour de cette requête
                if (!resultSet.next()) {
                    // utilise la donnée rentrée par l'utilisateur comme attribut
                    // ISBN de l'objet newBook instancié précédemment
                    newBook.ISBN = resultSet.getString("ISBN");
                }
                // s'il n'y pas de résultat, et donc que la requête ne revoit rien
                else {
                    // renvoi l'erreur associée au livre déjà existant
                    JOptionPane.showMessageDialog(addBook.this,
                            "Erreur d'ajout",
                            "Ce livre est déjà disponible à l'emprunt",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }// si le champ ISBN est vide
        else {
            String ISBN = null;
            error = true;
        }

        // vérification du nom et du prénom de l'auteur
        if (tfNom.getText().length()!=0){
            if (tfPrenom.getText().length()!=0){
                String auteurNom = tfNom.getText();
                String auteurPrenom = tfPrenom.getText();
                try {
                    // préparation de la requête SQL
                    String sql = "select into auteur (Nom,Prenom) values (?,?);";
                    // envoi de la requête et de ses paramètres
                    ResultSet resultSet = connexion.executeQuery(sql, auteurNom, auteurPrenom);

                    // s'il n'y pas de résultat, et donc que la requête ne revoit rien
                    if (!resultSet.next()) {
                        try {
                            // ajoute le nouvel auteur dans la base de données
                        String sql2 = "insert into auteur (Nom,Prenom) values (?,?);";
                        // envoi de la requête et de ses paramètres
                        int resultSet2 = connexion.executeUpdate(sql, auteurNom, auteurPrenom);}
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    newBook.auteurNom = resultSet.getString("Nom");
                    newBook.auteurPrenom = resultSet.getString("Prenom");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String sql = "select IdAuteur from auteur where nom = ? and prenom = ?;";
                    ResultSet resultSet = connexion.executeQuery(sql, auteurNom, auteurPrenom);
                    if (!resultSet.next()) {
                        // utilise la donnée rentrée par l'utilisateur comme attribut
                        // ISBN de l'objet newBook instancié précédemment
                        newBook.idAuteur = resultSet.getInt("IdAuteur");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }else{
                String auteurNom = null;
                error = true;}
        }else{
            String auteurPrenom = null;
            error = true;}


        //gestion de la catégorie
        if (tfCategorie.getText().length()!=0){
            String categorie = tfCategorie.getText();
            try {
                boolean boucle = false;
                while(boucle == false){
                // préparation de la requête SQL
                String sql = "select IdCategorie where NomCategorie = ?;";
                // envoi de la requête et de ses paramètres
                ResultSet resultSet = connexion.executeQuery(sql, categorie);

                // s'il n'y pas de résultat, et donc que la requête ne revoit rien
                if (!resultSet.next()) {
                    try {
                        // ajoute le nouvel auteur dans la base de données
                        String sql2 = "insert into categorie (NomCategorie) values (?);";
                        // envoi de la requête et de ses paramètres
                        int resultSet2 = connexion.executeUpdate(sql, categorie);}
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    newBook.idCategorie = resultSet.getInt("IdCategorie");
                    newBook.categorie = categorie;
                    boucle=true;
                }}

        }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            String categorie = null;
            error = true;}

        /*String edition = tfEdition.getText();
        String titre = tfTitre.getText();

        String nombre = tfNombrePage.getText();
        int nombrePage = Integer.parseInt(nombre);
        String resume = tfResume.getText();*/

    if (error== true){
    JOptionPane.showMessageDialog(addBook.this,
            "Merci de renseigner tous les champs obligatoires",
            "Erreur de formulaire",
            JOptionPane.ERROR_MESSAGE);
    }
        return newBook;
}
}

