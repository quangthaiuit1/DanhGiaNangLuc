package trong.lixco.com.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.DepartmentServicePublicProxy;
import trong.lixco.com.classInfor.ChiTietKyDanhGiaTable;
import trong.lixco.com.classInfor.DanhGia;
import trong.lixco.com.classInfor.DiemNhomNangLuc;
import trong.lixco.com.ejb.service.ChiTietKyDanhGiaService;
import trong.lixco.com.ejb.service.KhungNangLucService;
import trong.lixco.com.ejb.service.KyDanhGiaService;
import trong.lixco.com.ejb.service.NangLucService;
import trong.lixco.com.ejb.service.NhomNangLucService;
import trong.lixco.com.jpa.entity.ChiTietKyDanhGia;
import trong.lixco.com.jpa.entity.KhungNangLuc;
import trong.lixco.com.jpa.entity.KyDanhGia;
import trong.lixco.com.jpa.entity.NangLuc;
import trong.lixco.com.jpa.entity.NhomNangLuc;
import trong.lixco.com.servicepublic.PositionJobDTO;
import trong.lixco.com.servicepublic.PositionJobServicePublic;
import trong.lixco.com.servicepublic.PositionJobServicePublicProxy;
import trong.lixco.com.util.DepartmentUtil;
import trong.lixco.com.util.Notify;

@Named
@ViewScoped
public class ChiTietKyDanhGiaBean extends AbstractBean<KyDanhGia> {
	private static final long serialVersionUID = 1L;
	private Notify notify;
	private KyDanhGia kyDanhGia;
	private List<ColumnModel> columns;
	private Department department;
	private List<Department> departments;
	private PositionJobDTO positionJobDTO;
	private List<PositionJobDTO> positionJobDTOs;
	private List<KhungNangLuc> khungNangLucs;
	private KhungNangLuc khungNangLuc;
	private List<NangLuc> nangLucs;
	private List<ChiTietKyDanhGiaTable> chiTietKyDanhGiaTables;
	private List<DiemNhomNangLuc> diemNhomNangLucs;

	DepartmentServicePublic departmentServicePublic;
	PositionJobServicePublic positionJobServicePublic;

	@Inject
	private KhungNangLucService khungNangLucService;
	@Inject
	private ChiTietKyDanhGiaService chiTietKyDanhGiaService;
	@Inject
	private NhomNangLucService nhomNangLucService;
	@Inject
	private NangLucService nangLucService;
	@Inject
	private KyDanhGiaService kyDanhGiaService;
	@Inject
	private Logger logger;

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	public void initItem() {
		try {
			chiTietKyDanhGiaTables = new ArrayList<ChiTietKyDanhGiaTable>();
			diemNhomNangLucs = new ArrayList<DiemNhomNangLuc>();
			Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap();
			String kydanhgia_id = params.get("kydanhgia_id");
			kyDanhGia = kyDanhGiaService.findById(Long.parseLong(kydanhgia_id));

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

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	String[][] trongsonhoms;

	public void ajaxDep() {
		if (department != null) {
			try {
				List<NhomNangLuc> nhomnls = nhomNangLucService.findAll();
				chiTietKyDanhGiaTables.clear();
				diemNhomNangLucs.clear();
				nangLucs = nangLucService.findDepartment(department.getCode());
				PositionJobDTO[] poss = positionJobServicePublic.findDep(department.getCode());
				trongsonhoms = new String[nhomnls.size()][poss.length];
				for (int i = 0; i < nangLucs.size(); i++) {
					ChiTietKyDanhGiaTable knl = new ChiTietKyDanhGiaTable();
					knl.setManangluc(nangLucs.get(i).getMa());
					knl.setTennangluc(nangLucs.get(i).getTen());
					knl.setNangLuc(nangLucs.get(i));
					List<DanhGia> danhgias = new ArrayList<DanhGia>();
					for (int j = 0; j < poss.length; j++) {
						ChiTietKyDanhGia ct = chiTietKyDanhGiaService.findRange(kyDanhGia.getId(), poss[j].getCode(),
								nangLucs.get(i).getId());

						boolean check = false;
						if (ct != null) {
							check = true;
							trongsonhoms[(int) (nangLucs.get(i).getNhomNangLuc().getId() - 1)][j] = ct.getTrongsonhom()
									+ "";
						} else {
							KhungNangLuc khungnl = khungNangLucService.findByPosNangLuc(poss[j].getCode(),
									nangLucs.get(i).getId());
							if (khungnl != null){
								trongsonhoms[(int) (nangLucs.get(i).getNhomNangLuc().getId() - 1)][j] = khungnl
										.getTrongsonhom() + "";}
						}
						danhgias.add(new DanhGia(check, poss[j].getCode()));

					}
					knl.setDanhgias(danhgias);
					chiTietKyDanhGiaTables.add(knl);
					diemNhomNangLucs.add(new DiemNhomNangLuc(nangLucs.get(i).getNhomNangLuc().getMa(), 0));
				}

				positionJobDTOs = new ArrayList<PositionJobDTO>();
				columns = new ArrayList<ColumnModel>();
				columns.add(new ColumnModel("Mã", "manangluc", -1, "wctb50 tacenter"));
				columns.add(new ColumnModel("Năng lực", "tennangluc", -1, "wctb200"));
				for (int i = 0; i < poss.length; i++) {
					columns.add(new ColumnModel(poss[i].getName(), "danhgias", i, "wctb50 tacenter"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveAndUpdate() {
		try {
			if (chiTietKyDanhGiaTables.size() != 0) {
				if (allowSave(null) && allowUpdate(null)) {

					// duyet mang
					// for (int i = 0; i < trongsonhoms.length; i++) {
					// for (int j = 0; j < trongsonhoms[i].length; j++) {
					// System.out.println("Phần tử thứ [" + i + ", " + j +
					// "]: "+trongsonhoms[i][j]);
					// }
					// }
					// luu du lieu
					List<ChiTietKyDanhGia> chiTietKyDanhGias = new ArrayList<ChiTietKyDanhGia>();
					PositionJobDTO[] poss = positionJobServicePublic.findDep(department.getCode());
					boolean ischeck = false;
					for (int i = 0; i < chiTietKyDanhGiaTables.size(); i++) {
						ChiTietKyDanhGiaTable ctt = chiTietKyDanhGiaTables.get(i);
						for (int k = 0; k < ctt.getDanhgias().size(); k++) {
							if (ctt.getDanhgias().get(k).isCheck()) {
								ChiTietKyDanhGia ct = new ChiTietKyDanhGia();
								ct.setCodeDep(department.getCode());
								ct.setKyDanhGia(kyDanhGia);
								ct.setMavitri(ctt.getDanhgias().get(k).getChucvu());
								ct.setNangLuc(ctt.getNangLuc());
								long idnhomnangluc = ctt.getNangLuc().getNhomNangLuc().getId();
								int socot = 0;
								for (int j = 0; j < poss.length; j++) {
									if (ctt.getDanhgias().get(k).getChucvu().equals(poss[j].getCode())) {
										socot = j;
										break;
									}
								}
								String tsnhom = trongsonhoms[(int) (idnhomnangluc - 1)][socot];
								ct.setTrongsonhom(Integer.parseInt(tsnhom));
								chiTietKyDanhGias.add(ct);
								ischeck = true;
							} else {
								// khong chon muc tieu danh gia
							}
						}
					}
					if (ischeck) {
						boolean status = chiTietKyDanhGiaService.saveOrUpdate(chiTietKyDanhGias);
						if (status) {
							notice("Lưu thành công.");
						} else {
							error("Không lưu được");
						}
					} else {
						error("Chưa chọn các năng lực đánh giá");
					}
				} else {
					noticeDialog("Tài khoản không thao tác được");
				}
			}
		} catch (Exception e) {
			writeLogError(e.getLocalizedMessage());
			noticeDialog("Xảy ra lỗi: " + e.getLocalizedMessage());
		}
	}

	static public class ColumnModel implements Serializable {

		private String header;
		private String property;
		private int index = -1;
		private String style;

		public ColumnModel(String header, String property, int index, String style) {
			this.header = header;
			this.property = property;
			this.index = index;
			this.style = style;
		}

		public String getHeader() {
			return header;
		}

		public String getProperty() {
			return property;
		}

		public int getIndex() {
			return index;
		}

		public String getStyle() {
			return style;
		}

		public void setHeader(String header) {
			this.header = header;
		}

		public void setProperty(String property) {
			this.property = property;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public void setStyle(String style) {
			this.style = style;
		}

	}

	public List<ColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnModel> columns) {
		this.columns = columns;
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

	public List<ChiTietKyDanhGiaTable> getChiTietKyDanhGiaTables() {
		return chiTietKyDanhGiaTables;
	}

	public void setChiTietKyDanhGiaTables(List<ChiTietKyDanhGiaTable> chiTietKyDanhGiaTables) {
		this.chiTietKyDanhGiaTables = chiTietKyDanhGiaTables;
	}

	public List<DiemNhomNangLuc> getDiemNhomNangLucs() {
		return diemNhomNangLucs;
	}

	public void setDiemNhomNangLucs(List<DiemNhomNangLuc> diemNhomNangLucs) {
		this.diemNhomNangLucs = diemNhomNangLucs;
	}

	public String[][] getTrongsonhoms() {
		return trongsonhoms;
	}

	public void setTrongsonhoms(String[][] trongsonhoms) {
		this.trongsonhoms = trongsonhoms;
	}

}
