package org.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.model.Pet;
import org.model.User;
import java.util.ArrayList;

public class ShelterManagerController {

    private User user;

    @FXML
    private ListView pets = new ListView<>();

    @FXML
    private ChoiceBox type = new ChoiceBox();

    @FXML
    private Text AccountStatus;

    @FXML
    public void initialize() {
        type.getItems().addAll("Dog", "Cat", "Other");
    }

    @FXML
    public void updateList() {
        ObservableList<String> crtPets = FXCollections.observableArrayList();
        ArrayList<Pet> crtPetList = user.getPetList();

        for(Pet pet : crtPetList){
            crtPets.add(pet.toString());
        }

        if(crtPets.isEmpty()){
            crtPets.add("Currently you have no pets");
        }

        pets.setItems(crtPets);
    }

    @FXML
    public void handleSignOutAction(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
            currentStage.setTitle("Login");
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void handleAddPetAction(){

    }

    @FXML
    public void openAddPetPage(ActionEvent event) throws Exception{
        //User currentUser=userRepository.find
        Node node = (Node) event.getSource();
        Stage addPetStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("addPetPage.fxml"));
        addPetStage.setTitle("AddPet");
        addPetStage.setScene(new Scene(root, 500, 500));
        addPetStage.show();
    }

    @FXML
    public void redirectToShelterHomePage(ActionEvent event){
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("shelterHomePage.fxml"));
            currentStage.setTitle("Shelter Home Page");
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void setUser(User user){
        this.user = user;
        AccountStatus.setText("Logged-in as " + user.getUsername());
    }
}
