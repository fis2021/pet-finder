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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.model.Announcement;
import org.model.ImageStringTableRow;
import org.model.Pet;
import org.model.User;
import org.services.AnnouncementService;
import org.services.DatabaseService;
import org.services.UserService;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class ShelterManagerController extends Controller{
    @FXML
    private TextField petName;
    @FXML
    private TextField petInfo;
    @FXML
    private ChoiceBox type = new ChoiceBox();

    @FXML
    private Text AddStatus;

    @FXML
    private File image;
    @FXML
    private String imagePath = "src/main/resources/img/pet.png";
    @FXML
    private ImageView imageView;

    @FXML
    private TableView<ImageStringTableRow> petsTable;
    @FXML
    private TableColumn<ImageStringTableRow, ImageView> petImage;
    @FXML
    private TableColumn<ImageStringTableRow, String> petInformation;


    @FXML
    public void initialize() {
        type.getItems().addAll("Dog", "Cat", "Other");

        if(imageView != null){
            imagePath = "src/main/resources/img/pet.png";
            File file = new File(imagePath);
            String localUrl = null;
            try {
                localUrl = file.toURI().toURL().toExternalForm();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Image profile = new Image(localUrl, false);
            imageView.setImage(profile);
            imageView.setFitHeight(150);
            imageView.setFitWidth(100);
        }

        if(petImage != null && petInformation != null && petsTable != null){
            petImage.setPrefWidth(100);
            petImage.setCellValueFactory(new PropertyValueFactory<>("imageView"));
            petInformation.setCellValueFactory(new PropertyValueFactory<>("info"));
        }
    }

    @FXML
    public void updateList() throws MalformedURLException {
        ObservableList<ImageStringTableRow> pets = FXCollections.observableArrayList();
        ArrayList<Pet> crtPetList = user.getPetList();

        for(Pet pet : crtPetList){
            File file = new File(pet.getImagePath());
            String localUrl = file.toURI().toURL().toExternalForm();
            Image profile = new Image(localUrl, false);
            ImageView crtImg = new ImageView();
            crtImg.setImage(profile);
            crtImg.setFitHeight(100);
            crtImg.setFitWidth(100);

            String crtInfo = pet.toString();

            ImageStringTableRow crtPet = new ImageStringTableRow(crtImg, crtInfo, pet.getID());
            pets.add(crtPet);
        }

        petsTable.setItems(pets);
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
        imagePath = "src/main/resources/img/pet.png";
        File file = new File(imagePath);
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        imageView.setImage(profile);
        imageView.setFitHeight(150);
        imageView.setFitWidth(100);
    }

    @FXML
    public void handleAddPetAction(ActionEvent event) throws IOException {
        if(petName.getText() == "" || type.getValue() == null){
            AddStatus.setText("Name and type are required!");
            return;
        }
        Pet crt = new Pet(petName.getText(), (String) type.getValue());

        crt.setInfo(petInfo.getText());
        crt.setImagePath(imagePath);
        user.addPet(crt);
        UserService.updateUser(user);
        Announcement crtAd = new Announcement(crt,user,"Adoption");
        crtAd.setInfo("");
        AnnouncementService.addAnnouncement(crtAd);

        FXMLLoader loader = redirect(event,"shelterManagerScene.fxml", "Manage pets");
        ShelterManagerController smc = loader.getController();
        smc.setUser(user);
        smc.updateList();
    }

    @FXML
    public void handleRemovePetAction(ActionEvent event) throws MalformedURLException {
        ImageStringTableRow crt = (ImageStringTableRow) petsTable.getSelectionModel().getSelectedItem();

        String crtPetID = crt.getID();
        ArrayList<Pet> userPets;
        userPets = user.getPetList();

        Announcement ad = null;

        Pet crtPet = null;

        for(Pet pet : userPets){
            if(pet.getID().equals(crtPetID)){
                crtPet=pet;
            }
        }

        if(crtPet!=null){
            userPets.remove(crtPet);
            this.user.removePet(crtPet);

            ad = AnnouncementService.getPetAnnouncement(crtPet);
        }

        if(ad!=null) { AnnouncementService.removeAnnouncement(ad); }
        UserService.updateUser(user);

        this.updateList();
    }

    @FXML
    public void cancelAddPet(ActionEvent event) throws IOException {
        FXMLLoader loader = redirect(event,"shelterManagerScene.fxml", "Manage pets");
        ShelterManagerController smc = loader.getController();
        smc.setUser(user);
        smc.updateList();
    }

    @FXML
    public void openAddPetPage(ActionEvent event) throws Exception{
        FXMLLoader loader = redirect(event,"addPetPage.fxml", "Add pet");
        ShelterManagerController smc = loader.getController();
        smc.setUser(user);
    }

    @FXML
    public void redirectToHomePage(ActionEvent event) throws Exception{
        redirectToHome(event,user);
    }
}
