package org.ndungutse.model;

public class Patient {
    private int patientId;
    private String patientNumber;
    private String surname;
    private String firstName;
    private String address;
    private String phoneNumber;

    public Patient(int patientId, String patientNumber, String surname, String firstName, String address,
            String phoneNumber) {
        this.patientId = patientId;
        this.patientNumber = patientNumber;
        this.surname = surname;
        this.firstName = firstName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public String getSurname() {
        return surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Patient [patientId=" + patientId + ", patientNumber=" + patientNumber + ", surname=" + surname
                + ", firstName=" + firstName + ", address=" + address + ", phoneNumber=" + phoneNumber + "]";
    }

}
