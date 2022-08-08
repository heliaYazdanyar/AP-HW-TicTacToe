package swing;

import client.Client;
import models.Player;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreBoard extends JPanel {

    private Client client;
    private MainFrame mainFrame;

    private List<Player> onlineMembers;
    private List<Player> offlineMembers;

    private PlayerInfo[] playerInfos;
    private int cnt;

    private JMenuBar menuBar;
    private JButton mainMenu;
    private JButton refresh;
    private JPanel centerPanel;


    class PlayerInfo extends JPanel{

        private Player player;
        private boolean online;

        private JLabel name;
        private JLabel wins;
        private JLabel loses;
        private JLabel scores;
        private JLabel status;

        PlayerInfo(Player player,boolean online){
            this.player=player;
            this.online=online;
            initLabels();
        }

        private void initLabels(){
            name=new JLabel("name:"+player.getUsername(),SwingConstants.CENTER);
            name.setBackground(Color.GRAY);
            name.setForeground(Color.ORANGE);
            name.setFont(new Font("Courier New", Font.BOLD, 15));
            name.setHorizontalTextPosition(JLabel.CENTER);
            name.setVerticalTextPosition(JLabel.CENTER);
            name.setOpaque(true);

            if(online){
                status=new JLabel("online", SwingConstants.CENTER);
                status.setForeground(Color.GREEN);
            }
            else{
                status=new JLabel("offline", SwingConstants.CENTER);
                status.setForeground(Color.RED);
            }

            status.setBackground(Color.GRAY);
            status.setFont(new Font("Courier New", Font.BOLD, 15));
            status.setHorizontalTextPosition(JLabel.CENTER);
            status.setVerticalTextPosition(JLabel.CENTER);
            status.setOpaque(true);


            wins=new JLabel("wins: "+player.getWins(), SwingConstants.CENTER);
            wins.setBackground(Color.GRAY);
            wins.setForeground(Color.ORANGE);
            wins.setFont(new Font("Courier New", Font.BOLD, 15));
            wins.setHorizontalTextPosition(JLabel.CENTER);
            wins.setVerticalTextPosition(JLabel.CENTER);
            wins.setOpaque(true);


            loses=new JLabel("loses: "+player.getLoses(), SwingConstants.CENTER);
            loses.setBackground(Color.GRAY);
            loses.setForeground(Color.ORANGE);
            loses.setFont(new Font("Courier New", Font.BOLD, 15));
            loses.setHorizontalTextPosition(JLabel.CENTER);
            loses.setVerticalTextPosition(JLabel.CENTER);
            loses.setOpaque(true);

            scores=new JLabel("score: "+player.getScore(), SwingConstants.CENTER);
            scores.setBackground(Color.GRAY);
            scores.setForeground(Color.ORANGE);
            scores.setFont(new Font("Courier New", Font.BOLD, 15));
            scores.setHorizontalTextPosition(JLabel.CENTER);
            scores.setVerticalTextPosition(JLabel.CENTER);
            scores.setOpaque(true);



            this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            this.setBackground(Color.ORANGE);
            this.add(name);
            this.add(status);
            this.add(wins);
            this.add(loses);
            this.add(scores);
            this.add(new JLabel("*******************"));

        }

    }

    public ScoreBoard(MainFrame mainFrame,Client client){
        this.client=client;
        this.mainFrame=mainFrame;

        this.setLayout(new BorderLayout());

        menuBar=new JMenuBar();
        mainMenu=new JButton("Main Menu");
        mainMenu.addActionListener(e -> this.mainFrame.setCurrentPanel("mainMenu"));
        refresh=new JButton("Refresh");
        refresh.addActionListener(e -> this.mainFrame.setCurrentPanel("scoreBoard"));
        menuBar.add(mainMenu);
        menuBar.add(refresh);

        this.add(menuBar,BorderLayout.NORTH);


        centerPanel=new JPanel();
        centerPanel.setLayout(new GridLayout(5,3));
        centerPanel.setBackground(Color.ORANGE);
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.magenta));
        centerPanel.setOpaque(true);

        onlineMembers=new ArrayList<>();
        offlineMembers=new ArrayList<>();


        update();
        initPanel();

        this.add(centerPanel,BorderLayout.CENTER);

        System.out.println("init score board");
    }



    public void update(){
        this.cnt=0;

        onlineMembers.removeAll(onlineMembers);
        List<String> onlines=client.getOnlineNames();
        if(onlines.size()>0)
            for (String name:
                    onlines) {
                onlineMembers.add(Player.getFromJson(name));
                cnt++;
            }

        offlineMembers.removeAll(offlineMembers);
        List<String> offlines=client.getOfflineNames();
        for (String name:
                offlines) {
            offlineMembers.add(Player.getFromJson(name));
            cnt++;
        }

        playerInfos=new PlayerInfo[cnt];

    }

    private void initPanel(){
        int k=0;

        for (Player player:
                onlineMembers) {
            playerInfos[k]=new PlayerInfo(player,true);
            centerPanel.add(playerInfos[k]);
            playerInfos[k].setPreferredSize(new Dimension(300,200));
            k++;
        }

        for (Player player:
                offlineMembers) {
            playerInfos[k]=new PlayerInfo(player,false);
            centerPanel.add(playerInfos[k]);

            k++;
        }



    }


}
