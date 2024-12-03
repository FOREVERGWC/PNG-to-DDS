package com.pngtodds.utils;

import cn.hutool.core.util.StrUtil;
import com.pngtodds.domain.Large;
import com.pngtodds.domain.Small;

public class InterfaceUtils {
    public static final String TEMPLATE = """
            \tspriteType = {
            \t\tname = "{}"
            \t\ttexturefile = "gfx/{}/{}/{}"
            \t}""";
    public static final String LARGE = "leaders";
    public static final String SMALL = "interface/ideas/characters";

    /**
     * 获取人物肖像
     *
     * @param tag   国家标签
     * @param large 人物肖像
     * @return 结果
     */
    public static String getLarge(String tag, Large large) {
        return StrUtil.format(InterfaceUtils.TEMPLATE, large.getKey(), InterfaceUtils.LARGE, tag, large.getValue());
    }

    /**
     * 获取内阁肖像
     *
     * @param tag   国家标签
     * @param small 内阁肖像
     * @return 结果
     */
    public static String getSmall(String tag, Small small) {
        return StrUtil.format(InterfaceUtils.TEMPLATE, small.getKey(), InterfaceUtils.SMALL, tag, small.getValue());
    }
}
