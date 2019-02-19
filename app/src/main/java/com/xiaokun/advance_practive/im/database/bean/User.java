package com.xiaokun.advance_practive.im.database.bean;

/**
 * Created by 肖坤 on 2019/2/16.
 *
 * @author 肖坤
 * @date 2019/2/16
 */

public class User {

    private long userId = 1;
    public String nickName;
    public String phone;
    /**
     * 性别
     */
    public int gender;
    public String name;

    public User() {
        userId = 1;
    }

    public long getUserId() {
        return userId;
    }

}
