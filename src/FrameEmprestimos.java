import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static java.lang.System.out;


public class FrameEmprestimos extends JFrame{
    private static JTextField txtBib, txtIdLeitor, txtDevolucaoPrevista, txtIdExemplar, txtDataEmprestimo;
    private static JButton btnIncluir;
    private static JPanel pnlEmprestimo, pnlAtrasos, pnlConteudo;
    private static JTabbedPane tpEmprestimo;
    private static JTable tabAtrasos;
    private static JLabel lbIdLeitor, lbIdExemplar, lbDataEmprestimo, lbDevolucaoPrevista, vazio;
    static private Connection conexaoDados;
    private static ResultSet dadosDoSelect;
    private static Container cntForm;
    private static int idBibliotecaEscolhida;

    public FrameEmprestimos(Connection dados, int idBib){
        setTitle("Manutenção de Emprestimos");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());

        btnIncluir   = new JButton("Incluir");

        conexaoDados = dados;
        idBibliotecaEscolhida = idBib;

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

        pnlEmprestimo.add(lbIdLeitor);
        pnlEmprestimo.add(lbIdExemplar);
        pnlEmprestimo.add(lbDataEmprestimo);
        pnlEmprestimo.add(lbDevolucaoPrevista);
        pnlEmprestimo.add(vazio);
        pnlEmprestimo.add(txtIdLeitor);
        pnlEmprestimo.add(txtIdExemplar);
        pnlEmprestimo.add(txtDataEmprestimo);
        pnlEmprestimo.add(txtDevolucaoPrevista);
        pnlEmprestimo.add(btnIncluir);
        pnlConteudo.setLayout(new GridLayout(1, 1));
        pnlConteudo.add(tpEmprestimo, BorderLayout.CENTER);
        cntForm.add(pnlConteudo);

        tpEmprestimo.addChangeListener(
                new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        preencherEmprestimo();
                    }
                }
        );

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
                                        sql = "insert into SisBib.Emprestimo(idLeitor, idExemplar, dataEmprestimo, devolucaoPrevista)" +
                                                "values(?, ?, ?, ?)";
                                        PreparedStatement dados = conexaoDados.prepareStatement(sql);
                                        dados.setInt(1, Integer.parseInt(txtIdLeitor.getText()));
                                        dados.setInt(2, Integer.parseInt(txtIdExemplar.getText()));
                                        dados.setDate(3,  Date.valueOf(txtDataEmprestimo.getText()));
                                        dados.setDate(4, Date.valueOf(txtDevolucaoPrevista.getText()));
                                        dados.execute();
                                        JOptionPane.showMessageDialog(null, "Inclusão bem sucedida!");
                                    }
                                    catch (SQLException ex){
                                        JOptionPane.showMessageDialog(null, ex.getMessage());
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

    private static void preencherEmprestimo(){
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
        cntForm.revalidate();
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
        tabAtrasos.enable(false);
        JScrollPane barraRolagem = new JScrollPane(tabAtrasos);
        pnlAtrasos.add(barraRolagem, BorderLayout.CENTER);
        cntForm.revalidate();
    }
}
