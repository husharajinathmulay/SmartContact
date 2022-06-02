package com.smart.entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="orders")
public class Myorder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int myOrderId;
	private String orderId;
	private String amount;
	private String reciept;
	private String status;
	@ManyToOne
	private User user;
	private String paymentId;
	public int getMyOrderId() {
		return myOrderId;
	}
	public void setMyOrderId(int myOrderId) {
		this.myOrderId = myOrderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getReciept() {
		return reciept;
	}
	public void setReciept(String reciept) {
		this.reciept = reciept;
	}
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	@Override
	public String toString() {
		return "Myorder [myOrderId=" + myOrderId + ", orderId=" + orderId + ", amount=" + amount + ", reciept="
				+ reciept + ", status=" + status + ", user=" + user + ", paymentId=" + paymentId + "]";
	}
		
}
