package org.cat.irere.dto;

public class RegisterDTO {
    private String firstname;
    private String phone;
    private String email;
    private String password;

    public RegisterDTO() {
    }

    public RegisterDTO(String firstname, String phone, String email, String password) {
        this.firstname = firstname;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}