package com.example.shin.Model;

public class Users
{
    private String name,phone,password,image,adresse,email;
    public Users()
    {

    }

    public Users(String name, String phone, String password, String image, String adresse,String email) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.adresse = adresse;
        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
