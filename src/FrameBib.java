import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class FrameBib extends JFrame {
    private static  JToolBar tbNavegacao;
    //private static JDateChooser calCalendario;
    private static JTextField txtBib;
    private static JButton btnLivros, btnExemplares, btnEmprestimos, btnDevolucoes, btnConnect;
    private static JComboBox cbxBiblioteca;
    private static JTable tabLivros;
    static private Connection conexaoDados;
    private static ResultSet dadosDoSelect;
    private static Container cntForm;

    public FrameBib(Connection cnxDados){
        setTitle("Sistema de Biblioteca");
        setSize(1000, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        btnLivros = new JButton("Livros");
        btnLivros.setPreferredSize(new Dimension(90,30));
        btnLivros.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnLivros.setHorizontalTextPosition(SwingConstants.LEFT);
        btnLivros.setFocusPainted(false);
        btnLivros.setBorderPainted(false);

        btnExemplares = new JButton("Exemplares");
        btnExemplares.setPreferredSize(new Dimension(90,30));
        btnExemplares.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnExemplares.setHorizontalTextPosition(SwingConstants.LEFT);
        btnExemplares.setFocusPainted(false);
        btnExemplares.setBorderPainted(false);

        btnEmprestimos = new JButton("Empréstimos");
        btnEmprestimos.setPreferredSize(new Dimension(90,30));
        btnEmprestimos.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnEmprestimos.setHorizontalTextPosition(SwingConstants.LEFT);
        btnEmprestimos.setFocusPainted(false);
        btnEmprestimos.setBorderPainted(false);

        btnDevolucoes = new JButton("Devoluções");
        btnDevolucoes.setPreferredSize(new Dimension(90,30));
        btnDevolucoes.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnDevolucoes.setHorizontalTextPosition(SwingConstants.LEFT);
        btnDevolucoes.setFocusPainted(false);
        btnDevolucoes.setBorderPainted(false);

        tbNavegacao = new JToolBar();
        tbNavegacao.setLayout(new FlowLayout());
        tbNavegacao.setRollover(true);
        tbNavegacao.add(btnLivros);
        tbNavegacao.add(btnExemplares);
        tbNavegacao.add(btnEmprestimos);
        tbNavegacao.add(btnDevolucoes);
        cbxBiblioteca = new JComboBox();


        JPanel pnlBib = new JPanel();
        pnlBib.setLayout(new GridLayout(1, 2));
        pnlBib.add(new JLabel("Biblioteca:"));
        pnlBib.add(cbxBiblioteca);

        cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());
        cntForm.add(tbNavegacao , BorderLayout.NORTH);
        cntForm.add(pnlBib , BorderLayout.PAGE_END);

        conexaoDados = cnxDados;
        preencherDados();

        btnLivros.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String sql = "select * from SisBib.Livro order by codLivro";
                        try{
                            Statement comandoSQL = conexaoDados.createStatement(
                                    ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_UPDATABLE
                            );
                            try{
                                dadosDoSelect = comandoSQL.executeQuery(sql);
                                if(dadosDoSelect != null){
                                    while(dadosDoSelect.next()){
                                        exibirLivros();
                                    }
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Registro não encontrado!");
                                }
                            }
                            catch(SQLException exception){
                                exception.printStackTrace();
                            }
                        }
                        catch(SQLException exception){
                            exception.printStackTrace();
                        }
                    }
                }
        );
    }

    private static void preencherDados() {
        String sql = "SELECT * FROM SisBib.Biblioteca order by idBiblioteca";
        try {
            Statement comandoSQL = conexaoDados.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,	// permite navegação
                    ResultSet.CONCUR_UPDATABLE        // ResultSet é atualizável
            );
            try {
                dadosDoSelect = comandoSQL.executeQuery(sql);
                if (dadosDoSelect != null)
                    while (dadosDoSelect.next()) {
                        exibirBibilotecas();
                } else {
                    JOptionPane.showMessageDialog(null, "Registro não encontrado!");
                }
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    static private void exibirBibilotecas() throws SQLException
    {
        if (!dadosDoSelect.rowDeleted())
        {
            txtBib = new JTextField();
            String txtIdBib = dadosDoSelect.getString("idBiblioteca");
            String txtNomeBib = dadosDoSelect.getString("nome");
            txtBib.setText(txtIdBib + " - "+ txtNomeBib);
            cbxBiblioteca.addItem(txtBib.getText());
        }
    }

    private static void exibirLivros() throws SQLException {
        String[] colunas = {"Código", "Título", "ID Autor", "ID Área"};
        Object[][] dadosColunas = new Object[0][0];

        if (!dadosDoSelect.rowDeleted()) {
            String txtCodLivro = dadosDoSelect.getString("codLivro");
            String txtTitulo = dadosDoSelect.getString("titulo");
            String idAutor = dadosDoSelect.getString("idAutor");
            String idArea = dadosDoSelect.getString("idArea");
            dadosColunas = new Object[][]{{txtCodLivro, txtTitulo, idAutor, idArea}};
        }
        tabLivros = new JTable(dadosColunas, colunas);
        cntForm.add(tabLivros, BorderLayout.CENTER);
    }
}
