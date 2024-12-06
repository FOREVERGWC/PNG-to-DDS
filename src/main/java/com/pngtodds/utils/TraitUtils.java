package com.pngtodds.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TraitUtils {
    static final Map<String, String> map = new HashMap<>();

    static {
        map.put("步兵军官", "infantry_officer");
        map.put("骑兵军官", "cavalry_officer");
        map.put("职业军官", "career_officer");
        map.put("工兵指挥官", "trait_engineer");
        map.put("政治关联者", "politically_connected");
        map.put("严厉指挥官", "harsh_leader");
        map.put("优秀参谋辅佐", "skilled_staffer");
    }

    /**
     * 获取特质
     *
     * @param arr 数组
     * @return 特质
     */
    public static String getTrait(String[] arr) {
        return String.join(" ",
                Arrays.stream(Optional.of((arr.length == 3 ? arr[2] : "").split("、"))
                                .orElse(new String[0]))
                        .map(TraitUtils::getMappedTrait)
                        .toList());
    }

    /**
     * 获取特质映射
     *
     * @param key 键值
     * @return 特质
     */
    private static String getMappedTrait(String key) {
        return map.getOrDefault(key, key);
    }
}
