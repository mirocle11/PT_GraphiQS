package DataBase;

import Model.data.clientsData;
import Model.data.subtradesData;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import javax.swing.*;
import java.sql.*;

public class DataBase {

    private static final String URL = "jdbc:mysql://localhost:3307/pt_graphiqs_db";
    private static Connection con;

    private int user_id = 0;
    private int _rate = 0;

    //declare instance
    private static DataBase instance = null;
    private String username;
    private String position;
    private String full_name;
    private String subtrade;
    private int rate;

    public static DataBase getInstance() {
        if(instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    //connect to db
    private DataBase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, "root", "password");
            System.out.println("Successfully connected to mysql database!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //add
    public void addUser(String username, String password, String security_question, String security_answer) {
        try {
            String sql = "INSERT INTO USERS_TBL (USERNAME, PASSWORD, SECURITY_QUESTION, SECURITY_ANSWER) VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, security_question);
            preparedStatement.setString(4, security_answer);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addClient(String first_name, String last_name, String contact_person, String email,String mobile,
                          String landline) {
        try {
            String sql = "INSERT INTO CLIENTS_TBL (USER_ID, FIRST_NAME, LAST_NAME, FULL_NAME, CONTACT_PERSON, EMAIL," +
                    " MOBILE, LANDLINE) VALUES (LAST_INSERT_ID(), ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            preparedStatement.setString(3, first_name + " " +last_name);
            preparedStatement.setString(4, contact_person);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, mobile);
            preparedStatement.setString(7, landline);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSubtrade(int client_id, String subtrade, String first_name, String last_name, String contact_person, String address,
                            String email, String mobile, String best_way_to_contact) {

        try {
            String sql = "INSERT INTO SUBTRADES_TBL (CLIENT_ID, SUBTRADE, FIRST_NAME, LAST_NAME, CONTACT_PERSON, ADDRESS, EMAIL," +
                    " MOBILE, BEST_WAY_TO_CONTACT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, client_id);
            preparedStatement.setString(2, subtrade);
            preparedStatement.setString(3, first_name);
            preparedStatement.setString(4, last_name);
            preparedStatement.setString(5, contact_person);
            preparedStatement.setString(6, address);
            preparedStatement.setString(7, email);
            preparedStatement.setString(8, mobile);
            preparedStatement.setString(9, best_way_to_contact);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPriceCard(String material, String unit, String rate) {
        try {
            String sql = "INSERT INTO PRICE_CARD_TBL (SUBTRADE_ID, MATERIAL, UNIT, RATE) VALUES (LAST_INSERT_ID(), ?, ?, ?)";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, material);
            preparedStatement.setString(2, unit);
            preparedStatement.setString(3, rate);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //display
    public void displayClients(ObservableList<clientsData> clientsData) {
        try {
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM CLIENTS_TBL";
            ResultSet resultSet = statement.executeQuery(sql);

            clientsData.clear();

            while (resultSet.next()) {

                String ID = resultSet.getString("ID");
                String FULL_NAME = resultSet.getString("FULL_NAME");

                clientsData.addAll(new clientsData(ID, FULL_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displaySubtrades(int id, ObservableList<subtradesData> subtradesData) {
        try {
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM SUBTRADES_TBL WHERE CLIENT_ID = " + id;
            ResultSet resultSet = statement.executeQuery(sql);

            subtradesData.clear();

            while (resultSet.next()) {

                String ID = resultSet.getString("ID");
                String SUBTRADE = resultSet.getString("SUBTRADE");
                String FIRST_NAME = resultSet.getString("FIRST_NAME");
                String LAST_NAME = resultSet.getString("LAST_NAME");
                String CONTACT_PERSON = resultSet.getString("CONTACT_PERSON");
                String ADDRESS = resultSet.getString("ADDRESS");
                String EMAIL = resultSet.getString("EMAIL");
                String MOBILE = resultSet.getString("MOBILE");
                String BEST_CONTACT = resultSet.getString("BEST_WAY_TO_CONTACT");

                subtradesData.addAll(new subtradesData(ID, SUBTRADE, FIRST_NAME+" "+LAST_NAME, CONTACT_PERSON,
                        ADDRESS, EMAIL, MOBILE, BEST_CONTACT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewClientDetails(int id, Label FIRST_NAME, Label LAST_NAME, Label CONTACT_PERSON, Label EMAIL,
                                  Label MOBILE, Label LANDLINE) {
        try {
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM CLIENTS_TBL WHERE ID = "+id;
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {

                FIRST_NAME.setText(resultSet.getString("FIRST_NAME"));
                LAST_NAME.setText(resultSet.getString("LAST_NAME"));
                CONTACT_PERSON.setText(resultSet.getString("CONTACT_PERSON"));
                EMAIL.setText(resultSet.getString("EMAIL"));
                MOBILE.setText(resultSet.getString("MOBILE"));
                LANDLINE.setText(resultSet.getString("LANDLINE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update
    public void updateClient(int client_id, String first_name, String last_name, String contact_person, String email,
                             String mobile, String landline) {

        try {
            String sql = "UPDATE CLIENTS_TBL SET FIRST_NAME = ?, LAST_NAME = ?, CONTACT_PERSON = ?, EMAIL = ?, " +
                    "MOBILE = ?, LANDLINE = ? WHERE ID = ? ";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            preparedStatement.setString(3, contact_person);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, mobile);
            preparedStatement.setString(6, landline);
            preparedStatement.setInt(7, client_id);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //delete
    public void deleteClient(int client_id) {
        int user_id = 0;

        try {
            Statement statement = con.createStatement();
            String sql = "SELECT USER_ID FROM CLIENTS_TBL WHERE ID = " + client_id;
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                user_id = resultSet.getInt("USER_ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(user_id);
            String sql = "DELETE FROM USERS_TBL WHERE ID = ?";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.setInt(1, user_id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewSubtradeDetails(int id, Label SUBTRADE, Label FIRST_NAME, Label LAST_NAME, Label ADDRESS,
                                    Label CONTACT_PERSON, Label EMAIL, Label MOBILE, Label BEST_CONTACT,
                                    Label MATERIAL, Label RATE, Label UNIT) {
        try {
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM SUBTRADES_TBL WHERE ID = "+id;
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                SUBTRADE.setText(resultSet.getString("SUBTRADE"));
                FIRST_NAME.setText(resultSet.getString("FIRST_NAME"));
                LAST_NAME.setText(resultSet.getString("LAST_NAME"));
                ADDRESS.setText(resultSet.getString("ADDRESS"));
                CONTACT_PERSON.setText(resultSet.getString("CONTACT_PERSON"));
                EMAIL.setText(resultSet.getString("EMAIL"));
                MOBILE.setText(resultSet.getString("MOBILE"));
                BEST_CONTACT.setText(resultSet.getString("BEST_WAY_TO_CONTACT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM PRICE_CARD_TBL WHERE SUBTRADE_ID = "+id;
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                MATERIAL.setText(resultSet.getString("MATERIAL"));
                UNIT.setText(resultSet.getString("UNIT"));
                RATE.setText(resultSet.getString("RATE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSubtrade(int id, String subtrade, String first_name, String last_name, String address,
                               String contact_person, String email, String mobile, String best_contact) {

        try {
            String sql = "UPDATE SUBTRADES_TBL SET SUBTRADE = ?, FIRST_NAME = ?, LAST_NAME = ?, ADDRESS = ?, " +
                    "CONTACT_PERSON = ?, EMAIL = ?, MOBILE = ?, BEST_WAY_TO_CONTACT = ? WHERE ID = ? ";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, subtrade);
            preparedStatement.setString(2, first_name);
            preparedStatement.setString(3, last_name);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, contact_person);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, mobile);
            preparedStatement.setString(8, best_contact);
            preparedStatement.setInt(9, id);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePriceCard(int id, String material, String rate) {
        try {
            String sql = "UPDATE PRICE_CARD_TBL SET MATERIAL = ?, RATE = ? WHERE SUBTRADE_ID = ? ";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, material);
            preparedStatement.setString(2, rate);
            preparedStatement.setInt(3, id);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSubtrade(int id) {
        try {
            String sql = "DELETE FROM SUBTRADES_TBL WHERE ID = ?";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //login
    public boolean setAccountIndex(int id, String username, String password) {
        try {
            String sql = "SELECT * FROM USERS_TBL WHERE USERNAME = ? AND PASSWORD = ?";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("ID");
                user_id = id;
                System.out.println(user_id);
                JOptionPane.showMessageDialog(null, "Welcome");
                return true;
            }
            //default account
//            else if (username == "admin11" || password == "admin11"){
//                JOptionPane.showMessageDialog(null,"Welcome");
//            }
            else {
                JOptionPane.showMessageDialog(null,"Incorrect username or password");
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void displayOperator(Label USERNAME, Label POSITION) {
        try {
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM USERS_TBL WHERE ID = " + user_id;
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                USERNAME.setText(resultSet.getString("USERNAME"));
                POSITION.setText(resultSet.getString("POSITION"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clientComboBox(ObservableList<String> CLIENTS) {
        try {
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM CLIENTS_TBL";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String FULL_NAME = resultSet.getString("FULL_NAME");
                CLIENTS.addAll(FULL_NAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSubtrades(String full_name, String subtrade, Label rate) {
        int client_id = 0;
        int subtrade_id = 0;

        try {
            String sql = "SELECT * FROM CLIENTS_TBL WHERE FULL_NAME = ?";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, full_name);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                client_id = resultSet.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String sql = "SELECT * FROM SUBTRADES_TBL WHERE CLIENT_ID = ? AND SUBTRADE = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, client_id);
            preparedStatement.setString(2, subtrade);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                subtrade_id = resultSet.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String sql = "SELECT * FROM PRICE_CARD_TBL WHERE SUBTRADE_ID = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, subtrade_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                rate.setText(resultSet.getString("RATE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
