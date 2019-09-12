/**
 * @author Lucas da Silva Rezende
 * Trabalho de processamento de imagens
 * - Imagem aumentada {@link br.com.lucasilva.Principal#itemMenuImagemAumentadaActionEvent(java.awt.event.ActionEvent)}
 * - Imagem reduzida {@link br.com.lucasilva.Principal#itemMenuImagemReduzidaActionEvent(java.awt.event.ActionEvent)}
 * - Escala de cinza {@link br.com.lucasilva.Principal#converterTomCinza}
 * - Tons de vermelho {@link br.com.lucasilva.Principal#converterTomRed()}
 * - Tons de verde {@link br.com.lucasilva.Principal#converterTomGreen()}
 * - Tons de azul {@link br.com.lucasilva.Principal#converterTomBlue()}
 * - Histograma {@link br.com.lucasilva.Principal#showHistogram(org.opencv.core.Mat, br.com.lucasilva.Principal.TipoTom)}}
 * - Histograma Cinza {@link br.com.lucasilva.Principal#itemMenuHistogramaImgCinzaActionEvent(java.awt.event.ActionEvent)}
 * - Histograma Vermelho {@link br.com.lucasilva.Principal#itemMenuHistogramaImgRedActionEvent(java.awt.event.ActionEvent)}
 * - Histograma Green {@link br.com.lucasilva.Principal#itemMenuHistogramaImgGreenActionEvent(java.awt.event.ActionEvent)}
 * - Histograma Blue {@link br.com.lucasilva.Principal#itemMenuHistogramaImgBlueActionEvent(java.awt.event.ActionEvent)}
 */

package br.com.lucasilva;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Principal extends JFrame {

    private JLabel jLabel;

    public String nomeImgCarregada;
    public String nomeImgFracaoCarregada;
    public String pathImgCarregada;
    public String pathImgFracaoCarregada;
    public Mat matImgTrabalho;
    public Mat matImgTonCinza;
    public Mat matImgTonRed;
    public Mat matImgTonGreen;
    public Mat matImgTonBlue;
    public Mat matImgReduzida;
    public Mat matImgAumentada;
    public Mat matImgFracao;
    public Mat matImgInserirFracao;
    public Mat matImgAlterarContraste;
    public BufferedImage bufferImgTrabalho;
    public BufferedImage bufferImgFracao;

    enum TipoTom {
        ORIGINAL, CINZA, RED, GREEN, BLUE;
    }

    public Principal() {
        initComponents();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setJMenuBar(criarBarraDeMenu());
        add(criarLabelCarregarImagem());

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel)
                                .addContainerGap(225, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel)
                                .addContainerGap(254, Short.MAX_VALUE))
        );

        pack();
    }

    /**
     * Mostra a imagem original
     * @param evt
     */
    private void mostrarImagemEvent(java.awt.event.ActionEvent evt) {
        String fileStr = "resources/dw.jpg";
        File file = new File(Principal.class.getClassLoader().getResource(fileStr).getFile());
        fileStr = file.getAbsolutePath();

        Mat img;
        img = Imgcodecs.imread(fileStr);
        ShowImage.showWindow("Imagem Original", img);
    }

    private JMenuBar criarBarraDeMenu() {
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(criarMenuArquivo());
        jMenuBar.add(criarMenuEditar());
        jMenuBar.add(criarMenuHistograma());

        return jMenuBar;
    }

    private JMenu criarMenuArquivo() {
        JMenu jMenuArquivo = jMenuArquivo = new JMenu();
        jMenuArquivo.setText("Arquivo");
        jMenuArquivo.add(criarItemMenuCarregarImagem());
        jMenuArquivo.add(criarItemMenuExibirImagem());
        return jMenuArquivo;
    }

    private JMenuItem criarItemMenuCarregarImagem() {
        JMenuItem editarItemCarregarImg = editarItemCarregarImg = new JMenuItem();
        editarItemCarregarImg.setText("Carregar Imagem de Trabalho");
        editarItemCarregarImg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemMenuCarregarActionEvent(e);
            }
        });
        return editarItemCarregarImg;
    }

    /**
     * Carrega a imagem a ser processada
     * @param event
     */
    private void itemMenuCarregarActionEvent(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG Imagens", "jpg");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            nomeImgCarregada = chooser.getSelectedFile().getName();
            pathImgCarregada = chooser.getSelectedFile().getPath();
            this.matImgTrabalho = Imgcodecs.imread(pathImgCarregada);
            //carregar a imagem no label
            bufferImgTrabalho = ShowImage.matToBufferedImage(this.matImgTrabalho);
            Icon icon = new ImageIcon(bufferImgTrabalho);
            jLabel.setIcon(icon);
            jLabel.setText("");
            JLabel labelHist = new JLabel();
            this.setTitle(nomeImgCarregada);
        } else {
            nomeImgCarregada = "";
            pathImgCarregada = "";
            matImgTrabalho = null;
            jLabel.setText("Carregue uma imagem de trabalho.");
            jLabel.setIcon(null);
        }
    }

    /**
     * Exibe a imagem selecionada
     * @return
     */
    private JMenuItem criarItemMenuExibirImagem() {
        JMenuItem editarItemExibirImg = editarItemExibirImg = new JMenuItem();
        editarItemExibirImg.setText("Exibir Imagem de Trabalho");
        editarItemExibirImg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemMenuExibirActionEvent(e);
            }
        });
        return editarItemExibirImg;
    }


    private void itemMenuExibirActionEvent(ActionEvent event) {
        if (nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            ShowImage.showWindow(nomeImgCarregada, matImgTrabalho);
        } else {
            Object[] options = {"OK"};
            JOptionPane.showOptionDialog(this, "Nenhuma imagem para ser exibida!",
                    "Sem imagens", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        }
    }

    private JMenu criarMenuEditar() {
        JMenu jMenuEditar = jMenuEditar = new JMenu();
        jMenuEditar.setText("Editar");
        jMenuEditar.add(criarMenuConverterCinza());
        jMenuEditar.add(criarMenuConverterRed());
        jMenuEditar.add(criarMenuConverterGreen());
        jMenuEditar.add(criarMenuConverterBlue());
        jMenuEditar.add(criarMenuReduzirImagem());
        jMenuEditar.add(criarMenuAumentadaImagem());
        jMenuEditar.add(criarMenuAlterarContraste());
        return jMenuEditar;
    }

    /**
     * Exibe imagem em escala de cinza
     * @return
     */
    private JMenuItem criarMenuConverterCinza() {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setText("Converter imagem para tons de cinza.");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemMenuImagemTonsCinzaActionEvent(e);
            }
        });
        return jMenuItem;
    }

    public void itemMenuImagemTonsCinzaActionEvent(ActionEvent event) {
        if(nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            converterTomCinza();
            ShowImage.showWindow("Imagem em tons de Cinza", matImgTonCinza);
        } else {
            jOptionPaneSemImagem();
        }
    }

    public void converterTomCinza() {
        matImgTonCinza = new Mat(matImgTrabalho.rows(), matImgTrabalho.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(matImgTrabalho,matImgTonCinza, Imgproc.COLOR_BGR2GRAY);
    }

    /**
     * Exibe imagem em tons de vermelho
     * @return
     */
    private JMenuItem criarMenuConverterRed() {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setText("Converter imagem para tons de vermelho.");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemMenuImagemTonsRedActionEvent(e);
            }
        });
        return jMenuItem;
    }

    public void itemMenuImagemTonsRedActionEvent(ActionEvent event) {
        if(nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            converterTomRed();
            ShowImage.showWindow("Imagem em tons de Vermelho", matImgTonRed);
        } else {
            jOptionPaneSemImagem();
        }
    }

    public void converterTomRed() {
        matImgTonRed = new Mat(matImgTrabalho.rows(), matImgTrabalho.cols(), matImgTrabalho.type());
        //manual
        for(int i = 0; i < matImgTrabalho.rows(); i++) {
            for(int j = 0; j < matImgTrabalho.cols(); j++) {
                double pixel[] = matImgTrabalho.get(i, j);
                pixel[0] = 0;
                pixel[1] = 0;
                matImgTonRed.put(i, j, pixel);
            }
        }
    }

    /**
     * Exibe imagem em tons de verde
     * @return
     */
    private JMenuItem criarMenuConverterGreen() {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setText("Converter imagem para tons de verde.");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemMenuImagemTonsGreenActionEvent(e);
            }
        });
        return jMenuItem;
    }

    public void itemMenuImagemTonsGreenActionEvent(ActionEvent event) {
        if(nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            converterTomGreen();
            ShowImage.showWindow("Imagem em tons de Verde", matImgTonGreen);
        } else {
            jOptionPaneSemImagem();
        }
    }

    private void converterTomGreen() {
        matImgTonGreen = new Mat(matImgTrabalho.rows(), matImgTrabalho.cols(), matImgTrabalho.type());
        //manual
        for(int i = 0; i < matImgTrabalho.rows(); i++) {
            for(int j = 0; j < matImgTrabalho.cols(); j++) {
                double pixel[] = matImgTrabalho.get(i, j);
                pixel[0] = 0;
                pixel[2] = 0;
                matImgTonGreen.put(i, j, pixel);
            }
        }
    }

    /**
     * Exibe imagem em tons de azul
     * @return
     */
    private JMenuItem criarMenuConverterBlue() {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setText("Converter imagem para tons de azul.");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemMenuImagemTonsBlueActionEvent(e);
            }
        });
        return jMenuItem;
    }

    public void itemMenuImagemTonsBlueActionEvent(ActionEvent event) {
        if(nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            converterTomBlue();
            ShowImage.showWindow("Imagem em tons de Azul", matImgTonBlue);
        } else {
            jOptionPaneSemImagem();
        }
    }

    private void converterTomBlue() {
        matImgTonBlue = new Mat(matImgTrabalho.rows(), matImgTrabalho.cols(), matImgTrabalho.type());
        //manual
        for(int i = 0; i < matImgTrabalho.rows(); i++) {
            for(int j = 0; j < matImgTrabalho.cols(); j++) {
                double pixel[] = matImgTrabalho.get(i, j);
                pixel[1] = 0;
                pixel[2] = 0;
                matImgTonBlue.put(i, j, pixel);
            }
        }
    }

    /**
     * Reduz a imagem
     * @return
     */
    private JMenuItem criarMenuReduzirImagem() {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setText("Imagem reduzida.");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemMenuImagemReduzidaActionEvent(e);
            }
        });
        return jMenuItem;
    }

    public void itemMenuImagemReduzidaActionEvent(ActionEvent event) {
        if(nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            Size size = new Size(matImgTrabalho.rows()/2, matImgTrabalho.cols()/2);
            matImgReduzida = new Mat();
            Imgproc.resize(matImgTrabalho, matImgReduzida, size, 0, 0, Imgproc.INTER_AREA);
            ShowImage.showWindow("Imagem reduzida", matImgReduzida, 0, 0);
        } else {
            jOptionPaneSemImagem();
        }
    }

    /**
     * Aumenta a imagem
     * @return
     */
    private JMenuItem criarMenuAumentadaImagem() {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setText("Imagem aumentada.");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemMenuImagemAumentadaActionEvent(e);
            }
        });
        return jMenuItem;
    }

    public void itemMenuImagemAumentadaActionEvent(ActionEvent event) {
        if(nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            Size size = new Size(matImgTrabalho.rows()*2, matImgTrabalho.cols()*2);
            matImgAumentada = new Mat();
            Imgproc.resize(matImgTrabalho, matImgAumentada, size, 0, 0, Imgproc.INTER_AREA);
            ShowImage.showWindow("Imagem aumentada", matImgAumentada, 0, 0);
        } else {
            jOptionPaneSemImagem();
        }
    }

    private JMenuItem criarMenuAlterarContraste() {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setText("Alterar contraste");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemMenuAlterarContrasteActionEvent(e);
            }
        });
        return jMenuItem;
    }

    public void itemMenuAlterarContrasteActionEvent(ActionEvent event) {
        if(nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            matImgAlterarContraste = Mat.zeros(matImgTrabalho.size(), matImgTrabalho.type());
            double alpha = 2.0;
            int beta = 3;
            byte[] imageData = new byte[(int) (matImgTrabalho.total()*matImgTrabalho.channels())];
            matImgTrabalho.get(0, 0, imageData);
            byte[] newImageData = new byte[(int) (matImgAlterarContraste.total()*matImgAlterarContraste.channels())];
            for (int y = 0; y < matImgTrabalho.rows(); y++) {
                for (int x = 0; x < matImgTrabalho.cols(); x++) {
                    for (int c = 0; c < matImgTrabalho.channels(); c++) {
                        double pixelValue = imageData[(y * matImgTrabalho.cols() + x) * matImgTrabalho.channels() + c];
                        pixelValue = pixelValue < 0 ? pixelValue + 256 : pixelValue;
                        newImageData[(y * matImgTrabalho.cols() + x) * matImgTrabalho.channels() + c]
                                = saturate(alpha * pixelValue + beta);
                    }
                }
            }
            matImgAlterarContraste.put(0, 0, newImageData);

            ShowImage.showWindow("Imagem contraste e brilho alterado", matImgAlterarContraste, 0, 0);
        } else {
            jOptionPaneSemImagem();
        }
    }

    private byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
        return (byte) iVal;
    }

    private JMenu criarMenuHistograma() {
        JMenu jMenuArquivo = jMenuArquivo = new JMenu();
        jMenuArquivo.setText("Histogramas");
        jMenuArquivo.add(criarItemMenuHistogramaImgOriginal());
        jMenuArquivo.add(criarItemMenuHistogramaImgCinza());
        jMenuArquivo.add(criarItemMenuHistogramaImgBlue());
        jMenuArquivo.add(criarItemMenuHistogramaImgGreen());
        jMenuArquivo.add(criarItemMenuHistogramaImgRed());
        return jMenuArquivo;
    }

    private JMenuItem criarItemMenuHistogramaImgOriginal() {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setText("Histograma da imagem original");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemMenuHistogramaImgOriginalActionEvent(e);
            }
        });
        return jMenuItem;
    }

    public void itemMenuHistogramaImgOriginalActionEvent(ActionEvent event) {
        if(nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            ShowImage.showWindow("Histograma Imagem Trabalho", showHistogram(matImgTrabalho, TipoTom.ORIGINAL), 500, 300);
        } else {
            jOptionPaneSemImagem();
        }
    }

    private JMenuItem criarItemMenuHistogramaImgCinza() {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setText("Histograma da imagem em tons de cinza");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                converterTomCinza();
                itemMenuHistogramaImgCinzaActionEvent(e);
            }
        });
        return jMenuItem;
    }

    public void itemMenuHistogramaImgCinzaActionEvent(ActionEvent event) {
        if(nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            ShowImage.showWindow("Histograma Imagem Tons de Cinza", showHistogram(matImgTonCinza, TipoTom.CINZA), 500, 300);
        } else {
            jOptionPaneSemImagem();
        }
    }

    private JMenuItem criarItemMenuHistogramaImgGreen() {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setText("Histograma da imagem em tons de verde");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                converterTomGreen();
                itemMenuHistogramaImgGreenActionEvent(e);
            }
        });
        return jMenuItem;
    }

    public void itemMenuHistogramaImgGreenActionEvent(ActionEvent event) {
        if(nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            ShowImage.showWindow("Histograma Imagem Tons de Verde", showHistogram(matImgTonGreen, TipoTom.GREEN), 500, 300);
        } else {
            jOptionPaneSemImagem();
        }
    }

    private JMenuItem criarItemMenuHistogramaImgBlue() {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setText("Histograma da imagem em tons de azul");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                converterTomBlue();
                itemMenuHistogramaImgBlueActionEvent(e);
            }
        });
        return jMenuItem;
    }

    public void itemMenuHistogramaImgBlueActionEvent(ActionEvent event) {
        if(nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            ShowImage.showWindow("Histograma Imagem Tons de Azul", showHistogram(matImgTonBlue, TipoTom.BLUE), 500, 300);
        } else {
            jOptionPaneSemImagem();
        }
    }

    private JMenuItem criarItemMenuHistogramaImgRed() {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setText("Histograma da imagem em tons de Vermelho");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                converterTomRed();
                itemMenuHistogramaImgRedActionEvent(e);
            }
        });
        return jMenuItem;
    }

    public void itemMenuHistogramaImgRedActionEvent(ActionEvent event) {
        if(nomeImgCarregada != null && nomeImgCarregada.length() > 0) {
            ShowImage.showWindow("Histograma Imagem Tons de Vermelho", showHistogram(matImgTonRed, TipoTom.RED), 500, 300);
        } else {
            jOptionPaneSemImagem();
        }
    }

    /**
     * Cria o histograma
     * @param frame
     * @param tipoTon
     * @return
     */
    public Mat showHistogram(Mat frame, TipoTom tipoTon) {
        List<Mat> images = new ArrayList<Mat>();
        Core.split(frame, images);
        MatOfInt histSize = new MatOfInt(256);
        MatOfInt channels = new MatOfInt(0);
        MatOfFloat histRange = new MatOfFloat(0, 256);
        Mat hist_b = new Mat();
        Mat hist_g = new Mat();
        Mat hist_r = new Mat();
        Imgproc.calcHist(images.subList(0, 1), channels, new Mat(), hist_b, histSize, histRange, false);

        int hist_w = 150;
        int hist_h = 150;
        int bin_w = (int) Math.round(hist_w / histSize.get(0, 0)[0]);
        Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC3, new Scalar(0, 0, 0));

        float[] bHistData = null;
        if(tipoTon.equals(TipoTom.BLUE) || tipoTon.equals(TipoTom.CINZA) || tipoTon.equals(TipoTom.ORIGINAL)) {
            Imgproc.calcHist(images, new MatOfInt(0), new Mat(), hist_b, new MatOfInt(histSize), histRange, false);
            Core.normalize(hist_b, hist_b, 0, histImage.rows()*4, Core.NORM_MINMAX, -1, new Mat());
            bHistData = new float[(int) (hist_b.total() * hist_b.channels())];
            hist_b.get(0, 0, bHistData);
        }

        float[] gHistData = null;
        if(tipoTon.equals(TipoTom.GREEN) || tipoTon.equals(TipoTom.ORIGINAL)) {
            Imgproc.calcHist(images, new MatOfInt(1), new Mat(), hist_g, new MatOfInt(histSize), histRange, false);
            Core.normalize(hist_g, hist_g, 0, histImage.rows()*4, Core.NORM_MINMAX, -1, new Mat());
            gHistData = new float[(int) (hist_g.total() * hist_g.channels())];
            hist_g.get(0, 0, gHistData);
        }

        float[] rHistData = null;
        if(tipoTon.equals(TipoTom.RED) || tipoTon.equals(TipoTom.ORIGINAL)) {
            Imgproc.calcHist(images, new MatOfInt(2), new Mat(), hist_r, new MatOfInt(histSize), histRange, false);
            Core.normalize(hist_r, hist_r, 0, histImage.rows()*4, Core.NORM_MINMAX, -1, new Mat());
            rHistData = new float[(int) (hist_r.total() * hist_r.channels())];
            hist_r.get(0, 0, rHistData);
        }

        for (int i = 1; i < histSize.get(0, 0)[0]; i++)
        {
            if(tipoTon.equals(TipoTom.BLUE) || tipoTon.equals(TipoTom.CINZA) || tipoTon.equals(TipoTom.ORIGINAL)) {
                Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(bHistData[i - 1])),
                        new Point(bin_w * (i), hist_h - Math.round(bHistData[i])), new Scalar(255, 0, 0), 2);
            }
            if(tipoTon.equals(TipoTom.GREEN) || tipoTon.equals(TipoTom.ORIGINAL)) {
                Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(gHistData[i - 1])),
                        new Point(bin_w * (i), hist_h - Math.round(gHistData[i])), new Scalar(0, 255, 0), 2);
            }
            if(tipoTon.equals(TipoTom.RED) || tipoTon.equals(TipoTom.ORIGINAL)) {
                Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(rHistData[i - 1])),
                        new Point(bin_w * (i), hist_h - Math.round(rHistData[i])), new Scalar(0, 0, 255), 2);
            }
        }
        return histImage;
    }

    public void jOptionPaneSemImagem() {
        Object[] options = {"OK"};
        JOptionPane.showOptionDialog(this, "Nenhuma imagem para ser exibida!",
                "Sem imagens", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
    }

    private JLabel criarLabelCarregarImagem() {
        jLabel = new JLabel();
        jLabel.setText("Carregue uma imagem de trabalho");
        return jLabel;
    }

    public static void main(String[] args) {
        new Principal().setVisible(true);
    }
}
