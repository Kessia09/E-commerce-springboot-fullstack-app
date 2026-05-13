package org.cat.irere.dto;

public class CustomerDTO {
    private Long id;
    private String firstname;
    private String phone;
    private String email;

    public CustomerDTO() {
    }

    public CustomerDTO(Long id, String firstname, String phone, String email) {
        this.id = id;
        this.firstname = firstname;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}