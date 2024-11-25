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
    private static JTextField txtBib, txtIdLeitor, txtDevolucaoPrevista, txtIdExemplar, txtDataEmprestimo;
    private static JButton btnLivros, btnExemplares, btnEmprestimos, btnDevolucoes, btnIncluir;
    private static JComboBox cbxBiblioteca;
    private static JTabbedPane tpEmprestimo;
    private static JTable tabAtrasos;
    private static JPanel pnlNavegacao, pnlConteudo, pnlEmprestimo, pnlAtrasos;
    static private Connection conexaoDados;
    private static ResultSet dadosDoSelect;
    private static Container cntForm;
    private static int idBibliotecaEscolhida = 1;

    //TODAS ESSA VARIAVEIS PARA BAIXO PODEM SUMIR QUANDO SEPARAR OS ARQUIVOS
    private static JLabel lbIdLeitor, lbIdExemplar, lbDataEmprestimo, lbDevolucaoPrevista, vazio;

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

        pnlEmprestimo = new JPanel();
        pnlAtrasos    = new JPanel();
        tpEmprestimo  = new JTabbedPane();
        tpEmprestimo.add("Empréstimo", pnlEmprestimo);
        tpEmprestimo.add("Atrasos", pnlAtrasos);
        pnlEmprestimo.setLayout(new GridLayout(2, 4));
        lbIdLeitor           = new JLabel("ID Leitor");
        lbIdExemplar         = new JLabel("ID Exemplar");
        lbDataEmprestimo     = new JLabel("Data Empréstimo");
        lbDevolucaoPrevista  = new JLabel("Devolução Prevista");
        txtIdLeitor          = new JTextField();
        txtIdExemplar        = new JTextField();
        txtDataEmprestimo    = new JTextField();
        txtDevolucaoPrevista = new JTextField();

        vazio = new JLabel("");

        btnIncluir   = new JButton("Incluir");

        pnlConteudo = new JPanel();

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

        btnEmprestimos.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        preencherEmprestimo();
                    }
                }
        );

        tpEmprestimo.addChangeListener(
                new ChangeListener(){
                    @Override
                    public void stateChanged(ChangeEvent e){
                        preencherEmprestimo();
                    }
        });

        btnIncluir.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        String sql = "select * from SisBib.Exemplar where idBiblioteca = " + idBibliotecaEscolhida;
                        try{
                            Statement comandoSQL = conexaoDados.createStatement(
                                    ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_UPDATABLE
                            );
                            try{
                                dadosDoSelect = comandoSQL.executeQuery(sql);
                                String exemplarProcurado = txtIdExemplar.getText();
                                boolean achou = false;
                                while(dadosDoSelect.next() && !achou){
                                    String exemplarAtual = dadosDoSelect.getString("idExemplar");
                                    if(exemplarProcurado.equals(exemplarAtual)){
                                        achou = true;
                                    }
                                }
                                if(achou){
                                    try{
                                        sql = "SELECT * FROM SisBib.Emprestimo";
                                        dadosDoSelect = comandoSQL.executeQuery(sql);
                                        dadosDoSelect.moveToInsertRow();
                                        dadosDoSelect.updateInt("idLeitor",Integer.parseInt(txtIdLeitor.getText()));
                                        dadosDoSelect.updateInt("idExemplar", Integer.parseInt(txtIdExemplar.getText()));
                                        dadosDoSelect.updateDate("dataEmprestimo", Date.valueOf(txtDataEmprestimo.getText()));
                                        dadosDoSelect.updateDate("devolucaoPrevista", Date.valueOf(txtDevolucaoPrevista.getText()));
                                        dadosDoSelect.updateRow();
                                        //sql = "INSERT INTO SisBib.Emprestimo VALUES (?,?,?,?)";
                                        //PreparedStatement dados = comandoSQL.prepareStatement(sql);
                                        //dados.setInt(1, Integer.parseInt(txtIdLeitor.getText()));
                                        //dados.setInt(2, Integer.parseInt(txtIdExemplar.getText()));
                                        //dados.setDate(3,  Date.valueOf(txtDataEmprestimo.getText()));
                                        //dados.setDate(4, Date.valueOf(txtDevolucaoPrevista.getText()));
                                        //dados.executeUpdate();
                                        JOptionPane.showMessageDialog(null, "Inclusão bem sucedida!");
                                    }
                                    catch (SQLException ex){
                                        out.println(ex.getMessage());
                                    }
                                }
                                else{
                                    JOptionPane.showMessageDialog(null, "Exemplar não encontrado na biblioteca!");
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

    private static void preencherEmprestimo(){
        if(tpEmprestimo.getSelectedIndex() == 0){
            escrevePnlEmprestimos();
        }
        else if(tpEmprestimo.getSelectedIndex() == 1){
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
    }

    private static void escrevePnlEmprestimos(){
        pnlEmprestimo.add(lbIdLeitor);          //1,1
        pnlEmprestimo.add(lbIdExemplar);        //1,2
        pnlEmprestimo.add(lbDataEmprestimo);    //1,3
        pnlEmprestimo.add(lbDevolucaoPrevista);
        pnlEmprestimo.add(vazio);               //3,1//1,4
        pnlEmprestimo.add(txtIdLeitor);         //2,1
        pnlEmprestimo.add(txtIdExemplar);       //2,2
        pnlEmprestimo.add(txtDataEmprestimo);   //2,3
        pnlEmprestimo.add(txtDevolucaoPrevista);//2,4
//        pnlEmprestimo.add(vazio);               //3,2
//        pnlEmprestimo.add(vazio);               //3,3
        pnlEmprestimo.add(btnIncluir);          //3,4
        pnlConteudo.setLayout(new GridLayout(1, 1));
        pnlConteudo.add(tpEmprestimo, BorderLayout.CENTER);
        cntForm.add(pnlConteudo);
        cntForm.revalidate();
    }
}
