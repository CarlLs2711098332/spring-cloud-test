package com.lls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.Server;

public class H2ServerDemo {


    public static void main(String[] args) {

        new H2ServerDemo().start();
        new H2ServerDemo().crudTest();
        new H2ServerDemo().stop();
    }

    private Server server;

    public void start() {
        try {
            System.out.println("正在启动h2...");
            server = Server.createTcpServer(
                    new String[] { "-tcp", "-tcpAllowOthers", "-tcpPort",
                            "8043","-tcpPassword","123456","-key","db","-tcpDaemon" ,"-baseDir","/data/h2"}).start();
            System.out.println("启动成功：" + server.getStatus());
        } catch (SQLException e) {
            System.out.println("启动h2出错：" + e.toString());

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        if (server != null) {
            System.out.println("正在关闭h2...");
            server.stop();
            System.out.println("关闭成功.");
        }
    }

    public void crudTest() {
        try {
            Class.forName("org.h2.Driver");

            // connect to h2
            Connection conn = DriverManager.getConnection(
                    "jdbc:h2:tcp://localhost:8043/db", "sa", "sa");

            Statement stat = conn.createStatement();

            // create table
            stat.execute("CREATE TABLE TEST(NAME VARCHAR)");

            // insert table
            stat.execute("INSERT INTO TEST VALUES(‘菩提树下的杨过‘)");
            stat.execute("INSERT INTO TEST VALUES(‘http://yjmyzz.cnblogs.com/‘)");

            // retrive data
            ResultSet result = stat.executeQuery("select name from test ");
            int i = 1;
            while (result.next()) {
                System.out.println(i++ + ":" + result.getString("name"));
            }

            // drop table
            stat.execute("DROP TABLE TEST");

            result.close();
            stat.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}