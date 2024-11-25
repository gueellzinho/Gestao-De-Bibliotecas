import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.System.out;

public class FrameExemplares extends JFrame {
    private static JTextField txtCodLivro, txtIdExemplar, txtIdBiblioteca, txtNumeroExemplar, txtTitulo, txtIdAutor, txtIdArea;
    private static JButton btnAnterior, btnProximo, btnBusca, btnIncluir, btnExcluir, btnAlterar;
    private static JPanel pnlExemplares, pnlNavegacao, pnlConteudo;
    private static JLabel lbCodLivro,lbIdExemplar, lbIdBiblioteca, lbNumeroExemplar;
    private static ResultSet dadosDoSelect;
    private static Container cntForm;

    public FrameExemplares(ResultSet dados, Connection conexaoDados) throws SQLException {
        setTitle("Manutenção de Exemplares");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        dadosDoSelect = dados;

        cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());

        pnlNavegacao = new JPanel();
        btnAnterior  = new JButton("Anterior");
        btnProximo   = new JButton("Próximo");
        btnBusca     = new JButton("Buscar");
        btnAlterar   = new JButton("Alterar");
        btnIncluir   = new JButton("Incluir");
        btnExcluir   = new JButton("Excluir");

        txtCodLivro = new JTextField();
        txtTitulo   = new JTextField();
        txtIdAutor  = new JTextField();
        txtIdArea   = new JTextField();

        pnlNavegacao.add(btnAnterior);
        pnlNavegacao.add(btnProximo);
        pnlNavegacao.add(btnBusca);
        pnlNavegacao.add(btnIncluir);
        pnlNavegacao.add(btnExcluir);
        pnlNavegacao.add(btnAlterar);
        pnlNavegacao.setLayout(new GridLayout(1, 2));

        pnlExemplares       = new JPanel();
        txtIdExemplar       = new JTextField();
        txtIdBiblioteca     = new JTextField();
        txtCodLivro         = new JTextField();
        txtNumeroExemplar   = new JTextField();

        pnlConteudo = new JPanel();
        pnlConteudo.setLayout(new GridLayout(2, 1));

        pnlExemplares.add(new JLabel("ID Exemplar"));
        pnlExemplares.add(new JLabel("ID Biblioteca"));
        pnlExemplares.add(new JLabel("Código Livro"));
        pnlExemplares.add(new JLabel("Número Exemplar"));
        pnlExemplares.add(txtIdExemplar);
        pnlExemplares.add(txtIdBiblioteca);
        pnlExemplares.add(txtCodLivro);
        pnlExemplares.add(txtNumeroExemplar);
        pnlExemplares.setLayout(new GridLayout(2,4));
        pnlConteudo.add(pnlExemplares, BorderLayout.CENTER);
        pnlConteudo.add(pnlNavegacao, BorderLayout.PAGE_END);
        cntForm.remove(pnlConteudo);
        cntForm.add(pnlConteudo, BorderLayout.CENTER);
        exibirExemplares();

        btnAnterior.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (dadosDoSelect.previous()) {
                                exibirExemplares();
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
                                exibirExemplares();
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
                            String chaveProcurada = txtIdExemplar.getText();
                            dadosDoSelect.beforeFirst();
                            boolean achou = false;
                            while (! achou && dadosDoSelect.next())
                            {
                                if (dadosDoSelect.getString("idExemplar").equals(chaveProcurada))
                                    achou = true;
                            }
                            if (!achou)
                            {
                                JOptionPane.showMessageDialog(null, "Exemplar não encontrado!");
                                dadosDoSelect.absolute(posicaoAnterior);
                            }
                            exibirExemplares();
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
                            String sql = "insert into SisBib.Exemplar(idBiblioteca, codLivro, numeroExemplar)" +
                                         "values(?, ?, ?)";
                            PreparedStatement dados = conexaoDados.prepareStatement(sql);
                            dados.setInt(1, Integer.parseInt(txtIdBiblioteca.getText()));
                            dados.setString(2, txtCodLivro.getText());
                            dados.setInt(3, Integer.parseInt(txtNumeroExemplar.getText()));
                            dados.execute();
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
                                exibirExemplares();
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
                            dadosDoSelect.updateString("idBiblioteca", txtIdBiblioteca.getText());
                            dadosDoSelect.updateString("codLivro", txtCodLivro.getText());
                            dadosDoSelect.updateString("numeroExemplar", txtNumeroExemplar.getText());
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

}
