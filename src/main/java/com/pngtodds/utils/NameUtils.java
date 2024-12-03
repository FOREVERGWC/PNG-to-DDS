package com.pngtodds.utils;

import cn.hutool.core.util.StrUtil;
import com.pngtodds.domain.FullName;

public class NameUtils {
    /**
     * 姓名
     */
    public static final String FULL_NAME = "姓名";
    /**
     * 姓氏
     */
    public static final String LAST_NAME = "姓氏";
    /**
     * 名字
     */
    public static final String FIRST_NAME = "名字";
    /**
     * 现存复姓
     */
    private static final String[] COMPLEX_NOW = {"欧阳", "太史", "端木", "上官", "司马", "东方", "独孤", "南宫", "万俟", "闻人", "夏侯", "诸葛", "尉迟", "公羊", "赫连", "澹台", "皇甫", "宗政", "濮阳", "公冶", "太叔", "申屠", "公孙", "慕容", "仲孙", "钟离", "长孙", "宇文", "司徒", "鲜于", "司空", "闾丘", "子车", "亓官", "司寇", "巫马", "公西", "颛孙", "壤驷", "公良", "漆雕", "乐正", "宰父", "谷梁", "拓跋", "夹谷", "轩辕", "令狐", "段干", "百里", "呼延", "东郭", "南门", "羊舌", "微生", "公户", "公玉", "公仪", "梁丘", "公仲", "公上", "公门", "公山", "公坚", "左丘", "公伯", "西门", "公祖", "第五", "公乘", "贯丘", "公皙", "南荣", "东里", "东宫", "仲长", "子书", "子桑", "即墨", "达奚", "褚师", "萨嘛喇", "赫舍里", "萨克达", "钮祜禄", "他塔喇", "喜塔腊", "库雅喇", "瓜尔佳", "舒穆禄", "索绰络", "叶赫那拉", "依尔觉罗", "额尔德特", "讷殷富察", "叶赫那兰", "爱新觉罗", "依尔根觉罗"};

    /**
     * 优先获取复姓姓氏
     *
     * @param name 姓名
     * @return 姓氏
     */
    public static String getComplexLastName(String name) {
        for (String s : COMPLEX_NOW) {
            if (name.startsWith(s)) {
                return s;
            }
        }
        return name.substring(0, 1);
    }

    /**
     * 优先获取复姓姓名
     *
     * @param name 姓名
     * @return 名字
     */
    public static String getComplexFirstName(String name) {
        for (String s : COMPLEX_NOW) {
            if (name.startsWith(s)) {
                return name.substring(s.length());
            }
        }
        return name.substring(1);
    }

    /**
     * 获取姓氏与名字
     *
     * @param name 姓名
     * @return 姓氏与名字
     */
    public static FullName getFullName(String name) {
        if (StrUtil.isBlank(name)) {
            return new FullName("", "");
        }
        FullName fullName;
        if (name.length() <= 2) {
            fullName = new FullName(name.substring(0, 1), name.substring(1));
        } else {
            // 遍历复姓数组
            for (String s : COMPLEX_NOW) {
                if (name.startsWith(s)) {
                    return new FullName(s, name.substring(s.length()));
                }
            }
            // 姓氏没有匹配时，姓名多于5字符，姓与名均在姓氏中,否则采用第一个字为姓
            fullName = new FullName(name.length() >= 5 ? name : name.substring(0, 1),
                    name.length() >= 5 ? "" : name.substring(1));
        }
        return fullName;
    }
}
