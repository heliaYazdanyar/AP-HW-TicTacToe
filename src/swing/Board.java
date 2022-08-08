package swing;

import client.Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class Board extends JPanel {
    private MainFrame mainFrame;

    private JLabel myIcon;
    private JLabel turnAnnounce;

    private ImageIcon square;
    private ImageIcon x;
    private ImageIcon o;

    private int[][] currentBoard;
    private JLabel[][] labels;
    private JPanel mainBoard;
    private JPanel panel=new JPanel();
    private CardLayout cardLayout=new CardLayout();
    private JPanel mainPanel;
    private JButton button=new JButton("MainMenu");

    private Client player;
    private String gameState="";
    private boolean gameEnded=false;

    public Board(Client player,MainFrame mainFrame) {
        this.player = player;
        this.mainFrame=mainFrame;
        this.setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(cardLayout);

        currentBoard = new int[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                currentBoard[i][j] = 0;
            }
        }

        newEverything();
        loadIcons();
        initPanel(player.isMyTurn());
        this.add(panel, BorderLayout.NORTH);
        this.add(mainPanel,BorderLayout.CENTER);
        cardLayout.show(mainPanel,"mainBoard");

    }

    private void newEverything(){
        this.panel=new JPanel();
        this.panel.setLayout(new GridLayout(1,3));
        this.panel.setBackground(Color.ORANGE);
        this.panel.setPreferredSize(new Dimension(700,50));

        turnAnnounce=new JLabel();
        turnAnnounce.setBackground(Color.ORANGE);
        turnAnnounce.setForeground(Color.BLACK);
        turnAnnounce.setFont(new Font("Courier New", Font.ITALIC, 20));
        turnAnnounce.setHorizontalTextPosition(JLabel.CENTER);
        turnAnnounce.setVerticalTextPosition(JLabel.CENTER);
        turnAnnounce.setOpaque(true);

        myIcon=new JLabel();
        myIcon.setBackground(Color.ORANGE);
        myIcon.setForeground(Color.magenta);
        myIcon.setFont(new Font("Courier New", Font.BOLD, 30));
        myIcon.setHorizontalTextPosition(JLabel.CENTER);
        myIcon.setVerticalTextPosition(JLabel.CENTER);
        myIcon.setOpaque(true);
        myIcon.setText(player.getPlayersSign()+"");

        button.addActionListener(e -> mainFrame.setCurrentPanel("mainMenu"));


        panel.add(myIcon); panel.add(turnAnnounce); panel.add(button);

    }


    private void initPanel(boolean turn){

        if(turn) turnAnnounce.setText("Your turn");
        else turnAnnounce.setText("waiting");

        mainBoard=new JPanel(new GridLayout(7,7));
        mainBoard.setPreferredSize(new Dimension(700,700));
        mainBoard.setBackground(Color.DARK_GRAY);

        labels=new JLabel[7][7];
        for(int i=0;i<7;i++){
            for(int j=0;j<7;j++){
                if(currentBoard[i][j]==0) {
                    labels[i][j] = new JLabel(square);
                    final int t1 = i;
                    final int t2 = j;
                    labels[i][j].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (player.isMyTurn() && !gameEnded) {
                                if (player.getPlayersSign() == 'x') currentBoard[t1][t2] = 1;
                                else currentBoard[t1][t2] = 3;

                                player.endTurn(currentBoard);
                                update(currentBoard,false);
                            }
                        }
                    });
                }
                else if(currentBoard[i][j]==1) labels[i][j]=new JLabel(x);
                else labels[i][j]=new JLabel(o);
                mainBoard.add(labels[i][j]);
            }
        }

        mainPanel.add("mainBoard",mainBoard);
    }

    public void update(int[][] newBoard,boolean turn){
        for(int i=0;i<7;i++) {
            for (int j = 0; j < 7; j++) {
                currentBoard[i][j]=newBoard[i][j];
            }
        }
        if(gameEnded){
            setGameState(gameState);
            JOptionPane.showMessageDialog(this,"You won",
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
        else {
            initPanel(turn);
            cardLayout.show(mainPanel, "mainBoard");
            repaint();
        }
    }

    public void setGameState(String gameState){
        this.gameState=gameState;

        JPanel endingPanel=new JPanel();
        endingPanel.setLayout(new GridBagLayout());
        endingPanel.setBackground(Color.DARK_GRAY);

        GridBagConstraints g=new GridBagConstraints();

        JLabel l=new JLabel();
        l.setBackground(Color.ORANGE);
        l.setFont(new Font("Courier New", Font.ITALIC, 70));
        l.setHorizontalTextPosition(JLabel.CENTER);
        l.setVerticalTextPosition(JLabel.CENTER);
        l.setOpaque(true);
        if(gameState.equalsIgnoreCase("winner")){
            l.setForeground(Color.GREEN);
            l.setText("YOU WON");
        }
        else if(gameState.equalsIgnoreCase("loser")) {
            l.setForeground(Color.RED);
            l.setText("YOU LOST");
        }
        else{
            l.setForeground(Color.BLUE);
            l.setText("IT'S A DRAW");
        }

        g.weightx=1;
        g.fill=GridBagConstraints.HORIZONTAL;

        g.gridx=2; g.gridy=1;
        endingPanel.add(l,g);

        mainPanel.add("end",endingPanel);
        cardLayout.show(mainPanel,"end");

        gameEnded=true;
    }

    private void loadIcons(){
        square = loadIcon("square","png",70,70);
        o=loadIcon("o","png",70,70);
        x=loadIcon("x","png",70,70);
    }
    public BufferedImage loadImage(String name,String type,int width,int height){
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(System.getProperty("user.dir")+File.separator+
                    "resources"+File.separator+"images"+File.separator+name+"."+type));
        } catch (IOException e) { e.printStackTrace(); }

        return resize(image, width, height);
    }
    public ImageIcon loadIcon(String name,String type,int width,int height){
        ImageIcon imageIcon = null;
        try {
            BufferedImage image =loadImage(name,type,width,height);
            Image img = image.getScaledInstance(width,height,
                    Image.SCALE_SMOOTH);
            imageIcon=new ImageIcon(img);
        } catch (Exception e) { e.printStackTrace(); }

        return imageIcon;
    }
    private BufferedImage resize(BufferedImage img, int height, int width){
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }



}
