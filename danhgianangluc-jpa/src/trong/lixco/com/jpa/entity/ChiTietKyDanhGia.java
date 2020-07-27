package trong.lixco.com.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chitietkydanhgia")
public class ChiTietKyDanhGia extends AbstractEntity {
	private String mavitri;// vi tri/chuc danh
	private int trongsonhom;
	private String codeDep;
	
	@ManyToOne
	private NangLuc nangLuc;
	@ManyToOne
	private KyDanhGia kyDanhGia;
	public String getMavitri() {
		return mavitri;
	}
	public void setMavitri(String mavitri) {
		this.mavitri = mavitri;
	}
	public int getTrongsonhom() {
		return trongsonhom;
	}
	public void setTrongsonhom(int trongsonhom) {
		this.trongsonhom = trongsonhom;
	}
	public NangLuc getNangLuc() {
		return nangLuc;
	}
	public void setNangLuc(NangLuc nangLuc) {
		this.nangLuc = nangLuc;
	}
	public KyDanhGia getKyDanhGia() {
		return kyDanhGia;
	}
	public void setKyDanhGia(KyDanhGia kyDanhGia) {
		this.kyDanhGia = kyDanhGia;
	}
	public String getCodeDep() {
		return codeDep;
	}
	public void setCodeDep(String codeDep) {
		this.codeDep = codeDep;
	}
	
	
}
