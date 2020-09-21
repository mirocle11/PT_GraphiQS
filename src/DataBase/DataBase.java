package DataBase;

import Model.data.clientsData;
import Model.data.setupSheetsData;
import Model.data.shed.foundationsData;
import Model.data.subtradesData;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class DataBase {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static Connection connection;
    private static String dbName = "pt_graphiqs_db";

    private int user_id = 0;

    //declare instance
    private static DataBase instance = null;
    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    //create & connect to db
    private DataBase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL + dbName, "root", "password");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("No database detected proceeding to create");
            createDatabase();
        }
    }

    public void createDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, "root","password");
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            setTables();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void setTables() {
        try {
            connection = DriverManager.getConnection(URL + dbName, "root", "password");

            String CREATE_USERS_TBL = "CREATE TABLE IF NOT EXISTS USERS_TBL(ID INTEGER NOT NULL AUTO_INCREMENT, " +
                    "USERNAME VARCHAR(20), PASSWORD VARCHAR(20), POSITION VARCHAR(20), SECURITY_QUESTION VARCHAR(45), " +
                    "SECURITY_ANSWER VARCHAR(20), PRIMARY KEY (ID))";

            String CREATE_CLIENTS_TBL = "CREATE TABLE IF NOT EXISTS CLIENTS_TBL(ID INTEGER NOT NULL AUTO_INCREMENT, " +
                    "USER_ID INT, FIRST_NAME VARCHAR(20), LAST_NAME VARCHAR(20), FULL_NAME VARCHAR(20), " +
                    "CONTACT_PERSON VARCHAR(30), EMAIL VARCHAR(30), MOBILE VARCHAR(20), LANDLINE VARCHAR(20), " +
                    "PRIMARY KEY (ID), FOREIGN KEY (USER_ID) REFERENCES USERS_TBL(ID))";

            String CREATE_SUBTRADES_TBL = "CREATE TABLE IF NOT EXISTS SUBTRADES_TBL(ID INTEGER NOT NULL AUTO_INCREMENT, " +
                    "CLIENT_ID INT, SUBTRADE VARCHAR(20), FIRST_NAME VARCHAR(20), LAST_NAME VARCHAR(20), " +
                    "CONTACT_PERSON VARCHAR(30), ADDRESS VARCHAR(20), EMAIL VARCHAR(30), MOBILE VARCHAR(20), " +
                    "BEST_WAY_TO_CONTACT VARCHAR(20), PRIMARY KEY (ID), FOREIGN KEY (CLIENT_ID) REFERENCES CLIENTS_TBL(ID))";

            String CREATE_PRICE_CARDS_TBL = "CREATE TABLE IF NOT EXISTS PRICE_CARDS_TBL(ID INTEGER NOT NULL AUTO_INCREMENT, " +
                    "SUBTRADE_ID INT, MATERIAL VARCHAR(20), UNIT VARCHAR(20), RATE VARCHAR(20), PRIMARY KEY (ID), " +
                    "FOREIGN KEY (SUBTRADE_ID) REFERENCES SUBTRADES_TBL(ID))";

            String CREATE_STRUCTURE_TBL = "CREATE TABLE IF NOT EXISTS STRUCTURES_TBL(ID INTEGER NOT NULL AUTO_INCREMENT, " +
                    "STRUCTURE VARCHAR(45) UNIQUE , PRIMARY KEY (ID))";

            String CREATE_PARTS_TBL = "CREATE TABLE IF NOT EXISTS STRUCTURES_PARTS_TBL(ID INTEGER NOT NULL AUTO_INCREMENT, " +
                    "STRUCTURE_ID INT, PARTS VARCHAR(45) UNIQUE , PRIMARY KEY (ID), FOREIGN KEY (STRUCTURE_ID) REFERENCES " +
                    "STRUCTURES_TBL(ID))";
//
//            String CREATE_SHEDS_TBL = "CREATE TABLE IF NOT EXISTS SHED_STRUCTURE_TBL(ID INTEGER NOT NULL AUTO_INCREMENT, " +
//                    "STRUCTURES VARCHAR(45), PRIMARY KEY (ID))";
//
//            String CREATE_SHED_PARTS_TBL = "CREATE TABLE IF NOT EXISTS SHED_PARTS_TBL(ID INTEGER NOT NULL AUTO_INCREMENT, " +
//                    "SHED_STRUCTURE_ID INT, PARTS VARCHAR(45), PRIMARY KEY (ID), FOREIGN KEY (SHED_STRUCTURE_ID) " +
//                    "REFERENCES SHEDS_TBL(ID))";
//
//            String CREATE_SHED_COMPONENTS_TBL = "CREATE TABLE IF NOT EXISTS SHED_COMPONENTS_TBL(ID INTEGER NOT NULL AUTO_INCREMENT, " +
//                    "SHED_STRUCTURE_ID INT, PARTS VARCHAR(45), PRIMARY KEY (ID), FOREIGN KEY (SHED_STRUCTURE_ID) " +
//                    "REFERENCES SHEDS_TBL(ID))";

            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_USERS_TBL);
            statement.executeUpdate(CREATE_CLIENTS_TBL);
            statement.executeUpdate(CREATE_SUBTRADES_TBL);
            statement.executeUpdate(CREATE_PRICE_CARDS_TBL);
            statement.executeUpdate(CREATE_STRUCTURE_TBL);
            statement.executeUpdate(CREATE_PARTS_TBL);
//            statement.executeUpdate(CREATE_SHEDS_TBL);
//            statement.executeUpdate(CREATE_SHED_PARTS_TBL);
//            statement.executeUpdate(CREATE_SHED_COMPONENTS_TBL);

            //creates the default user
            addUser("estimator21", "password", "What is your Mother's maiden name?",
                    "secret");

            //inserting structures
            setupSheetsData.structures.forEach(this::insertStructures);

            //inserting parts
            setupSheetsData.foundations.forEach(part -> insertStructureParts(1, part));
            setupSheetsData.extOpenings.forEach(part -> insertStructureParts(9, part));
            setupSheetsData.intOpenings.forEach(part -> insertStructureParts(10, part));
            setupSheetsData.postBeamHardware.forEach(part -> insertStructureParts(17, part));
            setupSheetsData.wallsSglLev.forEach(part -> insertStructureParts(18, part));
            setupSheetsData.roof.forEach(part -> insertStructureParts(30, part));
            setupSheetsData.extLining.forEach(part -> insertStructureParts(31, part));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //add
    public void addUser(String username, String password, String security_question, String security_answer) {
        try {
            String sql = "INSERT INTO USERS_TBL (USERNAME, PASSWORD, SECURITY_QUESTION, SECURITY_ANSWER) VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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

    public void addSubtrade(int client_id, String subtrade, String first_name, String last_name, String contact_person,
                            String address, String email, String mobile, String best_way_to_contact) {

        try {
            String sql = "INSERT INTO SUBTRADES_TBL (CLIENT_ID, SUBTRADE, FIRST_NAME, LAST_NAME, CONTACT_PERSON," +
                    " ADDRESS, EMAIL, MOBILE, BEST_WAY_TO_CONTACT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
            Statement statement = connection.createStatement();
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
            Statement statement = connection.createStatement();
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
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM CLIENTS_TBL WHERE ID = " + id;
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

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
            Statement statement = connection.createStatement();
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

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
            Statement statement = connection.createStatement();
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
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM PRICE_CARD_TBL WHERE SUBTRADE_ID = " + id;
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

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("ID");
                user_id = id;
                System.out.println(user_id);
                return true;
            } else {
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
            Statement statement = connection.createStatement();
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
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM CLIENTS_TBL";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                CLIENTS.addAll(resultSet.getString("FULL_NAME"));
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

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, subtrade_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                rate.setText(resultSet.getString("RATE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //structures db
    public void insertStructures(String structureName) {
        try {
            String sql = "INSERT IGNORE INTO STRUCTURES_TBL (STRUCTURE) VALUES (?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, structureName);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertStructureParts(int structureID, String partName) {
        try {
            String sql = "INSERT IGNORE INTO STRUCTURES_PARTS_TBL (STRUCTURE_ID, PARTS) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, structureID);
            preparedStatement.setString(2, partName);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayParts(int structure_id, ObservableList<String> parts) {
        try {
            String sql = "SELECT PARTS FROM STRUCTURES_PARTS_TBL WHERE STRUCTURE_ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, structure_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //create container for data
                parts.add(resultSet.getString("PARTS"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayShedParts(int structure_id, ObservableList<String> parts) {
        try {
            String sql = "SELECT PARTS FROM SHED_PARTS_TBL WHERE STRUCTURE_ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, structure_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //create container for data
                parts.add(resultSet.getString("PARTS"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //this is for getting ID for every part before going into sheets
    public void getPartID(int structure_id, String part, Label id) {
        try {
            String sql = "SELECT ID FROM SHED_PARTS_TBL WHERE STRUCTURE_ID = ? AND PARTS = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, structure_id);
            preparedStatement.setString(2, part);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //create container for data
                id.setText(String.valueOf(resultSet.getInt("ID")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //should be called in condition when adding section
    public void setShedComponents(int parts_id, int section) {
        ArrayList<String> components = new ArrayList<>();
        try {
            String sql = "SELECT COMPONENTS FROM SHED_COMPONENTS_CONTENT_TBL WHERE PARTS_ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, parts_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                components.addAll(Collections.singleton(resultSet.getString("COMPONENTS")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        components.forEach(component -> {
            try {
                String sql = "INSERT INTO SHED_COMPONENTS_TBL (PARTS_ID, SECTION, COMPONENT) VALUES (?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, parts_id);
                preparedStatement.setInt(2, section);
                preparedStatement.setString(3, component);

                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void displayFoundationComponents(int parts_id, int section, ObservableList<foundationsData> content) {
        try {
            String sql = "SELECT * FROM SHED_COMPONENTS_TBL WHERE PARTS_ID = ? AND SECTION = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, parts_id);
            preparedStatement.setInt(2, section);

            System.out.println(parts_id + " " + section);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //create container for data
                content.addAll(new foundationsData(resultSet.getString("COMPONENT"),
                        resultSet.getString("CODE"), resultSet.getString("DESCRIPTION"),
                        resultSet.getString("EXTRA1"), resultSet.getString("EXTRA2"),
                        resultSet.getString("QUANTITY"), resultSet.getString("USAGE"),
                        resultSet.getString("WASTE"), resultSet.getString("SUBHEADING"),
                        resultSet.getString("USAGE2")) {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearComponents() {
        try {
            String sql = "TRUNCATE TABLE SHED_COMPONENTS_TBL";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSelectedSets(int part_id, int section_id, JFXComboBox set, JFXComboBox set_override) {
        //should set the comboboxes identified by the id's
        try {
            String sql = "SELECT * FROM SHED_SECTION_TBL WHERE PARTS_ID = ? AND SECTION = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, part_id);
            preparedStatement.setInt(2, section_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                set.getSelectionModel().select(resultSet.getString("SETS"));
                set_override.getSelectionModel().select(resultSet.getString("SET_OVERRIDE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSelectedSets(int part_id, int section_id, String set, String set_override) {
        try {
            String sql = "UPDATE SHED_SECTION_TBL SET SETS = ?, SET_OVERRIDE = ? WHERE PARTS_ID = ? AND SECTION = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, set);
            preparedStatement.setString(2, set_override);
            preparedStatement.setInt(3, part_id);
            preparedStatement.setInt(4, section_id);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSections(int part_id, int section) {
        try {
            String sql = "INSERT INTO SHED_SECTION_TBL (PARTS_ID, SECTION) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, part_id);
            preparedStatement.setInt(2, section);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
