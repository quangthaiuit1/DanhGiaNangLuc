package trong.lixco.com.jpa.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name="nangluc")
public class NangLuc extends AbstractEntity{
	private String ma;
	private String ten;
	private String dinhnghia;
	@ManyToOne
	private NhomNangLuc nhomNangLuc;
	private String codeDep;//phong ban
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "nangLuc")
	private List<ChiTietNangLuc> chiTietNangLucs;
	
	
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
	public String getDinhnghia() {
		return dinhnghia;
	}
	public void setDinhnghia(String dinhnghia) {
		this.dinhnghia = dinhnghia;
	}
	public NhomNangLuc getNhomNangLuc() {
		return nhomNangLuc;
	}
	public void setNhomNangLuc(NhomNangLuc nhomNangLuc) {
		this.nhomNangLuc = nhomNangLuc;
	}
	public String getCodeDep() {
		return codeDep;
	}
	public void setCodeDep(String codeDep) {
		this.codeDep = codeDep;
	}
	public List<ChiTietNangLuc> getChiTietNangLucs() {
		return chiTietNangLucs;
	}
	public void setChiTietNangLucs(List<ChiTietNangLuc> chiTietNangLucs) {
		this.chiTietNangLucs = chiTietNangLucs;
	}
	
}
