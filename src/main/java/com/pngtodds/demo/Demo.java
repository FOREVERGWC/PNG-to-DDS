package com.pngtodds.demo;

import com.pngtodds.utils.DDSUtils;
import com.pngtodds.utils.TraitUtils;

public class Demo {
    public static void main(String[] args) {
//        String inputPath = "src/main/resources/static/钢铁雄心4.png";
//        String outputPath = "src/main/resources/static/钢铁雄心4.dds";
//        DDSUtils.saveDDS(inputPath, outputPath);
        String trait = TraitUtils.getTrait(new String[]{});
        System.out.println(trait);
    }
}
