package Controllers;

import DataBase.DataBase;
import Model.data.clientsData;
import Model.data.subtradesData;
import Main.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class builderController implements Initializable {

    public JFXButton REGISTER_CLIENT;

    public JFXTreeTableView<clientsData> CLIENTS_TBL;
    public TreeTableColumn<clientsData, String> CLIENT_ID;
    public TreeTableColumn<clientsData, String> CLIENT_FULL_NAME;
    public static ObservableList<clientsData> clientsData;

    public JFXTreeTableView<subtradesData> SUBTRADE_TBL;
    public TreeTableColumn<subtradesData, String> SUBTRADE_ID;
    public TreeTableColumn<subtradesData, String> SUBTRADE_TYPE;
    public TreeTableColumn<subtradesData, String> SUBTRADE_FULL_NAME;
    public TreeTableColumn<subtradesData, String> SUBTRADE_ADDRESS;
    public TreeTableColumn<subtradesData, String> SUBTRADE_CONTACT;
    public TreeTableColumn<subtradesData, String> SUBTRADE_EMAIL;
    public TreeTableColumn<subtradesData, String> SUBTRADE_MOBILE;
    public TreeTableColumn<subtradesData, String> SUBTRADE_BEST_CONTACT;
    public static ObservableList<subtradesData> subtradesData;

    public static int client_id = 0;

    private ContextMenu CLIENT_MENU = new ContextMenu();
    private MenuItem CLIENT_ADD_SUBTRADE = new MenuItem("Register Subtrade");
    private MenuItem CLIENT_VIEW = new MenuItem("View");
    private MenuItem CLIENT_DELETE = new MenuItem("Delete");

    public static int subtrade_id = 0;

    private ContextMenu SUBTRADE_MENU = new ContextMenu();
    private MenuItem SUBTRADE_VIEW = new MenuItem("View");
    private MenuItem SUBTRADE_DELETE = new MenuItem("Delete");

    //create form
    private static Stage registerClientStage;
    private static Stage registerSubtradeStage;
    private static Stage viewClientStage;
    private static Stage viewSubtradeStage;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        REGISTER_CLIENT.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Views/registerClient.fxml"));
                AnchorPane pane = loader.load();

                //draggable pop up
                pane.setOnMousePressed(event1 -> {
                    xOffset = event1.getSceneX();
                    yOffset = event1.getSceneY();
                });

                pane.setOnMouseDragged(event1 -> {
                    registerClientStage.setX(event1.getScreenX() - xOffset);
                    registerClientStage.setY(event1.getScreenY() - yOffset);
                });

                Scene scene = new Scene(pane);
                scene.setFill(Color.TRANSPARENT);
                scene.getStylesheets().addAll(Main.class.getResource("/Views/CSS/style.css").toExternalForm());
                registerClientStage = new Stage();
                registerClientStage.setScene(scene);
                registerClientStage.initStyle(StageStyle.UNDECORATED);
                registerClientStage.initModality(Modality.APPLICATION_MODAL);
                registerClientStage.initStyle(StageStyle.TRANSPARENT);
                registerClientStage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //clients data & table
        clientsData = FXCollections.observableArrayList();

        CLIENT_ID.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("id")
        );
        CLIENT_FULL_NAME.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("full_name")
        );

        TreeItem<clientsData> client_root = new RecursiveTreeItem<>(clientsData, RecursiveTreeObject::getChildren);
        CLIENTS_TBL.setRoot(client_root);
        CLIENTS_TBL.setShowRoot(false);

        CLIENT_MENU.getItems().add(CLIENT_ADD_SUBTRADE);
        CLIENT_MENU.getItems().add(CLIENT_VIEW);
        CLIENT_MENU.getItems().add(CLIENT_DELETE);

        CLIENTS_TBL.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                setClientId(newValue));

        CLIENTS_TBL.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                DataBase db = DataBase.getInstance();
                db.displaySubtrades(client_id, subtradesData);
            }

            if (event.getButton() == MouseButton.SECONDARY) {
                CLIENT_MENU.show(CLIENTS_TBL, event.getScreenX(), event.getScreenY());
            } else {
                CLIENT_MENU.hide();
            }
        });

        CLIENT_ADD_SUBTRADE.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Views/registerSubtrade.fxml"));
                AnchorPane pane = loader.load();

                //draggable pop up
                pane.setOnMousePressed(event1 -> {
                    xOffset = event1.getSceneX();
                    yOffset = event1.getSceneY();
                });

                pane.setOnMouseDragged(event1 -> {
                    registerSubtradeStage.setX(event1.getScreenX() - xOffset);
                    registerSubtradeStage.setY(event1.getScreenY() - yOffset);
                });

                Scene scene = new Scene(pane);
                scene.setFill(Color.TRANSPARENT);
                scene.getStylesheets().addAll(Main.class.getResource("/Views/CSS/style.css").toExternalForm());
                registerSubtradeStage = new Stage();
                registerSubtradeStage.setScene(scene);
                registerSubtradeStage.initStyle(StageStyle.UNDECORATED);
                registerSubtradeStage.initModality(Modality.APPLICATION_MODAL);
                registerSubtradeStage.initStyle(StageStyle.TRANSPARENT);
                registerSubtradeStage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CLIENT_VIEW.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Views/viewClient.fxml"));
                AnchorPane pane = loader.load();

                //draggable pop up
                pane.setOnMousePressed(event1 -> {
                    xOffset = event1.getSceneX();
                    yOffset = event1.getSceneY();
                });

                pane.setOnMouseDragged(event1 -> {
                    viewClientStage.setX(event1.getScreenX() - xOffset);
                    viewClientStage.setY(event1.getScreenY() - yOffset);
                });

                Scene scene = new Scene(pane);
                scene.setFill(Color.TRANSPARENT);
                scene.getStylesheets().addAll(Main.class.getResource("/Views/CSS/style.css").toExternalForm());
                viewClientStage = new Stage();
                viewClientStage.setScene(scene);
                viewClientStage.initStyle(StageStyle.UNDECORATED);
                viewClientStage.initModality(Modality.APPLICATION_MODAL);
                viewClientStage.initStyle(StageStyle.TRANSPARENT);
                viewClientStage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CLIENT_DELETE.setOnAction(event -> {
            DataBase db = DataBase.getInstance();
            db.deleteClient(client_id);
            db.displayClients(clientsData);
            JOptionPane.showMessageDialog(null, "Client deleted!");
        });

        //clients data & table
        subtradesData = FXCollections.observableArrayList();

        SUBTRADE_ID.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("id")
        );
        SUBTRADE_TYPE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("subtrade")
        );
        SUBTRADE_FULL_NAME.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("full_name")
        );
        SUBTRADE_ADDRESS.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("address")
        );
        SUBTRADE_CONTACT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("contact_person")
        );
        SUBTRADE_EMAIL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("email")
        );
        SUBTRADE_MOBILE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("mobile")
        );
        SUBTRADE_BEST_CONTACT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("best_way_to_contact")
        );

        TreeItem<subtradesData> subtrade_root = new RecursiveTreeItem<>(subtradesData, RecursiveTreeObject::getChildren);
        SUBTRADE_TBL.setRoot(subtrade_root);
        SUBTRADE_TBL.setShowRoot(false);

        SUBTRADE_MENU.getItems().add(SUBTRADE_VIEW);
        SUBTRADE_MENU.getItems().add(SUBTRADE_DELETE);

        SUBTRADE_TBL.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setSubtradeId(newValue));

        SUBTRADE_TBL.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                SUBTRADE_MENU.show(SUBTRADE_TBL, event.getScreenX(), event.getScreenY());
            } else {
                SUBTRADE_MENU.hide();
            }
        });

        SUBTRADE_VIEW.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Views/viewSubtrade.fxml"));
                AnchorPane pane = loader.load();

                //draggable pop up
                pane.setOnMousePressed(event1 -> {
                    xOffset = event1.getSceneX();
                    yOffset = event1.getSceneY();
                });

                pane.setOnMouseDragged(event1 -> {
                    viewSubtradeStage.setX(event1.getScreenX() - xOffset);
                    viewSubtradeStage.setY(event1.getScreenY() - yOffset);
                });

                Scene scene = new Scene(pane);
                scene.setFill(Color.TRANSPARENT);
                scene.getStylesheets().addAll(Main.class.getResource("/Views/CSS/style.css").toExternalForm());
                viewSubtradeStage = new Stage();
                viewSubtradeStage.setScene(scene);
                viewSubtradeStage.initStyle(StageStyle.UNDECORATED);
                viewSubtradeStage.initModality(Modality.APPLICATION_MODAL);
                viewSubtradeStage.initStyle(StageStyle.TRANSPARENT);
                viewSubtradeStage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        SUBTRADE_DELETE.setOnAction(event -> {
            DataBase db = DataBase.getInstance();
            db.deleteSubtrade(subtrade_id);
            db.displaySubtrades(client_id, subtradesData);
            JOptionPane.showMessageDialog(null, "Subtrade deleted!");
        });
    }

    private void setClientId(TreeItem<clientsData> treeItem) {
        try {
            client_id = Integer.valueOf(treeItem.getValue().getId());
        } catch (Exception e) {
            e.getSuppressed();
        }
    }

    private void setSubtradeId(TreeItem<subtradesData> treeItem) {
        try {
            subtrade_id = Integer.valueOf(treeItem.getValue().getId());
        } catch (Exception e) {
            e.getSuppressed();
        }
    }

    static void closeClientStage() {
        registerClientStage.close();
    }
    static void closeSubtradeStage() {
        registerSubtradeStage.close();
    }
    static void closeViewClientStage() {
        viewClientStage.close();
    }
    static void closeViewSubtradeStage() {
        viewSubtradeStage.close();
    }

}
