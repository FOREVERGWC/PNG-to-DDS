package com.pngtodds.demo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.pngtodds.domain.FullName;
import com.pngtodds.domain.Localisation;
import com.pngtodds.domain.Small;
import com.pngtodds.utils.*;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Test {
    private final static String GENERATOR = System.getProperty("user.dir") + FileUtil.FILE_SEPARATOR + "generator";

    static List<String> characterList = new LinkedList<>();
    static List<String> interfaceSmallList = new LinkedList<>();
    static List<Localisation> localisationList = new LinkedList<>();
    static List<Localisation> japaneseList = new LinkedList<>();
    static List<Localisation> englishList = new LinkedList<>();
    static List<String> recruitList = new LinkedList<>();
    static List<String> mdList = new LinkedList<>();

    public static void main(String[] args) {
        String path = "C:\\Users\\91658\\Desktop\\头像\\中国将领\\中共顾问";
        Arrays.stream(FileUtil.ls(path))
                .filter(PortraitUtils::isPng)
                .forEach(Test::generator);
        interfaceSmallList.forEach(System.out::println);
        localisationList.forEach(System.out::println);
        japaneseList.forEach(System.out::println);
        englishList.forEach(System.out::println);
        recruitList.forEach(System.out::println);
        System.out.println(String.join("、", mdList));
    }

    @SneakyThrows
    private static void generator(File file) {
        String tag = "PRC";
        String fileName = FileNameUtil.mainName(file);
        String[] fileNameSplit = fileName.split("-");
        FullName fullName = NameUtils.getFullName(fileNameSplit[0]);
        String japaneseName = ZhConverterUtil.convertToTraditional(fileNameSplit[0]);
        String englishName = PinyinUtils.getPinyinName(fullName.getLastName(), fullName.getFirstName());
        String characterKey = CharacterUtils.getCharacterKey(tag, englishName);
        String nameKey = CharacterUtils.getNameKey(tag, characterKey);
        Small small = PortraitUtils.getSmall(tag, nameKey, GENERATOR);
        String traits = String.join(" ", (fileNameSplit.length == 2 ? fileNameSplit[1] : "").split("、"));
        characterList.add(CharacterUtils.getAdvisor(characterKey, tag, small, traits));
        DDSUtils.saveDDS(ImageIO.read(file), small.getPath());
        characterList.forEach(System.out::println);
        interfaceSmallList.add(InterfaceUtils.getSmall(tag, small));
        localisationList.add(new Localisation(characterKey, fileNameSplit[0]));
        japaneseList.add(new Localisation(characterKey, japaneseName));
        englishList.add(new Localisation(characterKey, englishName));
        recruitList.add(StrUtil.format("recruit_character = {}", characterKey));
        mdList.add(StrUtil.format("{}{}", tag, fileNameSplit[0]));
    }
}
