package com.pngtodds.util;

import java.io.IOException;

public class Demo {
    public static void main(String[] args) throws IOException {
        String inputPath = "src/main/resources/static/钢铁雄心4.png";
        String outputPath = "src/main/resources/static/钢铁雄心4.dds";
        ImgToDds.getDDSName(inputPath, outputPath);
    }
}
