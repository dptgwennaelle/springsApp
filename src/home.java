import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class home extends JDialog {
    private JPanel home;
    private JButton btnBook;
    private JButton btnClose;
    public User utilisateur;

    public home(JFrame parent, User utilisateur) {
        super(parent);
        this.utilisateur = utilisateur;
        // titre de la fenêtre
        setTitle("Accueil");
        // définit le composant d'affichage à utiliser comme contenu de cette fenêtre,
        //ici, c'est le composant "loginPanel"
        setContentPane(home);
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
        btnBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // si info correctes, on ferme le formulaire
                bookLister bookLister = new bookLister(null, utilisateur);
                dispose();
            }
        });
        setVisible(true);
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }


}