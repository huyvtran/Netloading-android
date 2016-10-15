package com.ketnoivantai.companies.models.webservice.json;

/**
 * Created by Dandoh on 3/23/16.
 */
public class GenericResultJSON {
    protected String status;
    protected String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "GenericResultJSON{" +
                "status='" + status + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
