package trong.lixco.com.classInfor;

import java.util.List;

import trong.lixco.com.jpa.entity.ChiTietNangLuc;
import trong.lixco.com.jpa.entity.NangLuc;

public class ChiTietDanhGiaNhanVien {
	private long id;
	private NangLuc nangLuc;
	private int trongso;
	private int mucdo;
	private int tieuchuan;
	private int tudanhgia;
	private int danhgiaql;
	private int danhgiahd;
	private int diemdat;
	private int ketqua;
	private String manv;
	private String tennv;
	private int tongdiem;
	private String machucdanh;

	private NhomNangLucDanhGia nhomNangLucDanhGia;

	private ChiTietNangLuc chiTietNangLucql;
	private String noidungql;
	private ChiTietNangLuc chiTietNangLuchd;
	private List<ChiTietNangLuc> chiTietNangLucs;

	public NangLuc getNangLuc() {
		return nangLuc;
	}

	public void setNangLuc(NangLuc nangLuc) {
		this.nangLuc = nangLuc;
	}

	public int getTrongso() {
		return trongso;
	}

	public void setTrongso(int trongso) {
		this.trongso = trongso;
	}

	public int getMucdo() {
		return mucdo;
	}

	public void setMucdo(int mucdo) {
		this.mucdo = mucdo;
	}

	public int getTieuchuan() {
		return tieuchuan;
	}

	public void setTieuchuan(int tieuchuan) {
		this.tieuchuan = tieuchuan;
	}

	public int getTudanhgia() {
		return tudanhgia;
	}

	public void setTudanhgia(int tudanhgia) {
		this.tudanhgia = tudanhgia;
	}

	public int getDanhgiaql() {
		return danhgiaql;
	}

	public void setDanhgiaql(int danhgiaql) {
		this.danhgiaql = danhgiaql;
	}

	public int getDanhgiahd() {
		return danhgiahd;
	}

	public void setDanhgiahd(int danhgiahd) {
		this.danhgiahd = danhgiahd;
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

	public ChiTietNangLuc getChiTietNangLucql() {
		return chiTietNangLucql;
	}

	public void setChiTietNangLucql(ChiTietNangLuc chiTietNangLucql) {
		this.chiTietNangLucql = chiTietNangLucql;
	}

	public List<ChiTietNangLuc> getChiTietNangLucs() {
		return chiTietNangLucs;
	}

	public void setChiTietNangLucs(List<ChiTietNangLuc> chiTietNangLucs) {
		this.chiTietNangLucs = chiTietNangLucs;
	}

	public NhomNangLucDanhGia getNhomNangLucDanhGia() {
		return nhomNangLucDanhGia;
	}

	public void setNhomNangLucDanhGia(NhomNangLucDanhGia nhomNangLucDanhGia) {
		this.nhomNangLucDanhGia = nhomNangLucDanhGia;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ChiTietNangLuc getChiTietNangLuchd() {
		return chiTietNangLuchd;
	}

	public void setChiTietNangLuchd(ChiTietNangLuc chiTietNangLuchd) {
		this.chiTietNangLuchd = chiTietNangLuchd;
	}

	public String getNoidungql() {
		return noidungql;
	}

	public void setNoidungql(String noidungql) {
		this.noidungql = noidungql;
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
