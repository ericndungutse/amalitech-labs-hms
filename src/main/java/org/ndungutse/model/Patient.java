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

    @Override
    public String toString() {
        return "Patient [patientId=" + patientId + ", patientNumber=" + patientNumber + ", surname=" + surname
                + ", firstName=" + firstName + ", address=" + address + ", phoneNumber=" + phoneNumber + "]";
    }

}
