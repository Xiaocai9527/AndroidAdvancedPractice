package com.xiaokun.baselib.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 肖坤 on 2018/12/19.
 * java反射工具类
 *
 * @author 肖坤
 * @date 2018/12/19
 */

public class RefInvoke {

    /**
     * 反射无参构造函数得到实例对象
     *
     * @param className 完整类名,包含包名信息
     * @return
     */
    public static Object createObject(String className) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};
        return createObject(className, pareTypes, pareValues);
    }

    /**
     * 反射无参构造函数得到实例对象
     *
     * @param clazz 类的类型
     * @return
     */
    public static Object createObject(Class clazz) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};
        return createObject(clazz, pareTypes, pareValues);
    }

    /**
     * 反射单个参数构造函数得到对象实例
     *
     * @param className 完整类名,包含包名信息
     * @param pareType  构造函数参数类型
     * @param pareValue 构造函数参数值
     * @return
     */
    public static Object createObject(String className, Class pareType, Object pareValue) {
        Class[] pareTypes = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};
        return createObject(className, pareTypes, pareValues);
    }

    /**
     * 反射单个参数构造函数得到对象实例
     *
     * @param clazz     类的类型
     * @param pareType  构造函数参数类型
     * @param pareValue 构造函数参数值
     * @return
     */
    public static Object createObject(Class clazz, Class pareType, Object pareValue) {
        Class[] pareTypes = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};
        return createObject(clazz, pareTypes, pareValues);
    }

    /**
     * 反射多个参数构造函数得到实例对象
     *
     * @param className  完整类名,包含包名信息
     * @param pareTypes  构造函数参数类型数组
     * @param pareValues 构造函数参数值数组
     * @return
     */
    public static Object createObject(String className, Class[] pareTypes, Object[] pareValues) {
        try {
            Class<?> aClass = Class.forName(className);
            return createObject(aClass, pareTypes, pareValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过类的类型反射多个参数构造函数得到实例对象
     *
     * @param clazz      对象类型
     * @param pareTypes  构造函数参数类型数组
     * @param pareValues 构造函数参数值数组
     * @return
     */
    public static Object createObject(Class clazz, Class[] pareTypes, Object[] pareValues) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor(pareTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(pareValues);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用实例对象中的任一空参数方法
     *
     * @param obj
     * @param methodName
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName) {
        if (obj == null) {
            return null;
        }
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};
        return invokeInstanceMethod(obj, methodName, pareTypes, pareValues);
    }

    /**
     * 调用实例对象中的任一单参数方法
     *
     * @param obj
     * @param methodName
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName, Class pareType, Object pareValue) {
        if (obj == null) {
            return null;
        }
        Class[] pareTypes = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};
        return invokeInstanceMethod(obj, methodName, pareTypes, pareValues);
    }

    /**
     * 调用实例对象中的任一方法包括私有方法
     *
     * @param obj        实例对象
     * @param methodName 方法名称
     * @param pareTypes  方法参数类型数组
     * @param pareValues 方法参数值数组
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName, Class[] pareTypes, Object[] pareValues) {
        if (obj == null) {
            return null;
        }
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName, pareTypes);
            method.setAccessible(true);
            return method.invoke(obj, pareValues);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用无参静态方法
     *
     * @param className
     * @param methodName
     * @return
     */
    public static Object invokeStaticMethod(String className, String methodName) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};
        return invokeStaticMethod(className, methodName, pareTypes, pareValues);
    }

    /**
     * 调用无参静态方法
     *
     * @param claszz
     * @param methodName
     * @return
     */
    public static Object invokeStaticMethod(Class claszz, String methodName) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};
        return invokeStaticMethod(claszz, methodName, pareTypes, pareValues);
    }

    /**
     * 调用单参数静态方法
     *
     * @param className
     * @param methodName
     * @param pareType
     * @param pareValue
     * @return
     */
    public static Object invokeStaticMethod(String className, String methodName, Class pareType, Object pareValue) {
        Class[] pareTypes = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};
        return invokeStaticMethod(className, methodName, pareTypes, pareValues);
    }

    /**
     * 调用单参数静态方法
     *
     * @param claszz
     * @param methodName
     * @param pareType
     * @param pareValue
     * @return
     */
    public static Object invokeStaticMethod(Class claszz, String methodName, Class pareType, Object pareValue) {
        Class[] pareTypes = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};
        return invokeStaticMethod(claszz, methodName, pareTypes, pareValues);
    }

    /**
     * 调用静态方法
     *
     * @param className  完整类名,包含包名信息
     * @param methodName 方法名称
     * @param pareTypes  方法参数类型数组
     * @param pareValues 方法参数值数组
     * @return
     */
    public static Object invokeStaticMethod(String className, String methodName, Class[] pareTypes, Object[] pareValues) {
        try {
            Class<?> aClass = Class.forName(className);
            return invokeStaticMethod(aClass, methodName, pareTypes, pareValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用静态方法
     *
     * @param clazz      类的类型
     * @param methodName 方法名称
     * @param pareTypes  方法参数类型数组
     * @param pareValues 方法参数值数组
     * @return
     */
    public static Object invokeStaticMethod(Class clazz, String methodName, Class[] pareTypes, Object[] pareValues) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, pareTypes);
            method.setAccessible(true);
            return method.invoke(null, pareValues);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取对象中的字段
     *
     * @param className 完整类名,包含包名信息
     * @param obj       对象(当获取静态字段时,obj传null)
     * @param fieldName 字段名称
     * @return
     */
    public static Object getFieldObject(String className, Object obj, String fieldName) {
        try {
            Class<?> aClass = Class.forName(className);
            return getFieldObject(aClass, obj, fieldName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取对象中的静态字段
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Object getStaticFieldObject(Class clazz, String fieldName) {
        return getFieldObject(clazz, null, fieldName);
    }

    /**
     * 获取对象中的字段
     *
     * @param obj       对象(当获取静态字段时,obj传null)
     * @param fieldName 字段名称
     * @return
     */
    public static Object getFieldObject(Object obj, String fieldName) {
        return getFieldObject(obj.getClass(), obj, fieldName);
    }

    /**
     * 获取对象中的字段
     *
     * @param clazz     完整类名,包含包名信息
     * @param obj       对象(当获取静态字段时,obj传null)
     * @param fieldName 字段名称
     * @return
     */
    public static Object getFieldObject(Class clazz, Object obj, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给类的静态字段赋值
     *
     * @param className
     * @param fieldName
     * @param fieldValue
     */
    public static void setStaticFieldObject(String className, String fieldName, Object fieldValue) {
        setFieldObject(className, null, fieldName, fieldValue);
    }

    /**
     * 给对象中的字段赋值
     *
     * @param className
     * @param obj
     * @param fieldName
     * @param fieldValue
     */
    public static void setFieldObject(String className, Object obj, String fieldName, Object fieldValue) {
        try {
            Class<?> aClass = Class.forName(className);
            setFieldObject(aClass, obj, fieldName, fieldValue);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给对象中的字段赋值
     *
     * @param obj
     * @param fieldName
     * @param fieldValue
     */
    public static void setFieldObject(Object obj, String fieldName, Object fieldValue) {
        setFieldObject(obj.getClass(), obj, fieldName, fieldValue);
    }

    /**
     * 给对象中的字段赋值
     *
     * @param clazz
     * @param obj
     * @param fieldName
     * @param fieldValue
     */
    public static void setFieldObject(Class clazz, Object obj, String fieldName, Object fieldValue) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, fieldValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


}
