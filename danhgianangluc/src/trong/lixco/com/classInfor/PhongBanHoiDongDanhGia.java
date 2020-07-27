package trong.lixco.com.classInfor;

import java.util.Objects;

public class PhongBanHoiDongDanhGia {

	private String maphongban;
	private String tenphongban;

	
	public PhongBanHoiDongDanhGia(String maphongban, String tenphongban) {
		this.maphongban = maphongban;
		this.tenphongban = tenphongban;
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

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		PhongBanHoiDongDanhGia guest = (PhongBanHoiDongDanhGia) obj;
		return maphongban.equals(guest.getMaphongban());
	}

	@Override
	public int hashCode() {
		return Objects.hash(maphongban);
	}

}
