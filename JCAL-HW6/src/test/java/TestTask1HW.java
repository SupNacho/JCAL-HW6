import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.supernacho.ju.Homework;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestTask1HW {
    static Integer[] src1 = {1,4,1,4,5,9,8};
    static Integer[] src11 = {1,4,1,42,53,94,4,8};
    static Integer[] src11ans = {8};
    static Integer[] src12 = {11,4,11,4,5,9,8,4,3,2,1,2,33,5,7};
    static Integer[] src12ans = {3,2,1,2,33,5,7};
    static Integer[] src1ans = {5,9,8};
    static Integer[] src2 = {1,3,1,45,5,9,8};
    static Integer[] src2ans = null;
    static Integer[] src3 = {1,4,1,4,5,9,8,4};
    static Integer[] src3ans = {};
    static Integer[] src4 = null;
    static Integer[] src4ans = null;

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {src1, src1ans},
                {src3, src3ans},
                {src11, src11ans},
                {src12, src12ans},

        });
    }

    private Integer[] a;
    private Integer[] b;

    public TestTask1HW(Integer[] a, Integer[] b){
        this.a = a;
        this.b = b;
    }


    @Test
    public void task1TestOne(){
        Assert.assertArrayEquals((Object[]) b, Homework.task1(a));
    }
    @Test(expected = RuntimeException.class)
    public void task1TestTwo(){
        Assert.assertArrayEquals(src2ans, Homework.task1(src2));
    }

    @Test(expected = RuntimeException.class)
    public void task1TestFour(){
        Assert.assertArrayEquals(src4ans, Homework.task1(src4));
    }
}
