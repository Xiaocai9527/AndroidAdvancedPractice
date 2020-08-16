package com.xiaokun.advance_practive.auto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by xiaokun on 2020/8/15.
 *
 * @author xiaokun
 * @date 2020/8/15
 */
class BufferedInputFile {

    public static String read(String filename) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = in.readLine()) != null)
            sb.append(s).append("\n");
        in.close();
        return sb.toString();
    }

}
