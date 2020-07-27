package trong.lixco.com.jpa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="kydanhgia")
public class KyDanhGia extends AbstractEntity{
	private String tenkydanhgia;
	private Date ngaybatdau;
	private Date ngayketthuc;
	private String nhanviendanhgia;
	@Transient
	private boolean dadanhgia;
	
	@ManyToOne
	private LoaiKyDanhGia loaiKyDanhGia;

	public String getTenkydanhgia() {
		return tenkydanhgia;
	}

	public void setTenkydanhgia(String tenkydanhgia) {
		this.tenkydanhgia = tenkydanhgia;
	}

	public Date getNgaybatdau() {
		return ngaybatdau;
	}

	public void setNgaybatdau(Date ngaybatdau) {
		this.ngaybatdau = ngaybatdau;
	}

	public Date getNgayketthuc() {
		return ngayketthuc;
	}

	public void setNgayketthuc(Date ngayketthuc) {
		this.ngayketthuc = ngayketthuc;
	}

	public String getNhanviendanhgia() {
		return nhanviendanhgia;
	}

	public void setNhanviendanhgia(String nhanviendanhgia) {
		this.nhanviendanhgia = nhanviendanhgia;
	}

	public LoaiKyDanhGia getLoaiKyDanhGia() {
		return loaiKyDanhGia;
	}

	public void setLoaiKyDanhGia(LoaiKyDanhGia loaiKyDanhGia) {
		this.loaiKyDanhGia = loaiKyDanhGia;
	}

	public boolean isDadanhgia() {
		return dadanhgia;
	}

	public void setDadanhgia(boolean dadanhgia) {
		this.dadanhgia = dadanhgia;
	}

}
