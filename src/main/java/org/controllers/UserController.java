package org.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.exceptions.InvalidPhoneNoException;
import org.model.Address;
import org.model.User;
import org.services.UserService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserController extends Controller {
    @FXML
    private String newPhotoPath;
    @FXML
    private ImageView newPhoto;
    @FXML
    private TextField newPhone;
    @FXML
    private TextField newCountry;
    @FXML
    private TextField newRegion;
    @FXML
    private TextField newTown;
    @FXML
    private TextField newStreet;

    @FXML
    private Text exceptionMessage;

    @FXML
    private CheckBox editToggle;
    @FXML
    private Button addPhotoButton;
    @FXML
    private Button updateButton;

    @FXML
    private Label username;

    @FXML
    public void initialize(){
        editToggle.selectedProperty().addListener((observable, oldValue, newValue) -> toggleEditProfile(editToggle.isSelected()));
    }

    @FXML
    public void toggleEditProfile(Boolean enable){
        newPhone.setDisable(!enable);
        newCountry.setDisable(!enable);
        newTown.setDisable(!enable);
        newRegion.setDisable(!enable);
        newStreet.setDisable(!enable);
        addPhotoButton.setDisable(!enable);
        updateButton.setDisable(!enable);
    }

    @FXML
    public void updateUserAccount(ActionEvent event) {
        Pattern phoneNoPattern = Pattern.compile("\\d{10}");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Confirm your identity");
        dialog.setHeaderText("Please enter your current password");
        try {
            if (newPhone.getText().isEmpty()) {
                throw new IOException("Phone number is required");
            }
            Matcher phoneNoMatcher = phoneNoPattern.matcher(newPhone.getText());

            if(!phoneNoMatcher.find()){
                throw new InvalidPhoneNoException();
            }

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (Objects.equals(UserService.encodePassword(user.getUsername(),result.get()),user.getPassword())) {
                    user.setPhoneNo(newPhone.getText());
                    user.setAddress(new Address(newCountry.getText(), newRegion.getText(), newTown.getText(), newStreet.getText()));
                    user.setImagePath(newPhotoPath);

                    UserService.updateUser(user);
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Profile modified");
                    a.showAndWait();

                    redirectToHomePage(event);
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR, "Invalid password!");
                    a.showAndWait();
                }
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING, "Your password is required!");
                a.showAndWait();
            }
        } catch (Exception e) {
            exceptionMessage.setText(e.getMessage());
        }
    }

    @FXML
    void handleAddPhotoAction() throws MalformedURLException {
        File image;
        Stage stage = new Stage();
        stage.setTitle("Add Photo");
        FileChooser filechooser = new FileChooser();
        filechooser.setInitialDirectory(new File("C:\\"));
        filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jpg files","*.jpg"));
        image = filechooser.showOpenDialog(stage);
        newPhotoPath = image.getAbsolutePath();
        filechooser.setInitialDirectory(image.getParentFile());
        File file = new File(newPhotoPath);
        String localUrl = file.toURI().toURL().toExternalForm();
        Image newProfile = new Image(localUrl, false);
        newPhoto.setImage(newProfile);
    }

    @FXML
    public void handleSignOutAction(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();

            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
            currentStage.setTitle("Login");
            currentStage.setScene(new Scene(root, 800, 600));
            currentStage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void setUser(User user) throws MalformedURLException {
        this.user = user;
        AccountStatus.setText("Logged-in as " + user.getUsername());

        File file = new File(user.getImagePath());
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        profilePicture.setImage(profile);
        username.setText("Profile - " + user.getUsername());

        newPhoto.setImage(profile);
        newCountry.setText(user.getAddress().getCountry());
        newRegion.setText(user.getAddress().getRegion());
        newTown.setText(user.getAddress().getTown());
        newStreet.setText(user.getAddress().getStreet());
        newPhotoPath = user.getImagePath();
        newPhone.setText(user.getPhoneNo());
    }

    @FXML
    public void redirectToProfile() throws IOException {
        Stage currentStage = (Stage) menu.getScene().getWindow();
        String page = "viewProfile.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
        Parent root = loader.load();
        currentStage.setTitle("My requests");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();
        UserController uc = loader.getController();
        uc.setUser(user);
    }

    @FXML
    public void redirectToHomePage(ActionEvent event){
        redirectToHome(event, user);
    }
}
