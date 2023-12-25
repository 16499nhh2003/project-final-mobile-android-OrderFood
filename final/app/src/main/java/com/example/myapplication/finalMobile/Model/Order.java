package com.example.myapplication.finalMobile.Model;

import com.example.myapplication.finalMobile.Enum.OrderStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Order {
    private String idOrder;
    private String idCustomer;
    private List<CartItem> cartItemList;
    private double total;
    private Date createdAt;
    private String nameCustomer;
    private String phoneCustomer;
    private String addressCustomer;
    private OrderStatus orderStatus;


    public Order() {
        this.orderStatus = OrderStatus.PROCESSING;
    }


    public Order(String idCustomer, List<CartItem> cartItemList, double total, Date createdAt, String nameCustomer, String phoneCustomer, String addressCustomer) {
        this.idCustomer = idCustomer;
        this.cartItemList = cartItemList;
        this.total = total;
        this.createdAt = createdAt;
        this.nameCustomer = nameCustomer;
        this.phoneCustomer = phoneCustomer;
        this.addressCustomer = addressCustomer;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getPhoneCustomer() {
        return phoneCustomer;
    }

    public void setPhoneCustomer(String phoneCustomer) {
        this.phoneCustomer = phoneCustomer;
    }

    public String getAddressCustomer() {
        return addressCustomer;
    }

    public void setAddressCustomer(String addressCustomer) {
        this.addressCustomer = addressCustomer;
    }

    @Override
    public String toString() {
        return "Order{" +
                "idOrder='" + idOrder + '\'' +
                ", idCustomer='" + idCustomer + '\'' +
                ", cartItemList=" + cartItemList +
                ", total=" + total +
                ", createdAt=" + createdAt +
                ", nameCustomer='" + nameCustomer + '\'' +
                ", phoneCustomer='" + phoneCustomer + '\'' +
                ", addressCustomer='" + addressCustomer + '\'' +
                ", orderStatus=" + orderStatus +
                '}';
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getFormattedCreatedAt() {
        if (createdAt != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", new Locale("vi", "VN"));
            return dateFormat.format(createdAt);
        }
        return "";
    }

}
