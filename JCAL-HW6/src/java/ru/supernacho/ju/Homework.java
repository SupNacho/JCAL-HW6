package ru.supernacho.ju;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Homework {
    private static final int MARKER_FOUR = 4;
    private static final int MARKER_ONE = 1;
    public static void main(String[] args) {
//        Integer[] srcArr = {1,4,1,4,5,9,8};
//        Integer[] srcArr = {1,3,1,45,5,9,8};
//        Integer[] srcArr = {1,4,1,4,5,9,8,4};
//        task1(srcArr);                             // Задание 1

//        int[] srcTaskTwo1 = {1,1,1,1,1,1,4,4,1,4,1};
//        int[] srcTaskTwo2 = {1,1,1,1,1,1,3,4,1,4,1};
//        int[] srcTaskTwo3 = {1,1,1,1,1,1,1,1};
//        System.out.println(task2(srcTaskTwo2));    // Задание 2

//        task3();                                   // Задание 3

    }

    private static void task3() {
        Task3Dbase task3Dbase = new Task3Dbase();
        try {
            task3Dbase.startBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Integer[] task1(Integer[] src){
        if (src == null) throw new RuntimeException("Пустая ссылка на массив");
        boolean borderFour = false;                     // Отмечаю наличие или отсутствие 4-ки.
        List<Integer> list = new ArrayList<>();
        for (int i = src.length - 1; i >= 0 ; i--) {    // Записываю все числа с конца массива до первой 4-ки в лист.
            if ( src[i] == MARKER_FOUR) {               // Получаем список в обратном порядке.
                borderFour = true;
                break;
            } else if(i == 0 && !borderFour) {
                throw new RuntimeException("В массиве отсутствуют 4-ки!");
            }
            list.add(src[i]);
        }
        Collections.reverse(list);                     // Переворачиваем полученный список для получения правильного порядка.
        return list.toArray(new Integer[list.size()]); // возвращаем массив.

                                                       // Первая версия была с 2-мя циклами, первый перебирал и определял
                                                       // положение последней 4-ки, потом второй цикл записывал остаток,
                                                       // но я решил сделать через коллекции.

    }

    public static boolean task2 (int[] srcTaskTwo){
        boolean hasOne = false;                       // Маркеры для проверки наличия 1 и 4-ок.
        boolean hasFour = false;
        if (srcTaskTwo == null) return false;
        for (int i = 0; i < srcTaskTwo.length; i++) {
            if (!hasOne) {                                      // Проверяем была ли уже обнаружена еденица и если была,
                if (srcTaskTwo[i] == MARKER_ONE) hasOne = true; // то проверку на еденицу и переключение маркера не делаем.
            }
            if (!hasFour) {                           // Тоже самое для 4-ки.
                if (srcTaskTwo[i] == MARKER_FOUR) hasFour = true;
            }
            if (srcTaskTwo[i] != MARKER_ONE && srcTaskTwo[i] != MARKER_FOUR) return false;
        }
        return hasOne && hasFour;
    }
}
