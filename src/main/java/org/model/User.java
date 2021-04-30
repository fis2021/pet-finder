package org.model;

import org.dizitart.no2.objects.Id;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    @Id
    private String username;
    private String password;
    private String role;
    private ArrayList<Pet> petList = new ArrayList<>();
    private String imagePath;


    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.imagePath = "";
    }

    public User(){

    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImagePath(){ return this.imagePath; }

    public void setImagePath(String imagePath){ this.imagePath = imagePath; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!Objects.equals(username, user.username)) return false;
        if (!Objects.equals(password, user.password)) return false;
        return Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    public void addPet(Pet pet){
        petList.add(pet);
    }

    public void removePet(Pet pet){
        petList.remove(pet);
    }

    public ArrayList<Pet> getPetList(){ return this.petList; }

}
