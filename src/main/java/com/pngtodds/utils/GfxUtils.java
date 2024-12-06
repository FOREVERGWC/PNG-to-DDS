package com.pngtodds.utils;

import com.pngtodds.domain.Small;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GfxUtils {
    private final static String ADVISOR_TEMPLATE = "src/main/resources/static/内阁模板.png";
    private final static String ADVISOR_TEMPLATE_2 = "src/main/resources/static/内阁模板2.png";
    private final static String ADVISOR_TEMPLATE_3 = "src/main/resources/static/内阁模板3.png";

    /**
     * 保存内阁肖像
     *
     * @param file  文件
     * @param small 内阁肖像
     */
    public static void saveAdvisorDDS(File file, Small small) {
        try {
            BufferedImage template = ImageIO.read(new File(ADVISOR_TEMPLATE));
            BufferedImage template2 = ImageIO.read(new File(ADVISOR_TEMPLATE_2));
            BufferedImage template3 = ImageIO.read(new File(ADVISOR_TEMPLATE_3));
            BufferedImage large = ImageIO.read(file);
            Rectangle bounds = getNonTransparentBounds(template);
            BufferedImage resizedAvatar = resizeAndMoveImage(large, template, bounds);
            BufferedImage finalAvatar = eraseRegion(resizedAvatar, template2);
            BufferedImage mergedTemplate = mergeLayers(finalAvatar, template3);
            DDSUtils.saveDDS(mergedTemplate, small.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取非透明像素的边界坐标
     *
     * @param image 输入图片
     * @return 边界坐标
     */
    private static Rectangle getNonTransparentBounds(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int left = width, right = 0, top = height, bottom = 0;

        // 遍历像素，查找非透明像素的边界
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int alpha = (image.getRGB(x, y) >> 24) & 0xff; // 获取像素的透明度
                if (alpha > 0) { // 如果不是完全透明
                    left = Math.min(left, x);
                    right = Math.max(right, x);
                    top = Math.min(top, y);
                    bottom = Math.max(bottom, y);
                }
            }
        }

        // 如果没有找到非透明像素，返回空矩形
        if (left > right || top > bottom) {
            return new Rectangle(0, 0, 0, 0);
        }

        return new Rectangle(left, top, right - left + 1, bottom - top + 1);
    }

    /**
     * 缩放并移动
     *
     * @param avatar   头像图像
     * @param template 模板图像
     * @param bounds   边界坐标
     * @return 结果
     */
    private static BufferedImage resizeAndMoveImage(BufferedImage avatar, BufferedImage template, Rectangle bounds) {
        // 创建与模板大小相同的图像
        BufferedImage outputImage = new BufferedImage(template.getWidth(), template.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = outputImage.createGraphics();

        // 绘制模板到输出图像（保持模板透明背景）
        g2d.drawImage(template, 0, 0, null);

        // 计算头像缩放后的大小
        int avatarWidth = bounds.width;
        int avatarHeight = bounds.height;

        // 将头像调整到指定位置
        g2d.drawImage(avatar, bounds.x, bounds.y, avatarWidth, avatarHeight, null);

        // 释放图形资源
        g2d.dispose();
        return outputImage;
    }

    /**
     * 擦除区域
     *
     * @param image     模板1图像
     * @param maskImage 模板2图像，用于限制保留的区域
     * @return 交集图像
     */
    private static BufferedImage eraseRegion(BufferedImage image, BufferedImage maskImage) {
        // 确保两个图像大小一致
        if (image.getWidth() != maskImage.getWidth() || image.getHeight() != maskImage.getHeight()) {
            throw new IllegalArgumentException("Image and maskImage must have the same dimensions.");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        // TODO 在image基础修改，节省空间

        // 创建一个新图像存储结果
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 获取模板2（maskImage）当前像素的透明度
                int maskAlpha = (maskImage.getRGB(x, y) >> 24) & 0xff;

                if (maskAlpha > 0) {
                    // 如果 maskImage 像素非透明，保留模板1（image）的像素
                    result.setRGB(x, y, image.getRGB(x, y));
                } else {
                    // 如果 maskImage 像素透明，将结果设为完全透明
                    result.setRGB(x, y, 0x00000000);
                }
            }
        }

        return result;
    }

    /**
     * 合并图像
     *
     * @param bottom 底层图像
     * @param top    上层图像
     * @return 结果
     */
    private static BufferedImage mergeLayers(BufferedImage bottom, BufferedImage top) {
        int width = bottom.getWidth();
        int height = bottom.getHeight();

        if (width != top.getWidth() || height != top.getHeight()) {
            throw new IllegalArgumentException("底层图像和上层图像必须有相同的尺寸！");
        }

        // 创建一个新图像存储合并结果
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 绘制底层图像
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(bottom, 0, 0, null);

        // 绘制上层图像
        g2d.drawImage(top, 0, 0, null);

        g2d.dispose();
        return result;
    }
}
