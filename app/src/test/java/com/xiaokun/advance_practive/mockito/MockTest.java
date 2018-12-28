package com.xiaokun.advance_practive.mockito;

import com.xiaokun.advance_practive.network.entity.GankResEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
@RunWith(MockitoJUnitRunner.class)//使用MockitoJUnitRunner
public class MockTest {

    @Test
    public void testMockObj1() {
        GankResEntity gankResEntity = mock(GankResEntity.class);
        System.out.println(gankResEntity.getCode());
        System.out.println(gankResEntity.getMessage());
        assertNotNull(gankResEntity);
    }

    @Mock
    GankResEntity mGankResEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMockObj2() {
        //1.before注解方法里初始化MockitoAnnotations
        //2.给mGankResEntity添加注解Mock
        System.out.println(mGankResEntity.getCode());
        System.out.println(mGankResEntity.getMessage());
        assertNotNull(mGankResEntity);
    }

    @Mock
    GankResEntity mEntity;

    @Test
    public void testMockObj3() {
        //1.给类添加注解RunWith
        //2.给属性添加@Mock注解
        System.out.println(mEntity.getCode());
        System.out.println(mEntity.getMessage());
        assertNotNull(mEntity);
    }

    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();

    @Mock
    GankResEntity mResEntity;

    @Test
    public void testMockObj4() {
        //1.@Rule
        // public MockitoRule mMockitoRule = MockitoJUnit.rule();
        //2.给mResEntity添加Mock注解
        System.out.println(mResEntity.getCode());
        System.out.println(mResEntity.getMessage());
        assertNotNull(mResEntity);
    }

    @Test
    public void testReturn() {
        when(mResEntity.getCode()).thenReturn(10);
        when(mResEntity.getData()).thenReturn(null);

        //when(mResEntity.getData()).thenThrow(new NullPointerException("data为null"));

        System.out.println(mResEntity.getCode());
        System.out.println(mResEntity.getData());

        doReturn("小菜").when(mResEntity).getMessage();
        System.out.println(mResEntity.getMessage());


        when(mResEntity.getMsg(anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                return arguments[0] + "是大帅比";
            }
        });
        System.out.println(mResEntity.getMsg("肖坤"));
    }

    @Test
    public void testVerifyAfter() {
        when(mResEntity.getCode()).thenReturn(18);
        System.out.println(mResEntity.getCode());
        System.out.println(getCurrentTime());

        verify(mResEntity, after(1000)).getCode();
        System.out.println(getCurrentTime());
        verify(mResEntity, atLeast(2)).getCode();
    }

    @Test
    public void testVerifyAtLeast() {
        mResEntity.getCode();
        mResEntity.getCode();
        verify(mResEntity, atLeast(2)).getCode();
    }

    @Test
    public void testVerfyAtMost() {
        mResEntity.getCode();
        verify(mResEntity, atMost(2)).getCode();
    }

    @Test
    public void testVerfyTimes() {
        mResEntity.getCode();
        mResEntity.getCode();
        verify(mResEntity, times(2)).getCode();
    }

    @Test
    public void testVerfyAny() {
        when(mResEntity.getMsg(anyString())).thenReturn("keke");

        System.out.println(mResEntity.getMsg("hehe"));
    }

    @Test
    public void testVerfyContain() {
        when(mResEntity.getMsg(contains("肖"))).thenReturn("肖坤");
        System.out.println(mResEntity.getMsg("小肖"));
    }

    //创建自定义的参数匹配模式
    @Test
    public void testArgThat() {
        when(mResEntity.getMsg(argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(String argument) {
                return argument.contains("肖");
            }
        }))).thenReturn("肖坤真帅");
        System.out.println(mResEntity.getMsg("肖"));
    }



    private long getCurrentTime() {
        return System.currentTimeMillis();
    }


}

