package com.xiaokun.advance_practive.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xiaokun.advance_practive.database.DatabaseHelper;
import com.xiaokun.advance_practive.database.table.UserBaseTable;
import com.xiaokun.advance_practive.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 肖坤 on 2019/2/16.
 *
 * @author 肖坤
 * @date 2019/2/16
 */

public class UserDao {

    private static final String TAG = "UserDao";
    private SQLiteDatabase mDb;

    private UserDao() {
        this.mDb = DatabaseHelper.getDatabase();
    }

    public static UserDao getInstance() {
        return UserDaoHolder.sInstance;
    }

    private static class UserDaoHolder {
        private static final UserDao sInstance = new UserDao();
    }

    public boolean insert(User user) {
        if (delete()) {
            ContentValues values = new ContentValues();
            values.put(UserBaseTable.ID, user.userId);
            values.put(UserBaseTable.NAME, user.name);
            values.put(UserBaseTable.NICKNAME, user.nickName);
            values.put(UserBaseTable.PHONE, user.phone);
            values.put(UserBaseTable.GENDER, user.gender);
            long result = mDb.insert(UserBaseTable.TABLE_NAME, null, values);
            return result != -1;
        } else {
            Log.e(TAG, "数据库删除失败");
            return false;
        }
    }

    public boolean delete() {
        int result = mDb.delete(UserBaseTable.TABLE_NAME, null, null);
        return result != -1;
    }

    public User queryCurrentUser() {
        Cursor cursor = mDb.query(UserBaseTable.TABLE_NAME, null,
                null, null, null, null, null);
        List<User> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            User user = new User();
            user.userId = cursor.getInt(UserBaseTable.ID_COLUMN_INDEX);
            user.nickName = cursor.getString(UserBaseTable.NICKNAME_COLUMN_INDEX);
            user.phone = cursor.getString(UserBaseTable.PHONE_COLUMN_INDEX);
            user.gender = cursor.getInt(UserBaseTable.GENDER_COLUMN_INDEX);
            user.name = cursor.getString(UserBaseTable.NAME_COLUMN_INDEX);
            list.add(user);
        }
        cursor.close();
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

}
