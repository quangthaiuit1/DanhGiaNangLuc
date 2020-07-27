package trong.lixco.com.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.DepartmentServicePublicProxy;
import trong.lixco.com.classInfor.DanhGiaNhanVien;
import trong.lixco.com.classInfor.NhanVienKyDanhGia;
import trong.lixco.com.ejb.service.KetQuaDanhGiaService;
import trong.lixco.com.ejb.service.KyDanhGiaService;
import trong.lixco.com.jpa.entity.KyDanhGia;
import trong.lixco.com.servicepublic.DepartmentDTO;
import trong.lixco.com.servicepublic.DepartmentServicePublicService;
import trong.lixco.com.servicepublic.EmpPJobServicePublic;
import trong.lixco.com.servicepublic.EmployeeDTO;
import trong.lixco.com.servicepublic.EmployeeServicePublic;
import trong.lixco.com.servicepublic.EmployeeServicePublicProxy;

@Named
@ViewScoped
public class BaiDanhGiaNangLucBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	private List<KyDanhGia> kyDanhGias;

	@Inject
	private KyDanhGiaService kyDanhGiaService;
	@Inject
	private Logger logger;
	EmployeeServicePublic employeeServicePublic;

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Inject
	private KetQuaDanhGiaService ketQuaDanhGiaService;
	boolean truongphong = false;

	List<DanhGiaNhanVien> danhGiaNhanViens;

	@Override
	public void initItem() {
		try {
			danhGiaNhanViens = new ArrayList<DanhGiaNhanVien>();
			employeeServicePublic = new EmployeeServicePublicProxy();
			trong.lixco.com.account.servicepublics.Member member = getAccount().getMember();
			kyDanhGias = kyDanhGiaService.kiemtrakydanhgia(new Date(), member.getCode());
			for (int i = 0; i < kyDanhGias.size(); i++) {
				boolean status = ketQuaDanhGiaService.findRange(kyDanhGias.get(i).getId(), member.getCode());
				kyDanhGias.get(i).setDadanhgia(status);
			}
			DepartmentServicePublic departmentServicePublic = new DepartmentServicePublicProxy();
			Department[] deps = departmentServicePublic.getAllDepartSubByParent("10001");
			String codedep[] = new String[1];
			for (int i = 0; i < deps.length; i++) {
				if (deps[i].getLevelDep().getLevel() <= 2) {
					if (member.getCode().equals(deps[i].getCodeMem())) {
						truongphong = true;
						codedep[0] = deps[i].getCode();
						// cai 1 người truong nhieu to
						// break;
					}
				}
			}
			if (truongphong) {
				trong.lixco.com.servicepublic.DepartmentServicePublic departmentServicePublicCenter = new trong.lixco.com.servicepublic.DepartmentServicePublicProxy();
				DepartmentDTO[] dep = departmentServicePublicCenter.findAllSubDepart(codedep[0]);
				String codedepAll[] = null;
				if (dep != null) {
					codedepAll = new String[dep.length + 1];
					for (int i = 0; i < dep.length; i++) {
						codedepAll[i] = dep[i].getCode();
					}
					codedepAll[dep.length]=codedep[0];
				} else {
					codedepAll=new String[1];
					codedepAll[0] = codedep[0];
				}
				EmployeeDTO[] employeeDTOs = employeeServicePublic.findByDep(codedepAll);
				for (int i = 0; i < employeeDTOs.length; i++) {
					if (!employeeDTOs[i].getCode().equals(member.getCode())) {
						List<KyDanhGia> kyDanhGias = kyDanhGiaService.kiemtrakydanhgia(new Date(),
								employeeDTOs[i].getCode());
						for (int j = 0; j < kyDanhGias.size(); j++) {
							DanhGiaNhanVien nv = new DanhGiaNhanVien();
							nv.setManhanvien(employeeDTOs[i].getCode());
							nv.setTennhanvien(employeeDTOs[i].getName());
							nv.setKyDanhGia(kyDanhGias.get(j));
							nv.setPhongban(employeeDTOs[i].getNameDepart());
							nv.setDanhgianv(ketQuaDanhGiaService.kiemtradanhgianv(kyDanhGias.get(j).getId(),
									employeeDTOs[i].getCode()));
							nv.setDanhgiaql(ketQuaDanhGiaService.kiemtradanhgiaql(kyDanhGias.get(j).getId(),
									employeeDTOs[i].getCode()));
							nv.setDanhgiahd(ketQuaDanhGiaService.kiemtradanhgiahd(kyDanhGias.get(j).getId(),
									employeeDTOs[i].getCode()));
							danhGiaNhanViens.add(nv);
						}
					}
				}
			}

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
								"/danhgianangluc/pages/danhgianhanvien.htm?kydanhgia_id="
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

}
