import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class FrameBib extends JFrame {
    private static  JToolBar tbNavegacao;
    private static JTextField txtServidor, txtBD, txtUser, txtPassword;
    //private static CALENDARIO ALGUMA COISA;
    private JButton btnLivros, btnExemplares, btnEmprestimos, btnDevolucoes, btnConnect;
    private JComboBox cbxBiblioteca;
    private static Connection conexaoDados = null;

    public FrameBib(){
        setTitle("Sistema de Biblioteca");
        setSize(1000, 200);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        formLogin();
    }

    private void formLogin(){
        btnLivros = new JButton("Livros");
        btnLivros.setPreferredSize(new Dimension(30,45));
        btnLivros.setVerticalTextPosition(SwingConstants.LEFT);
        btnLivros.setHorizontalTextPosition(SwingConstants.LEFT);
        btnLivros.setFocusPainted(false);
        btnLivros.setBorderPainted(false);

        btnExemplares = new JButton("Exemplares");
        btnExemplares.setPreferredSize(new Dimension(30,45));
        btnExemplares.setVerticalTextPosition(SwingConstants.LEFT);
        btnExemplares.setHorizontalTextPosition(SwingConstants.LEFT);
        btnExemplares.setFocusPainted(false);
        btnExemplares.setBorderPainted(false);

        btnEmprestimos = new JButton("Empréstimos");
        btnEmprestimos.setPreferredSize(new Dimension(30,45));
        btnEmprestimos.setVerticalTextPosition(SwingConstants.LEFT);
        btnEmprestimos.setHorizontalTextPosition(SwingConstants.LEFT);
        btnEmprestimos.setFocusPainted(false);
        btnEmprestimos.setBorderPainted(false);

        btnDevolucoes = new JButton("Devoluções");
        btnDevolucoes.setPreferredSize(new Dimension(30,45));
        btnDevolucoes.setVerticalTextPosition(SwingConstants.LEFT);
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

        txtServidor = new JTextField();
        txtBD = new JTextField();
        txtUser = new JTextField();
        txtPassword = new JTextField();

        btnConnect = new JButton("Conectar");

        cbxBiblioteca = new JComboBox();

        cbxBiblioteca.setEnabled(false);

        JPanel 
        JPanel pnlForm = new JPanel();
    }
}
