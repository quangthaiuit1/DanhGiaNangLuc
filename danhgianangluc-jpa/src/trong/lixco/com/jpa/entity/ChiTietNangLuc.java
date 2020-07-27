package trong.lixco.com.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="chitietnangluc")
public class ChiTietNangLuc extends AbstractEntity{
	@ManyToOne
	private NangLuc nangLuc;
	private String noidung;
	private int capdo;
	public NangLuc getNangLuc() {
		return nangLuc;
	}
	public void setNangLuc(NangLuc nangLuc) {
		this.nangLuc = nangLuc;
	}
	public String getNoidung() {
		return noidung;
	}
	public void setNoidung(String noidung) {
		this.noidung = noidung;
	}
	public int getCapdo() {
		return capdo;
	}
	public void setCapdo(int capdo) {
		this.capdo = capdo;
	}
	
}
