package trong.lixco.com.bean;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.docx4j.org.xhtmlrenderer.pdf.ITextRenderer;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.DepartmentServicePublicProxy;
import trong.lixco.com.classInfor.DanhGiaNhanVien;
import trong.lixco.com.classInfor.NhanVienKyDanhGia;
import trong.lixco.com.classInfor.PhongBanHoiDongDanhGia;
import trong.lixco.com.ejb.service.CaiDatHoiDongService;
import trong.lixco.com.ejb.service.KetQuaDanhGiaService;
import trong.lixco.com.ejb.service.KyDanhGiaService;
import trong.lixco.com.jpa.dto.BaoCaoTuyChonDTO;
import trong.lixco.com.jpa.entity.CaiDatHoiDong;
import trong.lixco.com.jpa.entity.KyDanhGia;
import trong.lixco.com.servicepublic.EmployeeDTO;
import trong.lixco.com.servicepublic.EmployeeServicePublic;
import trong.lixco.com.servicepublic.EmployeeServicePublicProxy;

@Named
@ViewScoped
public class HoiDongDanhGiaBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	private KyDanhGia kyDanhGia;
	private List<KyDanhGia> kyDanhGias;

	@Inject
	private KyDanhGiaService kyDanhGiaService;
	@Inject
	private Logger logger;
	EmployeeServicePublic employeeServicePublic;
	@Inject
	CaiDatHoiDongService caiDatHoiDongService;

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Inject
	private KetQuaDanhGiaService ketQuaDanhGiaService;
	boolean truongphong = false;
	List<DanhGiaNhanVien> danhGiaNhanViens;
	Gson gson;

	public void ajaxKyDanhGia() {
		danhGiaNhanViens.clear();
		if (kyDanhGia != null) {
			Type listType = new TypeToken<List<NhanVienKyDanhGia>>() {
			}.getType();
			List<NhanVienKyDanhGia> nvs = gson.fromJson(this.kyDanhGia.getNhanviendanhgia(), listType);
			if (getAccount().isAdmin()) {
				for (int k = 0; k < nvs.size(); k++) {
					DanhGiaNhanVien nv = new DanhGiaNhanVien();
					nv.setManhanvien(nvs.get(k).getManhanvien());
					nv.setTennhanvien(nvs.get(k).getTennhanvien());
					nv.setKyDanhGia(kyDanhGia);
					nv.setPhongban(nvs.get(k).getTenphongban());
					nv.setDanhgianv(ketQuaDanhGiaService.kiemtradanhgianv(kyDanhGia.getId(), nv.getManhanvien()));
					nv.setDanhgiaql(ketQuaDanhGiaService.kiemtradanhgiaql(kyDanhGia.getId(), nv.getManhanvien()));
					nv.setDanhgiahd(ketQuaDanhGiaService.kiemtradanhgiahd(kyDanhGia.getId(), nv.getManhanvien()));
					danhGiaNhanViens.add(nv);
				}
			} else {
				CaiDatHoiDong cd = caiDatHoiDongService.taidulieu(getAccount().getMember().getCode());
				if (cd != null) {
					Type listTypephongban = new TypeToken<List<NhanVienKyDanhGia>>() {
					}.getType();
					List<PhongBanHoiDongDanhGia> phongBanHoiDongDanhGias = gson.fromJson(cd.getPhongban(),
							listTypephongban);
					for (int k = 0; k < nvs.size(); k++) {
						boolean status = false;
						for (int i = 0; i < phongBanHoiDongDanhGias.size(); i++) {
							if (phongBanHoiDongDanhGias.get(i).getMaphongban().equals(nvs.get(k).getMaphongban())) {
								status = true;
								break;
							}
						}
						if (status) {
							DanhGiaNhanVien nv = new DanhGiaNhanVien();
							nv.setManhanvien(nvs.get(k).getManhanvien());
							nv.setTennhanvien(nvs.get(k).getTennhanvien());
							nv.setKyDanhGia(kyDanhGia);
							nv.setPhongban(nvs.get(k).getTenphongban());
							nv.setDanhgianv(ketQuaDanhGiaService.kiemtradanhgianv(kyDanhGia.getId(), nv.getManhanvien()));
							nv.setDanhgiaql(ketQuaDanhGiaService.kiemtradanhgiaql(kyDanhGia.getId(), nv.getManhanvien()));
							nv.setDanhgiahd(ketQuaDanhGiaService.kiemtradanhgiahd(kyDanhGia.getId(), nv.getManhanvien()));
							danhGiaNhanViens.add(nv);
						}
					}
				}
			}
		}
	}

	@Override
	public void initItem() {
		try {
			kyDanhGia = null;
			gson = new Gson();
			danhGiaNhanViens = new ArrayList<DanhGiaNhanVien>();
			employeeServicePublic = new EmployeeServicePublicProxy();
			trong.lixco.com.account.servicepublics.Member member = getAccount().getMember();
			kyDanhGias = kyDanhGiaService.kiemtrakydanhgia(new Date());
//			if (kyDanhGias.size() != 0)
//				kyDanhGia = kyDanhGias.get(0);
			ajaxKyDanhGia();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void chitietkydanhgia(KyDanhGia kyDanhGia) {
		try {
			if (kyDanhGia.getId() != null) {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/danhgianangluc/pages/ketquadanhgia.htm?kydanhgia_id=" + kyDanhGia.getId());
			} else {
				noticeDialog("Chưa chọn kỳ đánh giá.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void danhgianhanvien(DanhGiaNhanVien danhGiaNhanVien) {
		try {
			if (danhGiaNhanVien != null) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"/danhgianangluc/pages/chitiethoidongdanhgia.htm?kydanhgia_id="
										+ danhGiaNhanVien.getKyDanhGia().getId() + "&nv="
										+ danhGiaNhanVien.getManhanvien());
			} else {
				noticeDialog("Chưa chọn kỳ đánh giá.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

	public List<KyDanhGia> getKyDanhGias() {
		return kyDanhGias;
	}

	public void setKyDanhGias(List<KyDanhGia> kyDanhGias) {
		this.kyDanhGias = kyDanhGias;
	}

	public List<DanhGiaNhanVien> getDanhGiaNhanViens() {
		return danhGiaNhanViens;
	}

	public void setDanhGiaNhanViens(List<DanhGiaNhanVien> danhGiaNhanViens) {
		this.danhGiaNhanViens = danhGiaNhanViens;
	}

	public KyDanhGia getKyDanhGia() {
		return kyDanhGia;
	}

	public void setKyDanhGia(KyDanhGia kyDanhGia) {
		this.kyDanhGia = kyDanhGia;
	}

}
