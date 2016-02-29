package com.ketnoivantai.model.pojo;

/**
 * Created by AnhVu on 2/25/16.
 */
public class CompanyPOJO {

    private final int id;
    private final String email;
    private final String business_id;
    private final String fax;
    private final String website;
    private final String name;
    private final String phone;
    private final String address;

    public CompanyPOJO(int id, String email, String business_id, String fax, String website, String name,
                       String phone, String address) {
        this.id = id;
        this.email = email;
        this.business_id = business_id;
        this.fax = fax;
        this.website = website;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public String getFax() {
        return fax;
    }

    public String getWebsite() {
        return website;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
