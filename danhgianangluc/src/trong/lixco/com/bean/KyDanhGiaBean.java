package trong.lixco.com.bean;

import java.io.IOException;

import com.google.gson.reflect.TypeToken;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.google.gson.Gson;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.DepartmentServicePublicProxy;
import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.account.servicepublics.MemberServicePublic;
import trong.lixco.com.account.servicepublics.MemberServicePublicProxy;
import trong.lixco.com.classInfor.NhanVienKyDanhGia;
import trong.lixco.com.ejb.service.KyDanhGiaService;
import trong.lixco.com.ejb.service.LoaiKyDanhGiaService;
import trong.lixco.com.jpa.entity.KyDanhGia;
import trong.lixco.com.jpa.entity.LoaiKyDanhGia;
import trong.lixco.com.servicepublic.EmpPJobDTO;
import trong.lixco.com.servicepublic.EmpPJobServicePublic;
import trong.lixco.com.servicepublic.EmpPJobServicePublicProxy;
import trong.lixco.com.servicepublic.EmployeeDTO;
import trong.lixco.com.servicepublic.EmployeeServicePublic;
import trong.lixco.com.servicepublic.EmployeeServicePublicProxy;
import trong.lixco.com.servicepublic.PositionJobDTO;
import trong.lixco.com.servicepublic.PositionJobServicePublic;
import trong.lixco.com.servicepublic.PositionJobServicePublicProxy;
import trong.lixco.com.thai.mail.CONFIG_MAIL;
import trong.lixco.com.thai.mail.Mail;
import trong.lixco.com.thai.mail.MailDestinationEntity;
import trong.lixco.com.thai.mail.Notification;
import trong.lixco.com.util.DepartmentUtil;
import trong.lixco.com.util.Notify;
import trong.lixco.util.CheckInOut;

import java.lang.reflect.Type;
import java.rmi.RemoteException;

@Named
@ViewScoped
public class KyDanhGiaBean extends AbstractBean<KyDanhGia> {
	private static final long serialVersionUID = 1L;
	private Notify notify;
	private List<KyDanhGia> kyDanhGias;
	private KyDanhGia kyDanhGia;

	private Department department;
	private List<Department> departmentSelects;
	private List<Department> departments;
	private List<PositionJobDTO> positionJobDTOs;
	private Set<Department> departmentList;

	private List<LoaiKyDanhGia> loaiKyDanhGias;
	private List<EmployeeDTO> employeeDTOs;
	private EmployeeDTO employeeDTO;
	private Set<NhanVienKyDanhGia> nhanVienKyDanhGias;

	EmployeeServicePublic employeeServicePublic;
	DepartmentServicePublic departmentServicePublic;
	PositionJobServicePublic positionJobServicePublic;
	EmpPJobServicePublic empPJobServicePublic;

	MemberServicePublic MEMBER_SERVICE_PUBLIC;
	@Inject
	private KyDanhGiaService kyDanhGiaService;
	@Inject
	private LoaiKyDanhGiaService loaiKyDanhGiaService;
	@Inject
	private Logger logger;

	private Gson gson;

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
		empPJobServicePublic = new EmpPJobServicePublicProxy();
		departments = new ArrayList<Department>();
		departmentList = new HashSet<Department>();
		gson = new Gson();
		employeeDTOs = new ArrayList<EmployeeDTO>();
		nhanVienKyDanhGias = new HashSet<NhanVienKyDanhGia>();
		positionJobDTOs = new ArrayList<PositionJobDTO>();
		MEMBER_SERVICE_PUBLIC = new MemberServicePublicProxy();
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

	public void caidatchitietkydanhgia() {
		try {
			if (kyDanhGia.getId() != null) {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/danhgianangluc/pages/chitietkydanhgia.htm?kydanhgia_id=" + kyDanhGia.getId());
			} else {
				noticeDialog("Chưa chọn kỳ đánh giá.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void chitiet(KyDanhGia kyDanhGia) {
		this.kyDanhGia = kyDanhGia;
		nhanVienKyDanhGias.clear();
		// List<String> codeEmps = new
		// ArrayList<String>(Arrays.asList(kyDanhGia.getNhanviendanhgia().split(",")));
		Type listType = new TypeToken<List<NhanVienKyDanhGia>>() {
		}.getType();
		List<NhanVienKyDanhGia> nvs = gson.fromJson(this.kyDanhGia.getNhanviendanhgia(), listType);
		nhanVienKyDanhGias = new HashSet<NhanVienKyDanhGia>(nvs);
		// for (int i = 0; i < codeEmps.size(); i++) {
		// for (int j = 0; j < employeeDTOs.size(); j++) {
		// String code = codeEmps.get(i).trim().replace("[", "");
		// if (employeeDTOs.get(j).getCode().trim().equals(code)) {
		// employeeDTOSelects.add(employeeDTOs.get(j));
		// break;
		// }
		// }
		// }
	}

	public void search() {
		kyDanhGias = kyDanhGiaService.findAll();
	}

	public void selectDep() {
		for (Department dep : departmentList) {
			if (dep.isSelect()) {
				for (int i = 0; i < employeeDTOs.size(); i++) {
					try {
						if (dep.getCode().equals(employeeDTOs.get(i).getCodeDepart())) {
							NhanVienKyDanhGia nv = new NhanVienKyDanhGia();
							nv.setManhanvien(employeeDTOs.get(i).getCode());
							nv.setTennhanvien(employeeDTOs.get(i).getName());
							nv.setPhongban(dep.getName());
							nv.setMaphongban(dep.getCode());
							nv.setTenphongban(dep.getName());
							EmpPJobDTO[] eps = empPJobServicePublic.findByCodeEmp(employeeDTOs.get(i).getCode());
							if (eps != null && eps.length != 0) {
								PositionJobDTO pj = positionJobServicePublic.findByCode(eps[0].getCodePJob());
								if (pj != null) {
									nv.setMachucdanh(pj.getCode());
									nv.setTenchucdanh(pj.getName());
								}
							}
							nhanVienKyDanhGias.add(nv);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

	NhanVienKyDanhGia nhanVienKyDanhGia;
	Department departmentchon;

	public void chinhsuaphongban(NhanVienKyDanhGia nv) {
		this.nhanVienKyDanhGia = nv;
	}

	public void chonphongban() {
		try {
			if (departmentchon != null) {
				positionJobDTOs.clear();
				PositionJobDTO[] poss = positionJobServicePublic.findDep(departmentchon.getCode());
				for (int i = 0; i < poss.length; i++) {
					positionJobDTOs.add(poss[i]);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	PositionJobDTO positionJobDTO;

	public void chinhsuachucdanh(NhanVienKyDanhGia nv) {
		try {
			this.nhanVienKyDanhGia = nv;
			positionJobDTOs.clear();
			PositionJobDTO[] poss = positionJobServicePublic.findDep(nv.getMaphongban());
			for (int i = 0; i < poss.length; i++) {
				positionJobDTOs.add(poss[i]);
			}
		} catch (Exception e) {
		}

	}

	public void chonchucdanh() {
		if (positionJobDTO != null && nhanVienKyDanhGia != null) {
			nhanVienKyDanhGias.remove(nhanVienKyDanhGia);
			nhanVienKyDanhGia.setMachucdanh(positionJobDTO.getCode());
			nhanVienKyDanhGia.setTenchucdanh(positionJobDTO.getName());
			nhanVienKyDanhGia.setMaphongban(departmentchon.getCode());
			nhanVienKyDanhGia.setTenphongban(departmentchon.getName());
			nhanVienKyDanhGias.add(nhanVienKyDanhGia);
		}
	}

	public void saveAndUpdate() {
		notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (kyDanhGia.getId() == null) {
				if (allowSave(null)) {
					String nvs = gson.toJson(nhanVienKyDanhGias);
					kyDanhGia.setNhanviendanhgia(nvs);
					kyDanhGiaService.create(kyDanhGia);
					notify.success();
				} else {
					notify.warningDetail();
				}
			} else {
				if (allowUpdate(null)) {
					String nvs = gson.toJson(nhanVienKyDanhGias);
					kyDanhGia.setNhanviendanhgia(nvs);
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

	public void removeItem(NhanVienKyDanhGia nhanVienKyDanhGia) {
		nhanVienKyDanhGias.remove(nhanVienKyDanhGia);
	}

	// Nhan vien
	public void ajaxEmp() {
		try {
			NhanVienKyDanhGia nv = new NhanVienKyDanhGia();
			nv.setManhanvien(employeeDTO.getCode());
			nv.setTennhanvien(employeeDTO.getName());
			nv.setPhongban(employeeDTO.getNameDepart());
			nv.setMaphongban(employeeDTO.getCodeDepart());
			nv.setTenphongban(employeeDTO.getNameDepart());
			EmpPJobDTO[] eps = empPJobServicePublic.findByCodeEmp(employeeDTO.getCode());
			if (eps != null && eps.length != 0) {
				PositionJobDTO pj = positionJobServicePublic.findByCode(eps[0].getCodePJob());
				if (pj != null) {
					nv.setMachucdanh(pj.getCode());
					nv.setTenchucdanh(pj.getName());
				}
			}
			nhanVienKyDanhGias.add(nv);
			employeeDTO = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public List<EmployeeDTO> completeEmployee(final String containedStr)
			throws NamingException, ClassNotFoundException {
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

	// Thai
	public void sendMail() throws RemoteException {
		trong.lixco.com.account.servicepublics.Member member = getAccount().getMember();
		List<NhanVienKyDanhGia> listEmployee = this.getNhanVienKyDanhGias();
		boolean notExistMail = false;
		boolean notExistAccount = false;

		// Vi tri cong viec
		String tenChucDanh = "";
		String tenNhanVien = "";
		// Create list mail by search employee
		// lap toan bo ds -> nhung nguoi duoc gui mail
		// THAI NEW
		for (int i = 0; i < listEmployee.size(); i++) {
			if (listEmployee.get(i).isSelected()) {

				// tao mail tam thoi cho tung nhan vien
				MailDestinationEntity mailDestinationTemp = new MailDestinationEntity();

				EmployeeDTO temp = employeeServicePublic.findByCode(listEmployee.get(i).getManhanvien());
				// Get account
				Member memberTemp = MEMBER_SERVICE_PUBLIC.findByCode(temp.getCode());
				Account accountByEmployee = accountServicePublic.findMember(memberTemp);
				if (accountByEmployee == null || accountByEmployee.getUserName() == null) {
					notExistAccount = true;
				}
				if (notExistAccount) {
					Notification.NOTI_ERROR("Nhân viên " + temp.getName() + "chưa có tài khoản!");
					return;
				}
				if (accountByEmployee != null) {
					mailDestinationTemp.setAccount(accountByEmployee);
				}

				// ten nhan vien
				tenNhanVien = temp.getName();
				tenChucDanh = listEmployee.get(i).getTenchucdanh();
				if (temp.getEmail() != null && !temp.getEmail().isEmpty()) {
					mailDestinationTemp.setDestinationTo(temp.getEmail());
					// test
				} else {
					// khong co mail
					notExistMail = true;
				}
				// neu khong co mail
				if (notExistMail) {
					Notification.NOTI_ERROR("Nhân viên " + temp.getName() + "chưa có mail!");
					return;
				}
				// Add mail manager to list destination

				String managerCode = memberTemp.getDepartment().getCodeMem();

				EmployeeDTO manager = employeeServicePublic.findByCode(managerCode);
				List<String> mailCC = new ArrayList<>();
				// add mail truong don vi
				mailCC.add(manager.getEmail()); // chinh thuc
				mailCC.add("toan-tranquoc@lixco.com"); // chinh thuc
				String[] mailCCArray = mailCC.toArray(new String[mailCC.size()]);
				// using mailDestinationTemp

				// danh gia nang luc tuyen dung
				if (this.kyDanhGia.getLoaiKyDanhGia().getId() == 3) {
					Mail.processSendMailAfterAddEmployee(CONFIG_MAIL.mailSend, CONFIG_MAIL.passMailSend, mailCCArray,
							mailDestinationTemp.getDestinationTo(), this.kyDanhGia, tenNhanVien, tenChucDanh,
							mailDestinationTemp.getAccount());
				}
				// Danh gia nang luc toang cong ty
				if (this.kyDanhGia.getLoaiKyDanhGia().getId() == 1) {
					Mail.processSendMailPersonalCompany(CONFIG_MAIL.mailSend, CONFIG_MAIL.passMailSend, mailCCArray,
							mailDestinationTemp.getDestinationTo(), this.kyDanhGia, tenNhanVien, tenChucDanh);
				}
				// danh gia quy hoach can bo
				if (this.kyDanhGia.getLoaiKyDanhGia().getId() == 2) {
					Mail.processSendMailQuyHoachCanBoNhanVien(CONFIG_MAIL.mailSend, CONFIG_MAIL.passMailSend,
							mailCCArray, mailDestinationTemp.getDestinationTo(), this.kyDanhGia, tenNhanVien,
							tenChucDanh);
				}
			}
			Notification.NOTI_SUCCESS("Thành công");
		}
		// END THAI NEW
	}
	// End thai

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

	public List<NhanVienKyDanhGia> getNhanVienKyDanhGias() {
		List<NhanVienKyDanhGia> results = new ArrayList<>(nhanVienKyDanhGias);
		Collections.sort(results, new Comparator<NhanVienKyDanhGia>() {
			public int compare(NhanVienKyDanhGia o1, NhanVienKyDanhGia o2) {
				try {
					int rs = o1.getPhongban().compareTo(o2.getPhongban());
					if (rs == 0) {
						return o1.getTennhanvien().compareTo(o2.getTennhanvien());
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

	public void setNhanVienKyDanhGias(Set<NhanVienKyDanhGia> nhanVienKyDanhGias) {
		this.nhanVienKyDanhGias = nhanVienKyDanhGias;
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

	public List<PositionJobDTO> getPositionJobDTOs() {
		return positionJobDTOs;
	}

	public void setPositionJobDTOs(List<PositionJobDTO> positionJobDTOs) {
		this.positionJobDTOs = positionJobDTOs;
	}

	public Department getDepartmentchon() {
		return departmentchon;
	}

	public void setDepartmentchon(Department departmentchon) {
		this.departmentchon = departmentchon;
	}

	public PositionJobDTO getPositionJobDTO() {
		return positionJobDTO;
	}

	public void setPositionJobDTO(PositionJobDTO positionJobDTO) {
		this.positionJobDTO = positionJobDTO;
	}

}
