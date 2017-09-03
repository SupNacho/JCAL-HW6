import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.supernacho.ju.Task3Dbase;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestTask3HW {
    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList( new Object[][]{
                {"Тест111", 10, 50, "Фамилия1", 10},
                {"Тест50", 650, 420, "Фамилия2", 20},
                {"Фамилия12", 15, 0, "Фамилия5", 50}
        });
    }
    private String a;
    private int b;
    private int c;
    private String d;
    private  int e;

    public TestTask3HW(String a, int b, int c, String d, int e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
    Task3Dbase t;
    @Before
    public void init(){
        t = new Task3Dbase();
        try {
            t.connectDB();
            t.autoComOFF();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void testAddStdud(){
        try {
            Assert.assertEquals(a+b, t.addStudent(a,b));
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void testViewStud(){
        try {
            Assert.assertEquals(d+e, t.viewByLastName(d));
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void testUpdateStud() {
        try {
            Assert.assertEquals(d+c, t.changeScore(d,c));
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    @After
    public void atLast(){
        t.disconnect();
    }
}
