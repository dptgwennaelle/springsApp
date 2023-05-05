import de.mkammerer.argon2.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class App extends JDialog {
    // Composants de l'interface graphique de la fenêtre, notamment
    // zones de texte (pour identifiant et mot de passe) et boutons
    // cliquables (pour confirmer ou annuler/fermer le formulaire)
    private JTextField tfIdentifiant;
    private JPasswordField pfPassword;
    private JButton btnSeConnecter;
    private JPanel loginJPanel;
    private Connexion connexion;

    // Constructeur de la fenêtre
    // Rentrer (null) si la fenêtre générée est une fenêtre autonome,
    // ne s'ouvrant pas par dessus une fenêtre parent
    public App(JFrame parent) {
        // Appel le constructeur de la classe parent (ici, JDialog) en lui
        //passant (parent) en argument
        super(parent);
        // titre de la fenêtre
        setTitle("Bibliothèque collaborative : Connexion ");
        // définit le composant d'affichage à utiliser comme contenu de cette fenêtre,
        //ici, c'est le composant "loginPanel"
        setContentPane(loginJPanel);
        // taille minimale
        setMinimumSize(new Dimension(600, 600));
        // défini la fenêtre en mode modal (pour empêcher l'utilisateur
        // d'interagir avec l'application tant qu'il n'est pas connecté)
        setModal(true);
        // position par rapport à la fenêtre parente
        setLocationRelativeTo(parent);
        // action à réaliser lorsqu'on ferme la fenêtre
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Actions à réaliser lorsque l'utilisateur appui sur le bouton connexion
        btnSeConnecter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // récupération de l'identifiant et du mot de passe
                String identifiant = tfIdentifiant.getText();
                String password = String.valueOf(pfPassword.getPassword());

                // appelle de la méthode getAuthenticatedUser pour vérifier si les informations sont correctes
                utilisateur = getAuthenticatedUser(identifiant, password);

                // si info correctes, on ferme le formulaire
                if (utilisateur != null) {
                    home home = new home(null, utilisateur);
                    dispose();
                    // si infos incorrectes, on affiche un message d'erreur
                } else {
                    JOptionPane.showMessageDialog(App.this,
                            "Identifiant ou mot de passe incorrect",
                            "Réessayez",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Rend le formulaire visible
        setVisible(true);

    }

    // Variable user définie permettant de stocker les informations de l'utilisateur connecté
    public User utilisateur;

    // Méthode prenant en entrée identifiant et mot de passe, si existe renvoi un user,
    // sinon renvoi null
    private User getAuthenticatedUser(String identifiant, String password) {

        User user = null;
        connexion = new Connexion();
        Argon2 argon = Argon2Factory.create();
        try {
            String sql = "select MotPasse, Pseudo from utilisateur where Pseudo = ?;";
            ResultSet resultSet = connexion.executeQuery(sql, identifiant);

            if (resultSet.next()) {
                String passBDD = resultSet.getString("MotPasse");
                if(argon.verify(passBDD,password.toCharArray())){
                user = new User();
                user.identifiant = resultSet.getString("Pseudo");
                user.password = resultSet.getString("MotPasse");
            }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;

    }



    public static void main(String[] args) {

        App app = new App(null);
        User user = app.utilisateur;
        if (user != null) {
            System.out.println("Connexion réussie à : " + user.identifiant);
        } else {
            System.out.println("Échec de connexion ");
        }
    }}