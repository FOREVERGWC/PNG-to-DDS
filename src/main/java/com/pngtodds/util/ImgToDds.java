package com.pngtodds.util;

import com.jogamp.opengl.util.texture.spi.DDSImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

//@Slf4j
public class ImgToDds {
    private static final int BYTES_PER_PIXEL = 4;

    public static String getDDSName(String inputPath, String outputPath) throws IOException {
        BufferedImage image = ImageIO.read(new File(inputPath));
        ByteBuffer[] mipmaps = convertToMipmaps(image);
        DDSImage ddsImage = DDSImage.createFromData(DDSImage.D3DFMT_A8R8G8B8, image.getWidth(), image.getHeight(), mipmaps);
        ddsImage.write(outputPath);
//        log.info("图像：{}转换完成", outputPath);
        System.out.println("图像：" + outputPath + "转换完成");
        return outputPath;
    }

    private static ByteBuffer[] convertToMipmaps(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int numMipmaps = calculateNumMipmaps(width, height);

        ByteBuffer[] mipmaps = new ByteBuffer[numMipmaps];

        for (int i = 0; i < numMipmaps; i++) {
            int currentWidth = Math.max(image.getWidth() >> i, 1);
            int currentHeight = Math.max(image.getHeight() >> i, 1);

            int bufferSize = currentWidth * currentHeight * BYTES_PER_PIXEL;
            ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);

            int[] pixels = image.getRGB(0, 0, currentWidth, currentHeight, null, 0, currentWidth);
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
