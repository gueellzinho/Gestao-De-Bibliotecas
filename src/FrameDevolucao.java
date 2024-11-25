import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static java.lang.System.out;

public class FrameDevolucao extends JFrame {
    private static Connection conexaoDados;
    private static JButton btnAlterar;
    private static JPanel pnlConteudo;
    private static Container cntForm;
    private static JLabel lbIdLeitor, lbIdLivro, lbExemplar;
    private static JTextField txtIdLeitor, txtIdLivro, txtExemplar;
    private static int idBiblioteca;
    private static ResultSet dadosDoSelect;

    public FrameDevolucao(Connection dados, int idBib){
        setTitle("Manutenção de Devolução");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());

        btnAlterar   = new JButton("Alterar");
        idBiblioteca = idBib;

        pnlConteudo = new JPanel();
        lbIdLeitor  = new JLabel("Id Leitor");
        lbIdLivro    = new JLabel("Id Livro");
        lbExemplar   = new JLabel("Núm. Exemplar");
        txtIdLeitor = new JTextField();
        txtIdLivro   = new JTextField();
        txtExemplar  = new JTextField();

        cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());

        pnlConteudo.add(lbIdLeitor);
        pnlConteudo.add(lbIdLivro);
        pnlConteudo.add(lbExemplar);
        pnlConteudo.add(txtIdLeitor);
        pnlConteudo.add(txtIdLivro);
        pnlConteudo.add(txtExemplar);
        pnlConteudo.setLayout(new GridLayout(2, 4));
        cntForm.add(pnlConteudo, BorderLayout.CENTER);

        conexaoDados = dados;

        btnAlterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (existeExemplar()){

                }
                else {
                    out.println("Exemplar não existe na Biblioteca escolhida");
                }

            }
        });
    }

    public boolean existeExemplar(){
        String sql = "Select * from Sisbib.exemplar where idexemplar = " + txtExemplar.getText();
        try {
            Statement comandoSQL = conexaoDados.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            try {
                dadosDoSelect = comandoSQL.executeQuery(sql);
                return true;
            }
            catch(SQLException exception){
                exception.printStackTrace();
                return false;
            }
        }
        catch(SQLException exception){
            exception.printStackTrace();
            return false;
        }

    }
}
