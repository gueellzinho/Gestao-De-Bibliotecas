import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class FrameConexao extends JFrame  {
    private static JTextField txtServidor;
    private static JTextField txtBD;
    private static JTextField txtUser;
    private static JPasswordField txtPassword;
    static private Connection conexaoDados = null;
    private static JButton btnConnect;
    private static FrameConexao form;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                form = new FrameConexao();
                form.setLocationRelativeTo(null);
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

    public FrameConexao(){
        setTitle("Conexão no Sistema de Biblioteca");
        JPanel pnlCampos = new JPanel();
        pnlCampos.setLayout(new GridLayout(5, 2));
        pnlCampos.setPreferredSize(new Dimension(410,150));
        txtServidor = new JTextField("regulus.cotuca.unicamp.br");
        txtBD = new JTextField("BD24587");
        txtUser  = new JTextField("BD24587");
        txtPassword  = new JPasswordField("Miguel$PFC13579");
        btnConnect = new JButton("Conectar");
        btnConnect.setPreferredSize(new Dimension(90,30));

        pnlCampos.add(new JLabel("Servidor:"));		 //1, 1
        pnlCampos.add(txtServidor);					         //1, 2
        pnlCampos.add(new JLabel("Banco de Dados:"));   //2, 1
        pnlCampos.add(txtBD);					             //2, 2
        pnlCampos.add(new JLabel("Usuário:"));		     //3, 1
        pnlCampos.add(txtUser);					             //3, 2
        pnlCampos.add(new JLabel("Senha:"));		     //4, 1
        pnlCampos.add(txtPassword);                          //4, 2
        pnlCampos.add(new JLabel(""));                  //5, 1
        pnlCampos.add(btnConnect);                           //5, 2

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
                            FrameBib Biblioteca = new FrameBib(conexaoDados);
                            Biblioteca.setLocationRelativeTo(null);
                            Biblioteca.addWindowListener(
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

                            form.setVisible(false);
                            Biblioteca.setVisible(true);
                        }
                        catch (SQLException ex){
                            System.out.println(ex.getMessage());
                        }
                    }
                }
        );

        Container cntForm = getContentPane();
        cntForm.add(pnlCampos , BorderLayout.WEST);
    }
}
