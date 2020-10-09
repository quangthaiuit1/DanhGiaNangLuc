package trong.lixco.com.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.DepartmentServicePublicProxy;
import trong.lixco.com.ejb.service.ChiTietNangLucService;
import trong.lixco.com.ejb.service.KhungNangLucService;
import trong.lixco.com.ejb.service.NangLucService;
import trong.lixco.com.jpa.entity.KhungNangLuc;
import trong.lixco.com.jpa.entity.NangLuc;
import trong.lixco.com.servicepublic.PositionJobDTO;
import trong.lixco.com.servicepublic.PositionJobServicePublic;
import trong.lixco.com.servicepublic.PositionJobServicePublicProxy;
import trong.lixco.com.util.DepartmentUtil;
import trong.lixco.com.util.Notify;

@Named
@ViewScoped
public class KhungNangLucBean extends AbstractBean<KhungNangLuc> {
	private static final long serialVersionUID = 1L;
	private Notify notify;
	private List<KhungNangLuc> khungNangLucs;
	private KhungNangLuc khungNangLuc;

	private Department department;
	private List<Department> departments;

	private PositionJobDTO positionJobDTO;
	private List<PositionJobDTO> positionJobDTOs;

	DepartmentServicePublic departmentServicePublic;
	PositionJobServicePublic positionJobServicePublic;
	@Inject
	private NangLucService nangLucService;
	@Inject
	private KhungNangLucService khungNangLucService;
	@Inject
	private Logger logger;

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	public void initItem() {
		departmentServicePublic = new DepartmentServicePublicProxy();
		positionJobServicePublic = new PositionJobServicePublicProxy();
		departments = new ArrayList<Department>();
		try {
			Department[] deps = departmentServicePublic.getAllDepartSubByParent("10001");
			for (int i = 0; i < deps.length; i++) {
				if (deps[i].getLevelDep().getLevel() <= 2)
					departments.add(deps[i]);
			}
			if (departments.size() != 0) {
				departments = DepartmentUtil.sort(departments);
			}

		} catch (Exception e) {
		}
	}

	public void ajaxDep() {
		if (department != null) {
			try {
				PositionJobDTO[] poss = positionJobServicePublic.findDep(department.getCode());
				positionJobDTOs = new ArrayList<PositionJobDTO>();
				for (int i = 0; i < poss.length; i++) {
					positionJobDTOs.add(poss[i]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void ajaxPos() {
		if (positionJobDTO != null) {
			try {
				khungNangLucs = khungNangLucService.findByPos(positionJobDTO.getCode());
				List<NangLuc> nangLucs = nangLucService.findDepartment(department.getCode());
				for (int i = 0; i < nangLucs.size(); i++) {
					boolean status = false;
					int indexremove=-1;
					for (int j = 0; j < khungNangLucs.size(); j++) {
						if (nangLucs.get(i).equals(khungNangLucs.get(j).getNangLuc())) {
							if (nangLucs.get(i).isDisable()) {
								indexremove = j;
								break;
							} else {
								status = true;
								break;
							}
						}
					}
					if(indexremove!=-1){
						khungNangLucs.remove(indexremove);
					}
					if (!status) {
						KhungNangLuc knl = new KhungNangLuc();
						knl.setMavitri(positionJobDTO.getCode());
						knl.setNangLuc(nangLucs.get(i));
						khungNangLucs.add(knl);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveAndUpdate() {
		notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (khungNangLucs.size() != 0) {
				if (allowSave(null) && allowUpdate(null)) {
					for (int i = 0; i < khungNangLucs.size(); i++) {
						if (khungNangLucs.get(i).getId() == null) {
							khungNangLucService.create(khungNangLucs.get(i));
						} else {
							khungNangLucService.update(khungNangLucs.get(i));
						}
					}
					notify.success();
					ajaxPos();
				} else {
					notify.warningDetail();
				}
			}
		} catch (Exception e) {
			writeLogError(e.getLocalizedMessage());
			notify.warning("Lỗi khi lưu: " + e.getLocalizedMessage());
		}
	}

	public List<KhungNangLuc> getKhungNangLucs() {
		return khungNangLucs;
	}

	public void setKhungNangLucs(List<KhungNangLuc> khungNangLucs) {
		this.khungNangLucs = khungNangLucs;
	}

	public KhungNangLuc getKhungNangLuc() {
		return khungNangLuc;
	}

	public void setKhungNangLuc(KhungNangLuc khungNangLuc) {
		this.khungNangLuc = khungNangLuc;
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

	public PositionJobDTO getPositionJobDTO() {
		return positionJobDTO;
	}

	public void setPositionJobDTO(PositionJobDTO positionJobDTO) {
		this.positionJobDTO = positionJobDTO;
	}

	public List<PositionJobDTO> getPositionJobDTOs() {
		return positionJobDTOs;
	}

	public void setPositionJobDTOs(List<PositionJobDTO> positionJobDTOs) {
		this.positionJobDTOs = positionJobDTOs;
	}

}
