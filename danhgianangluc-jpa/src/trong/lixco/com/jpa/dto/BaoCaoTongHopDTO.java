package trong.lixco.com.jpa.dto;

import trong.lixco.com.jpa.entity.KyDanhGia;
import trong.lixco.com.jpa.entity.NangLuc;

public class BaoCaoTongHopDTO {
	private KyDanhGia kyDanhGia;
	private NangLuc nangLuc;
	private long soluong;
	
	public BaoCaoTongHopDTO(KyDanhGia kyDanhGia, NangLuc nangLuc, long soluong) {
		this.kyDanhGia = kyDanhGia;
		this.nangLuc = nangLuc;
		this.soluong = soluong;
	}
	public KyDanhGia getKyDanhGia() {
		return kyDanhGia;
	}
	public void setKyDanhGia(KyDanhGia kyDanhGia) {
		this.kyDanhGia = kyDanhGia;
	}
	public NangLuc getNangLuc() {
		return nangLuc;
	}
	public void setNangLuc(NangLuc nangLuc) {
		this.nangLuc = nangLuc;
	}
	public long getSoluong() {
		return soluong;
	}
	public void setSoluong(long soluong) {
		this.soluong = soluong;
	}
	

}
