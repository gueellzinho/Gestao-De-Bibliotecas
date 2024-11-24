import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.Objects;

import static java.lang.System.out;

public class FrameBib extends JFrame {
    private static  JMenuBar menuNavegacao;
    //private static JDateChooser calCalendario;
    private static JTextField txtBib, txtCodLivro, txtTitulo, txtIdAutor, txtIdArea,
<<<<<<< Updated upstream
                              txtIdExemplar, txtIdBiblioteca, txtNumeroExemplar;
    private static JButton btnLivros, btnExemplares, btnEmprestimos, btnDevolucoes;
    private static JComboBox cbxBiblioteca;
    private static JPanel pnlExemplares, pnlNavegacao, pnlConteudo;
    private static JLabel lbCodLivro, lbIdExemplar, lbIdBiblioteca,
                          lbNumeroExemplar;
=======
                              txtIdExemplar, txtIdBiblioteca, txtNumeroExemplar,
                              txtIdLeitor, txtNomeLeitor, txtDevolucaoPrevista;
    private static JButton btnLivros, btnExemplares, btnEmprestimos, btnDevolucoes,
                           btnAnterior, btnProximo, btnBusca, btnIncluir, btnExcluir, btnAlterar;
    private static JComboBox cbxBiblioteca;
    private static JTabbedPane tpEmprestimo;
    private static JTable tabAtrasos;
>>>>>>> Stashed changes
    static private Connection conexaoDados;
    private static ResultSet dadosDoSelect;
    private static Container cntForm;
    private static int idBibliotecaEscolhida = 1;

    //TODAS ESSA VARIAVEIS PARA BAIXO PODEM SUMIR QUANDO SEPARAR OS ARQUIVOS
    private static JPanel pnlLivros, pnlExemplares, pnlNavegacao, pnlConteudo, pnlEmprestimos, pnlAtrasos;
    private static JLabel lbCodLivro, lbTitulo, lbIdAutor, lbIdArea, lbIdExemplar, lbIdBiblioteca,
                          lbNumeroExemplar, lbIdLeitor, lbNomeLeitor, lbDevolucaoPrevista;
    private enum Tela {INICIAL, LIVROS, EXEMPLARES, EMPRESTIMOS, DEVOLUCOES}
    private static Tela estado;

    public FrameBib(Connection cnxDados){
        estado = Tela.INICIAL;
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

<<<<<<< Updated upstream
=======
        pnlLivros   = new JPanel();
        lbCodLivro  = new JLabel("Código Livro");
        lbTitulo    = new JLabel("Título");
        lbIdAutor   = new JLabel("ID Autor");
        lbIdArea    = new JLabel("ID Área");
        txtCodLivro = new JTextField();
        txtTitulo   = new JTextField();
        txtIdAutor  = new JTextField();
        txtIdArea   = new JTextField();

        pnlExemplares       = new JPanel();
        lbIdExemplar        = new JLabel("ID Exemplar");
        lbIdBiblioteca      = new JLabel("ID Biblioteca");
        lbNumeroExemplar    = new JLabel("Número Exemplar");
        txtIdExemplar       = new JTextField();
        txtIdBiblioteca     = new JTextField();
        txtNumeroExemplar   = new JTextField();

        pnlEmprestimos = new JPanel();
        pnlAtrasos     = new JPanel();
        tpEmprestimo   = new JTabbedPane();
        tpEmprestimo.add("Empréstimo", pnlEmprestimos);
        tpEmprestimo.add("Atrasos", pnlAtrasos);
        lbIdLeitor           = new JLabel("ID Leitor");
        lbNomeLeitor         = new JLabel("Nome Leitor");
        lbDevolucaoPrevista  = new JLabel("Devolução Prevista");
        txtIdLeitor          = new JTextField();
        txtNomeLeitor        = new JTextField();
        txtDevolucaoPrevista = new JTextField();

        pnlNavegacao = new JPanel();
        btnAnterior  = new JButton("Anterior");
        btnProximo   = new JButton("Próximo");
        btnBusca     = new JButton("Buscar");
        btnAlterar   = new JButton("Alterar");
        btnIncluir   = new JButton("Incluir");
        btnExcluir   = new JButton("Excluir");

        pnlNavegacao.add(btnAnterior);
        pnlNavegacao.add(btnProximo);
        pnlNavegacao.add(btnBusca);
        pnlNavegacao.add(btnIncluir);
        pnlNavegacao.add(btnExcluir);
        pnlNavegacao.add(btnAlterar);
        pnlNavegacao.setLayout(new GridLayout(1, 2, 10, 10));
        pnlNavegacao.setBorder(new EmptyBorder(10, 10, 10, 10));

>>>>>>> Stashed changes
        pnlConteudo = new JPanel();
        pnlConteudo.setLayout(new GridLayout(2, 1));

        conexaoDados = cnxDados;
        preencherBibliotecas();

        cbxBiblioteca.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        String comboBox = Objects.requireNonNull(cbxBiblioteca.getSelectedItem()).toString();
                        idBibliotecaEscolhida = Integer.parseInt(comboBox.substring(0, 1));
                    }
                }
        );

        btnLivros.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        estado = Tela.LIVROS;
                        String sql = "select * from SisBib.Livro order by codLivro";
                        try{
                            Statement comandoSQL = conexaoDados.createStatement(
                                    ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_UPDATABLE
                            );
                            try{
                                dadosDoSelect = comandoSQL.executeQuery(sql);
                                if (dadosDoSelect != null && dadosDoSelect.next()) {
                                    FrameLivros Livros = new FrameLivros(dadosDoSelect);
                                    FrameBib.this.setVisible(false);
                                    Livros.setLocationRelativeTo(null);
                                    Livros.addWindowListener(
                                            new WindowAdapter() {
                                                @Override
                                                public void windowClosing(WindowEvent e) {
                                                    FrameBib.this.setVisible(true);
                                                    Livros.dispose();
                                                }
                                            }
                                    );
                                    Livros.setVisible(true);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Registros de livros não encontrados!");
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

        btnExemplares.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        estado = Tela.EXEMPLARES;
                        String sql = "select * from SisBib.Exemplar where idBiblioteca = " + idBibliotecaEscolhida + "order by idExemplar";
                        try{
                            Statement comandoSQL = conexaoDados.createStatement(
                                    ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_UPDATABLE
                            );
                            try{
                                dadosDoSelect = comandoSQL.executeQuery(sql);
                                if (dadosDoSelect != null && dadosDoSelect.next()) {
                                    FrameExemplares Exemplares = new FrameExemplares(dadosDoSelect);
                                    FrameBib.this.setVisible(false);
                                    Exemplares.setLocationRelativeTo(null);
                                    Exemplares.addWindowListener(
                                            new WindowAdapter() {
                                                @Override
                                                public void windowClosing(WindowEvent e) {
                                                    FrameBib.this.setVisible(true);
                                                    Exemplares.dispose();
                                                }
                                            }
                                    );
                                    Exemplares.setVisible(true);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Registros de livros não encontrados!");
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
<<<<<<< Updated upstream
=======

        btnEmprestimos.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        estado = Tela.EMPRESTIMOS;
                        preencherEmprestimos();
                    }
                }
        );

        tpEmprestimo.addChangeListener(
                new ChangeListener(){
                    @Override
                    public void stateChanged(ChangeEvent e){
                        preencherEmprestimos();
                    }
        });

        btnAnterior.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (dadosDoSelect.previous()) {
                                switch(estado){
                                    case LIVROS: exibirLivros(); break;
                                    case EXEMPLARES: exibirExemplares(); break;
                                    case EMPRESTIMOS: exibirEmprestimos();break;
                                }
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Não achou registro anterior!");
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
                                switch(estado){
                                    case LIVROS: exibirLivros(); break;
                                    case EXEMPLARES: exibirExemplares(); break;
                                    case EMPRESTIMOS: exibirEmprestimos();break;
                                }
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

        btnBusca.addActionListener(
                new ActionListener() {
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
                        JOptionPane.showMessageDialog(null, "Livro não encontrado!");
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
                            out.println(ex.getMessage());
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
                            out.println(ex.getMessage());
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
                            out.println(ex.getMessage());
                        }
                    }
                }
        );
>>>>>>> Stashed changes
    }

    private static void preencherBibliotecas() {
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
                    JOptionPane.showMessageDialog(null, "Bibliotecas não encontradas!");
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

    private static void preencherEmprestimos(){
        if(tpEmprestimo.getSelectedIndex() == 1){
            String sql = "select * from SisBib.AtrasosLivros";
            try{
                Statement comandoSQL = conexaoDados.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE
                );
                try{
                    dadosDoSelect = comandoSQL.executeQuery(sql);
                    if(dadosDoSelect != null && dadosDoSelect.next()){
                        exibirAtrasos();
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
        else{
            String sql = "select e.idLeitor, le.nome, e.idExemplar, l.titulo, e.devolucaoPrevista " +
                    "     from SisBib.Emprestimo e" +
                    "     inner join SisBib.Leitor le" +
                    "           on e.idLeitor = le.idLeitor" +
                    "     inner join SisBib.Exemplar ex" +
                    "           on e.idExemplar = ex.idExemplar" +
                    "     inner join SisBib.Livro l" +
                    "           on ex.codLivro = l.codLivro" +
                    "     where ex.idBiblioteca = " + idBibliotecaEscolhida;
            try{
                Statement comandoSQL = conexaoDados.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE
                );
                try{
                    dadosDoSelect = comandoSQL.executeQuery(sql);
                    if(dadosDoSelect != null && dadosDoSelect.next()){
                        escrevePnlEmprestimos();
                        exibirEmprestimos();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Registros de empréstimos não encontrados!");
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
        cntForm.revalidate();
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
<<<<<<< Updated upstream
=======

    private static void exibirLivros() throws SQLException{
        if(!dadosDoSelect.rowDeleted()){
            txtCodLivro.setText(dadosDoSelect.getString("codLivro"));
            txtTitulo.setText(dadosDoSelect.getString("titulo"));
            txtIdAutor.setText(dadosDoSelect.getString("idAutor"));
            txtIdArea.setText(dadosDoSelect.getString("idArea"));
            cntForm.revalidate();
        }
    }

    private static void exibirExemplares() throws SQLException{
        if(!dadosDoSelect.rowDeleted()){
            txtIdExemplar.setText(dadosDoSelect.getString("idExemplar"));
            txtIdBiblioteca.setText(dadosDoSelect.getString("idBiblioteca"));
            txtCodLivro.setText(dadosDoSelect.getString("codLivro"));
            txtNumeroExemplar.setText(dadosDoSelect.getString("numeroExemplar"));
            cntForm.revalidate();
        }
    }

    private static void exibirEmprestimos() throws SQLException{
        if(!dadosDoSelect.rowDeleted()){
            txtIdLeitor.setText(dadosDoSelect.getString("idLeitor"));
            txtNomeLeitor.setText(dadosDoSelect.getString("nome"));
            txtIdExemplar.setText(dadosDoSelect.getString("idExemplar"));
            txtTitulo.setText(dadosDoSelect.getString("titulo"));
            txtDevolucaoPrevista.setText(dadosDoSelect.getString("devolucaoPrevista"));
        }
    }

    private static void exibirAtrasos() throws SQLException{
        String[] colunas = {"Código Livro", "Titulo", "ID Leitor", "Nome Leitor", "ID Exemplar", "Devolucao Prevista", "Multa"};
        dadosDoSelect.last();
        int totalRegistros = dadosDoSelect.getRow();
        dadosDoSelect.beforeFirst();
        Object[][] dadosColunas = new Object[totalRegistros][7];
        int  indice = 0;
        while(dadosDoSelect.next()){
            String codLivro          = dadosDoSelect.getString("codLivro");
            String titulo            = dadosDoSelect.getString("titulo");
            String idLeitor          = dadosDoSelect.getString("idLeitor");
            String nomeLeitor        = dadosDoSelect.getString("nome");
            String idExemplar        = dadosDoSelect.getString("idExemplar");
            String devolucaoPrevista = dadosDoSelect.getString("devolucaoPrevista");
            String multa             = dadosDoSelect.getString("Multa");
            dadosColunas[indice][0] = codLivro;
            dadosColunas[indice][1] = titulo;
            dadosColunas[indice][2] = idLeitor;
            dadosColunas[indice][3] = nomeLeitor;
            dadosColunas[indice][4] = idExemplar;
            dadosColunas[indice][5] = devolucaoPrevista;
            dadosColunas[indice][6] = multa;
            indice++;
        }
        tabAtrasos = new JTable(dadosColunas, colunas);
        JScrollPane barraRolagem = new JScrollPane(tabAtrasos);
        pnlAtrasos.add(barraRolagem, BorderLayout.CENTER);
        pnlConteudo.remove(pnlNavegacao);
    }

    //PODE SUMIR QUANDO MUDAR AS TELAS
    private static void escrevePnlLivros(){
        pnlLivros.add(lbCodLivro);
        pnlLivros.add(lbTitulo);
        pnlLivros.add(lbIdAutor);
        pnlLivros.add(lbIdArea);
        pnlLivros.add(txtCodLivro);
        pnlLivros.add(txtTitulo);
        pnlLivros.add(txtIdAutor);
        pnlLivros.add(txtIdArea);
        pnlLivros.setLayout(new GridLayout(2, 4));
        pnlConteudo.add(pnlLivros, BorderLayout.CENTER);
        pnlConteudo.add(pnlNavegacao, BorderLayout.PAGE_END);
        cntForm.remove(pnlConteudo);
        cntForm.add(pnlConteudo, BorderLayout.CENTER);
    }

    private static void escrevePnlExemplares(){
        pnlExemplares.add(lbIdExemplar);
        pnlExemplares.add(lbIdBiblioteca);
        pnlExemplares.add(lbCodLivro);
        pnlExemplares.add(lbNumeroExemplar);
        pnlExemplares.add(txtIdExemplar);
        pnlExemplares.add(txtIdBiblioteca);
        pnlExemplares.add(txtCodLivro);
        pnlExemplares.add(txtNumeroExemplar);
        pnlExemplares.setLayout(new GridLayout(2,4));
        pnlConteudo.add(pnlExemplares, BorderLayout.CENTER);
        pnlConteudo.add(pnlNavegacao, BorderLayout.PAGE_END);
        cntForm.remove(pnlConteudo);
        cntForm.add(pnlConteudo, BorderLayout.CENTER);
    }

    private static void escrevePnlEmprestimos(){
        pnlEmprestimos.add(lbIdLeitor);         //1, 1
        pnlEmprestimos.add(lbNomeLeitor);       //1, 2
        pnlEmprestimos.add(lbIdExemplar);       //1, 3
        pnlEmprestimos.add(lbTitulo);           //1, 4
        pnlEmprestimos.add(lbDevolucaoPrevista);//1, 5
        pnlEmprestimos.add(txtIdLeitor);        //2, 1
        pnlEmprestimos.add(txtNomeLeitor);      //2, 2
        pnlEmprestimos.add(txtIdExemplar);      //2, 3
        pnlEmprestimos.add(txtTitulo);          //2, 4
        pnlEmprestimos.add(txtDevolucaoPrevista);//2, 5
        pnlEmprestimos.setLayout(new GridLayout(2, 5));
        pnlConteudo.add(tpEmprestimo, BorderLayout.CENTER);
        pnlConteudo.add(pnlNavegacao, BorderLayout.SOUTH);
        cntForm.remove(pnlConteudo);
        cntForm.add(pnlConteudo, BorderLayout.CENTER);
    }
>>>>>>> Stashed changes
}
