import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class FrameBib extends JFrame {
    private static  JToolBar tbNavegacao;
    private static JTextField txtServidor, txtBD, txtUser, txtBib;
    private static JPasswordField txtPassword;
    //private static JDateChooser calCalendario;
    private static JButton btnLivros, btnExemplares, btnEmprestimos, btnDevolucoes, btnConnect;
    private static JComboBox cbxBiblioteca;
    private static JTable tabLivros;
    static private Connection conexaoDados = null;
    private static ResultSet dadosDoSelect;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrameBib form = new FrameBib();
                form.setLocationRelativeTo(null);
                // Adaptador para o fechamento da janela, matando o processo
                form.addWindowListener(
                        new WindowAdapter()
                        {
                            public void windowClosing (WindowEvent e)
                            {
                                try {
                                    conexaoDados.close();
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                                System.exit(0);
                            }
                        }
                );

                form.pack();
                form.setVisible(true);
            }
        });
    }

    public FrameBib(){
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

        btnConnect = new JButton("Conectar");
        btnConnect.setPreferredSize(new Dimension(90,30));

        tbNavegacao = new JToolBar();
        tbNavegacao.setLayout(new FlowLayout());
        tbNavegacao.setRollover(true);
        tbNavegacao.add(btnLivros);
        tbNavegacao.add(btnExemplares);
        tbNavegacao.add(btnEmprestimos);
        tbNavegacao.add(btnDevolucoes);
        tbNavegacao.setVisible(false);
        cbxBiblioteca = new JComboBox();

        JPanel pnlCampos = new JPanel();
        pnlCampos.setLayout(new GridLayout(5, 2));
        pnlCampos.setPreferredSize(new Dimension(410,150));
        txtServidor = new JTextField();
        txtBD = new JTextField();
        txtUser  = new JTextField();
        txtPassword  = new JPasswordField();
        txtBib = new JTextField();

        pnlCampos.add(new JLabel("Servidor:"));			// 1, 1
        pnlCampos.add(txtServidor);					// 1, 2
        pnlCampos.add(new JLabel("Banco de Dados:"));				// 2, 1
        pnlCampos.add(txtBD);					// 2, 2
        pnlCampos.add(new JLabel("Usuário:"));		// 3, 1
        pnlCampos.add(txtUser);					// 3, 2
        pnlCampos.add(new JLabel("Senha:"));		// 4, 1
        pnlCampos.add(txtPassword);
        pnlCampos.add(new JLabel(""));
        pnlCampos.add(btnConnect);


        JPanel pnlBib = new JPanel();
        pnlBib.setLayout(new GridLayout(1, 2));
        pnlBib.add(new JLabel("Biblioteca:"));
        pnlBib.add(cbxBiblioteca);
        pnlBib.setVisible(false);

        Container cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());
        cntForm.add(tbNavegacao , BorderLayout.NORTH);
        cntForm.add(pnlCampos , BorderLayout.WEST);
        cntForm.add(pnlBib , BorderLayout.PAGE_END);

        btnConnect.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String URL = txtServidor.getText();
                            String User = txtUser.getText();
                            String BD = txtBD.getText();
                            String Password = txtPassword.getText();
                            conexaoDados = ConexaoBD.getConnection(URL,User,BD,Password);
                            pnlCampos.setVisible(false);
                            tbNavegacao.setVisible(true);
                            pnlBib.setVisible(true);
                            preencherDados();
                        }
                        catch (SQLException ex){
                            System.out.println(ex.getMessage());
                        }
                    }
                }
        );

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
                                    String[] colunas = {"Código", "Título", "ID Autor", "ID Área"};
                                    tabLivros = new JTable();
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
            String txtIdBib = dadosDoSelect.getString("idBiblioteca");
            String txtNomeBib = dadosDoSelect.getString("nome");
            txtBib.setText(txtIdBib + " - "+ txtNomeBib);
            cbxBiblioteca.addItem(txtBib.getText());
        }
    }

    private static void exibirLivros() throws SQLException{
        if(!dadosDoSelect.rowDeleted()){
            String txtCodLivro = dadosDoSelect.getString("codLivro");
            String txtTitulo = dadosDoSelect.getString("titulo");
            String idAutor = dadosDoSelect.getString("idAutor");
            String idArea = dadosDoSelect.getString("idArea");
        }
    }

}
