package trong.lixco.com.bean;

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
import trong.lixco.com.ejb.service.KyDanhGiaService;
import trong.lixco.com.ejb.service.LoaiKyDanhGiaService;
import trong.lixco.com.jpa.entity.KyDanhGia;
import trong.lixco.com.jpa.entity.LoaiKyDanhGia;
import trong.lixco.com.servicepublic.EmployeeDTO;
import trong.lixco.com.servicepublic.EmployeeServicePublic;
import trong.lixco.com.servicepublic.EmployeeServicePublicProxy;
import trong.lixco.com.servicepublic.PositionJobServicePublic;
import trong.lixco.com.servicepublic.PositionJobServicePublicProxy;
import trong.lixco.com.util.DepartmentUtil;
import trong.lixco.com.util.Notify;
import trong.lixco.util.CheckInOut;

@Named
@ViewScoped
public class DanhSachKyDanhGiaBean extends AbstractBean<KyDanhGia> {
	private static final long serialVersionUID = 1L;
	private Notify notify;
	private List<KyDanhGia> kyDanhGias;
	private KyDanhGia kyDanhGia;

	private Department department;
	private List<Department> departmentSelects;
	private List<Department> departments;
	private Set<Department> departmentList;

	private List<LoaiKyDanhGia> loaiKyDanhGias;
	private List<EmployeeDTO> employeeDTOs;
	private EmployeeDTO employeeDTO;
	private Set<EmployeeDTO> employeeDTOSelects;

	EmployeeServicePublic employeeServicePublic;
	DepartmentServicePublic departmentServicePublic;
	PositionJobServicePublic positionJobServicePublic;
	@Inject
	private KyDanhGiaService kyDanhGiaService;
	@Inject
	private LoaiKyDanhGiaService loaiKyDanhGiaService;
	@Inject
	private Logger logger;

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	public void initItem() {
		kyDanhGia = new KyDanhGia();
		employeeServicePublic = new EmployeeServicePublicProxy();
		departmentServicePublic = new DepartmentServicePublicProxy();
		positionJobServicePublic = new PositionJobServicePublicProxy();
		departments = new ArrayList<Department>();
		departmentList = new HashSet<Department>();

		employeeDTOs = new ArrayList<EmployeeDTO>();
		employeeDTOSelects = new HashSet<EmployeeDTO>();
		try {
			Department[] deps = departmentServicePublic.getAllDepartSubByParent("10001");
			for (int i = 0; i < deps.length; i++) {
				if (deps[i].getLevelDep().getLevel() <= 2) {
					departments.add(deps[i]);
					Department[] departments = departmentServicePublic.getAllDepartSubByParent(deps[i].getCode());
					List<String> codeDeps = new ArrayList<String>();
					if (departments != null)
						for (int j = 0; j < departments.length; j++) {
							codeDeps.add(departments[j].getCode());
						}
					if (deps[i].getLevelDep().getLevel() == 2) {
						codeDeps.add(deps[i].getCode());
						departmentList.add(deps[i]);
						String[] codearr = codeDeps.stream().toArray(String[]::new);
						EmployeeDTO[] employeeDTOs = employeeServicePublic.findByDep(codearr);
						if (employeeDTOs != null)
							for (int j = 0; j < employeeDTOs.length; j++) {
								employeeDTOs[j].setCodeDepart(deps[i].getCode());
								employeeDTOs[j].setNameDepart(deps[i].getName());
								this.employeeDTOs.add(employeeDTOs[j]);
							}
					}
				}
			}
			if (departments.size() != 0) {
				departments = DepartmentUtil.sort(departments);
			}
			loaiKyDanhGias = loaiKyDanhGiaService.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void selectDep() {
		for (Department dep : departmentList) {
			if (dep.isSelect()) {
				for (int i = 0; i < employeeDTOs.size(); i++) {
					if (dep.getCode().equals(employeeDTOs.get(i).getCodeDepart())) {
						employeeDTOSelects.add(employeeDTOs.get(i));
					}
				}
			}
		}
	}

	public void saveAndUpdate() {
		notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (kyDanhGia.getId() == null) {
				if (allowSave(null)) {
					List<String> codeEmp = new ArrayList<String>();
					for (EmployeeDTO emp : employeeDTOSelects) {
						codeEmp.add(emp.getCode());
					}
					String code = codeEmp.toString();
					kyDanhGia.setNhanviendanhgia(code);
					kyDanhGiaService.create(kyDanhGia);
					notify.success();
				} else {
					notify.warningDetail();
				}
			} else {
				if (allowUpdate(null)) {
					kyDanhGiaService.update(kyDanhGia);
					notify.success();
				} else {
					notify.warningDetail();
				}
			}
		} catch (Exception e) {
			writeLogError(e.getLocalizedMessage());
			notify.warning("Lỗi khi lưu: " + e.getLocalizedMessage());
		}
	}

	public void removeItem(EmployeeDTO employeeDTO) {
		employeeDTOSelects.remove(employeeDTO);
	}

	// Nhan vien
	public void ajaxEmp() {
		employeeDTOSelects.add(employeeDTO);
		employeeDTO = null;
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

	public List<KyDanhGia> getKyDanhGias() {
		return kyDanhGias;
	}

	public void setKyDanhGias(List<KyDanhGia> KyDanhGias) {
		this.kyDanhGias = KyDanhGias;
	}

	public KyDanhGia getKyDanhGia() {
		return kyDanhGia;
	}

	public void setKyDanhGia(KyDanhGia KyDanhGia) {
		this.kyDanhGia = KyDanhGia;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<LoaiKyDanhGia> getLoaiKyDanhGias() {
		return loaiKyDanhGias;
	}

	public void setLoaiKyDanhGias(List<LoaiKyDanhGia> loaiKyDanhGias) {
		this.loaiKyDanhGias = loaiKyDanhGias;
	}

	public List<Department> getDepartmentSelects() {
		return departmentSelects;
	}

	public void setDepartmentSelects(List<Department> departmentSelects) {
		this.departmentSelects = departmentSelects;
	}

	public EmployeeDTO getEmployeeDTO() {
		return employeeDTO;
	}

	public void setEmployeeDTO(EmployeeDTO employeeDTO) {
		this.employeeDTO = employeeDTO;
	}

	public List<EmployeeDTO> getEmployeeDTOSelects() {
		List<EmployeeDTO> results = new ArrayList<>(employeeDTOSelects);
		Collections.sort(results, new Comparator<EmployeeDTO>() {
			public int compare(EmployeeDTO o1, EmployeeDTO o2) {
				try {
					int rs = o1.getCodeDepart().compareTo(o2.getCodeDepart());
					if (rs == 0) {
						return o1.getName().compareTo(o2.getName());
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

}
