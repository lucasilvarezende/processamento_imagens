package br.com.lucasilva;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Start {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);

        try {
            File input = new File(Start.class.getClassLoader().getResource("resources/dw.jpg").toURI());
            BufferedImage image = ImageIO.read(input);

            boolean existeDir = new File("C:\\OpenCV").exists();
            if(!existeDir) {
                new File("C:\\OpenCV").mkdir();
            }
            File arquivo = new File("C:/OpenCV/sample.jpg");
            boolean existeArquivo = arquivo.exists();
            if(existeArquivo) {
                arquivo.delete();
            }
            File ouptut = new File("C:/OpenCV/sample.jpg");
            ImageIO.write(image, "jpg", ouptut);

            System.out.println("image Saved");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
