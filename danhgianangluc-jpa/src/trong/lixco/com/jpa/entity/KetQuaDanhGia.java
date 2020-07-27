package trong.lixco.com.jpa.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ketquadanhgia")
public class KetQuaDanhGia extends AbstractEntity {
	private String manhanvien;
	@ManyToOne
	private KyDanhGia KyDanhGia;
	@ManyToOne
	private ChiTietNangLuc chiTietNangLuc;
	@ManyToOne
	private ChiTietNangLuc chiTietNangLucql;
	@ManyToOne
	private ChiTietNangLuc chiTietNangLuchd;
	@ManyToOne
	private NangLuc nangLuc;
	private String manl;
	private String tennl;
	private int diem;
	private int diemql;
	private int diemhd;
	private int diemdat;
	private int ketqua;
	private int tongdiem;
	private String machucdanh;
	@Transient
	private List<ChiTietNangLuc> chiTietNangLucs;
	public String getManhanvien() {
		return manhanvien;
	}
	public void setManhanvien(String manhanvien) {
		this.manhanvien = manhanvien;
	}
	public KyDanhGia getKyDanhGia() {
		return KyDanhGia;
	}
	public void setKyDanhGia(KyDanhGia kyDanhGia) {
		KyDanhGia = kyDanhGia;
	}
	public ChiTietNangLuc getChiTietNangLuc() {
		return chiTietNangLuc;
	}
	public void setChiTietNangLuc(ChiTietNangLuc chiTietNangLuc) {
		this.chiTietNangLuc = chiTietNangLuc;
	}
	public List<ChiTietNangLuc> getChiTietNangLucs() {
		return chiTietNangLucs;
	}
	public void setChiTietNangLucs(List<ChiTietNangLuc> chiTietNangLucs) {
		this.chiTietNangLucs = chiTietNangLucs;
	}
	public NangLuc getNangLuc() {
		return nangLuc;
	}
	public void setNangLuc(NangLuc nangLuc) {
		this.nangLuc = nangLuc;
	}
	public String getManl() {
		return manl;
	}
	public void setManl(String manl) {
		this.manl = manl;
	}
	public String getTennl() {
		return tennl;
	}
	public void setTennl(String tennl) {
		this.tennl = tennl;
	}
	public int getDiem() {
		return diem;
	}
	public void setDiem(int diem) {
		this.diem = diem;
	}
	public int getDiemql() {
		return diemql;
	}
	public void setDiemql(int diemql) {
		this.diemql = diemql;
	}
	public int getDiemhd() {
		return diemhd;
	}
	public void setDiemhd(int diemhd) {
		this.diemhd = diemhd;
	}
	public ChiTietNangLuc getChiTietNangLucql() {
		return chiTietNangLucql;
	}
	public void setChiTietNangLucql(ChiTietNangLuc chiTietNangLucql) {
		this.chiTietNangLucql = chiTietNangLucql;
	}
	public ChiTietNangLuc getChiTietNangLuchd() {
		return chiTietNangLuchd;
	}
	public void setChiTietNangLuchd(ChiTietNangLuc chiTietNangLuchd) {
		this.chiTietNangLuchd = chiTietNangLuchd;
	}
	public int getDiemdat() {
		return diemdat;
	}
	public void setDiemdat(int diemdat) {
		this.diemdat = diemdat;
	}
	public int getKetqua() {
		return ketqua;
	}
	public void setKetqua(int ketqua) {
		this.ketqua = ketqua;
	}
	public int getTongdiem() {
		return tongdiem;
	}
	public void setTongdiem(int tongdiem) {
		this.tongdiem = tongdiem;
	}
	public String getMachucdanh() {
		return machucdanh;
	}
	public void setMachucdanh(String machucdanh) {
		this.machucdanh = machucdanh;
	}
	
}
