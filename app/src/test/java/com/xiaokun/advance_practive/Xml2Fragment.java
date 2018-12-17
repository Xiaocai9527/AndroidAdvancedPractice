package com.xiaokun.advance_practive;

import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/11/29
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class Xml2Fragment {

    public String company = "xiaokun";
    public String appName = "advance_practive";
    public String xmlApp = "\\src\\main\\res\\layout\\";
    public String activityApp = "\\src\\main\\java\\com\\" + company + "\\" + appName;

    @Test
    public void main() {
        String rootDir = System.getProperty("user.dir");
        String xmlPathDir = rootDir + xmlApp;
        String activityPathDir = rootDir + activityApp + "\\ui\\fragment_nest";

        //你的Activity布局xml所在路径
        File file = new File(xmlPathDir + "fragment_detail.xml");
        //你的Activity的java类放在哪个包里
        File out = new File(activityPathDir);
        //你的Activity的名字--不要加.java
        String name = "NestFragment2";
        initView(file, out, name);
    }

    /**
     * 读取文件
     *
     * @param file xml文件路径
     * @param out  输出的java路径
     * @param name
     */
    private static void initView(File file, File out, String name) {
        FileWriter fw = null;
        try {
            HashMap<String, String> map = split(readFile(file));
            System.out.println(map.toString());
            String result = contactFragment(file, out, name, map);

            //写出到磁盘
            File outFile = new File(out, name + ".java");
            fw = new FileWriter(outFile);
            fw.write(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String contactFragment(File file, File out, String name, HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        String path = out.getAbsolutePath();
        path.split("java");

        sb.append("package " + path.split("java\\\\")[1].replaceAll("\\\\", ".") + ";\n");
        sb.append("import android.content.Context;\n" +
                "import android.content.Intent;\n" +
                "import android.os.Bundle;\n" +
                "import android.support.annotation.Nullable;\n" +
                "import android.view.View;\n" +
                "import android.os.Bundle;\n" +
                "import android.support.v4.app.Fragment;;\n");
        sb.append("/**\n" +
                " * <pre>\n" +
                " *      作者  ：肖坤\n" +
                " *      时间  ：" + getTimeStr() + "\n" +
                " *      描述  ：\n" +
                " *      版本  ：1.0\n" +
                " * </pre>\n" +
                " */\n");
        sb.append("public class " + name + " extends Fragment implements View.OnClickListener{\n");
        map.forEach((id, view) -> {
            sb.append("public ").append(view).append(" ").append(formatId2Field(id)).append(";").append("\r\n");
        });
        sb.append("public static " + name + " newInstance(){\n" +
                "    Bundle args = new Bundle();\n" +
                name + " fragment = new " + name + "();\n" +
                "fragment.setArguments(args);\n" +
                "return fragment;\n" +
                "}\n");
        sb.append("@Override\n");
        sb.append("public void onAttach(Context context) {\n");
        sb.append("super.onAttach(context);\n");
        sb.append("}\n");
        sb.append("@Nullable\n");
        sb.append("@Override\n");
        sb.append("public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {\n");
        sb.append("View view = inflater.inflate(R.layout." + file.getName().substring(0, file.getName().length() - 4) + ", container, false);\n");
        sb.append("initView(view);\n");
        sb.append("return view;\n");
        sb.append("}\n");
        sb.append("private void initView(View view) {\n");
        map.forEach((id, view) -> {
            sb.append(formatId2Field(id))
                    .append("= view.findViewById(R.id.")
                    .append(id).append(");").append("\r\n");
        });

        List<String> stringList = new ArrayList<>();

        //设置点击事件,
        //button直接设置点击事件
        //id以click结尾的设置点击事件
        map.forEach((id, view) -> {
            if (view.equals("Button")) {
                sb.append(formatId2Field(id) + ".setOnClickListener(this);\n");
                stringList.add(id);
            }
            if (id.endsWith("click")) {
                sb.append(formatId2Field(id) + ".setOnClickListener(this);\n");
                stringList.add(id);
            }
        });
        sb.append("}\n");

        sb.append("@Override\n");
        sb.append("public void onClick(View v) {\n");
        sb.append("    switch (v.getId()) {\n");
        for (String view : stringList) {
            sb.append("case R.id." + view + ":\n\n");
            sb.append("break;\n");
        }
        sb.append("default:\n");
        sb.append("break;\n");
        sb.append("}\n");
        sb.append("}\n");
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * 获取时间字符串
     *
     * @return 类似2018/11/22
     */
    private static String getTimeStr() {
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        int day = instance.get(Calendar.DAY_OF_MONTH);
        return year + "/" + (month + 1) + "/" + day;
    }

    private static String formatId2Field(String id) {
        if (id.contains("_")) {
            String[] partStrArray = id.split("_");
            id = "";
            for (String part : partStrArray) {
                String partStr = upAChar(part);
                id += partStr;
            }
        }
        return "m" + id;
    }

    /**
     * 将字符串仅首字母大写
     *
     * @param str 待处理字符串
     * @return 将字符串仅首字母大写
     */
    public static String upAChar(String str) {
        String a = str.substring(0, 1);
        String tail = str.substring(1);
        return a.toUpperCase() + tail;
    }

    /**
     * 读取文件
     *
     * @param in
     * @return
     */
    private static String readFile(File in) {
        if (!in.exists() && in.isDirectory()) {
            return "";
        }

        FileReader fr = null;
        try {
            fr = new FileReader(in);
            //字符数组循环读取
            char[] buf = new char[1024];
            int len = 0;
            StringBuilder sb = new StringBuilder();
            while ((len = fr.read(buf)) != -1) {
                sb.append(new String(buf, 0, len));
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static HashMap<String, String> split(String res) {
        String[] split = res.split("<");
        for (String s : split) {
//            System.out.println(s);
        }
        HashMap<String, String> viewMap = new HashMap<>();
        for (String s : split) {
            if (s.contains("android:id=\"@+id") && !s.contains("Guideline")) {
                for (String s1 : s.split("@")) {
                    System.out.println(s1);
                }

                String id = s.split("@")[1];
                System.out.println(id);
                id = id.substring(id.indexOf("/") + 1, id.indexOf("\""));
                String view = s.split("\r\n")[0];
                String[] viewNameArr = view.split("\\.");
                if (viewNameArr.length > 0) {
                    view = viewNameArr[viewNameArr.length - 1];
                }
                viewMap.put(id, view);
            }
        }
        return viewMap;
    }

}
