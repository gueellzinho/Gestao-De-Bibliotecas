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
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Properties;

import org.jdatepicker.impl.*;
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
    private static Date dataAtual;

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

        JPanel pCalendario = new JPanel();
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setBounds(110, 100, 200, 25);
        model.setSelected(true);
        datePicker.setVisible(true);
        pCalendario.add(datePicker);
        cntForm.add(pCalendario,BorderLayout.CENTER);

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
                                    FrameExemplares Exemplares = new FrameExemplares(dadosDoSelect, conexaoDados);
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
                        FrameEmprestimos Emprestimos = new FrameEmprestimos(conexaoDados, idBibliotecaEscolhida);
                        FrameBib.this.setVisible(false);
                        Emprestimos.setLocationRelativeTo(null);
                        Emprestimos.addWindowListener(
                                new WindowAdapter() {
                                    @Override
                                    public void windowClosing(WindowEvent e) {
                                        FrameBib.this.setVisible(true);
                                        Emprestimos.dispose();
                                    }
                                }
                        );
                        Emprestimos.setVisible(true);
                    }
                }
        );

        btnDevolucoes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "select * from SisBib.Exemplar where idBiblioteca = " + idBibliotecaEscolhida + "order by idExemplar";
                try{
                    Statement comandoSQL = conexaoDados.createStatement(
                            ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    );
                    try{
                        java.util.Date utilDate = (java.util.Date) model.getValue();
                        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                        String dataDevolucao = new SimpleDateFormat("yyyy-MM-dd").format(sqlDate);
                        dadosDoSelect = comandoSQL.executeQuery(sql);
                        if (dadosDoSelect != null && dadosDoSelect.next()){
                            FrameDevolucao Devolucao = new FrameDevolucao(conexaoDados, idBibliotecaEscolhida, dataDevolucao);
                            FrameBib.this.setVisible(false);
                            Devolucao.setLocationRelativeTo(null);
                            Devolucao.addWindowListener(
                                    new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            FrameBib.this.setVisible(true);
                                            Devolucao.dispose();
                                        }
                                    }
                            );
                            Devolucao.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Registros de livros não encontrados!");
                        }

                    } catch(SQLException exception){
                        exception.printStackTrace();
                    }
                }
                catch(SQLException exception){
                    exception.printStackTrace();
                }
            }
        });
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
}
