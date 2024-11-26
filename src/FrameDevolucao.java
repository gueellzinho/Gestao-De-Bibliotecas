import javax.swing.*;
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
    private static JTextField txtIdLeitor, txtCodLivro, txtExemplar;
    private static int idBiblioteca;
    private static ResultSet dadosDoSelect;
    private static String data;     //MUDA ISSO AQUI
    private static String idExemplar;

    public FrameDevolucao(Connection dados, int idBib){
        setTitle("Manutenção de Devolução");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());

        btnAlterar   = new JButton("Alterar");
        data = "2024-11-27";
        idBiblioteca = idBib;

        pnlConteudo = new JPanel();
        txtIdLeitor = new JTextField();
        txtCodLivro = new JTextField();
        txtExemplar  = new JTextField();

        cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());

        pnlConteudo.add(new JLabel("ID Leitor"));
        pnlConteudo.add(new JLabel("Cod Livro"));
        pnlConteudo.add(new JLabel("Núm. Exemplar"));
        pnlConteudo.add(new JLabel(""));
        pnlConteudo.add(txtIdLeitor);
        pnlConteudo.add(txtCodLivro);
        pnlConteudo.add(txtExemplar);
        pnlConteudo.add(btnAlterar);
        pnlConteudo.setLayout(new GridLayout(2, 4));
        cntForm.add(pnlConteudo, BorderLayout.CENTER);

        conexaoDados = dados;

        btnAlterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (existeExemplar()){
                    String sql = "update SisBib.Emprestimo set devolucaoEfetiva = ? where idExemplar = ?";
                    try{
                        PreparedStatement dados = conexaoDados.prepareStatement(sql);
                        dados.setDate(1, Date.valueOf(data));
                        dados.setInt(2, Integer.parseInt(idExemplar));
                        dados.execute();
                        JOptionPane.showMessageDialog(null, "Empréstimo alterado!");
                    }
                    catch(SQLException exception){
                        exception.printStackTrace();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Exemplar não existe na Biblioteca escolhida");
                }

            }
        });
    }

    public boolean existeExemplar(){
        String sql = "select idExemplar from SisBib.Exemplar where idBiblioteca = " + idBiblioteca +
                " and codLivro = '" + txtCodLivro.getText() +
                "' and numeroExemplar = " + txtExemplar.getText();
        try {
            Statement comandoSQL = conexaoDados.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            try{
                dadosDoSelect = comandoSQL.executeQuery(sql);
                if(dadosDoSelect.next()){
                    idExemplar = dadosDoSelect.getString("idExemplar");
                    return true;
                }
                else
                    return false;
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
