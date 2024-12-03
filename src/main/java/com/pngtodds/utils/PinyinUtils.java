package com.pngtodds.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PinyinUtils {
    /**
     * 获取姓名拼音键值
     *
     * @param lastName  姓氏
     * @param firstName 名字
     * @return 姓名拼音键值
     */
    public static String getPinyinName(String lastName, String firstName) {
        String lastNamePinyin = Arrays.stream(PinyinUtil.getPinyin(lastName).split(" "))
                .map(StrUtil::upperFirst)
                .collect(Collectors.joining());
        String firstNamePinyin = Arrays.stream(PinyinUtil.getPinyin(firstName).split(" "))
                .map(StrUtil::upperFirst)
                .collect(Collectors.joining());
        return StrUtil.format("{} {}", lastNamePinyin, firstNamePinyin);
    }
}
