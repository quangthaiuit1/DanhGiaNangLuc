package trong.lixco.com.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "khungnangluc")
public class KhungNangLuc extends AbstractEntity {
	private String mavitri;// vi tri/chuc danh
	private int trongsonhom;
	@ManyToOne
	private NangLuc nangLuc;
	private int mucdoquantrong=1;
	private int tieuchuan=1;
	private int diemchuan;

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

	public int getMucdoquantrong() {
		return mucdoquantrong;
	}

	public void setMucdoquantrong(int mucdoquantrong) {
		this.mucdoquantrong = mucdoquantrong;
	}

	public int getTieuchuan() {
		return tieuchuan;
	}

	public void setTieuchuan(int tieuchuan) {
		this.tieuchuan = tieuchuan;
	}

	public int getDiemchuan() {
		return diemchuan;
	}

	public void setDiemchuan(int diemchuan) {
		this.diemchuan = diemchuan;
	}
	
}
