package yph.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Convert {

    public static void main(String[] args) {
        File[] fileList = new File("test-output/html").listFiles();
        for (File file : fileList) {
            cover(file.getPath());
        }
    }

    public static void cover(String path) {
        File srcFile = new File(path + ".tmp");
        File destFile = new File(path);
        try {
            FileUtils.copyFile(destFile, srcFile);
            FileUtils.writeLines(destFile, "UTF-8", FileUtils.readLines(srcFile, "GBK"));
            if (srcFile.exists())
                srcFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
