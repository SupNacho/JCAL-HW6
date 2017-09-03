import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.supernacho.ju.Homework;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestTask2HW {
    static int[] srcTaskTwo1 = {1,1,1,1,1,1,4,4,1,4,1};
    static int[] srcTaskTwo2 = {1,1,1,1,1,1,3,4,1,4,1};
    static int[] srcTaskTwo3 = {1,1,1,1,1,1,1,1};
    static int[] srcTaskTwo31 = {1,1,1,1,1,1,1,1,4,1};
    static int[] srcTaskTwo4 = null;

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {srcTaskTwo1,true},
                {srcTaskTwo2, false},
                {srcTaskTwo3,false},
                {srcTaskTwo31, true},
                {srcTaskTwo4, false}
        });
    }

    private int[] a;
    private boolean b;

    public TestTask2HW(int[] a, boolean b){
        this.a = a;
        this.b = b;
    }

    @Test
    public void testTask2One(){
        Assert.assertEquals(b,Homework.task2(a));
    }
}
