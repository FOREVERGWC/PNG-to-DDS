package com.pngtodds.utils;

import cn.hutool.core.util.StrUtil;
import com.pngtodds.domain.Large;
import com.pngtodds.domain.Skill;
import com.pngtodds.domain.Small;

public class CharacterUtils {
    public static final String TEMPLATE = """
            \t{} = {
            \t\tname = {}
            \t\tportraits = {
            \t\t\tarmy = {
            \t\t\t\tlarge = {}
            \t\t\t\tsmall = {}
            \t\t\t}
            \t\t}
            \t\tcorps_commander = {
            \t\t\ttraits = {
            \t\t\t\t{}
            \t\t\t}
            \t\t\tskill = {}
            \t\t\tattack_skill = {}
            \t\t\tdefense_skill = {}
            \t\t\tplanning_skill = {}
            \t\t\tlogistics_skill = {}
            \t\t\tlegacy_id = -1
            \t\t}
            \t}""";

    public static final String TEMPLATE_ADVISOR = """
            \t{} = {
            \t\tname = {}
            \t\tportraits = {
            \t\t\tcivilian = {
            \t\t\t\tsmall = {}
            \t\t\t}
            \t\t}
            \t\tadvisor = {
            \t\t\tslot = {}
            \t\t\tidea_token = {}
            \t\t\tallowed = {
            \t\t\t\toriginal_tag = {}
            \t\t\t}
            \t\t\ttraits = {
            \t\t\t\t{}
            \t\t\t}
            \t\t\tai_will_do = {
            \t\t\t\tfactor = 1
            \t\t\t}
            \t\t}
            \t}""";

    /**
     * 获取人物键值
     *
     * @param tag         国家标签
     * @param englishName 英文姓名
     * @return 人物键值
     */
    public static String getCharacterKey(String tag, String englishName) {
        String name = englishName.replaceAll(" ", "_");
        return StrUtil.format("{}_{}", tag, name);
    }

    /**
     * 获取姓名键值
     *
     * @param tag          国家标签
     * @param characterKey 人物键值
     * @return 姓名键值
     */
    public static String getNameKey(String tag, String characterKey) {
        return StrUtil.removePrefix(characterKey, tag + "_");
    }

    /**
     * 获取人物
     *
     * @param characterKey 人物键值
     * @param large        人物肖像
     * @param small        内阁肖像
     * @param traits       特质
     * @param skill        技能
     * @return 结果
     */
    public static String getCharacter(String characterKey, Large large, Small small, String traits, Skill skill) {
        return StrUtil.format(CharacterUtils.TEMPLATE, characterKey, characterKey, large.getKey(), small.getKey(), traits, skill.getSkill(), skill.getAttack(), skill.getDefense(), skill.getPlanning(), skill.getLogistics());
    }

    /**
     * 获取内阁
     *
     * @param characterKey 人物键值
     * @param tag          国家标签
     * @param small        内阁肖像
     * @param traits       特质
     * @return 结果
     */
    public static String getAdvisor(String characterKey, String tag, Small small, String traits) {
        return StrUtil.format(CharacterUtils.TEMPLATE_ADVISOR, characterKey, characterKey, small.getKey(), "political_advisor", characterKey, tag, traits);
    }
}
