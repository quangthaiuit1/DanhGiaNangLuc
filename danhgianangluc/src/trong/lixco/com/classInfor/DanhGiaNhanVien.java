package trong.lixco.com.classInfor;

import trong.lixco.com.jpa.entity.KyDanhGia;

public class DanhGiaNhanVien {
	private String manhanvien;
	private String tennhanvien;
	private String phongban;
	private String chucdanh;
	private KyDanhGia kyDanhGia;
	private boolean danhgianv = false;
	private boolean danhgiaql = false;
	private boolean danhgiahd = false;

	public String getManhanvien() {
		return manhanvien;
	}

	public void setManhanvien(String manhanvien) {
		this.manhanvien = manhanvien;
	}

	public String getTennhanvien() {
		return tennhanvien;
	}

	public void setTennhanvien(String tennhanvien) {
		this.tennhanvien = tennhanvien;
	}

	public KyDanhGia getKyDanhGia() {
		return kyDanhGia;
	}

	public void setKyDanhGia(KyDanhGia kyDanhGia) {
		this.kyDanhGia = kyDanhGia;
	}

	public String getPhongban() {
		return phongban;
	}

	public void setPhongban(String phongban) {
		this.phongban = phongban;
	}

	public boolean isDanhgiaql() {
		return danhgiaql;
	}

	public void setDanhgiaql(boolean danhgiaql) {
		this.danhgiaql = danhgiaql;
	}

	public boolean isDanhgiahd() {
		return danhgiahd;
	}

	public void setDanhgiahd(boolean danhgiahd) {
		this.danhgiahd = danhgiahd;
	}

	public boolean isDanhgianv() {
		return danhgianv;
	}

	public void setDanhgianv(boolean danhgianv) {
		this.danhgianv = danhgianv;
	}

	public String getChucdanh() {
		return chucdanh;
	}

	public void setChucdanh(String chucdanh) {
		this.chucdanh = chucdanh;
	}

}
