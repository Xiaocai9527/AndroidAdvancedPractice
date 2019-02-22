package com.xiaokun.advance_practive;

import com.xiaokun.advance_practive.im.entity.Conversation;
import com.xiaokun.baselib.muti_rv.BaseMultiHodler;
import com.xiaokun.baselib.util.RefInvoke;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by xiaokun on 2019/2/22.
 *
 * @author xiaokun
 * @date 2019/2/22
 */
public class InnerClassReflectTest {


    @Test
    public void testInnerReflect() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        InnerClassTest innerClassTest = new InnerClassTest() {

            @Override
            public String getType() {
                return "xiaocai";
            }
        };

        Class<? extends InnerClassTest> aClass = innerClassTest.getClass();
        Class<?> aClass1 = Class.forName(getClass().getName());
        Object object = RefInvoke.createObject(InnerClassReflectTest.class);
        Constructor<? extends InnerClassTest> declaredConstructor = aClass.getDeclaredConstructor(aClass1);
        InnerClassTest innerClassTest1 = declaredConstructor.newInstance(object);

        System.out.println(aClass.getName());
        System.out.println(innerClassTest1.getClass().getName());
    }

    private void test(Object object) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> aClass = object.getClass();
        String name = aClass.getName();
        if (name.contains("$")) {
            //代表就是内部类
            //1.截取到到父类的name
            String parentName = name.substring(0, name.lastIndexOf("$"));
            //2.通过父类name获取到父类的对象
            Object parentObj = RefInvoke.createObject(parentName);
            //3.获取父类声明的内部类构造函数
            Class<?> parentClass = Class.forName(parentName);
            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(parentClass);
            Object o = declaredConstructor.newInstance(parentObj);
            System.out.println(o.getClass().getName());
        }
    }
}
