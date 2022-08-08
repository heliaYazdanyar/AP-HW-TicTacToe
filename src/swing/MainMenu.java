package swing;

import client.Client;
import models.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {

    private final MainFrame mainFrame;

    private Client client;


    private JLabel gameIcon;
    private JLabel username;
    private JLabel wins;
    private JLabel loses;
    private JLabel score;

    private final JButton play=new JButton("New Game");
    private final JButton otherPlayers=new JButton("Other Players");

    private GridBagConstraints gbc=new GridBagConstraints();

    MainMenu(MainFrame mainFrame, Client client){
        this.mainFrame=mainFrame;
        this.client=client;

        this.setLayout(new GridBagLayout());
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(BorderFactory.createLineBorder(Color.magenta));
        this.setOpaque(true);


        initLabels();
        initPanel();
    }


    private void initLabels(){
        gameIcon=new JLabel(loadIcon("TicTacToe","jpg",500,500));

        username=new JLabel("", SwingConstants.CENTER);
        username.setBackground(Color.GRAY);
        username.setForeground(Color.pink);
        username.setFont(new Font("Courier New", Font.BOLD, 20));
        username.setHorizontalTextPosition(JLabel.CENTER);
        username.setVerticalTextPosition(JLabel.CENTER);
        username.setOpaque(true);

        wins=new JLabel("", SwingConstants.CENTER);
        wins.setBackground(Color.GRAY);
        wins.setForeground(Color.GREEN);
        wins.setFont(new Font("Courier New", Font.BOLD, 20));
        wins.setHorizontalTextPosition(JLabel.CENTER);
        wins.setVerticalTextPosition(JLabel.CENTER);
        wins.setOpaque(true);

        loses=new JLabel("", SwingConstants.CENTER);
        loses.setBackground(Color.GRAY);
        loses.setForeground(Color.red);
        loses.setFont(new Font("Courier New", Font.BOLD, 20));
        loses.setHorizontalTextPosition(JLabel.CENTER);
        loses.setVerticalTextPosition(JLabel.CENTER);
        loses.setOpaque(true);

        score=new JLabel("", SwingConstants.CENTER);
        score.setBackground(Color.GRAY);
        score.setForeground(Color.BLUE);
        score.setFont(new Font("Courier New", Font.BOLD, 20));

        score.setOpaque(true);

        Player player=Player.getFromJson(client.getUsername());

        //set text in labels
        username.setText(player.getUsername());
        wins.setText("wins:  "+player.getWins()+"");
        loses.setText("loses:  "+player.getLoses()+"");
        score.setText("score:  "+player.getScore()+"");

        score.setHorizontalTextPosition(JLabel.CENTER);
        score.setVerticalTextPosition(JLabel.CENTER);
    }

    private void initPanel(){
        gbc.weightx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;

        gbc.gridx=1; gbc.gridy=1;
        this.add(gameIcon,gbc);
        gbc.gridy=2;
        this.add(username,gbc);
        gbc.gridy=3;
        this.add(wins,gbc);
        gbc.gridy=4;
        this.add(loses,gbc);
        gbc.gridy=5;
        this.add(score,gbc);

        gbc.gridy=6;
        this.add(play,gbc);
        gbc.gridy=7;
        this.add(otherPlayers,gbc);

        play.addActionListener(e->{
            client.requestForGame();
        });

        otherPlayers.addActionListener(e-> {
            mainFrame.setCurrentPanel("scoreBoard");
        });

    }

    public BufferedImage loadImage(String name, String type, int width, int height){
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
