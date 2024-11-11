package ru.mirea.pkmn;

import java.io.Serializable;

public class Student implements Serializable {
    public static final long serialVersionUID = 1L;
    String surName;
    String firstName;
    String familyName;
    String group;

    public Student(String surName, String firstName, String familyName, String group) {
        this.surName = surName;
        this.firstName = firstName;
        this.familyName = familyName;
        this.group = group;
    }

    public Student() {
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Student" +
                " surName=" + surName +
                ", firstName=" + firstName +
                ", familyName=" + familyName +
                ", group=" + group;
    }
}
