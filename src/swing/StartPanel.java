package swing;

import client.Client;
import server.Config;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class StartPanel extends JPanel {
    private int width=400;
    private int height=400;

    private MainFrame mainFrame;

    private GridBagConstraints gbc=new GridBagConstraints();
    private JButton signin=new JButton("SignIn");
    private JButton login=new JButton("Login");
    private JTextField username=new JTextField("username");
    private  JTextField password=new JTextField("password");

    private Client client;

    StartPanel(Client client,MainFrame mainFrame){
        setSize(width,height);
        setBackground(Color.GREEN);
        setLayout(new GridBagLayout());

        this.mainFrame=mainFrame;
        this.client=client;

        initPanel();
    }


    private void initPanel(){
        gbc.weightx=2;
        gbc.fill=GridBagConstraints.HORIZONTAL;

        gbc.gridx=2; gbc.gridy=2;
        this.add(username,gbc);
        gbc.gridy=3;
        this.add(password,gbc);
        gbc.gridy=4;
        this.add(login,gbc);
        gbc.gridy=5;
        this.add(signin,gbc);


        login.addActionListener(e -> {
            if(Config.login(username.getText(),password.getText())){

                client.login(username.getText(),password.getText());
                mainFrame.setCurrentPanel("mainMenu");



            }else {
                JOptionPane.showMessageDialog(this,"Wrong username or password",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        });

        signin.addActionListener(e -> {
            if(Config.signIn(username.getText(),password.getText())){

                client.login(username.getText(),password.getText());
                mainFrame.setCurrentPanel("mainMenu");

            }else {
                JOptionPane.showMessageDialog(this,"this username already exists.",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        });


    }




}
