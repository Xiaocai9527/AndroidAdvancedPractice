package com.xiaokun.advance_practive.emoji;


import android.support.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EmojiFilter {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        System.out.println(Integer.toHexString(25105));
        System.out.println("\u6211");
        String s = "我";//'我' 25105
        s = "坤";
        char a = (char) -1;
        Matcher matcher = pattern.matcher(s);
        boolean b = matcher.find();
        System.out.println(b);

        System.out.println(s);
        for (char c : new StringBuilder().appendCodePoint(Integer.parseInt("25105", 16)).toString().toCharArray()) {
            System.out.print("\\u" + Integer.toHexString(c));
        }
    }

    //Pattern pattern = Pattern.compile("[^\\u0000-\\uFFFF]");

    Pattern pattern = Pattern.compile(getFullEmojiRegex());

    @Test
    public void parseJson() throws IOException {
        Document document = Jsoup.connect("http://www.unicode.org/emoji/charts/full-emoji-list.html")
                .timeout(0)
                .maxBodySize(0)
                .get();
        Elements codes = document.getElementsByClass("code");
        System.out.println(codes.size());

        StringBuilder stringBuilder = new StringBuilder();
        for (Element code : codes) {
            String text = code.text().replace("U+", "0x");
            stringBuilder.append(text).append(",");
            System.out.println(text);
        }
        saveCodesToFile(stringBuilder.toString());
    }

    private void saveCodesToFile(String input) throws FileNotFoundException {
        String rootDir = System.getProperty("user.dir");
        String fileDirPath = rootDir + "/src/test/java/com/xiaokun/advance_practive/emoji/codes.txt";

        try (PrintWriter out = new PrintWriter(fileDirPath)) {
            out.println(input);
        }
    }

    @Test
    public void testPattern() throws IOException {
        String rootDir = System.getProperty("user.dir");
        String fileDirPath = rootDir + "/src/test/java/com/xiaokun/advance_practive/emoji/codes.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileDirPath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

            String allCodes = sb.toString();
            allCodes = allCodes.replaceAll("0x", "");
            System.out.println(allCodes);

            String[] split = allCodes.split(",");
            System.out.println("表情数量:" + split.length);
            boolean filter = true;
            HashMap<String, Boolean> map = new HashMap<>();
            java.util.List<String> strings = new ArrayList<>();
            for (String s : split) {
                String[] s1 = s.split(" ");
                if (s1.length > 1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s2 : s1) {
                        stringBuilder.appendCodePoint(Integer.parseInt(s2, 16));
                    }
                    boolean b = pattern.matcher(stringBuilder).find();
                    if (!b) {
                        filter = false;
                        map.put(stringBuilder.toString(), false);
//                        System.out.println("s2:" + stringBuilder);
//                        System.out.println("s1:" + Arrays.toString(s1));
                        strings.add(s);
                    }
                } else {
                    StringBuilder stringBuilder = new StringBuilder().appendCodePoint(Integer.parseInt(s, 16));
                    boolean b = pattern.matcher(stringBuilder).find();
                    if (!b) {
                        filter = false;
                        map.put(stringBuilder.toString(), false);
//                        System.out.println(stringBuilder);
//                        System.out.println(s);
                        strings.add(s);
                    }
                }
            }
            System.out.println("正则:" + filter);
            System.out.println("未过滤数:" + map.size());
            System.out.println(map.toString());

            java.util.List<Integer> integers = new ArrayList<>();
            java.util.List<String> stringList = new ArrayList<>();
            for (String string : strings) {
                String[] s = string.split(" ");
                //length 大于1 的是表情多码点
                if (s.length > 1) {
                    stringList.add(parseHexArray(s));
//                    StringBuilder str = new StringBuilder();
//                    for (String s1 : s) {
//                        str.append(parseHex(s1));
//                        //integers.add(parseHex(s1));
//                    }
                    //stringList.add(str.toString());
                } else {
                    integers.add(parseHex(string));
                }
//                int i = Integer.parseInt(string, 16);
//                integers.add(i);
//                System.out.println(string + ";" + Integer.parseInt(string, 16));
//                for (char c : new StringBuilder().appendCodePoint(Integer.parseInt(string, 16)).toString().toCharArray()) {
//                    System.out.print("\\u" + Integer.toHexString(c));
//                }
//                System.out.println();
            }

            Collections.sort(integers);
            //Collections.sort(stringList);
            System.out.println(integers.toString());
            System.out.println(stringList.toString());
        }
    }

    private String parseHexArray(String[] hexs) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String hex : hexs) {
            int i = Integer.parseInt(hex, 16);
            for (char c : new StringBuilder().appendCodePoint(i).toString().toCharArray()) {
                System.out.print("\\u" + Integer.toHexString(c));
                stringBuilder.append("\\u").append(Integer.toHexString(c));
            }
        }
        System.out.println();
        System.out.println(Arrays.toString(hexs));
        return stringBuilder.toString();
    }

    private int parseHex(String hex) {
        int i = Integer.parseInt(hex, 16);
        //integers.add(i);
        System.out.println(hex + ";" + Integer.parseInt(hex, 16));
        for (char c : new StringBuilder().appendCodePoint(Integer.parseInt(hex, 16)).toString().toCharArray()) {
            System.out.print("\\u" + Integer.toHexString(c));
        }
        System.out.println();
        return i;
    }

    private static final String MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS = "[\\uD83C\\uDF00-\\uD83D\\uDDFF]";

    private static final String SUPPLEMENTAL_SYMBOLS_AND_PICTOGRAPHS = "[\\uD83E\\uDD00-\\uD83E\\uDDFF]";

    private static final String EMOTICONS = "[\\uD83D\\uDE00-\\uD83D\\uDE4F]";

    private static final String TRANSPORT_AND_MAP_SYMBOLS = "[\\uD83D\\uDE80-\\uD83D\\uDEFF]";

    private static final String MISCELLANEOUS_SYMBOLS = "[\\u2600-\\u26FF]\\uFE0F?";

    private static final String DINGBATS = "[\\u2700-\\u27BF]\\uFE0F?";

    private static final String ENCLOSED_ALPHANUMERICS = "\\u24C2\\uFE0F?";

    private static final String REGIONAL_INDICATOR_SYMBOL = "[\\uD83C\\uDDE6-\\uD83C\\uDDFF]{1,2}";

    private static final String ENCLOSED_ALPHANUMERIC_SUPPLEMENT = "[\\uD83C\\uDD70\\uD83C\\uDD71\\uD83C\\uDD7E\\uD83C\\uDD7F\\uD83C\\uDD8E\\uD83C\\uDD91-\\uD83C\\uDD9A]\\uFE0F?";

    private static final String BASIC_LATIN = "[\\u0023\\u002A\\u0030-\\u0039]\\uFE0F?\\u20E3";

    private static final String ARROWS = "[\\u2194-\\u2199\\u21A9-\\u21AA]\\uFE0F?";

    private static final String MISCELLANEOUS_SYMBOLS_AND_ARROWS = "[\\u2B05-\\u2B07\\u2B1B\\u2B1C\\u2B50\\u2B55]\\uFE0F?";

    private static final String SUPPLEMENTAL_ARROWS = "[\\u2934\\u2935]\\uFE0F?";

    private static final String CJK_SYMBOLS_AND_PUNCTUATION = "[\\u3030\\u303D]\\uFE0F?";

    private static final String ENCLOSED_CJK_LETTERS_AND_MONTHS = "[\\u3297\\u3299]\\uFE0F?";

    private static final String ENCLOSED_IDEOGRAPHIC_SUPPLEMENT = "[\\uD83C\\uDE01\\uD83C\\uDE02\\uD83C\\uDE1A\\uD83C\\uDE2F\\uD83C\\uDE32-\\uD83C\\uDE3A\\uD83C\\uDE50\\uD83C\\uDE51]\\uFE0F?";

    private static final String GENERAL_PUNCTUATION = "[\\u203C\\u2049]\\uFE0F?";

    private static final String GEOMETRIC_SHAPES = "[\\u25AA\\u25AB\\u25B6\\u25C0\\u25FB-\\u25FE]\\uFE0F?";

    private static final String LATIN_SUPPLEMENT = "[\\u00A9\\u00AE]\\uFE0F?";

    private static final String LETTERLIKE_SYMBOLS = "[\\u2122\\u2139]\\uFE0F?";

    private static final String MAHJONG_TILES = "\\uD83C\\uDC04\\uFE0F?";

    private static final String PLAYING_CARDS = "\\uD83C\\uDCCF\\uFE0F?";

    private static final String MISCELLANEOUS_TECHNICAL = "[\\u231A\\u231B\\u2328\\u23CF\\u23E9-\\u23F3\\u23F8-\\u23FA]\\uFE0F?";

    public static final String ADD_1 = "[\\ud83d\\udfe0-\\ud83d\\udfeb]";
    public static final String ADD_2 = "[\\ud83e\\ude70-\\ud83e\\ude74]";
    public static final String ADD_3 = "[\\ud83e\\ude78-\\ud83e\\ude7a]";
    public static final String ADD_4 = "[\\ud83e\\ude80-\\ud83e\\ude86]";
    public static final String ADD_5 = "[\\ud83e\\ude90-\\ud83e\\udea8]";
    public static final String ADD_6 = "[\\ud83e\\udeb0-\\ud83e\\udeb6]";
    public static final String ADD_7 = "[\\ud83e\\udec0-\\ud83e\\udec2]";
    public static final String ADD_8 = "[\\ud83e\\uded0-\\ud83e\\uded6]";

    public static String getRegex() {
        return "(?:" +
                ADD_1 + "|"
                + ADD_2 + "|"
                + ADD_3 + "|"
                + ADD_4 + "|"
                + ADD_5 + "|"
                + ADD_6 + "|"
                + ADD_7 + "|"
                + ADD_8 + ")";
    }

    @NonNull
    public static String getFullEmojiRegex() {
        return "(?:"
                + MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS + "|"
                + SUPPLEMENTAL_SYMBOLS_AND_PICTOGRAPHS + "|"
                + EMOTICONS + "|"
                + TRANSPORT_AND_MAP_SYMBOLS + "|"
                + MISCELLANEOUS_SYMBOLS + "|"
                + DINGBATS + "|"
                + ENCLOSED_ALPHANUMERICS + "|"
                + REGIONAL_INDICATOR_SYMBOL + "|"
                + ENCLOSED_ALPHANUMERIC_SUPPLEMENT + "|"
                + BASIC_LATIN + "|"
                + ARROWS + "|"
                + MISCELLANEOUS_SYMBOLS_AND_ARROWS + "|"
                + SUPPLEMENTAL_ARROWS + "|"
                + CJK_SYMBOLS_AND_PUNCTUATION + "|"
                + ENCLOSED_CJK_LETTERS_AND_MONTHS + "|"
                + ENCLOSED_IDEOGRAPHIC_SUPPLEMENT + "|"
                + GENERAL_PUNCTUATION + "|"
                + GEOMETRIC_SHAPES + "|"
                + LATIN_SUPPLEMENT + "|"
                + LETTERLIKE_SYMBOLS + "|"
                + MAHJONG_TILES + "|"
                + PLAYING_CARDS + "|"
                + MISCELLANEOUS_TECHNICAL + "|"
                + ADD_1 + "|"
                + ADD_2 + "|"
                + ADD_3 + "|"
                + ADD_4 + "|"
                + ADD_5 + "|"
                + ADD_6 + "|"
                + ADD_7 + "|"
                + ADD_8 + ")";
    }
}