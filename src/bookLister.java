import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class bookLister extends JDialog {
    private JComboBox<Book> comboBox1;
    private JButton modifierButton;
    private JButton ajouterButton;
    private JPanel bookListerField;
    private JButton supprimerButton;
    private JLabel vosLivres;
    User utilisateur;
    private ArrayList<Book> collectionBook;

    public bookLister(JFrame parent, User utilisateur){
        super();
        this.utilisateur = utilisateur;
        // titre de la fenêtre
        setTitle("Accueil");
        // définit le composant d'affichage à utiliser comme contenu de cette fenêtre,
        //ici, c'est le composant "loginPanel"
        setContentPane(bookListerField);
        // taille minimale
        setMinimumSize(new Dimension(600, 600));
        // défini la fenêtre en mode modal (pour empêcher l'utilisateur
        // d'interagir avec l'application tant qu'il n'est pas connecté)
        setModal(true);
        // position par rapport à la fenêtre parente
        setLocationRelativeTo(parent);
        // action à réaliser lorsqu'on ferme la fenêtre
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // création d'un modèle de ComboBox pour les livres
        DefaultComboBoxModel<Book> comboBoxModel = new DefaultComboBoxModel<>();

        // appel à la méthode initBooks pour remplir le modèle de ComboBox avec les livres
        initBooks(utilisateur, comboBoxModel);

        // affectation du modèle de ComboBox à la ComboBox1
        comboBox1.setModel(comboBoxModel);

        modifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer le livre sélectionné dans la ComboBox1
                Book selectedBook = comboBox1.getItemAt(comboBox1.getSelectedIndex());

                // Créer une nouvelle JDialog pour la modification du livre
                JDialog modifyDialog = new JDialog();
                modifyDialog.setTitle("Modifier le livre");
                modifyDialog.setSize(new Dimension(400, 300));
                modifyDialog.setModal(true);
                modifyDialog.setLocationRelativeTo(bookListerField);

                // Créer des champs textuels pour chaque information du livre
                JTextField titreField = new JTextField(selectedBook.nom);
                JTextField isbnField = new JTextField(selectedBook.ISBN);
                JTextField pageField = new JTextField(Integer.toString(selectedBook.page));

                // Créer un bouton "Confirmer" pour mettre à jour le livre
                JButton confirmerButton = new JButton("Confirmer");
                confirmerButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Mettre à jour le livre dans la base de données
                        Connexion connexion = new Connexion();
                        try {
                            String sql = "UPDATE exemplaire SET ISBN = ?, NombrePage = ? WHERE Donateur = ? AND IdLivre = (SELECT IdLivre FROM oeuvre WHERE NomLivre = ?)";
                            String[] params = {isbnField.getText(), pageField.getText(), utilisateur.getIdentifiant(), titreField.getText()};
                            connexion.executeUpdate(sql, params);

                            // Mettre à jour le livre dans la ComboBox1
                            selectedBook.nom = titreField.getText();
                            selectedBook.ISBN = isbnField.getText();
                            selectedBook.page = Integer.parseInt(pageField.getText());
                            comboBox1.repaint();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        // Fermer la JDialog de modification
                        modifyDialog.dispose();
                    }
                });

                // Ajouter les champs textuels et le bouton "Confirmer" à la JDialog de modification
                JPanel modifyPanel = new JPanel();
                modifyPanel.setLayout(new GridLayout(5, 2, 10, 10));
                modifyPanel.add(new JLabel("Titre :"));
                modifyPanel.add(titreField);
                modifyPanel.add(new JLabel("ISBN :"));
                modifyPanel.add(isbnField);
                modifyPanel.add(new JLabel("Nombre de pages :"));
                modifyPanel.add(pageField);
                modifyPanel.add(new JLabel(""));
                modifyPanel.add(confirmerButton);
                modifyDialog.setContentPane(modifyPanel);
                modifyPanel.setBorder(BorderFactory.createEmptyBorder(50,10,10,10));

                // Afficher la JDialog de modification
                modifyDialog.setVisible(true);
            }
        });


        ajouterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBook addBook = new addBook(null, utilisateur);
            }
        });

        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            // Récupère le livre sélectionné dans la ComboBox
                Book bookToDelete = (Book) comboBox1.getSelectedItem();

                // Supprime le livre de la base de données
                deleteBook(bookToDelete);

                // Supprime le livre de la ComboBox
                comboBox1.removeItem(bookToDelete);
            }
        });
        GridLayout layout = new GridLayout(5, 1, 0, 10);
        bookListerField.setLayout(layout);
        // on ajoute les composants à la fenêtre
        bookListerField.add(vosLivres);
        bookListerField.add(comboBox1);
        bookListerField.add(modifierButton);
        bookListerField.add(ajouterButton);
        bookListerField.add(supprimerButton);
        // Ajout de la marge autour de la fenêtre
        bookListerField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // affichage de la fenêtre
        setVisible(true);
    }

    public void initBooks(User utilisateur, DefaultComboBoxModel<Book> comboBoxModel) {
        Connexion connexion = new Connexion();
        Book book;
        String pseudo = utilisateur.getIdentifiant();
        try {
            String sql = "select exemplaire.ISBN, exemplaire.NombrePage, oeuvre.NomLivre, editeur.Nom " +
                    "from exemplaire, oeuvre, editeur where editeur.IdEditeur = exemplaire.IdEditeur and " +
                    "exemplaire.IdLivre = oeuvre.IdLivre and Donateur = ?";
            ResultSet resultSet = connexion.executeQuery(sql, pseudo);
            while (resultSet.next()) {
                book = new Book();
                book.nom = resultSet.getString("NomLivre");
                book.edition = resultSet.getString("Nom");
                book.ISBN = resultSet.getString("ISBN");
                book.page = resultSet.getInt("NombrePage");
                comboBoxModel.addElement(book); // ajout du livre à la ComboBoxModel
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteBook(Book bookToDelete) {
        Connexion connexion = new Connexion();
        String sql = "delete from exemplaire where ISBN = ?";
        try {
            connexion.executeUpdate(sql, bookToDelete.getISBN());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
