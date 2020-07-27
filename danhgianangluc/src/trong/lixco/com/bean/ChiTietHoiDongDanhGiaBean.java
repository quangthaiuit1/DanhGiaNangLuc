package trong.lixco.com.bean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.classInfor.ChiTietDanhGiaNhanVien;
import trong.lixco.com.classInfor.NhanVienKyDanhGia;
import trong.lixco.com.classInfor.NhomNangLucDanhGia;
import trong.lixco.com.ejb.service.ChiTietKyDanhGiaService;
import trong.lixco.com.ejb.service.ChiTietNangLucService;
import trong.lixco.com.ejb.service.KetQuaDanhGiaService;
import trong.lixco.com.ejb.service.KhungNangLucService;
import trong.lixco.com.ejb.service.KyDanhGiaService;
import trong.lixco.com.ejb.service.NangLucService;
import trong.lixco.com.jpa.entity.ChiTietKyDanhGia;
import trong.lixco.com.jpa.entity.KetQuaDanhGia;
import trong.lixco.com.jpa.entity.KhungNangLuc;
import trong.lixco.com.jpa.entity.KyDanhGia;
import trong.lixco.com.servicepublic.EmpPJobServicePublic;
import trong.lixco.com.servicepublic.EmpPJobServicePublicProxy;
import trong.lixco.com.servicepublic.EmployeeDTO;
import trong.lixco.com.servicepublic.EmployeeServicePublic;
import trong.lixco.com.servicepublic.EmployeeServicePublicProxy;
import trong.lixco.com.util.Notify;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Named
@ViewScoped
public class ChiTietHoiDongDanhGiaBean extends AbstractBean<KetQuaDanhGia> {
	private static final long serialVersionUID = 1L;
	private Notify notify;

	private EmployeeDTO nhanvien;

	EmpPJobServicePublic empPJobServicePublic;
	EmployeeServicePublic employeeServicePublic;
	private KyDanhGia kyDanhGia;
	String tenchucdanh;

	@Inject
	private KetQuaDanhGiaService ketQuaDanhGiaService;
	@Inject
	private KyDanhGiaService kyDanhGiaService;
	@Inject
	private ChiTietKyDanhGiaService chiTietKyDanhGiaService;
	@Inject
	private NangLucService nangLucService;
	@Inject
	private ChiTietNangLucService chiTietNangLucService;
	@Inject
	private Logger logger;
	@Inject
	KhungNangLucService khungNangLucService;
	Gson gson;
	int tongdiemnv = 0;
	int tongdiemql = 0;
	private int tongdiem = 0;

	@Override
	protected Logger getLogger() {
		return logger;
	}

	private List<ChiTietDanhGiaNhanVien> chiTietDanhGiaNhanViens;

	public void setdanhgia(String ma) {
		for (int i = 0; i < chiTietDanhGiaNhanViens.size(); i++) {
			if (chiTietDanhGiaNhanViens.get(i).getNangLuc().getMa().equals(ma)) {
				if (chiTietDanhGiaNhanViens.get(i).getChiTietNangLuchd() != null)
					chiTietDanhGiaNhanViens.get(i).setDanhgiahd(
							chiTietDanhGiaNhanViens.get(i).getChiTietNangLuchd().getCapdo());
			}
		}
	}

	String kydanhgia_id;
	String manhanvien;
	String codePjob = "";

	@Override
	public void initItem() {
		try {
			tongdiem = 0;
			employeeServicePublic = new EmployeeServicePublicProxy();
			chiTietDanhGiaNhanViens = new ArrayList<ChiTietDanhGiaNhanVien>();
			empPJobServicePublic = new EmpPJobServicePublicProxy();
			Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap();
			kydanhgia_id = params.get("kydanhgia_id");
			manhanvien = params.get("nv");
			nhanvien = employeeServicePublic.findByCode(manhanvien);
			kyDanhGia = kyDanhGiaService.findById(Long.parseLong(kydanhgia_id));
			gson = new Gson();
			Type listType = new TypeToken<List<NhanVienKyDanhGia>>() {
			}.getType();
			List<NhanVienKyDanhGia> nvs = gson.fromJson(this.kyDanhGia.getNhanviendanhgia(), listType);

			for (int i = 0; i < nvs.size(); i++) {
				if (nvs.get(i).getManhanvien().equals(manhanvien)) {
					tenchucdanh = nvs.get(i).getTenchucdanh();
					codePjob = nvs.get(i).getMachucdanh();
					break;
				}
			}
			taichitiet();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void taichitiet() {
		chiTietDanhGiaNhanViens.clear();
		List<ChiTietKyDanhGia> chitiets = chiTietKyDanhGiaService.findRange(Long.parseLong(kydanhgia_id), codePjob);
		for (int i = 0; i < chitiets.size(); i++) {
			ChiTietDanhGiaNhanVien ct = new ChiTietDanhGiaNhanVien();
			ct.setManv(nhanvien.getCode());
			ct.setTennv(nhanvien.getName());
			ct.setNangLuc(chitiets.get(i).getNangLuc());
			ct.setMachucdanh(chitiets.get(i).getMavitri());
			NhomNangLucDanhGia nhom = new NhomNangLucDanhGia(chitiets.get(i).getNangLuc().getNhomNangLuc().getMa(),
					chitiets.get(i).getNangLuc().getNhomNangLuc().getTen());
			ct.setNhomNangLucDanhGia(nhom);
			ct.setChiTietNangLucs(chiTietNangLucService.findSortNangLuc(chitiets.get(i).getNangLuc().getId()));
			ct.setTrongso(chitiets.get(i).getTrongsonhom());
			KhungNangLuc knl = khungNangLucService.taikhungnangluc(codePjob, chitiets.get(i).getNangLuc().getId());
			if (knl != null) {
				ct.setMucdo(knl.getMucdoquantrong());
				ct.setTieuchuan(knl.getTieuchuan());
			}
			KetQuaDanhGia kq = ketQuaDanhGiaService.ketquadanhgianhanvien(kyDanhGia.getId(), manhanvien, chitiets
					.get(i).getNangLuc().getId());
			if (kq != null) {
				ct.setId(kq.getId());
				ct.setChiTietNangLucql(kq.getChiTietNangLucql());
				ct.setChiTietNangLuchd(kq.getChiTietNangLuchd());
				ct.setTudanhgia(kq.getDiem());
				ct.setDanhgiaql(kq.getDiemql());
				ct.setDanhgiahd(kq.getDiemhd());
			}
			chiTietDanhGiaNhanViens.add(ct);
		}
		caidatdiemtong();
	}

	public void caidatdiemtong() {
		tongdiemnv = 0;
		tongdiemql = 0;
		tongdiem = 0;
		for (int i = 0; i < chiTietDanhGiaNhanViens.size(); i++) {
			int diemdat = chiTietDanhGiaNhanViens.get(i).getMucdo() * chiTietDanhGiaNhanViens.get(i).getDanhgiahd();
			chiTietDanhGiaNhanViens.get(i).setDiemdat(diemdat);
			double ketqua = (double) diemdat
					/ (double) (chiTietDanhGiaNhanViens.get(i).getMucdo() * chiTietDanhGiaNhanViens.get(i)
							.getTieuchuan());
//			if (ketqua > 1)
//				ketqua = 1;
			chiTietDanhGiaNhanViens.get(i).setKetqua((int) (ketqua * 100));
		}
		Map<String, List<ChiTietDanhGiaNhanVien>> datagroups1 = chiTietDanhGiaNhanViens.stream().collect(
				Collectors.groupingBy(p -> p.getNhomNangLucDanhGia().getManhom(), Collectors.toList()));
		for (String manhom : datagroups1.keySet()) {
			List<ChiTietDanhGiaNhanVien> invs = datagroups1.get(manhom);
			int diemchuan = 0;
			int dgql = 0;
			int diemdatnv = 0;
			int diemdatql = 0;
			int diemdat = 0;
			int dghd = 0;
			for (int i = 0; i < invs.size(); i++) {
				diemchuan += invs.get(i).getTieuchuan() * invs.get(i).getMucdo();
				dgql += invs.get(i).getDanhgiaql();
				diemdatnv += invs.get(i).getTudanhgia() * invs.get(i).getMucdo();
				diemdatql += invs.get(i).getDanhgiaql() * invs.get(i).getMucdo();
				diemdat += invs.get(i).getDiemdat();
				dghd += invs.get(i).getDanhgiahd();
			}
			for (int i = 0; i < chiTietDanhGiaNhanViens.size(); i++) {
				if (chiTietDanhGiaNhanViens.get(i).getNhomNangLucDanhGia().getManhom().equals(manhom)) {
					chiTietDanhGiaNhanViens.get(i).getNhomNangLucDanhGia().setDiemchuan(diemchuan);
					chiTietDanhGiaNhanViens.get(i).getNhomNangLucDanhGia().setQuanlydanhgia(dgql);
					chiTietDanhGiaNhanViens.get(i).getNhomNangLucDanhGia().setDiemdat(diemdat);
					chiTietDanhGiaNhanViens.get(i).getNhomNangLucDanhGia().setHoidongdanhgia(dghd);
					// ket qua nhan vien
					double ketquanv = (double) diemdatnv / (double) diemchuan;
					// if (ketquanv > 1)
					// ketquanv = 1;
					chiTietDanhGiaNhanViens.get(i).getNhomNangLucDanhGia()
							.setKetquanv((int) (ketquanv * chiTietDanhGiaNhanViens.get(i).getTrongso()));
					// ket qua nhan vien ql
					double ketquaql = (double) diemdatql / (double) diemchuan;
					// if (ketquaql > 1)
					// ketquaql = 1;
					chiTietDanhGiaNhanViens.get(i).getNhomNangLucDanhGia()
							.setKetquaql((int) (ketquaql * chiTietDanhGiaNhanViens.get(i).getTrongso()));
					// ket qua
					double ketqua = (double) diemdat / (double) diemchuan;
					// if (ketqua > 1)
					// ketqua = 1;

					chiTietDanhGiaNhanViens.get(i).getNhomNangLucDanhGia()
							.setKetqua((int) (ketqua * chiTietDanhGiaNhanViens.get(i).getTrongso()));
				}
			}
			for (int i = 0; i < chiTietDanhGiaNhanViens.size(); i++) {
				if (chiTietDanhGiaNhanViens.get(i).getNhomNangLucDanhGia().getManhom().equals(manhom)) {
					tongdiemnv += chiTietDanhGiaNhanViens.get(i).getNhomNangLucDanhGia().getKetquanv();
					tongdiemql += chiTietDanhGiaNhanViens.get(i).getNhomNangLucDanhGia().getKetquaql();
					tongdiem += chiTietDanhGiaNhanViens.get(i).getNhomNangLucDanhGia().getKetqua();
					break;
				}
			}
		}
	}

	public void caidat(ChiTietDanhGiaNhanVien item) {
	}

	public void luuketqua() {
		try {
			caidatdiemtong();
			boolean checkdanhgia = true;
			List<KetQuaDanhGia> kqs = new ArrayList<KetQuaDanhGia>();
			for (int i = 0; i < chiTietDanhGiaNhanViens.size(); i++) {
				if (chiTietDanhGiaNhanViens.get(i).getChiTietNangLuchd() == null) {
					checkdanhgia = false;
					break;
				} else {
					KetQuaDanhGia kq = null;
					if (chiTietDanhGiaNhanViens.get(i).getId() != 0) {
						kq = ketQuaDanhGiaService.findById(chiTietDanhGiaNhanViens.get(i).getId());
						if (kq != null) {
							kq.setDiemhd(chiTietDanhGiaNhanViens.get(i).getDanhgiahd());
							kq.setDiemdat(chiTietDanhGiaNhanViens.get(i).getDiemdat());
							kq.setKetqua(chiTietDanhGiaNhanViens.get(i).getKetqua());
							kq.setChiTietNangLuchd(chiTietDanhGiaNhanViens.get(i).getChiTietNangLuchd());
							kq.setMachucdanh(chiTietDanhGiaNhanViens.get(i).getMachucdanh());
							kq.setTongdiem(tongdiem);
							kqs.add(kq);
						}
					} else {
						kq = new KetQuaDanhGia();
						kq.setKyDanhGia(kyDanhGia);
						kq.setNangLuc(chiTietDanhGiaNhanViens.get(i).getNangLuc());
						kq.setManl(chiTietDanhGiaNhanViens.get(i).getNangLuc().getMa());
						kq.setTennl(chiTietDanhGiaNhanViens.get(i).getNangLuc().getTen());
						kq.setManhanvien(chiTietDanhGiaNhanViens.get(i).getManv());
						kq.setDiemdat(chiTietDanhGiaNhanViens.get(i).getDiemdat());
						kq.setKetqua(chiTietDanhGiaNhanViens.get(i).getKetqua());
						kq.setDiemhd(chiTietDanhGiaNhanViens.get(i).getDanhgiahd());
						kq.setChiTietNangLuchd(chiTietDanhGiaNhanViens.get(i).getChiTietNangLuchd());
						kq.setMachucdanh(chiTietDanhGiaNhanViens.get(i).getMachucdanh());
						kq.setTongdiem(tongdiem);
						kqs.add(kq);
					}
				}
			}
			if (checkdanhgia) {
				boolean status = ketQuaDanhGiaService.update(kqs);
				if (status) {
					taichitiet();
					noticeDialog("Đã lưu thông tin đánh giá.");
				} else {
					errorDialog("Xảy ra lỗi khi lưu");
				}
			} else {
				warnDialog("Một số câu hỏi bạn chưa đánh giá.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void luutam() {
		try {
			caidatdiemtong();
			List<KetQuaDanhGia> kqs = new ArrayList<KetQuaDanhGia>();
			for (int i = 0; i < chiTietDanhGiaNhanViens.size(); i++) {
				if (chiTietDanhGiaNhanViens.get(i).getChiTietNangLuchd() == null) {
				} else {
					KetQuaDanhGia kq = null;
					if (chiTietDanhGiaNhanViens.get(i).getId() != 0) {
						kq = ketQuaDanhGiaService.findById(chiTietDanhGiaNhanViens.get(i).getId());
						if (kq != null) {
							kq.setDiemhd(chiTietDanhGiaNhanViens.get(i).getDanhgiahd());
							kq.setDiemdat(chiTietDanhGiaNhanViens.get(i).getDiemdat());
							kq.setKetqua(chiTietDanhGiaNhanViens.get(i).getKetqua());
							kq.setChiTietNangLuchd(chiTietDanhGiaNhanViens.get(i).getChiTietNangLuchd());
							kq.setMachucdanh(chiTietDanhGiaNhanViens.get(i).getMachucdanh());
							kq.setTongdiem(tongdiem);
							kqs.add(kq);
						}
					} else {
						kq = new KetQuaDanhGia();
						kq.setKyDanhGia(kyDanhGia);
						kq.setNangLuc(chiTietDanhGiaNhanViens.get(i).getNangLuc());
						kq.setManl(chiTietDanhGiaNhanViens.get(i).getNangLuc().getMa());
						kq.setTennl(chiTietDanhGiaNhanViens.get(i).getNangLuc().getTen());
						kq.setManhanvien(chiTietDanhGiaNhanViens.get(i).getManv());
						kq.setDiemdat(chiTietDanhGiaNhanViens.get(i).getDiemdat());
						kq.setKetqua(chiTietDanhGiaNhanViens.get(i).getKetqua());
						kq.setDiemhd(chiTietDanhGiaNhanViens.get(i).getDanhgiahd());
						kq.setChiTietNangLuchd(chiTietDanhGiaNhanViens.get(i).getChiTietNangLuchd());
						kq.setMachucdanh(chiTietDanhGiaNhanViens.get(i).getMachucdanh());
						kq.setTongdiem(tongdiem);
						kqs.add(kq);
					}
				}
			}

			boolean status = ketQuaDanhGiaService.update(kqs);
			if (status) {
				taichitiet();
				noticeDialog("Đã lưu tạm thông tin đánh giá.");
			} else {
				errorDialog("Xảy ra lỗi khi lưu");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public KyDanhGia getKyDanhGia() {
		return kyDanhGia;
	}

	public void setKyDanhGia(KyDanhGia kyDanhGia) {
		this.kyDanhGia = kyDanhGia;
	}

	public List<ChiTietDanhGiaNhanVien> getChiTietDanhGiaNhanViens() {
		return chiTietDanhGiaNhanViens;
	}

	public void setChiTietDanhGiaNhanViens(List<ChiTietDanhGiaNhanVien> chiTietDanhGiaNhanViens) {
		this.chiTietDanhGiaNhanViens = chiTietDanhGiaNhanViens;
	}

	public EmployeeDTO getNhanvien() {
		return nhanvien;
	}

	public void setNhanvien(EmployeeDTO nhanvien) {
		this.nhanvien = nhanvien;
	}

	public int getTongdiem() {
		return tongdiem;
	}

	public void setTongdiem(int tongdiem) {
		this.tongdiem = tongdiem;
	}

	public int getTongdiemnv() {
		return tongdiemnv;
	}

	public void setTongdiemnv(int tongdiemnv) {
		this.tongdiemnv = tongdiemnv;
	}

	public int getTongdiemql() {
		return tongdiemql;
	}

	public void setTongdiemql(int tongdiemql) {
		this.tongdiemql = tongdiemql;
	}

	public String getTenchucdanh() {
		return tenchucdanh;
	}

	public void setTenchucdanh(String tenchucdanh) {
		this.tenchucdanh = tenchucdanh;
	}

}
