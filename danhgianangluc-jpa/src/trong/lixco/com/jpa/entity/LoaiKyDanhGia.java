package trong.lixco.com.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
/*
 * - Ky danh gia toan the lix
 * - Kỳ danh gia nhan vien moi
 * - Ky danh gia bo nhiem
 * - Kỳ danh gia bat thuong
 */
@Entity
@Table(name="loaikydanhgia")
public class LoaiKyDanhGia extends AbstractEntity{
	private String ma;
	private String ten;
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
	
	
	
}
