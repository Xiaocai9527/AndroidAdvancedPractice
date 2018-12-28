package com.xiaokun.advance_practive.powermock;

import com.xiaokun.advance_practive.network.entity.Banana;
import com.xiaokun.advance_practive.network.entity.Fruit;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Banana.class})
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class PowerMockTest {

    /**
     * 测试调用静态方法
     */
    @Test
    public void testStaticMethod() {
        PowerMockito.mockStatic(Banana.class);
        Mockito.when(Banana.getColor()).thenReturn("红色");
        Assert.assertEquals("绿色", Banana.getColor());
    }

    /**
     * 测试修改静态字段
     */
    @Test
    public void testStaticField() {
        Whitebox.setInternalState(Banana.class, "COLOR", "红色");
        Assert.assertEquals("红色", Banana.getColor());
    }

    /**
     * 测试调用私有方法
     *
     * @throws Exception
     */
    @Test
    public void testPrivateMethod() throws Exception {
        Banana banana = PowerMockito.mock(Banana.class);
        when(banana.getBananaInfo()).thenCallRealMethod();
        //调用私有方法
        when(banana, "flavor").thenReturn("苦苦的");
        Assert.assertEquals("苦苦的黄色的", banana.getBananaInfo());
        System.out.println(banana.getBananaInfo());
        //验证flavor是否调用了一次
        //PowerMockito.verifyPrivate(banana).invoke("flavor");
    }

    /**
     * 测试跳过私有方法
     */
    @Test
    public void testSkipPrivateMethod() {
        Banana banana = new Banana();
        //跳过私有方法
        PowerMockito.suppress(PowerMockito.method(Banana.class, "flavor"));
        System.out.println(banana.getBananaInfo());
        Assert.assertEquals("null黄色的", banana.getBananaInfo());
    }

    /**
     * 测试改变父类私有字段
     *
     * @throws IllegalAccessException
     */
    @Test
    public void testChangeParentPrivateField() throws IllegalAccessException {
        Banana banana = new Banana();
        MemberModifier.field(Fruit.class, "fruit").set(banana, "哈皮");
        System.out.println(banana.getFruit());
        Assert.assertEquals("哈皮", banana.getFruit());
    }

    /**
     * 测试final方法
     */
    @Test
    public void testFinalMethod() {
        Banana banana = PowerMockito.mock(Banana.class);
        when(banana.isLike()).thenReturn(true);
        Assert.assertTrue(banana.isLike());
    }

    /**
     * 测试构造方法
     *
     * @throws Exception
     */
    @Test
    public void testConstructorMethod() throws Exception {
        //Creates a mock object that supports mocking of final and native methods.
        Banana banana = PowerMockito.mock(Banana.class);
        when(banana.getBananaInfo()).thenReturn("哈哈哈哈");
        System.out.println(banana.getBananaInfo());
        assertEquals("哈哈哈哈", banana.getBananaInfo());

        //当new Banana创建对象的收返回banana
        PowerMockito.whenNew(Banana.class).withNoArguments().thenReturn(banana);
        Banana banana1 = new Banana();//并没有真的new一个对象,而是返回之前的banana
        assertEquals("哈哈哈哈", banana1.getBananaInfo());
    }


}
