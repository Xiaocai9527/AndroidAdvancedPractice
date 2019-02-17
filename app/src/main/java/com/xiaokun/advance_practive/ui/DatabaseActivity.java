package com.xiaokun.advance_practive.ui;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.database.DatabaseHelper;
import com.xiaokun.advance_practive.database.dao.UserDao;
import com.xiaokun.advance_practive.entity.User;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by 肖坤 on 2019/2/17.
 *
 * @author 肖坤
 * @date 2019/2/17
 */

public class DatabaseActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;

    public static void start(Context context) {
        Intent starter = new Intent(context, DatabaseActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
    }

    private void createDb() {
        if (mDb == null) {
            mDb = DatabaseHelper.getDatabase();
        }
    }

    public void createBook(View view) {
        createDb();
    }

    public void addBookDatas(View view) {
        createDb();
//        mDb.execSQL("insert into Book (name,author,pages,price) values(?,?,?,?)",
//                new String[]{"第一行代码", "郭霖", "100", "56"});
        mDb.execSQL("insert into Book (name,author,pages,price) " +
                "values(\"第二行代码\", \"郭霖\", \"100\", \"56\")");
    }


    public void addUserDatas(View view) {
        createDb();
        User user = new User();
        user.userId = testRandom1();
        user.gender = 0;
        user.phone = "13812341234";
        user.nickName = "小菜";
        user.name = "肖坤";
        UserDao.getInstance().insert(user);
    }

    //生成随机数
    private int testRandom1() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            System.out.println("random.nextInt()=" + random.nextInt());
        }
        System.out.println("/////以上为testRandom1()的测试///////");
        return random.nextInt();
    }


    //在一定范围内生成随机数.
    //比如此处要求在[0 - n)内生成随机数.
    //注意:包含0不包含n
    private void testRandom2() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            System.out.println("random.nextInt()=" + random.nextInt(20));
        }
        System.out.println("/////以上为testRandom2()的测试///////");
    }


    //在一定范围内生成不重复的随机数
    //在testRandom2中生成的随机数可能会重复.
    //在此处避免该问题
    private void testRandom3() {
        HashSet<Integer> integerHashSet = new HashSet<Integer>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randomInt = random.nextInt(20);
            System.out.println("生成的randomInt=" + randomInt);
            if (!integerHashSet.contains(randomInt)) {
                integerHashSet.add(randomInt);
                System.out.println("添加进HashSet的randomInt=" + randomInt);
            } else {
                System.out.println("该数字已经被添加,不能重复添加");
            }
        }
        System.out.println("/////以上为testRandom3()的测试///////");
    }
}
