package com.xiaokun.advance_practive.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xiaokun.advance_practive.database.table.BaseTable;
import com.xiaokun.advance_practive.database.table.ConversationTable;
import com.xiaokun.advance_practive.database.table.MessageTable;
import com.xiaokun.advance_practive.database.table.UserTable;
import com.xiaokun.baselib.util.ContextHolder;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.xiaokun.advance_practive.database.table.BaseTable.DataType.INTEGER;
import static com.xiaokun.advance_practive.database.table.BaseTable.DataType.TEXT;

/**
 * Created by 肖坤 on 2019/2/16.
 *
 * @author 肖坤
 * @date 2019/2/16
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    public static final String DB_NAME = "PdIm.db";
    public static final int DB_VERSION = 4;

    private Context mContext;
    private static DatabaseHelper instance = null;
    private static SQLiteDatabase mDb = null;

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    private static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper(ContextHolder.getContext(), DB_NAME, null, DB_VERSION);
        }
        return instance;
    }

    public static SQLiteDatabase getDatabase() {
        if (mDb == null) {
            mDb = getInstance().getWritableDatabase();
        }
        return mDb;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTable.getSql());
        Log.e(TAG, "用户表创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(UserTable.getSql());
            case 2:
                db.execSQL(ConversationTable.getSql());
            case 3:
                db.execSQL(MessageTable.getSql());
        }
    }
}
