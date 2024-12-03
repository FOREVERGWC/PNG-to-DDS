package com.pngtodds.demo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.pngtodds.domain.*;
import com.pngtodds.utils.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HOI4 {
    private final static String TAG = "tag";
    private final static String GENERATOR = System.getProperty("user.dir") + FileUtil.FILE_SEPARATOR + "generator";
    private final static String ADVISOR_TEMPLATE = "src/main/resources/static/内阁模板.png";
    private final static String ADVISOR_TEMPLATE_2 = "src/main/resources/static/内阁模板2.png";
    private final static String ADVISOR_TEMPLATE_3 = "src/main/resources/static/内阁模板3.png";

    static List<String> characterList = new LinkedList<>();
    static List<String> interfaceLargeList = new LinkedList<>();
    static List<String> interfaceSmallList = new LinkedList<>();
    static List<Localisation> localisationList = new LinkedList<>();
    static List<Localisation> japaneseList = new LinkedList<>();
    static List<Localisation> englishList = new LinkedList<>();
    static List<Localisation> russianList = new LinkedList<>();

    public static void main(String[] args) throws URISyntaxException {
        String path = Paths.get(HOI4.class.getClassLoader().getResource(TAG).toURI()).toString();
        Arrays.stream(FileUtil.ls(path))
                .filter(File::isDirectory)
                .forEach(HOI4::generator);
        characterList.forEach(System.out::println);
        interfaceLargeList.forEach(System.out::println);
        interfaceSmallList.forEach(System.out::println);
        localisationList.forEach(System.out::println);
        japaneseList.forEach(System.out::println);
        englishList.forEach(System.out::println);
//        russianList.forEach(System.out::println);
    }

    private static void generator(File item) {
        FileUtil.loopFiles(item, PortraitUtils::isPng).forEach(file -> {
            String tag = file.getParentFile().getName();
            String fileName = FileNameUtil.mainName(file);
            String[] fileNameSplit = fileName.split("-");
            String chineseName = fileNameSplit[0];
            String japaneseName = ZhConverterUtil.convertToTraditional(fileNameSplit[0]);
            FullName fullName = NameUtils.getFullName(chineseName);
            String englishName = PinyinUtils.getPinyinName(fullName.getLastName(), fullName.getFirstName());
            String characterKey = CharacterUtils.getCharacterKey(tag, englishName);
            String nameKey = CharacterUtils.getNameKey(tag, characterKey);
            Large large = PortraitUtils.getLarge(tag, nameKey, GENERATOR);
            Small small = PortraitUtils.getSmall(tag, nameKey, GENERATOR);
            Integer[] skills = Arrays.stream(fileNameSplit[1].split("")).map(Integer::valueOf).toArray(Integer[]::new);
            Skill skill = new Skill(skills[0], skills[1], skills[2], skills[3], skills[4]);
            String traits = TraitUtils.getTrait(fileNameSplit);
            characterList.add(CharacterUtils.getCharacter(characterKey, large, small, traits, skill));
            interfaceLargeList.add(InterfaceUtils.getLarge(tag, large));
            interfaceSmallList.add(InterfaceUtils.getSmall(tag, small));
            // 头像
            DDSUtils.saveDDS(file.getPath(), large.getPath());
            // 内阁头像
            saveAdvisorDDS(file, small);
            // 本地化
            localisationList.add(new Localisation(characterKey, chineseName));
            japaneseList.add(new Localisation(characterKey, japaneseName));
            englishList.add(new Localisation(characterKey, englishName));
            // TODO 调用翻译API
            russianList.add(new Localisation(characterKey, englishName));
        });
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
     * 将模板2（maskImage）的非透明部分应用到模板1（image），仅保留交集部分
     *
     * @param image     模板1图像
     * @param maskImage 模板2图像，用于限制保留的区域
     * @return 处理后的图像，仅保留交集部分
     */
    private static BufferedImage eraseRegion(BufferedImage image, BufferedImage maskImage) {
        // 确保两个图像大小一致
        if (image.getWidth() != maskImage.getWidth() || image.getHeight() != maskImage.getHeight()) {
            throw new IllegalArgumentException("Image and maskImage must have the same dimensions.");
        }

        int width = image.getWidth();
        int height = image.getHeight();

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
     * 合并两张图像，overlayImage 在 baseImage 之上
     *
     * @param baseImage    底层图像
     * @param overlayImage 上层图像
     * @return 合并后的图像
     */
    private static BufferedImage mergeLayers(BufferedImage baseImage, BufferedImage overlayImage) {
        // 确保两个图像大小一致
        if (baseImage.getWidth() != overlayImage.getWidth() || baseImage.getHeight() != overlayImage.getHeight()) {
            throw new IllegalArgumentException("BaseImage and OverlayImage must have the same dimensions.");
        }

        int width = baseImage.getWidth();
        int height = baseImage.getHeight();

        // 创建一个新图像存储合并结果
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 绘制底层图像
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(baseImage, 0, 0, null);

        // 绘制上层图像
        g2d.drawImage(overlayImage, 0, 0, null);

        g2d.dispose();
        return result;
    }

    /**
     * 保存内阁肖像
     *
     * @param file  文件
     * @param small 内阁肖像
     */
    private static void saveAdvisorDDS(File file, Small small) {
        try {
            BufferedImage template = ImageIO.read(new File(ADVISOR_TEMPLATE));
            BufferedImage template2 = ImageIO.read(new File(ADVISOR_TEMPLATE_2));
            BufferedImage template3 = ImageIO.read(new File(ADVISOR_TEMPLATE_3));
            BufferedImage avatar = ImageIO.read(file);
            // 获取模板非透明区域的边界坐标
            Rectangle bounds = getNonTransparentBounds(template);
            // 修改头像大小和位置
            BufferedImage resizedAvatar = resizeAndMoveImage(avatar, template, bounds);
            // 在 resizedAvatar 上擦去 template2 中的指定区域
            BufferedImage finalAvatar = eraseRegion(resizedAvatar, template2);
            // 合并 template2 和 template3，template3 在上层
            BufferedImage mergedTemplate = mergeLayers(finalAvatar, template3);
            DDSUtils.saveDDS(mergedTemplate, small.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
