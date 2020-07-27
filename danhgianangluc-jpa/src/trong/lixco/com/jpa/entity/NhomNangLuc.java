package trong.lixco.com.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


/*
 * 1. Host quan ly account (Account database)
 * 2. Host web
 */
@Entity
@Table(name="nhomnangluc")
public class NhomNangLuc extends AbstractEntity{
	private String ma;
	private String ten;
	private boolean nangluccongty=false;
	public String getMa() {
		return ma;
	}
	public void setMa(String ma) {
		this.ma = ma;
	}
	public String getTen() {
		return ten;
	}
	public void setTen(String ten) {
		this.ten = ten;
	}
	public boolean isNangluccongty() {
		return nangluccongty;
	}
	public void setNangluccongty(boolean nangluccongty) {
		this.nangluccongty = nangluccongty;
	}
	
}
