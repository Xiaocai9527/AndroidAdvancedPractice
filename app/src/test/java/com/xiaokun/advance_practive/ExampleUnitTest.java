package com.xiaokun.advance_practive;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

import static com.xiaokun.baselib.util.Utils.close;
import static org.junit.Assert.*;

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
                bufferedWriter.flush();
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


}