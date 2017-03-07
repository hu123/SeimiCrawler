package org.huluo.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class InsertVideoUrlTodb {
    static Connection connection;
    static PreparedStatement preparedStatement;
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seimidemo?useUnicode=true&characterEncoding=utf-8", "root", "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized static boolean insertUrlTdb(String id, String url) {
        try {
            preparedStatement = connection.prepareStatement("insert into temp VALUES (?,?)");

            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, url);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
