package com.pngtodds.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.pngtodds.domain.Large;
import com.pngtodds.domain.Small;

import java.io.File;

public class PortraitUtils {
    /**
     * 判断是否为PNG图片
     *
     * @param file 文件
     * @return 结果
     */
    public static boolean isPng(File file) {
        return "png".equalsIgnoreCase(FileNameUtil.extName(file));
    }

    /**
     * 获取人物肖像
     *
     * @param tag    国家标签
     * @param key    姓名键值
     * @param parent 父级路径
     * @return 人物肖像
     */
    public static Large getLarge(String tag, String key, String parent) {
        String base = parent + File.separator + "gfx" + FileUtil.FILE_SEPARATOR + "leaders" + File.separator + tag;
        FileUtil.mkdir(base);
        String largeKey = PortraitUtils.getLargeKey(key);
        String gfx = PortraitUtils.getLargeGfx(tag, key);
        String path = base + FileUtil.FILE_SEPARATOR + gfx;
        return new Large(largeKey, gfx, path);
    }

    /**
     * 获取内阁肖像
     *
     * @param tag    国家标签
     * @param key    姓名键值
     * @param parent 父级路径
     * @return 人物肖像
     */
    public static Small getSmall(String tag, String key, String parent) {
        String base = parent + File.separator + "gfx" + FileUtil.FILE_SEPARATOR + "interface" + File.separator + "ideas" + FileUtil.FILE_SEPARATOR + "characters" + FileUtil.FILE_SEPARATOR + tag;
        FileUtil.mkdir(base);
        String smallKey = PortraitUtils.getSmallKey(key);
        String gfx = PortraitUtils.getSmallGfx(key);
        String path = base + FileUtil.FILE_SEPARATOR + gfx;
        return new Small(smallKey, gfx, path);
    }

    /**
     * 获取人物肖像键值
     *
     * @param key 姓名键值
     * @return 人物肖像键值
     */
    private static String getLargeKey(String key) {
        return StrUtil.format("{}_{}", "GFX_portrait", key);
    }

    /**
     * 获取内阁肖像键值
     *
     * @param key 姓名键值
     * @return 内阁肖像键值
     */
    private static String getSmallKey(String key) {
        return StrUtil.format("{}_{}_{}", "GFX_portrait", key, "small");
    }

    /**
     * 获取人物肖像文件名称
     *
     * @param tag 国家标签
     * @param key 姓名键值
     * @return 文件名称
     */
    private static String getLargeGfx(String tag, String key) {
        return StrUtil.format("{}_{}_{}{}", "portrait", tag, key, ".dds");
    }

    /**
     * 获取内阁肖像文件名称
     *
     * @param key 姓名键值
     * @return 文件名称
     */
    private static String getSmallGfx(String key) {
        return StrUtil.format("{}_{}{}", "idea_generic", key, ".dds");
    }
}
