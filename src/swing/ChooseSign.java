package swing;

import client.Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChooseSign extends JPanel {

    public int width=700;
    public int height=700;

    private Client client;
    private MainFrame mainFrame;

    private GridBagConstraints gbc=new GridBagConstraints();
    private JLabel xLabel;
    private JLabel oLabel;

    public ChooseSign(MainFrame mainFrame, Client client){
        this.mainFrame=mainFrame;
        this.client=client;

        this.setLayout(new GridBagLayout());
        this.setBackground(Color.GRAY);
        initPanel();
    }


    public BufferedImage loadImage(String name, String type, int width, int height){
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(System.getProperty("user.dir")+File.separator+
                    "resources"+File.separator+"images"+File.separator+name+"."+type));
        } catch (IOException e) { e.printStackTrace(); }

        BufferedImage resized = resize(image, width, height);

        return resized;
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

    private void initPanel(){
        xLabel=new JLabel(loadIcon("x","png",200,200));
        oLabel=new JLabel(loadIcon("o","png",200,200));

        xLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                client.setSign('x',true);
            }
        });
        oLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                client.setSign('o',true);
            }
        });

        gbc.weightx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;

        gbc.gridx=1; gbc.gridy=1;
        this.add(xLabel,gbc);
        gbc.gridx=2;
        this.add(oLabel,gbc);

    }








}
