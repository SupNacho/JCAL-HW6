package ru.supernacho.ju;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class Task3Dbase {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement pst;

    private static final  String SCORE_CHANGE = "/изменитьбаллы";
    private static final  String ADD_STD = "/добавить";
    private static final  String BY_SCORE_RANGE = "/студентыпобаллам";
    private static final  String BY_LASTNAME = "/фамилия";
    private static final  String VIEW_ALL = "/все";
    private static final  String EXIT = "/выход";
    private static final  String NO_ARGS = "Нехватает аргументов! пример: ";
    private static final  String NO_STUDENTS = "Такого студента не найдено.";
    private static ResultSet resultSet;

    public void startBase() throws SQLException{
      try {
          connectDB();
          connection.setAutoCommit(true);
          usageInfo();
          Scanner scanner = new Scanner(System.in);
          while (true) {
              String[] input = scanner.nextLine().split(" ");
              String cmd = input[0];
              switch (cmd) {
                  case BY_LASTNAME:
                      if (input.length < 2) {
                          System.out.println(NO_ARGS + BY_LASTNAME + " <Фамилия1>");
                          break;
                      }
                      String students = input[1];
                      viewByLastName(students);
                      break;

                      case ADD_STD:
                      if (input.length < 2) {
                          System.out.println(NO_ARGS + ADD_STD + " <Фамилия1> <100>");
                          break;
                      }
                      students = input[1];
                      int score = Integer.parseInt(input[2]);
                      addStudent(students, score);
                      break;

                  case SCORE_CHANGE:
                      if (input.length < 3) {
                          System.out.println(NO_ARGS + SCORE_CHANGE + " <Фамилия1> <500>");
                          break;
                      }
                      students = input[1];
                      int costOne = Integer.parseInt(input[2]);
                      changeScore(students, costOne);
                      break;

                  case BY_SCORE_RANGE:
                      if (input.length < 3) {
                          System.out.println(NO_ARGS + BY_SCORE_RANGE + " <10> <100>");
                          break;
                      }
                      costOne = Integer.parseInt(input[1]);
                      int costTwo = Integer.parseInt(input[2]);
                      studsInRange(costOne, costTwo);
                      break;

                  case VIEW_ALL:
                      resultSet = stmt.executeQuery("SELECT * FROM students");
                      while (resultSet.next()) {
                          System.out.println(
                                  "[ " +
                                          resultSet.getInt("studID") + " | " +
                                          resultSet.getString("LastName") + " | " +
                                          resultSet.getInt("Score") + " ]"
                          );
                      }
                      break;

                  case EXIT:
                      System.out.println("Завершение работы.");
                      disconnect();
                      return;

                  default:
                      usageInfo();
                      break;
              }
          }
      } catch (SQLException e) {
          e.printStackTrace();
      } finally {
          disconnect();
      }
    }

    public void connectDB() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:StudentsDB.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        File file = new File("StudentsDB.db");
        if (!file.exists()) {
        stmt.execute("CREATE TABLE IF NOT EXISTS students (\n" +
                "    ID     INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                   NOT NULL\n" +
                "                   UNIQUE,\n" +
                "    studID INTEGER  UNIQUE,\n" +
                "    LastName  STRING,\n" +
                "    Score   INTEGER\n" +
                ");");
        pst = connection.prepareStatement("INSERT INTO students (studId, LastName, Score) VALUES (?, ?, ?)");
        connection.setAutoCommit(false);
        for (int i = 1; i <= 10; i++) {
            pst.setInt(1, i);
            pst.setString(2, "Фамилия" + i);
            pst.setInt(3, i * 10);
            pst.addBatch();
        }
        pst.executeBatch();
        connection.commit();
        }
    }  // Отделил методы подключения отключения, для тестирования

    private void studsInRange(int scoreOne, int scoreTwo) throws SQLException {
        pst = connection.prepareStatement("SELECT LastName, Score FROM students WHERE Score >= ? AND Score <= ?;");
        pst.setInt(1,scoreOne);
        pst.setInt(2,scoreTwo);
        resultSet = pst.executeQuery();
        if (resultSet.isClosed()) {
            System.out.println(NO_STUDENTS);
            return;
        }

        System.out.println("Студенты с баллами в диапарозне от: " + scoreOne + " до: " + scoreTwo);
        while (resultSet.next()) {
            System.out.println(
                    "[ " +  resultSet.getString("LastName") + " | " +
                            resultSet.getInt("Score") + " ]"
            );
        }
    } // Сделал доступ паблик, методам для тестирования

    public String changeScore(String lastName, int scoreOne) throws SQLException {
        pst = connection.prepareStatement("UPDATE students SET Score = ? WHERE LastName = ?;");
        pst.setInt(1,scoreOne);
        pst.setString(2,lastName);
        int chngCnt = pst.executeUpdate();
        pst.close();

        pst = connection.prepareStatement("SELECT LastName, Score FROM students WHERE LastName = ?;");
        pst.setString(1,lastName);
        resultSet = pst.executeQuery();
        if (resultSet.isClosed()) {
            System.out.println(NO_STUDENTS);
            return NO_STUDENTS;
        }

        if (chngCnt > 0) {
            System.out.println(
                    "Изменены баллы студента: " +
                            resultSet.getString("LastName") +
                            " Баллы: " +
                            resultSet.getInt("Score")
            );
        } else {
            System.out.println("Студент не найден.");
        }
        return resultSet.getString("LastName") + resultSet.getInt("Score");
    }

    public String viewByLastName(String lastName) throws SQLException {
        pst = connection.prepareStatement("SELECT LastName, Score FROM students WHERE LastName = ?;");
        pst.setString(1,lastName);

        resultSet = pst.executeQuery();
        if (resultSet.isClosed()) {
            System.out.println(NO_STUDENTS);
            return NO_STUDENTS;
        }

        System.out.println(
                "Студент: " +
                        resultSet.getString("LastName") +
                        " Баллы: " +
                        resultSet.getInt("Score")
        );
        return resultSet.getString("LastName") + resultSet.getInt("Score");
    }

    public String addStudent (String lastName, int score) throws SQLException {
        resultSet = stmt.executeQuery("SELECT studID FROM students ORDER BY studID DESC LIMIT 1");
        int newID = resultSet.getInt("studID") + 1;
        pst = connection.prepareStatement("INSERT INTO students (studId, LastName, Score) VALUES (?, ?, ?)");
        pst.setInt(1, newID);
        pst.setString(2, lastName);
        pst.setInt(3, score);
        pst.execute();

        pst = connection.prepareStatement("SELECT LastName, Score FROM students WHERE LastName = ?;");
        pst.setString(1,lastName);
        resultSet = pst.executeQuery();

        if (resultSet.isClosed()) {
            System.out.println(NO_STUDENTS);
            return NO_STUDENTS;
        }


            System.out.println(
                    "Добавлен студент: " +
                            resultSet.getString("LastName") +
                            " Баллы: " +
                            resultSet.getInt("Score")
            );
        return resultSet.getString("LastName") + resultSet.getInt("Score");
    }

    public void autoComON(){
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void autoComOFF(){
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private static void usageInfo(){
        System.out.println("Для работы используйте следующие комманды:\n" +
                ADD_STD + " <фамилия студента> <int - баллы> - для добавления нового студента" +
                BY_LASTNAME + " <фамилия студента> - для вывода конкретного студентаа из БД;\n" +
                BY_SCORE_RANGE + " <int> <int> - для вывода студентов в заданном диапазоне баллов;\n" +
                SCORE_CHANGE + " <фамилия студента> <int - новый балл> - для изменения баллов студента;\n" +
                VIEW_ALL + " - вывести список всех студентов в БД;\n" +
                EXIT + " - для выхода из программы.\n");
    }
}
