package org.huluo.dbutil;

import org.huluo.entity.AntiCorruptionEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
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


    public static void insertDataToDb(Set<AntiCorruptionEntity> set) {
        try {
            preparedStatement = connection.prepareStatement("insert into anticorruption values(?,?,?,?,?)");
            for (AntiCorruptionEntity antiCorruptionEntity : set) {
                preparedStatement.setObject(1, UUID.randomUUID().toString());
                preparedStatement.setObject(2, antiCorruptionEntity.getImageUrl());
                preparedStatement.setObject(3, antiCorruptionEntity.getTitle());
                preparedStatement.setObject(4, antiCorruptionEntity.getOnlineVedioUrl());
                preparedStatement.setObject(5, antiCorruptionEntity.getChatcontent());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertDataToDbWithoutTitleAndChatContent(Set<AntiCorruptionEntity> set) {
        try {
            preparedStatement = connection.prepareStatement("insert into anticorruption (id,imageUrl,onlineVedioUrl)values(?,?,?)");
            for (AntiCorruptionEntity antiCorruptionEntity : set) {
                preparedStatement.setObject(1, UUID.randomUUID().toString().replaceAll("-","").trim());
                preparedStatement.setObject(2, antiCorruptionEntity.getImageUrl());
                preparedStatement.setObject(3, antiCorruptionEntity.getOnlineVedioUrl());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void insertIdOnly() {
        try {
            preparedStatement = connection.prepareStatement("insert into anticorruption (id) VALUES (?)");
            preparedStatement.setObject(1, UUID.randomUUID().toString().replaceAll("-","").trim());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
