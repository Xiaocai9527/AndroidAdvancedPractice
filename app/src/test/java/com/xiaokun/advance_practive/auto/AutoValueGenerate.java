package com.xiaokun.advance_practive.auto;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AutoValueGenerate {

    String path = "/Users/frontend/AndroidStudioProjects/AndroidAdvancedPractice/app/src/test/java/com/xiaokun/advance_practive/auto/People.java";

    String regex1 = "public class";
    Pattern classPattern = Pattern.compile(regex1);
    String regex2 = "public class \\w* ";
    Pattern classNamePattern = Pattern.compile(regex2);
    String[] types = new String[]{"String", "int", "boolean", "float", "long", "double", "List", "Custom"};
    private Set<String> mCustomeSet;

    @Test
    public void generateCode() throws IOException {
        String read = BufferedInputFile.read(path);
        Matcher classNameMatcher = classNamePattern.matcher(read);
        String className = "";
        if (classNameMatcher.find()) {
            className = classNameMatcher.group().replace("public class ", "").trim();
            ////System.out.println(className);
        }
        Matcher matcher = classPattern.matcher(read);
        String s = matcher.replaceFirst("@AutoValue\n" + "public abstract class ");

        for (String type : types) {
            s = parser(type, s);
        }

        Pattern pattern = Pattern.compile("public abstract.*;");
        Matcher matcher1 = pattern.matcher(s);
        List<String> strings = new ArrayList<>();
        while (matcher1.find()) {
            String group = matcher1.group();
            group = group.replace("public abstract ", "").replace("();", "").trim();
            strings.add(group);
        }

        s = s.replace("}", "");
        s += "@NonNull public static " + className + " create( " + getWithTypeString(strings)
                + "){ return new AutoValue_" + className + "(" + getString(strings) + ");}}";
        System.out.println(s);

        saveStrToFile(s, path);
    }

    @Test
    public void generateAssociatedCode() throws IOException {
        mCustomeSet = new HashSet<>();
        String projectRootPath = path.replaceAll("\\\\", ".").replaceAll("com.*", "");
        //System.out.println(projectRootPath);
        //System.out.println(path);
        String read = BufferedInputFile.read(path);
        Matcher classNameMatcher = classNamePattern.matcher(read);
        String className = "";
        if (classNameMatcher.find()) {
            className = classNameMatcher.group().replace("public class ", "").trim();
            ////System.out.println(className);
        }
        String currentPackagePath = path.replaceAll("\\\\", ".").replaceAll(className + ".java", "");
        //System.out.println(currentPackagePath);
        Matcher matcher = classPattern.matcher(read);
        String s = matcher.replaceFirst("@AutoValue\n" + "public abstract class ");

        for (String type : types) {
            s = parser(type, s);
        }

        Pattern pattern = Pattern.compile("public abstract.*;");
        Matcher matcher1 = pattern.matcher(s);
        List<String> strings = new ArrayList<>();
        while (matcher1.find()) {
            String group = matcher1.group();
            group = group.replace("public abstract ", "").replace("();", "").trim();
            strings.add(group);
        }
        ////System.out.println("strings:" + strings.toString());

        s = s.replace("}", "");
        s += "@NonNull public static " + className + " create( " + getWithTypeString(strings)
                + "){ return new AutoValue_" + className + "(" + getString(strings) + ");}}";
        System.out.println(s);

        saveStrToFile(s, path);
        if (mCustomeSet.isEmpty()) {
            return;
        }
        for (String custom : mCustomeSet) {
            String customStr = getSplit(custom);
            Pattern compile = Pattern.compile("com.*." + customStr + ";");
            Matcher matcher2 = compile.matcher(s);
            if (matcher2.find()) {
                path = (projectRootPath + matcher2.group()).replaceAll("\\.", "\\\\")
                        .replaceAll(";", ".java");
                generateAssociatedCode();
            } else {
                path = (currentPackagePath + customStr).replaceAll("\\.", "\\\\") + ".java";
                generateAssociatedCode();
            }
        }
    }

    private String getSplit(String str) {
        String[] s = str.split(" ");
        if (s.length > 1) {
            return s[0];
        } else {
            return str;
        }
    }

    private String parser(String type, String text) {
        //System.out.println(type);
        Pattern pattern;
        if (isListDataType(type)) {
            pattern = Pattern.compile("public " + type + "<" + "\\w*>" + " " + "\\w*;");
        } else {
            //自定义类型
            pattern = Pattern.compile("public \\w* \\w*;");
        }
        Matcher matcher = pattern.matcher(text);
        List<String> properties = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group();
            group = group.replace("public ", "").replace(";", "").trim();
            properties.add(group);
            if (mCustomeSet == null) {
                continue;
            }
            String typeStr = getSplit(group);
            if (isCustomDataType(typeStr) && !isListDataType(type)) {
                mCustomeSet.add(group);
            }
        }
        //System.out.println("customeList:" + mCustomeSet.toString());
        if (properties.isEmpty()) {
            return text;
        }
        //System.out.println(properties.toString());
        int i = 0;
        for (String property : properties) {
            i++;
            if (isBaseDataType(getSplit(property))) {
                text = matcher.replaceFirst("public abstract " + property + "();");
            } else {
                text = matcher.replaceFirst("@NonNull public abstract " + property + "();");
            }
            if (i < properties.size()) {
                return parser(type, text);
            }
            break;
        }
        return text;
    }

    private String getWithTypeString(List<String> strings) {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (String withTypeField : strings) {
            i++;
            if (isBaseDataType(getSplit(withTypeField))) {
                stringBuilder.append(withTypeField);
                if (i < strings.size()) {
                    stringBuilder.append(",");
                }
            } else {
                stringBuilder.append("@NonNull ").append(withTypeField);
                if (i < strings.size()) {
                    stringBuilder.append(",");
                }
            }
        }
        return stringBuilder.toString();
    }

    private String getString(List<String> strings) {
        StringBuilder stringBuilder = new StringBuilder();
        int j = 0;
        for (String field : strings) {
            j++;
            String[] s1 = field.split(" ");
            if (s1.length > 1) {
                stringBuilder.append(s1[s1.length - 1]);
            } else {
                stringBuilder.append(field);
            }
            if (j < strings.size()) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

    private boolean isBaseDataType(String type) {
        return type.contains("int")
                || type.contains("boolean")
                || type.contains("float")
                || type.contains("long")
                || type.contains("double");
    }

    private boolean isListDataType(String type) {
        return "List".equals(type);
    }

    private boolean isCustomDataType(String type) {
        return !isBaseDataType(type) && (!isListDataType(type))
                && (!"String".equals(type));
    }

    private void saveStrToFile(String text, String path) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new StringReader(text));
             PrintWriter printWriter = new PrintWriter(path)) {
            int lineCount = 1;
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                ////System.out.println(lineCount++);
                printWriter.println(s);
            }
        }
    }
}