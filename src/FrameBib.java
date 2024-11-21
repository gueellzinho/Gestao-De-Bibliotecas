import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.lang.System;

public class FrameBib extends JFrame {
    private static  JMenuBar menuNavegacao;
    //private static JDateChooser calCalendario;
    private static JTextField txtBib, txtCodLivro, txtTitulo, txtIdAutor, txtIdArea;
    private static JButton btnLivros, btnExemplares, btnEmprestimos, btnDevolucoes,
                           btnAnterior, btnProximo, btnBusca, btnIncluir, btnExcluir, btnAlterar;
    private static JComboBox cbxBiblioteca;
    private static JTable tabLivros;
    private static JPanel pnlLivros, pnlNavegacao, pnlConteudo;
    private static JLabel lbCodLivro, lbTitulo, lbIdAutor, lbIdArea;
    static private Connection conexaoDados;
    private static ResultSet dadosDoSelect;
    private static Container cntForm;

    public FrameBib(Connection cnxDados){
        setTitle("Sistema de Biblioteca");
        setSize(600, 300);
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

        menuNavegacao = new JMenuBar();
        menuNavegacao.add(btnLivros);
        menuNavegacao.add(btnExemplares);
        menuNavegacao.add(btnEmprestimos);
        menuNavegacao.add(btnDevolucoes);
        cbxBiblioteca = new JComboBox();


        JPanel pnlBib = new JPanel();
        pnlBib.setLayout(new GridLayout(1, 2));
        pnlBib.add(new JLabel("Biblioteca:"));
        pnlBib.add(cbxBiblioteca);

        cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());
        cntForm.add(menuNavegacao, BorderLayout.NORTH);
        cntForm.add(pnlBib , BorderLayout.PAGE_END);

        pnlLivros = new JPanel();
        lbCodLivro = new JLabel("Código Livro");
        lbTitulo = new JLabel("Título");
        lbIdAutor = new JLabel("ID Autor");
        lbIdArea = new JLabel("ID Área");
        txtCodLivro = new JTextField();
        txtTitulo = new JTextField();
        txtIdAutor = new JTextField();
        txtIdArea = new JTextField();

        pnlNavegacao = new JPanel();
        btnAnterior = new JButton("Anterior");
        btnProximo = new JButton("Próximo");
        btnBusca = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnIncluir = new JButton("Incluir");
        btnExcluir = new JButton("Excluir");

        pnlConteudo = new JPanel();
        pnlConteudo.setLayout(new GridLayout(2, 1));

        //String comboBox = String.valueOf(cbxBiblioteca.getSelectedItem());

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
                                if(dadosDoSelect != null && dadosDoSelect.next()){

                                    escrevePnl();
                                    exibirLivros();
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

        btnAnterior.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (dadosDoSelect.previous()) {
                                exibirLivros();
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Não achou Registro anterior!");
                            }
                        }
                        catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        );

        btnProximo.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (dadosDoSelect.next()) {
                                exibirLivros();
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Não achou próximo registro!");
                            }
                        }
                        catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        );
        btnBusca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    int posicaoAnterior = dadosDoSelect.getRow();
                    String chaveProcurada = txtCodLivro.getText();
                    dadosDoSelect.beforeFirst();
                    boolean achou = false;
                    while (! achou && dadosDoSelect.next())
                    {
                        if (dadosDoSelect.getString("codLivro").equals(chaveProcurada))
                            achou = true;
                    }
                    if (!achou)
                    {
                        JOptionPane.showMessageDialog(null, "Registro não encontrado!");
                        dadosDoSelect.absolute(posicaoAnterior);
                    }
                    exibirLivros();
                }
                catch (SQLException exception)
                {
                    throw new RuntimeException(exception);
                }
            }
        });
        btnIncluir.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            dadosDoSelect.moveToInsertRow();
                            dadosDoSelect.updateString("codLivro", txtCodLivro.getText());
                            dadosDoSelect.updateString("titulo", txtTitulo.getText());
                            dadosDoSelect.updateString("idAutor", txtIdAutor.getText());
                            dadosDoSelect.updateString("idArea", txtIdArea.getText());
                            dadosDoSelect.insertRow();
                            JOptionPane.showMessageDialog(null, "Inclusão bem sucedida!");
                        }
                        catch (SQLException ex)
                        {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
        );
        btnExcluir.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            if (JOptionPane.showConfirmDialog(
                                    null, "Deseja realmente excluir?") ==
                                    JOptionPane.OK_OPTION)
                            {
                                dadosDoSelect.deleteRow();
                                JOptionPane.showMessageDialog(null, "Exclusão bem sucedida!");
                                exibirLivros();
                            }
                        }
                        catch (SQLException ex)
                        {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
        );
        btnAlterar.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            dadosDoSelect.updateString("titulo", txtTitulo.getText());
                            dadosDoSelect.updateString("idAutor", txtIdAutor.getText());
                            dadosDoSelect.updateString("idArea", txtIdArea.getText());
                            dadosDoSelect.updateRow();
                            JOptionPane.showMessageDialog(null,"Atualização bem sucedida!");
                        }
                        catch (SQLException ex)
                        {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
        );
    }

    private static void preencherDados() {
        String sql = "SELECT * FROM SisBib.Biblioteca order by idBiblioteca";
        try {
            Statement comandoSQL = conexaoDados.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
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

    //tem que ajeitar isso aqui
    private static void escrevePnl(){
        pnlLivros.add(lbCodLivro);
        pnlLivros.add(lbTitulo);
        pnlLivros.add(lbIdAutor);
        pnlLivros.add(lbIdArea);
        pnlLivros.add(txtCodLivro);
        pnlLivros.add(txtTitulo);
        pnlLivros.add(txtIdAutor);
        pnlLivros.add(txtIdArea);
        pnlNavegacao.add(btnAnterior);
        pnlNavegacao.add(btnProximo);
        pnlNavegacao.add(btnBusca);
        pnlNavegacao.add(btnIncluir);
        pnlNavegacao.add(btnExcluir);
        pnlNavegacao.add(btnAlterar);
        pnlLivros.setLayout(new GridLayout(2, 4));
        pnlNavegacao.setLayout(new GridLayout(1, 2));
        pnlConteudo.add(pnlLivros, BorderLayout.CENTER);
        pnlConteudo.add(pnlNavegacao, BorderLayout.PAGE_END);
        cntForm.remove(pnlConteudo);
        cntForm.add(pnlConteudo, BorderLayout.CENTER);
    }

    private static void exibirLivros() throws SQLException{
        txtCodLivro.setText(dadosDoSelect.getString("codLivro"));
        txtTitulo.setText(dadosDoSelect.getString("titulo"));
        txtIdAutor.setText(dadosDoSelect.getString("idAutor"));
        txtIdArea.setText(dadosDoSelect.getString("idArea"));
        cntForm.revalidate();
    }
}
