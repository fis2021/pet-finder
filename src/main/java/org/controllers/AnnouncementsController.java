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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.model.Pet;
import org.model.User;
import org.services.DatabaseService;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class AnnouncementsController {

    private User user;

    @FXML
    private TextField petName;

    @FXML
    private TextField petInfo;

    @FXML
    private ChoiceBox petType = new ChoiceBox();

    @FXML
    private TextField adInfo;

    @FXML
    private ChoiceBox category = new ChoiceBox();

    @FXML
    private Text AccountStatus;

    @FXML
    private Text AddStatus;

    @FXML
    private File image;
    @FXML
    private String imagePath="";
    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        category.getItems().addAll("Lost", "Found", "Adoption");
        petType.getItems().addAll("Cat","Dog","Other");
    }

   /* @FXML
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
    */


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
    void handleAddPhotoAction() throws MalformedURLException {
        Stage stage = new Stage();
        stage.setTitle("Add Photo");
        FileChooser filechooser = new FileChooser();
        filechooser.setInitialDirectory(new File("C:\\"));
        filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jpg files","*.jpg"));
        image = filechooser.showOpenDialog(stage);
        imagePath = image.getAbsolutePath();
        filechooser.setInitialDirectory(image.getParentFile());
        File file = new File(imagePath);
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        imageView.setImage(profile);
        imageView.setFitHeight(100);
        imageView.setFitWidth(150);
        imageView.rotateProperty();
    }

    @FXML
    public void clearImageAction(ActionEvent event) throws MalformedURLException {
        imagePath = "";
        File file = new File(imagePath);
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        imageView.setImage(profile);
        imageView.setFitHeight(0);
        imageView.setFitWidth(0);
        imageView.rotateProperty();
    }

    @FXML
    public void handleAddPetAction(ActionEvent event) throws IOException {
        if(petName.getText() == "" || petType.getValue() == null){
            AddStatus.setText("Name and type are required!");
            return;
        }
        Pet crt = new Pet(petName.getText(), (String) petType.getValue());

        crt.setInfo(petInfo.getText());
        crt.setImagePath(imagePath);
        user.addPet(crt);
        DatabaseService.updateUser(user);

        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("shelterManagerPage.fxml"));
        Parent root = loader.load();
        currentStage.setTitle("Manage pets");
        currentStage.setScene(new Scene(root, 500, 500));
        currentStage.show();

        ShelterManagerController smc = loader.getController();
        smc.setUser(user);
        smc.updateList();
    }


    /*@FXML
    public void handleRemovePetAction(ActionEvent event) {
        String crt = (String) pets.getSelectionModel().getSelectedItem();

        ArrayList<Pet> userPets;

        userPets = user.getPetList();

        for(Pet pet : userPets){
            if(pet.toString().equals(crt)){
                userPets.remove(pet);
            }
        }

        DatabaseService.updateUser(user);

        this.updateList();
    }*/

    /*@FXML
    public void cancelAddPet(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("shelterManagerPage.fxml"));
        Parent root = loader.load();
        currentStage.setTitle("Manage pets");
        currentStage.setScene(new Scene(root, 500, 500));
        currentStage.show();

        ShelterManagerController smc = loader.getController();
        smc.setUser(user);
        smc.updateList();
    }*/

    /*@FXML
    public void openAddPetPage(ActionEvent event) throws Exception{
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addPetPage.fxml"));
        Parent root = loader.load();
        currentStage.setTitle("AddPet");
        currentStage.setScene(new Scene(root, 500, 500));
        currentStage.show();

        ShelterManagerController smc = loader.getController();
        smc.setUser(user);

    }*/

    @FXML
    public void redirectToHomePage(ActionEvent event){
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("homePage.fxml"));
            Parent root = loader.load();
            currentStage.setTitle("Home");
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();

            HomePageController hpc = loader.getController();
            hpc.setUser(user);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void setUser(User user){
        this.user = user;
        AccountStatus.setText("Logged-in as " + user.getUsername());
    }
}