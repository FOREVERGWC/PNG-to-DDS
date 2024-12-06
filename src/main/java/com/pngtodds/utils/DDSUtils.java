package com.pngtodds.utils;

import com.jogamp.opengl.util.texture.spi.DDSImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DDSUtils {
    private static final int BYTES_PER_PIXEL = 4;

    public static void saveDDS(String inputPath, String outputPath) {
        try {
            BufferedImage image = ImageIO.read(new File(inputPath));
            ByteBuffer[] mipmaps = convertToMipmaps(image);
            DDSImage ddsImage = DDSImage.createFromData(DDSImage.D3DFMT_A8R8G8B8, image.getWidth(), image.getHeight(), mipmaps);
            ddsImage.write(outputPath);
            ddsImage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveDDS(BufferedImage bufferedImage, String path) {
        try {
            ByteBuffer[] mipmaps = convertToMipmaps(bufferedImage);
            DDSImage ddsImage = DDSImage.createFromData(DDSImage.D3DFMT_A8R8G8B8, bufferedImage.getWidth(), bufferedImage.getHeight(), mipmaps);
            ddsImage.write(path);
            ddsImage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ByteBuffer[] convertToMipmaps(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int numMipmaps = calculateNumMipmaps(width, height);

        ByteBuffer[] mipmaps = new ByteBuffer[numMipmaps];

        for (int i = 0; i < numMipmaps; i++) {
            int currentWidth = Math.max(image.getWidth() >> i, 1);
            int currentHeight = Math.max(image.getHeight() >> i, 1);

            BufferedImage mipmapImage = new BufferedImage(currentWidth, currentHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = mipmapImage.createGraphics();
            g.drawImage(image, 0, 0, currentWidth, currentHeight, null);
            g.dispose();

            int bufferSize = currentWidth * currentHeight * BYTES_PER_PIXEL;
            ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);

            int[] pixels = mipmapImage.getRGB(0, 0, currentWidth, currentHeight, null, 0, currentWidth);
            for (int pixel : pixels) {
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                buffer.put((byte) blue);
                buffer.put((byte) green);
                buffer.put((byte) red);
                buffer.put((byte) alpha);
            }

            buffer.flip();
            mipmaps[i] = buffer;
        }

        return mipmaps;
    }

    private static int calculateNumMipmaps(int width, int height) {
        int maxSize = Math.max(width, height);
        return (int) (Math.log(maxSize) / Math.log(2)) + 1;
    }
}
