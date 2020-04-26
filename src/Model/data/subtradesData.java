package Model.data;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class subtradesData extends RecursiveTreeObject<subtradesData> {

    private SimpleStringProperty id, subtrade, full_name, contact_person, address, email, mobile, best_way_to_contact;

    public subtradesData(String id, String subtrade, String full_name, String contact_person, String address, String email,
                         String mobile, String best_way_to_contact) {

        this.id = new SimpleStringProperty(id);
        this.subtrade = new SimpleStringProperty(subtrade);
        this.full_name = new SimpleStringProperty(full_name);
        this.contact_person = new SimpleStringProperty(contact_person);
        this.address = new SimpleStringProperty(address);
        this.email = new SimpleStringProperty(email);
        this.mobile = new SimpleStringProperty(mobile);
        this.best_way_to_contact = new SimpleStringProperty(best_way_to_contact);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getSubtrade() {
        return subtrade.get();
    }

    public SimpleStringProperty subtradeProperty() {
        return subtrade;
    }

    public void setSubtrade(String subtrade) {
        this.subtrade.set(subtrade);
    }

    public String getFull_name() {
        return full_name.get();
    }

    public SimpleStringProperty full_nameProperty() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name.set(full_name);
    }

    public String getContact_person() {
        return contact_person.get();
    }

    public SimpleStringProperty contact_personProperty() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person.set(contact_person);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getMobile() {
        return mobile.get();
    }

    public SimpleStringProperty mobileProperty() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile.set(mobile);
    }

    public String getBest_way_to_contact() {
        return best_way_to_contact.get();
    }

    public SimpleStringProperty best_way_to_contactProperty() {
        return best_way_to_contact;
    }

    public void setBest_way_to_contact(String best_way_to_contact) {
        this.best_way_to_contact.set(best_way_to_contact);
    }
}
