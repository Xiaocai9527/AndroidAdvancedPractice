package com.xiaokun.advance_practive;

import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/11/21
 *      描述  ：https://www.jianshu.com/p/0e656257f25d  作者：张风捷特烈
 *      版本  ：1.0
 * </pre>
 */
public class Xml2Adapter {
    public String xmlApp = "\\src\\main\\res\\layout\\";
    public String adapterApp = "\\src\\main\\java\\com\\xiaokun\\advance_practive";

    @Test
    public void main() {
        String rootDir = System.getProperty("user.dir");
        String xmlPathDir = rootDir + xmlApp;
        String adapterPathDir = rootDir + adapterApp;

        //你的布局xml所在路径
//        File file = new File(xmlPathDir + "item_list_pic.xml");
        File file = new File("D:\\AndroidStudioProjects\\app\\PDYongMaAndroid\\library.yongma\\src\\main\\res\\layout\\item_loan_record_layout.xml");
        System.out.println(file.getPath());
        //你的Adapter的java类放在哪个包里
        File out = new File("D:\\AndroidStudioProjects\\app\\PDYongMaAndroid\\library.yongma\\src\\main\\java\\com\\peidou\\yongma\\ui\\repayment\\adapter\\");
        //你的Adapter的名字--不要加.java
        String name = "LoanRecordAdapter";
        initView(file, out, name);
    }

    @Test
    public void findView() {
        //你的布局xml所在路径
        File file = new File("D:\\AndroidStudioProjects\\TemplateTest\\app\\src\\main\\res\\layout\\item_list_pic.xml");
        findViewById(file);
    }

    private void findViewById(File in) {
        String res = readFile(in);
        HashMap<String, String> map = split(res);
        StringBuilder sb = new StringBuilder();
        map.forEach((id, view) -> {
            sb.append("public ").append(view).append(" ").append(formatId2Field(id)).append(";").append("\r\n");
        });
        sb.append("\n\n");
        map.forEach((id, view) -> {
            sb.append(formatId2Field(id))
                    .append("= itemView.findViewById(R.id.")
                    .append(id).append(");").append("\r\n");

            if ("Button".equals(view)) {
                sb.append(formatId2Field(id) + ".setOnClickListener(v -> {\n" +
                        "        });\n");
            }
        });
        System.out.println(sb.toString());
    }

    /**
     * 读取文件
     *
     * @param in   xml文件路径
     * @param out  输出的java路径
     * @param name
     */
    private static void initView(File in, File out, String name) {
        FileWriter fw = null;
        try {
            HashMap<String, String> map = split(readFile(in));
            String result = contactAdapter(in, out, name, map);

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

    @Test
    public void testDate() {
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        int day = instance.get(Calendar.DAY_OF_MONTH);
        System.out.println(year + "/" + (month + 1) + "/" + day);
    }

    @Test
    public void testCurrentPath() throws IOException {
        System.out.println(System.getProperty("user.dir"));

        File f = new File(this.getClass().getResource("/").getPath());
        System.out.println(f);

        File f1 = new File(this.getClass().getResource("").getPath());
        System.out.println(f1);

        File directory = new File("");//参数为空
        String courseFile = directory.getCanonicalPath();
        System.out.println(courseFile);

        System.out.println(System.getProperty("java.class.path"));
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

    /**
     * 直接拼接出Adapter
     */
    private static String contactAdapter(File file, File out, String name, HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        String path = out.getAbsolutePath();
        path.split("java");

        sb.append("package " + path.split("java\\\\")[1].replaceAll("\\\\", ".") + ";\n");
        System.out.println(sb);
        sb.append("import android.content.Context;\n" +
                "import android.support.annotation.NonNull;\n" +
                "import android.support.constraint.ConstraintLayout;\n" +
                "import android.support.v7.widget.RecyclerView;\n" +
                "import android.view.LayoutInflater;\n" +
                "import android.view.View;\n" +
                "import android.view.ViewGroup;\n" +
                "import android.widget.Button;\n" +
                "import java.util.List;\n" +
                "import java.util.ArrayList;\n" +
                "import android.widget.ImageView;\n" +
                "import android.widget.TextView;\n");
        sb.append("/**\n" +
                " * <pre>\n" +
                " *      作者  ：肖坤\n" +
                " *      时间  ：" + getTimeStr() + "\n" +
                " *      描述  ：\n" +
                " *      版本  ：1.0\n" +
                " * </pre>\n" +
                " */\n");
        sb.append("public class " + name + " extends RecyclerView.Adapter<" + name + ".MyViewHolder> {\n");
        sb.append("private Context mContext;\n");
        sb.append("private LayoutInflater mInflater;\n");
        sb.append("private List<String> mDatas;\n");
        sb.append("public " + name + "(Context context) {\n" +
                "    mContext = context;\n" +
                "    mInflater = LayoutInflater.from(mContext);\n" +
                "}\n");

        sb.append("public void setDatas(List<String> datas) {\n" +
                "   if(mDatas==null){\n" +
                "       mDatas = new ArrayList();\n" +
                "   }\n" +
                "   mDatas.clear();\n" +
                "   mDatas.addAll(datas);\n" +
                "   notifyDataSetChanged();\n" +
                "   }\n");

        String layoutId = file.getName().substring(0, file.getName().indexOf(".x"));
        sb.append("@NonNull\n" +
                "@Override\n" +
                "public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {\n" +
                "    View view = mInflater.inflate(R.layout." + layoutId + ", parent, false);\n" +
                "    return new MyViewHolder(view);\n" +
                "}\n");

        sb.append("@Override \n" +
                "public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {\n");

        map.forEach((id, view) -> {
            if ("Button".equals(view)) {
                sb.append("holder." + formatId2Field(id) + ".setOnClickListener(v -> {\n" +
                        "        });\n");
            }

            if ("TextView".equals(view)) {
                sb.append("holder." + formatId2Field(id) + ".setText(\"\");\n");
            }
            if ("ImageView".equals(view)) {
                sb.append("holder." + formatId2Field(id) + ".setImageBitmap(null);\n");
            }
        });

        sb.append("}\n" +
                "@Override\n" +
                "public int getItemCount() {\n" +
                "return mDatas==null?0:mDatas.size();\n" +
                "}");

        sb.append(contactViewHolder(map));
        return sb.toString();
    }

    /**
     * 连接字符串:ViewHolder
     */
    private static String contactViewHolder(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("class MyViewHolder extends RecyclerView.ViewHolder {\r\n");
        map.forEach((id, view) -> {
            sb.append("public ").append(view).append(" ").append(formatId2Field(id)).append(";").append("\r\n");
        });

        sb.append("public MyViewHolder(View itemView) {\n" +
                "super(itemView);");

        map.forEach((id, view) -> {
            sb.append(formatId2Field(id))
                    .append("= itemView.findViewById(R.id.")
                    .append(id).append(");").append("\r\n");
        });
        sb.append("}\n" +
                "}\n}");

        return sb.toString();
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

    private static HashMap<String, String> split(String res) {
        String[] split = res.split("<");
        HashMap<String, String> viewMap = new HashMap<>();
        for (String s : split) {
            if (s.contains("android:id=\"@+id") && !s.contains("Guideline")) {
                String id = s.split("@")[1];
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
