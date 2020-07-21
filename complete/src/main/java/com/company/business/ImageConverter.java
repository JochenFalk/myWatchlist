package com.company.business;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageConverter {

    public static byte[] convertToThumb(String inputUrl) {

        try {

            URL url = new URL(inputUrl);
            int scaledWidth = 248;
            int scaledHeight = 375;
            String format = "PNG";

            InputStream inputStream = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            BufferedImage inputImage = ImageIO.read(inputStream);
            BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();

            ImageIO.write(outputImage, format, outputStream);

            inputStream.close();
            outputStream.close();

            return outputStream.toByteArray();

        } catch (IOException ex) {
            System.out.println("Error during saving image to disk.");
            ex.printStackTrace();
        }

        return null;
    }
}
