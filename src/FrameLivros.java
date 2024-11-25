import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.System.out;

public class FrameLivros extends JFrame {

    private static Container cntForm;
    private static JButton btnAnterior, btnProximo, btnBusca, btnIncluir, btnExcluir, btnAlterar;
    private static ResultSet dadosDoSelect;
    private static JPanel pnlNavegacao, pnlConteudo, pnlLivros;
    private static JTextField txtCodLivro, txtTitulo, txtIdAutor, txtIdArea;

    public FrameLivros(ResultSet dados) throws SQLException {
        setTitle("Manutenção de Livros");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        pnlLivros   = new JPanel();
        txtCodLivro = new JTextField();
        txtTitulo   = new JTextField();
        txtIdAutor  = new JTextField();
        txtIdArea   = new JTextField();

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
        pnlNavegacao.setLayout(new GridLayout(1, 2));

        pnlConteudo = new JPanel();
        pnlConteudo.setLayout(new GridLayout(2, 1));

        dadosDoSelect = dados;

        cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());

        pnlLivros.add(new JLabel("Código Livro"));
        pnlLivros.add(new JLabel("Título"));
        pnlLivros.add(new JLabel("ID Autor"));
        pnlLivros.add(new JLabel("ID Área"));
        pnlLivros.add(txtCodLivro);
        pnlLivros.add(txtTitulo);
        pnlLivros.add(txtIdAutor);
        pnlLivros.add(txtIdArea);
        pnlLivros.setLayout(new GridLayout(2, 4));
        pnlConteudo.add(pnlLivros, BorderLayout.CENTER);
        pnlConteudo.add(pnlNavegacao, BorderLayout.PAGE_END);
        cntForm.add(pnlConteudo, BorderLayout.CENTER);
        exibirLivros();

        btnAnterior.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (dadosDoSelect.previous()) {
                                exibirLivros();
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
    }
    private static void escrevePnlLivros(){

    }

    private static void exibirLivros() throws SQLException {
        if(!dadosDoSelect.rowDeleted()){
            txtCodLivro.setText(dadosDoSelect.getString("codLivro"));
            txtTitulo.setText(dadosDoSelect.getString("titulo"));
            txtIdAutor.setText(dadosDoSelect.getString("idAutor"));
            txtIdArea.setText(dadosDoSelect.getString("idArea"));
            cntForm.revalidate();
        }
    }
}
