package com.ketnoivantai.customers.model.pojo;

/**
 * Created by AnhVu on 3/12/16.
 */
public class ChangePasswordPOJO {

    private String old_password;
    private String new_password;

    public ChangePasswordPOJO(String old_password, String new_password) {
        this.old_password = old_password;
        this.new_password = new_password;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
