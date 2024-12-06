package com.pngtodds.demo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.pngtodds.domain.*;
import com.pngtodds.utils.*;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HOI4 {
    private final static String TAG = "tag";
    private final static String GENERATOR = System.getProperty("user.dir") + FileUtil.FILE_SEPARATOR + "generator";

    static List<String> characterList = new LinkedList<>();
    static List<String> interfaceLargeList = new LinkedList<>();
    static List<String> interfaceSmallList = new LinkedList<>();
    static List<Localisation> localisationList = new LinkedList<>();
    static List<Localisation> japaneseList = new LinkedList<>();
    static List<Localisation> englishList = new LinkedList<>();
    static List<Localisation> russianList = new LinkedList<>();
    static List<String> recruitList = new LinkedList<>();
    static List<String> mdList = new LinkedList<>();

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
        recruitList.forEach(System.out::println);
        System.out.println(String.join("、", mdList));
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
            Integer[] skills = Arrays.stream(fileNameSplit.length > 2 ? fileNameSplit[1].split("") : new String[]{"1", "1", "1", "1", "1"})
                    .map(Integer::valueOf)
                    .toArray(Integer[]::new);
            Skill skill = new Skill(skills[0], skills[1], skills[2], skills[3], skills[4]);
            String traits = TraitUtils.getTrait(fileNameSplit);
            characterList.add(CharacterUtils.getCharacter(characterKey, large, small, traits, skill));
            interfaceLargeList.add(InterfaceUtils.getLarge(tag, large));
            interfaceSmallList.add(InterfaceUtils.getSmall(tag, small));
            // 头像
            DDSUtils.saveDDS(file.getPath(), large.getPath());
            // 内阁头像
            GfxUtils.saveAdvisorDDS(file, small);
            // 本地化
            localisationList.add(new Localisation(characterKey, chineseName));
            japaneseList.add(new Localisation(characterKey, japaneseName));
            englishList.add(new Localisation(characterKey, englishName));
            // TODO 调用翻译API
            russianList.add(new Localisation(characterKey, englishName));
            recruitList.add(StrUtil.format("recruit_character = {}", characterKey));
            mdList.add(StrUtil.format("{}{}", tag, chineseName));
        });
    }
}
