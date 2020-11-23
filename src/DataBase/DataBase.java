package DataBase;

import Model.data.clientsData;
import Model.data.layouts.externalFramingColumnsData;
import Model.data.layouts.externalFramingPolesData;
import Model.data.layouts.foundationsConcreteBoresData;
import Model.data.layouts.foundationsPostFootingsData;
import Model.data.setupSheetsData;
import Model.data.shed.externalFraming.columnsSec;
import Model.data.shed.externalFraming.polesSec;
import Model.data.shed.foundations.concreteBoresSec;
import Model.data.shed.foundations.postFootingsSec;
import Model.data.shed.foundationsMaterials;
import Model.data.subtradesData;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.sql.*;
import java.text.DecimalFormat;

public class DataBase {

    private static final String URL = "jdbc:mysql://localhost:3306/pt_graphiqs_db?serverTimezone=UTC";
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
            connection = DriverManager.getConnection(URL, "root", "password");
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            setTables();
        } catch (SQLException e) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, "root", "");
                Statement statement = connection.createStatement();
                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
                setTables();
            } catch (Exception exc) {

            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void setTables() {
        try {

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

    public void addClient(String first_name, String last_name, String contact_person, String email, String mobile,
                          String landline) {
        try {
            String sql = "INSERT INTO CLIENTS_TBL (USER_ID, FIRST_NAME, LAST_NAME, FULL_NAME, CONTACT_PERSON, EMAIL," +
                    " MOBILE, LANDLINE) VALUES (LAST_INSERT_ID(), ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            preparedStatement.setString(3, first_name + " " + last_name);
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

                subtradesData.addAll(new subtradesData(ID, SUBTRADE, FIRST_NAME + " " + LAST_NAME, CONTACT_PERSON,
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
            String sql = "SELECT * FROM SUBTRADES_TBL WHERE ID = " + id;
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
                JOptionPane.showMessageDialog(null, "Incorrect username or password");
                return false;
            }
        } catch (Exception e) {
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

    public void displayFoundationComponents(int parts_id, int section, ObservableList<foundationsMaterials> content) {
        try {
            String sql = "SELECT * FROM SHED_COMPONENTS_TBL WHERE PARTS_ID = ? AND SECTION = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, parts_id);
            preparedStatement.setInt(2, section);

            System.out.println(parts_id + " " + section);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //create container for data
                content.addAll(new foundationsMaterials(resultSet.getString("COMPONENT"),
                        resultSet.getString("CODE"), resultSet.getString("DESCRIPTION"),
                        resultSet.getString("UNIT"), resultSet.getString("QUANTITY"),
                        resultSet.getString("USAGE")));
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

    public void getSets(int part_id, ObservableList<String> setsList) {
        try {
            setsList.clear();
            String sql = "SELECT * FROM SHED_SETS WHERE PART_ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, part_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                setsList.addAll(resultSet.getString("SETS"));
            }
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

    public void setSelectedSets(int part_id, int section_id, String set, String set_override, String material) {
        try {
            String sql = "SELECT ID FROM SHED_SECTION_TBL WHERE PARTS_ID = ? AND SECTION = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, part_id);
            preparedStatement.setInt(2, section_id);

            int id = 0;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("ID");
            }

            String sql1 = "UPDATE SHED_SECTION_TBL SET SETS = ?, SET_OVERRIDE = ? WHERE ID = ?";

            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setString(1, set);
            preparedStatement1.setString(2, set_override);
            preparedStatement1.setInt(3, id);

            preparedStatement1.executeUpdate();

            if (!material.equals("")) {
                String sql2 = "UPDATE SHED_SECTION_TBL SET MATERIAL = ? WHERE ID = ?";

                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setString(1, material);
                preparedStatement2.setInt(2, id);

                preparedStatement2.executeUpdate();
            }

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

    public void clearSectionsTbl() {
        try {
            String sql = "TRUNCATE TABLE SHED_SECTION_TBL";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSectionDimensions(int part_id, int section, int dimension, String depth, String width, String length,
                                     String qty, String thickness) {
        try {
            String sql = "INSERT INTO SHED_SECTION_DIMENSIONS (PARTS_ID, SECTION, DIMENSION, DEPTH, WIDTH, LENGTH, QTY," +
                    " THICKNESS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, part_id);
            preparedStatement.setInt(2, section);
            preparedStatement.setInt(3, dimension);
            preparedStatement.setString(4, depth);
            preparedStatement.setString(5, width);
            preparedStatement.setString(6, length);
            preparedStatement.setString(7, qty);
            preparedStatement.setString(8, thickness);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //section dimensions
    public void getConcreteBoresSD(int part_id, int section, ObservableList<concreteBoresSec> sectionDimensions) {
        try {
            String sql = "SELECT * FROM shed_foundations_concrete_bores WHERE ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, section);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //create container for data
                System.out.println(resultSet.getString("QUANTITY"));
                System.out.println(resultSet.getString("DIAMETER"));
                System.out.println(resultSet.getString("HEIGHT"));
                System.out.println(resultSet.getString("VOLUME"));

                sectionDimensions.addAll(new concreteBoresSec("1",
                        String.valueOf(resultSet.getString("QUANTITY")),
                        resultSet.getString("DIAMETER"), resultSet.getString("HEIGHT"),
                        resultSet.getString("VOLUME")
                ));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPostFootingsSD(int part_id, int section, ObservableList<postFootingsSec> sectionDimensions) {
        try {
            String sql = "SELECT * FROM SHED_FOUNDATIONS_POST_FOOTINGS WHERE ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1, "test");
            preparedStatement.setInt(1, section);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //create container for data

                sectionDimensions.addAll(new postFootingsSec("1",
                        resultSet.getString("DEPTH"),
                        resultSet.getString("WIDTH"),
                        resultSet.getString("LENGTH"),
                        String.valueOf(resultSet.getString("QUANTITY")),
                        resultSet.getString("VOLUME")
                ));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSection(int part_id, int section) {
        try {
            String sql = "DELETE FROM SHED_SECTION_TBL WHERE PARTS_ID = ? AND SECTION = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, part_id);
            preparedStatement.setInt(2, section);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFoundationsPFData(int section, Label volume) {
        try {
            String sql = "SELECT * FROM SHED_FOUNDATIONS_POST_FOOTINGS WHERE ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, section);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                volume.setText(resultSet.getString("VOLUME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFoundationsCBData(int section, Label volume) {
        try {
            String sql = "SELECT * FROM SHED_FOUNDATIONS_CONCRETE_BORES WHERE ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, section);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                volume.setText(resultSet.getString("VOLUME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertComponents(int parts_id, int section, String component, String code, String description,
                                 String unit, String quantity, String usage) {
        try {
            String sql = "INSERT INTO SHED_COMPONENTS_TBL (PARTS_ID, SECTION, COMPONENT, 'CODE', 'DESCRIPTION', UNIT," +
                    " QUANTITY, 'USAGE') VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, parts_id);
            preparedStatement.setInt(2, section);
            preparedStatement.setString(3, component);
            preparedStatement.setString(4, code);
            preparedStatement.setString(5, description);
            preparedStatement.setString(6, unit);
            preparedStatement.setString(7, quantity);
            preparedStatement.setString(8, usage);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearComponentsTable(int part_id, int section) {
        try {
            String sql = "DELETE FROM SHED_COMPONENTS_TBL WHERE PARTS_ID = ? AND SECTION = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, part_id);
            preparedStatement.setInt(2, section);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //foundations layout
    public void showFoundationsPF(ObservableList<foundationsPostFootingsData> foundationsPostFootingsData) {
        try {
            String sql = "SELECT * FROM SHED_FOUNDATIONS_POST_FOOTINGS";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //create label (load string from resource)
                Image foundations_img = new Image(getClass().getResourceAsStream(resultSet.getString("IMAGE")));
                Label image = new Label();
                image.setMinWidth(60);
                image.setMinHeight(30);
                image.setGraphic(new ImageView(foundations_img));
                image.setAlignment(Pos.CENTER);

                //create container for data
                foundationsPostFootingsData.addAll(new foundationsPostFootingsData(String.valueOf(
                        resultSet.getInt("ID")), image, String.valueOf(resultSet
                        .getString("QUANTITY")), resultSet.getString("DEPTH"),
                        resultSet.getString("WIDTH"), resultSet.getString("LENGTH"),
                        resultSet.getString("VOLUME")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertFoundationsPF(int section, String path, int quantity, String depth, String width, String length,
                                    String volume) {
        try {
            String sql = "INSERT INTO SHED_FOUNDATIONS_POST_FOOTINGS (ID, IMAGE, QUANTITY, DEPTH, WIDTH, LENGTH," +
                    " VOLUME) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, section);
            preparedStatement.setString(2, path);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, depth);
            preparedStatement.setString(5, width);
            preparedStatement.setString(6, length);
            preparedStatement.setString(7, volume);

            preparedStatement.executeUpdate();

            setSections(1, section);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFoundationsPFCount(int quantity, String volume, int section) {
        try {
            String sql = "UPDATE SHED_FOUNDATIONS_POST_FOOTINGS SET QUANTITY = ?, VOLUME = ? WHERE ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, volume);
            preparedStatement.setInt(3, section);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void identifyFoundationsPFValues(String depth, String width, String length, Label section, Label volume,
                                            Label quantity) {
        try {
            String sql = "SELECT * FROM SHED_FOUNDATIONS_POST_FOOTINGS WHERE DEPTH = ? AND WIDTH = ? AND LENGTH = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, depth);
            preparedStatement.setString(2, width);
            preparedStatement.setString(3, length);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                section.setText(String.valueOf(resultSet.getInt("ID")));
                quantity.setText(String.valueOf(resultSet.getInt("QUANTITY")));
                volume.setText(resultSet.getString("VOLUME"));
                System.out.println("section layout: " + resultSet.getInt("ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearLayoutsFoundations() {
        //todo -> add all tables
        try {
            String sql = "TRUNCATE TABLE SHED_FOUNDATIONS_POST_FOOTINGS";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String sql = "TRUNCATE TABLE SHED_FOUNDATIONS_CONCRETE_BORES";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFoundationsPFLastRow(Label section) {
        try {
            String sql = "SELECT ID FROM SHED_FOUNDATIONS_POST_FOOTINGS ORDER BY ID DESC LIMIT 1";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                section.setText(String.valueOf(resultSet.getInt("ID") + 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFoundationsTotal(JFXTextField post_footings, JFXTextField concrete_bores) {
        try {
            String sql = "SELECT * FROM SHED_FOUNDATIONS_POST_FOOTINGS";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            double total = 0.0;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                total += Double.parseDouble(resultSet.getString("VOLUME"));
                post_footings.setText(String.valueOf(new DecimalFormat("0.00").format(total)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String sql = "SELECT * FROM SHED_FOUNDATIONS_CONCRETE_BORES";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            double total = 0.0;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                total += Double.parseDouble(resultSet.getString("VOLUME"));
                concrete_bores.setText(String.valueOf(new DecimalFormat("0.00").format(total)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFoundationsPFSections(ObservableList<Integer> sectionList) {
        try {
            sectionList.clear();
            String sql = "SELECT * FROM SHED_SECTION_TBL WHERE PARTS_ID = 1";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int sections = 0;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                sections++;
                sectionList.addAll(resultSet.getInt("SECTION"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFoundationsPFSections(int part, int section) {

        try {
            String sql = "DELETE FROM SHED_SECTION_TBL WHERE PARTS_ID = ? AND SECTION = ? ";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, part);
            preparedStatement.setInt(2, section);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //concrete bores
    public void showFoundationsCB(ObservableList<foundationsConcreteBoresData> foundationsConcreteBoresData) {
        try {
            String sql = "SELECT * FROM SHED_FOUNDATIONS_CONCRETE_BORES";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //create label (load string from resource)
                Image foundations_img = new Image(getClass().getResourceAsStream(resultSet.getString("IMAGE")));
                Label image = new Label();
                image.setMinWidth(60);
                image.setMinHeight(30);
                image.setGraphic(new ImageView(foundations_img));
                image.setAlignment(Pos.CENTER);

                System.out.println(resultSet.getString("IMAGE"));

                //create container for data
                foundationsConcreteBoresData.addAll(new foundationsConcreteBoresData(String.valueOf(resultSet
                        .getInt("ID")), image, String.valueOf(resultSet.getString("QUANTITY")),
                        resultSet.getString("DIAMETER"), resultSet.getString("HEIGHT"),
                        resultSet.getString("VOLUME")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertFoundationsCB(int section, String path, int quantity, String diameter, String height, String volume) {
        try {
            String sql = "INSERT INTO SHED_FOUNDATIONS_CONCRETE_BORES (ID, IMAGE, QUANTITY, DIAMETER, HEIGHT, VOLUME)" +
                    " VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, section);
            preparedStatement.setString(2, path);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, diameter);
            preparedStatement.setString(5, height);
            preparedStatement.setString(6, volume);

            preparedStatement.executeUpdate();

            setSections(2, section);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void identifyFoundationsCBValues(String diameter, String height, Label section, Label volume, Label quantity) {
        try {
            String sql = "SELECT * FROM SHED_FOUNDATIONS_CONCRETE_BORES WHERE DIAMETER = ? AND HEIGHT = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, diameter);
            preparedStatement.setString(2, height);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                section.setText(String.valueOf(resultSet.getInt("ID")));
                quantity.setText(String.valueOf(resultSet.getInt("QUANTITY")));
                volume.setText(resultSet.getString("VOLUME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFoundationsCBCount(int quantity, String volume, int section) {
        try {
            String sql = "UPDATE SHED_FOUNDATIONS_CONCRETE_BORES SET QUANTITY = ?, VOLUME = ? WHERE ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, volume);
            preparedStatement.setInt(3, section);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFoundationsCBLastRow(Label section) {
        try {
            String sql = "SELECT ID FROM SHED_FOUNDATIONS_CONCRETE_BORES ORDER BY ID DESC LIMIT 1";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                section.setText(String.valueOf(resultSet.getInt("ID") + 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFoundationsCBSections(ObservableList<Integer> sectionList) {
        try {
            sectionList.clear();
            String sql = "SELECT * FROM SHED_SECTION_TBL WHERE PARTS_ID = 2";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int sections = 0;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                sections++;
                sectionList.addAll(resultSet.getInt("SECTION"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //TODO SIMPLIFIED DB QUERIES

    public void addSectionDimension(String structure, String part, String path, String diameter, String height, String depth, String width, String length, String volume) {
        try {
            System.out.println("ADD STRUCTURE "+structure );
            String sql = "INSERT INTO SHED_SECTION_DIMENSIONS (IMAGE,STRUCTURE,PART,HEIGHT,DEPTH," +
                    "DIAMETER,VOLUME,WIDTH,LENGTH,QTY,SECTION_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
                    " ON DUPLICATE KEY UPDATE QTY = QTY + 1 , VOLUME = CAST(VOLUME as FLOAT ) * QTY ";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, path);
            preparedStatement.setString(2, structure);
            preparedStatement.setString(3, part);
            preparedStatement.setString(4, height);
            preparedStatement.setString(5, depth);
            preparedStatement.setString(6, diameter);
            preparedStatement.setString(7, volume);
            preparedStatement.setString(8, width);
            preparedStatement.setString(9, length);
            preparedStatement.setInt(10, 1);
            preparedStatement.setInt(11, getLastSection(part) + 1);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePFLayoutsTable(String structure, String part, ObservableList<foundationsPostFootingsData> observableList) {

        try {
            String sql = "SELECT * FROM shed_section_dimensions WHERE STRUCTURE=? AND PART = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, structure);
            preparedStatement.setString(2, part);
            ResultSet resultSet = preparedStatement.executeQuery();
            int section = 1;
            observableList.clear();
            while (resultSet.next()) {
                //create label (load string from resource)
                Image foundations_img = new Image(getClass().getResourceAsStream(resultSet.getString("IMAGE")));
                Label image = new Label();
                image.setMinWidth(60);
                image.setMinHeight(30);
                image.setGraphic(new ImageView(foundations_img));
                image.setAlignment(Pos.CENTER);

                //create container for data
                observableList.addAll(new foundationsPostFootingsData(String.valueOf(section++), image, String.valueOf(resultSet
                        .getString("QTY")), resultSet.getString("DEPTH"),
                        resultSet.getString("WIDTH"), resultSet.getString("LENGTH"),
                        resultSet.getString("VOLUME")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCBLayoutsTable(String structure, String part, ObservableList<foundationsConcreteBoresData> observableList) {
        try {
            String sql = "SELECT * FROM shed_section_dimensions WHERE STRUCTURE=? AND PART = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, structure);
            preparedStatement.setString(2, part);
            ResultSet resultSet = preparedStatement.executeQuery();
            int section = 1;
            observableList.clear();
            while (resultSet.next()) {
                //create label (load string from resource)
                Image foundations_img = new Image(getClass().getResourceAsStream(resultSet.getString("IMAGE")));
                Label image = new Label();
                image.setMinWidth(60);
                image.setMinHeight(30);
                image.setGraphic(new ImageView(foundations_img));
                image.setAlignment(Pos.CENTER);

                //create container for data
                observableList.addAll(new foundationsConcreteBoresData(String.valueOf(section++), image, String.valueOf(resultSet.getString("QTY")),
                        resultSet.getString("DIAMETER"), resultSet.getString("HEIGHT"),
                        resultSet.getString("VOLUME")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLastSection(String part) {
        try {
            String sql = "SELECT SECTION_ID FROM SHED_FOUNDATIONS_SECTION_DIMENSIONS WHERE  PART = ? ORDER BY SECTION_ID DESC LIMIT 1";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, part);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                return resultSet.getInt("SECTION_ID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    //TODO SIMPLIFIED DB QUERIES


    public String[] getSectionData(String tableName, int section) {
        String[] result = new String[2];
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, section);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                result[0] = resultSet.getString("VOLUME");
                System.out.println("0 " + result[0]);
                result[1] = String.valueOf(resultSet.getInt("QUANTITY"));
                System.out.println("1 " + result[1]);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteSectionData(String tableName, int section) {

        try {
            String sql = "DELETE FROM " + tableName + " WHERE ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, section);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTotalVolume(String tablename) {
        double total = 0;
        try {
            String sql = "SELECT VOLUME FROM " + tablename;

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                double volume = Double.parseDouble(resultSet.getString("VOLUME"));
                total += volume;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Double.toString(total);
    }

    public void getExternalFramingLastRow(Label section, String part) {
        try {
            String sql = "SELECT * FROM SHED_EXTERNAL_FRAMING WHERE PART = ? ORDER BY SECTION_ID DESC LIMIT 1";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, part);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                section.setText(String.valueOf(resultSet.getInt("SECTION_ID") + 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void identifyExternalFramingValues(String part, String length, Label section, Label quantity) {
        try {
            String sql = "SELECT * FROM SHED_EXTERNAL_FRAMING WHERE PART = ? AND LENGTH = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, part);
            preparedStatement.setString(2, length);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                section.setText(String.valueOf(resultSet.getInt("SECTION_ID")));
                quantity.setText(String.valueOf(resultSet.getInt("QUANTITY")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertExternalFraming(int section, String path, String part, int quantity, String length) {
        try {
            String sql = "INSERT INTO SHED_EXTERNAL_FRAMING (SECTION_ID, IMAGE, PART, QUANTITY, LENGTH) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, section);
            preparedStatement.setString(2, path);
            preparedStatement.setString(3, part);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setString(5, length);

            preparedStatement.executeUpdate();

            switch (part) {
                case "Poles":
                    setSections(4, section);
                    break;
                case "Columns":
                    setSections(5, section);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateExternalFramingCount(int quantity, String part, int section) {
        try {
            String sql = "UPDATE SHED_EXTERNAL_FRAMING SET QUANTITY = ? WHERE PART = ? AND SECTION_ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, part);
            preparedStatement.setInt(3, section);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertExternalFramingSectionDimension(String part, String path, String length) {
        try {
            String sql = "INSERT INTO SHED_EXTERNAL_FRAMING_SECTION_DIMENSIONS (IMAGE, PART, LENGTH, QTY, SECTION_ID)" +
                    " VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE QTY = QTY + 1 ";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, path);
            preparedStatement.setString(2, part);
            preparedStatement.setString(3, length);
            preparedStatement.setInt(4, 1);
            preparedStatement.setInt(5, getLastSection(part) + 1);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearLayoutsExternalFraming() {
        //todo -> add all tables
        try {
            String sql = "TRUNCATE TABLE SHED_EXTERNAL_FRAMING";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showExternalFramingLayouts(ObservableList<externalFramingPolesData> externalFramingPolesData,
                                           ObservableList<externalFramingColumnsData> externalFramingColumnsData) {
        try {
            String sql = "SELECT * FROM SHED_EXTERNAL_FRAMING WHERE PART = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "Poles");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //create label (load string from resource)
                Image external_framing_img = new Image(getClass().getResourceAsStream(resultSet.getString("IMAGE")));
                Label image = new Label();
                image.setMinWidth(60);
                image.setMinHeight(30);
                image.setGraphic(new ImageView(external_framing_img));
                image.setAlignment(Pos.CENTER);

                System.out.println(resultSet.getString("IMAGE"));

                //create container for data
                externalFramingPolesData.addAll(new externalFramingPolesData(String.valueOf(
                        resultSet.getInt("SECTION_ID")), image, String.valueOf(resultSet
                        .getString("QUANTITY")), resultSet.getString("LENGTH")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String sql = "SELECT * FROM SHED_EXTERNAL_FRAMING WHERE PART = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "Columns");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //create label (load string from resource)
                Image external_framing_img = new Image(getClass().getResourceAsStream(resultSet.getString("IMAGE")));
                Label image = new Label();
                image.setMinWidth(60);
                image.setMinHeight(30);
                image.setGraphic(new ImageView(external_framing_img));
                image.setAlignment(Pos.CENTER);

                System.out.println(resultSet.getString("IMAGE"));

                //create container for data
                externalFramingColumnsData.addAll(new externalFramingColumnsData(String.valueOf(
                        resultSet.getInt("SECTION_ID")), image, String.valueOf(resultSet
                        .getString("QUANTITY")), resultSet.getString("LENGTH")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showExternalFramingSD(String part, int section, ObservableList<polesSec> polesSec,
                                      ObservableList<columnsSec> columnsSec) {
        try {
            String sql = "SELECT * FROM SHED_EXTERNAL_FRAMING WHERE PART = ? AND SECTION_ID = ?";

            if (part.equals("Poles")) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, "Poles");
                preparedStatement.setInt(2, section);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    //create container for data
                    polesSec.addAll(new polesSec("1", String.valueOf(resultSet.getInt("QUANTITY")),
                            resultSet.getString("LENGTH")));
                }
            }
            else if (part.equals("Columns")) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, "Columns");
                preparedStatement.setInt(2, section);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    //create container for data
                    columnsSec.addAll(new columnsSec("1", String.valueOf(resultSet.getInt("QUANTITY")),
                            resultSet.getString("LENGTH")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getExternalFramingSections(ObservableList<Integer> sectionListPL, ObservableList<Integer> sectionListCL) {
        try {
            sectionListPL.clear();
            String sql = "SELECT * FROM SHED_SECTION_TBL WHERE PARTS_ID = 4";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sectionListPL.addAll(resultSet.getInt("SECTION"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sectionListCL.clear();
            String sql = "SELECT * FROM SHED_SECTION_TBL WHERE PARTS_ID = 5";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sectionListCL.addAll(resultSet.getInt("SECTION"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSetMaterials(String set, ObservableList<String> materialList) {
        int set_id = 0;

        try {
            String sql = "SELECT ID FROM SHED_SETS WHERE SETS = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, set);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                set_id = resultSet.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            materialList.clear();
            String sql = "SELECT * FROM SHED_SET_MATERIALS WHERE SET_ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, set_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                materialList.addAll(resultSet.getString("MATERIAL"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSelectedMaterial(int parts_id, int section, Label selectedMaterial) {
        try {
            String sql = "SELECT * FROM SHED_SECTION_TBL WHERE PARTS_ID = ? AND SECTION = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, parts_id);
            preparedStatement.setInt(2, section);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                selectedMaterial.setText(resultSet.getString("MATERIAL"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
