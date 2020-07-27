package trong.lixco.com.bean;

import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.DepartmentServicePublicProxy;
import trong.lixco.com.classInfor.NhanVienKyDanhGia;
import trong.lixco.com.classInfor.PhongBanHoiDongDanhGia;
import trong.lixco.com.ejb.service.CaiDatHoiDongService;
import trong.lixco.com.jpa.entity.CaiDatHoiDong;
import trong.lixco.com.servicepublic.EmployeeDTO;
import trong.lixco.com.servicepublic.EmployeeServicePublic;
import trong.lixco.com.servicepublic.EmployeeServicePublicProxy;
import trong.lixco.com.util.Notify;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Named
@ViewScoped
public class CaiDatHoiDongBean extends AbstractBean<CaiDatHoiDong> {
	private static final long serialVersionUID = 1L;
	private Notify notify;

	private List<PhongBanHoiDongDanhGia> phongBanHoiDongDanhGias;
	private Set<Department> departmentList;
	private List<EmployeeDTO> employeeDTOs;
	private EmployeeDTO employeeDTO;
	private Gson gson;
	CaiDatHoiDong caiDatHoiDong;

	EmployeeServicePublic employeeServicePublic;
	DepartmentServicePublic departmentServicePublic;
	@Inject
	private Logger logger;
	@Inject
	CaiDatHoiDongService caiDatHoiDongService;

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	public void initItem() {
		employeeServicePublic = new EmployeeServicePublicProxy();
		departmentServicePublic = new DepartmentServicePublicProxy();
		phongBanHoiDongDanhGias = new ArrayList<PhongBanHoiDongDanhGia>();
		departmentList = new HashSet<Department>();
		caiDatHoiDong = new CaiDatHoiDong();
		employeeDTOs = new ArrayList<EmployeeDTO>();
		gson = new Gson();
		try {
			Department[] deps = departmentServicePublic.findAll();
			List<String> codeDeps = new ArrayList<String>();
			for (int i = 0; i < deps.length; i++) {

				codeDeps.add(deps[i].getCode());
				if (deps[i].getLevelDep().getLevel() <= 2) {
					Department[] departments = departmentServicePublic.getAllDepartSubByParent(deps[i].getCode());
					if (departments != null)
						for (int j = 0; j < departments.length; j++) {
							codeDeps.add(departments[j].getCode());
						}
					if (deps[i].getLevelDep().getLevel() == 2) {
						codeDeps.add(deps[i].getCode());
						departmentList.add(deps[i]);

					}
				}
			}
			String[] codearr = codeDeps.stream().toArray(String[]::new);
			EmployeeDTO[] employeeDTOs = employeeServicePublic.findByDep(codearr);
			if (employeeDTOs != null)
				for (int j = 0; j < employeeDTOs.length; j++) {
					this.employeeDTOs.add(employeeDTOs[j]);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDep() {
		phongBanHoiDongDanhGias = new ArrayList<PhongBanHoiDongDanhGia>();
		for (Department dep : departmentList) {
			if (dep.isSelect()) {
				boolean status = true;
				for (int i = 0; i < phongBanHoiDongDanhGias.size(); i++) {
					if (dep.getCode().equals(phongBanHoiDongDanhGias.get(i).getMaphongban())) {
						status = false;
						break;
					}
				}
				if (status) {
					PhongBanHoiDongDanhGia pb = new PhongBanHoiDongDanhGia(dep.getCode(), dep.getName());
					phongBanHoiDongDanhGias.add(pb);
				}
			}
		}
	}

	public void selectEmp() {
		if (employeeDTO != null) {
			CaiDatHoiDong cd = caiDatHoiDongService.taidulieu(employeeDTO.getCode());
			if (cd != null) {
				caiDatHoiDong = cd;
				Type listType = new TypeToken<List<NhanVienKyDanhGia>>() {
				}.getType();
				phongBanHoiDongDanhGias = gson.fromJson(cd.getPhongban(), listType);
			} else {
				caiDatHoiDong = new CaiDatHoiDong();
				caiDatHoiDong.setManhanvien(employeeDTO.getCode());
				caiDatHoiDong.setTennhanvien(employeeDTO.getName());
			}
		}
	}

	public void createOrUpdate() {
		notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (employeeDTO != null) {
				if (allowSave(null) && allowUpdate(null)) {
					if (phongBanHoiDongDanhGias == null) {
						if (caiDatHoiDong.getId() != null) {
							caiDatHoiDongService.delete(caiDatHoiDong);
							notice("Cài đặt thành công.");
						} else {
							warnDialog("Chưa cài đặt phòng ban.");
						}
					} else {
						String nvs = gson.toJson(phongBanHoiDongDanhGias);
						caiDatHoiDong.setPhongban(nvs);
						if (caiDatHoiDong.getId() == null) {
							caiDatHoiDongService.create(caiDatHoiDong);
						} else {
							caiDatHoiDongService.update(caiDatHoiDong);
						}
						notice("Cài đặt thành công.");
					}
				} else {
					warnDialog("Tài khoản này không cập nhật được.");
				}

			} else {
				warnDialog("Nhập đầy đủ thông tin.");
			}
		} catch (Exception e) {
			errorDialog(e.getLocalizedMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmployeeDTO> completeEmployee(final String containedStr) throws NamingException, ClassNotFoundException {
		List<EmployeeDTO> linkedList = new LinkedList<EmployeeDTO>();
		String searchText = converViToEn(containedStr.toLowerCase());
		for (int i = 0; i < employeeDTOs.size(); i++) {
			if (converViToEn(employeeDTOs.get(i).getCode()).toLowerCase().contains(searchText)
					|| converViToEn(employeeDTOs.get(i).getName()).toLowerCase().contains(searchText))
				linkedList.add(employeeDTOs.get(i));
		}
		return linkedList;
	}

	public static String converViToEn(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		String result = pattern.matcher(temp).replaceAll("");
		return pattern.matcher(result).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
	}

	public List<Department> getDepartmentList() {
		List<Department> results = new ArrayList<>(departmentList);
		Collections.sort(results, new Comparator<Department>() {
			public int compare(Department o1, Department o2) {
				try {
					int rs = o1.getDepartment().getCode().compareTo(o2.getDepartment().getCode());
					if (rs == 0) {
						return o1.getCode().compareTo(o2.getCode());
					} else {
						return rs;
					}
				} catch (Exception e) {
					return -1;
				}
			}
		});
		return results;
	}

	public List<EmployeeDTO> getEmployeeDTOs() {
		return employeeDTOs;
	}

	public void setEmployeeDTOs(List<EmployeeDTO> employeeDTOs) {
		this.employeeDTOs = employeeDTOs;
	}

	public EmployeeDTO getEmployeeDTO() {
		return employeeDTO;
	}

	public void setEmployeeDTO(EmployeeDTO employeeDTO) {
		this.employeeDTO = employeeDTO;
	}

	public CaiDatHoiDong getCaiDatHoiDong() {
		return caiDatHoiDong;
	}

	public void setCaiDatHoiDong(CaiDatHoiDong caiDatHoiDong) {
		this.caiDatHoiDong = caiDatHoiDong;
	}

	public List<PhongBanHoiDongDanhGia> getPhongBanHoiDongDanhGias() {
		return phongBanHoiDongDanhGias;
	}

	public void setPhongBanHoiDongDanhGias(List<PhongBanHoiDongDanhGia> phongBanHoiDongDanhGias) {
		this.phongBanHoiDongDanhGias = phongBanHoiDongDanhGias;
	}

}
