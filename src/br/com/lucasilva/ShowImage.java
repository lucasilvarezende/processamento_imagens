package br.com.lucasilva;

import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ShowImage extends JFrame {
    public static JFrame frame;
    public static Mat mat;
    public static BufferedImage image;
    public static int larg;
    public static int alt;
    public static int modo;

    public static void showWindow(String nomeJanela, Mat picture) {
        frame = new JFrame(nomeJanela);
        frame.setSize(800, 800);
        image = matToBufferedImage(picture);
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(image, 0, 0, frame.getWidth(), frame.getHeight(), this);
            }
        };
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    public static void showWindow(String nomeJanela, Mat picture, int l, int a) {
        frame = new JFrame(nomeJanela);
        image = matToBufferedImage(picture);
        if (l == 0) {
            larg = image.getWidth();
        } else {
            larg = l;
        }
        if(a == 0 ) {
            alt = image.getHeight();
        } else {
            alt = a;
        }
        JPanel panelScroll = new JPanel();
        JScrollPane scrollPane = new JScrollPane(panelScroll);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(50, 30, 400, 400);
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(image, 0, 0, larg, alt, this);
                super.paintComponents(g);
            }
        };
        panel.setPreferredSize(new Dimension(larg, alt));
        scrollPane.setViewportView(panel);
        frame.setSize(larg, alt);
        frame.setContentPane(scrollPane);
        frame.setVisible(true);
    }

    public static BufferedImage matToBufferedImage(Mat matrix) {
        int cols = matrix.cols();
        int rows = matrix.rows();
        int elemSize = (int) matrix.elemSize();
        byte[] data = new byte[cols * rows * elemSize];
        int type;
        matrix.get(0, 0, data);
        switch (matrix.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;
                byte b;
                for (int i = 0; i < data.length; i = i + 3) {
                    b = data[i];
                    data[i] = data[i + 2];
                    data[i + 2] = b;
                }
                break;
            default:
                return null;
        }
        BufferedImage image = new BufferedImage(cols, rows, type);
        image.getRaster().setDataElements(0, 0, cols, rows, data);
        return image;

    }
}
