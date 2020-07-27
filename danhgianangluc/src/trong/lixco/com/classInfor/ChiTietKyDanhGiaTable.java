package trong.lixco.com.classInfor;

import java.util.List;

import trong.lixco.com.jpa.entity.NangLuc;

public class ChiTietKyDanhGiaTable {
	String manhomnangluc;
	String nhomnangluc;
	String manangluc;
	String tennangluc;
	NangLuc nangLuc;
	int trongsonhom=0;
	List<DanhGia> danhgias;
	public String getManhomnangluc() {
		return manhomnangluc;
	}
	public void setManhomnangluc(String manhomnangluc) {
		this.manhomnangluc = manhomnangluc;
	}
	public String getNhomnangluc() {
		return nhomnangluc;
	}
	public void setNhomnangluc(String nhomnangluc) {
		this.nhomnangluc = nhomnangluc;
	}
	public String getManangluc() {
		return manangluc;
	}
	public void setManangluc(String manangluc) {
		this.manangluc = manangluc;
	}
	public String getTennangluc() {
		return tennangluc;
	}
	public void setTennangluc(String tennangluc) {
		this.tennangluc = tennangluc;
	}
	public NangLuc getNangLuc() {
		return nangLuc;
	}
	public void setNangLuc(NangLuc nangLuc) {
		this.nangLuc = nangLuc;
	}
	public int getTrongsonhom() {
		return trongsonhom;
	}
	public void setTrongsonhom(int trongsonhom) {
		this.trongsonhom = trongsonhom;
	}
	public List<DanhGia> getDanhgias() {
		return danhgias;
	}
	public void setDanhgias(List<DanhGia> danhgias) {
		this.danhgias = danhgias;
	}
	
}
