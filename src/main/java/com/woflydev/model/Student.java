package com.woflydev.model;

public class Student {
    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private String schoolHouse;

    private int age;
    private String uuid;

    public Student(String username,
                   String password,
                   String firstName,
                   String lastName,
                   String address,
                   String gender,
                   String schoolHouse,
                   int age,
                   String uuid
    ) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.gender = gender;
        this.schoolHouse = schoolHouse;
        this.age = age;
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "UUID: " + uuid + "\n" +
                "Name: " + firstName + " " + lastName + "\n" +
                "Address: " + address + "\n" +
                "Gender: " + gender + "\n" +
                "School House: " + schoolHouse + "\n" +
                "Age: " + age + "\n";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHouse() {
        return schoolHouse;
    }

    public void setSchoolHouse(String schoolHouse) {
        this.schoolHouse = schoolHouse;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }
}
