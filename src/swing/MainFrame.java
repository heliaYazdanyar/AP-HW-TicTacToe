package swing;

import client.Client;
import javax.swing.*;

public class MainFrame extends JFrame {
    private int width=790;
    private int height=1020;

    private Client client;

    private JPanel currentPanel;
    private StartPanel startPanel;
    private MainMenu mainMenu;
    private ScoreBoard scoreBoard;
    private ChooseSign chooseSign;

    public MainFrame(Client client){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width,height);
        setTitle("TicTacToe");
//        setVisible(true);

        this.client=client;
        initPanels();

        setCurrentPanel("Start");
    }

    public void initPanels(){
        startPanel=new StartPanel(client,this);
    }

    public void setCurrentPanel(String panelName){
        System.out.println("setting panel:"+panelName);
        switch (panelName){
            case"Start":
                if(currentPanel!=null) this.remove(currentPanel);
                this.currentPanel=startPanel;
                this.add(currentPanel);
                this.setSize(500,500);
                break;

            case"play":
                if(currentPanel!=null) this.remove(currentPanel);
                currentPanel=client.getBoard();
                this.add(currentPanel);
                this.setSize(700,800);
                break;

            case"mainMenu":
                mainMenu=new MainMenu(this,client);
                if(currentPanel!=null) this.remove(currentPanel);
                this.currentPanel=mainMenu;
                this.add(currentPanel);
                this.setSize(400,800);
                break;

            case"scoreBoard":
                scoreBoard=new ScoreBoard(this,client);
                if(currentPanel!=null) this.remove(currentPanel);
                this.currentPanel=scoreBoard;
                this.add(currentPanel);
                this.setSize(400,900);
                break;

            case"chooseSign":
                chooseSign=new ChooseSign(this,client);
                if(currentPanel!=null) this.remove(currentPanel);
                this.currentPanel=chooseSign;
                this.add(currentPanel);
                this.setSize(600,600);
                break;

        }

    }




}
