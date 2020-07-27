package trong.lixco.com.classInfor;

public class DanhGia {
	boolean check;
	String chucvu;

	public DanhGia(boolean check, String chucvu) {
		this.check = check;
		this.chucvu = chucvu;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getChucvu() {
		return chucvu;
	}

	public void setChucvu(String chucvu) {
		this.chucvu = chucvu;
	}
}
