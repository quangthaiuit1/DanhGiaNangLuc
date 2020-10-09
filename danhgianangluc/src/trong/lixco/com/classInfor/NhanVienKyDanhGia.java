package trong.lixco.com.classInfor;

import java.util.Objects;

public class NhanVienKyDanhGia {
	private String manhanvien;
	private String tennhanvien;
	private String phongban;
	private String maphongban;
	private String tenphongban;
	private String machucdanh;
	private String tenchucdanh;
	// thai
	private boolean isSelected = false;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	// end thai

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

	public String getMaphongban() {
		return maphongban;
	}

	public void setMaphongban(String maphongban) {
		this.maphongban = maphongban;
	}

	public String getTenphongban() {
		return tenphongban;
	}

	public void setTenphongban(String tenphongban) {
		this.tenphongban = tenphongban;
	}

	public String getMachucdanh() {
		return machucdanh;
	}

	public void setMachucdanh(String machucdanh) {
		this.machucdanh = machucdanh;
	}

	public String getTenchucdanh() {
		return tenchucdanh;
	}

	public void setTenchucdanh(String tenchucdanh) {
		this.tenchucdanh = tenchucdanh;
	}

	public String getPhongban() {
		return phongban;
	}

	public void setPhongban(String phongban) {
		this.phongban = phongban;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			if (obj == this) {
				return true;
			}
			if (obj == null || obj.getClass() != this.getClass()) {
				return false;
			}
			NhanVienKyDanhGia guest = (NhanVienKyDanhGia) obj;
			return manhanvien.equals(guest.getManhanvien());
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public int hashCode() {
		return Objects.hash(manhanvien);
	}

}
