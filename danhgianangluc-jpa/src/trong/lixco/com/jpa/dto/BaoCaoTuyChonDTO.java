package trong.lixco.com.jpa.dto;

import trong.lixco.com.jpa.entity.KyDanhGia;

public class BaoCaoTuyChonDTO {
	private KyDanhGia kyDanhGia;
	private String maphongban;
	private String phongban;
	private String manv;
	private String tennv;
	private int ketqua;
	private String machucdanh;
	private String chucdanh;

	public BaoCaoTuyChonDTO(KyDanhGia kyDanhGia, String manv,int ketqua, String machucdanh) {
		this.kyDanhGia = kyDanhGia;
		this.manv = manv;
		this.ketqua = ketqua;
		this.machucdanh=machucdanh;
	}

	public KyDanhGia getKyDanhGia() {
		return kyDanhGia;
	}

	public void setKyDanhGia(KyDanhGia kyDanhGia) {
		this.kyDanhGia = kyDanhGia;
	}



	public String getChucdanh() {
		return chucdanh;
	}

	public void setChucdanh(String chucdanh) {
		this.chucdanh = chucdanh;
	}

	public String getMachucdanh() {
		return machucdanh;
	}

	public void setMachucdanh(String machucdanh) {
		this.machucdanh = machucdanh;
	}

	public String getPhongban() {
		return phongban;
	}

	public void setPhongban(String phongban) {
		this.phongban = phongban;
	}

	public String getManv() {
		return manv;
	}

	public void setManv(String manv) {
		this.manv = manv;
	}

	public String getTennv() {
		return tennv;
	}

	public void setTennv(String tennv) {
		this.tennv = tennv;
	}

	public int getKetqua() {
		return ketqua;
	}

	public void setKetqua(int ketqua) {
		this.ketqua = ketqua;
	}

	public String getMaphongban() {
		return maphongban;
	}

	public void setMaphongban(String maphongban) {
		this.maphongban = maphongban;
	}
	
}
