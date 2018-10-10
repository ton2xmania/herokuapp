package com.herokuapp.heroku.model;

/**
 * Created by antonyhilary on 09 October 2018
 */
public class Contact {
    private String id;
    private String firstName;
    private String lastName;
    private int age;
    private String photo;

    public Contact(String id, String firstName, String lastName, int age, String photo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.photo = photo;
    }

    public Contact(String firstName, String lastName, int age, String photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.photo = photo;
    }

    public Contact(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
