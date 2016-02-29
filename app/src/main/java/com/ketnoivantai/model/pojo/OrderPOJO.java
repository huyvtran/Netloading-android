package com.ketnoivantai.model.pojo;

/**
 * Created by AnhVu on 2/26/16.
 */
public class OrderPOJO {


    private final AcceptTripPOJO order;
    private final RequestPOJO request;
    private String company_name;
    private int price;

    public OrderPOJO(AcceptTripPOJO acceptTripPOJO, RequestPOJO request) {
        this.order = acceptTripPOJO;
        this.request = request;
    }


    @Override
    public String toString() {
        return "OrderPOJO{" +
                "order=" + order.toString() +
                "\n request=" + request.toString() +
                '}';
    }

    public AcceptTripPOJO getOrder() {
        return order;
    }

    public RequestPOJO getRequest() {
        return request;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
