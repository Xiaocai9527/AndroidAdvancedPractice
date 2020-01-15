package com.xiaokun.advance_practive;

import com.xiaokun.advance_practive.artimgloader.ArtImageLoader;
import com.xiaokun.advance_practive.network.entity.GankResEntity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static com.xiaokun.baselib.util.Utils.close;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Example local unit test, which will execute on the development machine (host).
 * <p>
 * d:
 * cd D:\AndroidStudioProjects\app\PDServiceAndroid
 * echo =============BEGIN===============
 * gradle clean
 * echo ===========CLEAN DONE============
 * cd module.ui
 * gradle uploadArchives
 * cd ..
 * cd module.common
 * gradle uploadArchives
 * cd ..
 * cd easeui
 * gradle uploadArchives
 * cd ..
 * cd businessApp
 * gradle uploadArchives
 * cd ..
 * cd customerApp
 * gradle uploadArchives
 * cd ..
 * echo ===========UPLOAD DONE===========
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static String str;

    {
        str = "ceshi";
    }


    @Test
    public void main() {
        System.out.println(str);
        System.out.println(111);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("1", null);
        System.out.println(hashMap.toString());
        System.out.println(hashMap.get("1"));
    }


    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void processFile() {
        //重新给bat文件改成一行命令
        File file = new File("C:\\Users\\mayn\\Desktop\\uploadLibrary.bat");
        File file2 = new File("C:\\Users\\mayn\\Desktop\\uploadLibrary2.bat");
        File file3 = new File("C:\\Users\\mayn\\Desktop\\uploadLibrary3.bat");

        file2 = checkFile(file2);
        file3 = checkFile(file3);

        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;
        FileOutputStream fileOutputStream = null;
        FileWriter fileWriter = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            bufferedWriter = new BufferedWriter(new FileWriter(file2));
            fileWriter = new FileWriter(file3);
            String s = null;
            while ((s = bufferedReader.readLine()) != null) {
                bufferedWriter.write(s + " && ");
                fileWriter.write(s + " && ");
                System.out.println(s + " && ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(bufferedReader);
            close(bufferedWriter);
            close(fileWriter);
        }
    }

    /**
     * 创建文件
     */
    private File checkFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    @Test
    public void testFile() {
        //创建文件夹
        File file3 = new File("C:\\Users\\mayn\\Desktop\\uploadLibrary4.bat");
        if (!file3.exists()) {
            file3.mkdirs();
        }
    }

    @Test
    public void generateDimen() {
        File file = new File("D:\\AndroidStudioProjects\\app\\PDYongMaAndroid\\library.yongma\\src\\main\\res\\values\\dimen1.xml");

        BufferedWriter bufferedWriter = null;
//        BufferedReader bufferedReader = null;
        FileOutputStream fileOutputStream = null;
        FileWriter fileWriter = null;
        try {
//            bufferedReader = new BufferedReader(new FileReader(file));
            bufferedWriter = new BufferedWriter(new FileWriter(file));
//            fileWriter = new FileWriter(file);
            String s = null;

            bufferedWriter.write("<?xml version=\"1.0\" encoding=\"utf-8\"?><resources xmlns:tools=\"http://schemas.android.com/tools\">");

            //<dimen name="dimen_24dp" tools:ignore="ResourceName">24dp</dimen>
            //<dimen name="dimen_12sp" tools:ignore="ResourceName">12sp</dimen>
            for (int i = 5; i < 40; i++) {
                bufferedWriter.write("<dimen name=\"dimen_" + i + "sp\" tools:ignore=\"ResourceName\">" + i + "sp</dimen>");
            }
            bufferedWriter.write("</resources>");
//            while ((s = bufferedReader.readLine()) != null) {
//                bufferedWriter.write(s + " && ");
//                fileWriter.write(s + " && ");
//                System.out.println(s + " && ");
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            close(bufferedReader);
            close(bufferedWriter);
            close(fileWriter);
        }
    }

    @Test
    public void hashMapTest() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(1, "111");

        HashMap<Integer, String> hashMap1 = new HashMap<>();
        hashMap1.put(2, "222");
        hashMap1.put(3, "333");

        hashMap.putAll(hashMap1);


        System.out.println(hashMap.size());
    }

    @Test
    public void testList() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        list.add(list.size(), "4");

        System.out.println(list.toString());
    }

    @Test
    public void testList1() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        //验证
        List<String> oldItems = new ArrayList<>(list);
        list.add("4");

        System.out.println(list.toString());
        System.out.println(oldItems.toString());
    }

    @Test
    public void testList2() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        //验证
        List<String> oldItems = new ArrayList<>();
        oldItems.add("4");
        oldItems.add("5");
        oldItems.add("6");

//        oldItems.addAll(0, list);
        oldItems.addAll(list);
        System.out.println(oldItems.toString());

    }

    @Test
    public void testObject() {
        String a = "1234";

        modify(a);

        System.out.println(a);
    }

    private void modify(String str) {
        str += "5";
    }

    int num = 1000;

    @Test
    public void testConcurrencyThread() {
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doSomething();
                }

                private void doSomething() {
                    System.out.println(Thread.currentThread().getName() + " do in work thread");
                    reduceNum();
                }

                private synchronized void reduceNum() {
                    num = num - 1;
                    System.out.println(Thread.currentThread().getName() + " num:" + num);
                    if (num == 0) {
                        System.out.println("last thread execute finish");
                    }
                }
            }).start();
        }

        //System.out.println("do in main thread");

    }

}