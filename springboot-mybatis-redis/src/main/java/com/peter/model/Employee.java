package com.peter.model;

import java.io.Serializable;

public class Employee implements Serializable {

    private Integer id;
    private String lastName;
    private String email;
    private String gender;

    @Override
    public String toString() {
        return "Emplyee{" +
            "id=" + id +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", gender='" + gender + '\'' +
            '}';
    }
}
