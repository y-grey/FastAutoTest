package yph.utils;

import android.content.res.AXmlResourceParser;
import android.util.TypedValue;

import org.xmlpull.v1.XmlPullParser;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by yph on 2018/3/23.
 */

public class ApkUtil {

    public static Map<String, Object> readApk(String apkUrl) {
        ZipFile zipFile;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            zipFile = new ZipFile(apkUrl);
            Enumeration<?> enumeration = zipFile.entries();
            ZipEntry zipEntry = null;
            while (enumeration.hasMoreElements()) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                if (zipEntry.isDirectory()) {

                } else {
                    if ("androidmanifest.xml".equals(zipEntry.getName().toLowerCase())) {
                        AXmlResourceParser parser = new AXmlResourceParser();
                        parser.open(zipFile.getInputStream(zipEntry));
                        while (true) {
                            int type = parser.next();
                            if (type == XmlPullParser.END_DOCUMENT) {
                                break;
                            }
                            String name = parser.getName();
                            if (null != name && name.toLowerCase().equals("manifest")) {
                                for (int i = 0; i != parser.getAttributeCount(); i++) {
                                    if ("versionName".equals(parser.getAttributeName(i))) {
                                        String versionName = getAttributeValue(parser, i);
                                        if (null == versionName) {
                                            versionName = "";
                                        }
                                        map.put("versionName", versionName);
                                    } else if ("package".equals(parser.getAttributeName(i))) {
                                        String packageName = getAttributeValue(parser, i);
                                        if (null == packageName) {
                                            packageName = "";
                                        }
                                        map.put("package", packageName);
                                    } else if ("versionCode".equals(parser.getAttributeName(i))) {
                                        String versionCode = getAttributeValue(parser, i);
                                        if (null == versionCode) {
                                            versionCode = "";
                                            map.put("versionCode", versionCode);
                                        }else {
                                            map.put("versionCode", Integer.valueOf(versionCode));
                                        }

                                    }
                                }
                                break;
                            }
                        }
                    }

                }
            }
            zipFile.close();
        } catch (Exception e) {
            map.put("code", "fail");
            map.put("error", "读取apk失败");
        }
        return map;
    }

    private static String getAttributeValue(AXmlResourceParser parser, int index) {
        int type = parser.getAttributeValueType(index);
        int data = parser.getAttributeValueData(index);
        if (type == TypedValue.TYPE_STRING) {
            return parser.getAttributeValue(index);
        }
        if (type == TypedValue.TYPE_ATTRIBUTE) {
            return String.format("?%s%08X", getPackage(data), data);
        }
        if (type == TypedValue.TYPE_REFERENCE) {
            return String.format("@%s%08X", getPackage(data), data);
        }
        if (type == TypedValue.TYPE_FLOAT) {
            return String.valueOf(Float.intBitsToFloat(data));
        }
        if (type == TypedValue.TYPE_INT_HEX) {
            return String.format("0x%08X", data);
        }
        if (type == TypedValue.TYPE_INT_BOOLEAN) {
            return data != 0 ? "true" : "false";
        }
        if (type == TypedValue.TYPE_DIMENSION) {
            return Float.toString(complexToFloat(data)) + DIMENSION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
        }
        if (type == TypedValue.TYPE_FRACTION) {
            return Float.toString(complexToFloat(data)) + FRACTION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
        }
        if (type >= TypedValue.TYPE_FIRST_COLOR_INT && type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return String.format("#%08X", data);
        }
        if (type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT) {
            return String.valueOf(data);
        }
        return String.format("<0x%X, type 0x%02X>", data, type);
    }

    private static String getPackage(int id) {
        if (id >>> 24 == 1) {
            return "android:";
        }
        return "";
    }

    // ///////////////////////////////// ILLEGAL STUFF, DONT LOOK :)
    public static float complexToFloat(int complex) {
        return (float) (complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4) & 3];
    }

    private static final float RADIX_MULTS[] =
            {
                    0.00390625F, 3.051758E-005F,
                    1.192093E-007F, 4.656613E-010F
            };
    private static final String DIMENSION_UNITS[] = {"px", "dip", "sp", "pt", "in", "mm", "", ""};
    private static final String FRACTION_UNITS[] = {"%", "%p", "", "", "", "", "", ""};

}
