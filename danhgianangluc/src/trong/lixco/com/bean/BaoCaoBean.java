package trong.lixco.com.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.docx4j.org.xhtmlrenderer.pdf.ITextRenderer;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.DepartmentServicePublicProxy;
import trong.lixco.com.classInfor.BaoCaoTongHopTable;
import trong.lixco.com.classInfor.BaoCaoTuyChonTable;
import trong.lixco.com.classInfor.DanhGiaNhanVien;
import trong.lixco.com.ejb.service.ChiTietNangLucService;
import trong.lixco.com.ejb.service.KetQuaDanhGiaService;
import trong.lixco.com.ejb.service.KhungNangLucService;
import trong.lixco.com.ejb.service.KyDanhGiaService;
import trong.lixco.com.ejb.service.LoaiKyDanhGiaService;
import trong.lixco.com.ejb.service.NangLucService;
import trong.lixco.com.jpa.dto.BaoCaoTongHopDTO;
import trong.lixco.com.jpa.dto.BaoCaoTuyChonDTO;
import trong.lixco.com.jpa.entity.KhungNangLuc;
import trong.lixco.com.jpa.entity.KyDanhGia;
import trong.lixco.com.jpa.entity.LoaiKyDanhGia;
import trong.lixco.com.jpa.entity.NangLuc;
import trong.lixco.com.servicepublic.EmployeeDTO;
import trong.lixco.com.servicepublic.EmployeeServicePublic;
import trong.lixco.com.servicepublic.EmployeeServicePublicProxy;
import trong.lixco.com.servicepublic.PositionJobDTO;
import trong.lixco.com.servicepublic.PositionJobServicePublic;
import trong.lixco.com.servicepublic.PositionJobServicePublicProxy;
import trong.lixco.com.util.DepartmentUtil;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToExcel;
import trong.lixco.com.util.ToObjectFromClass;

@Named
@ViewScoped
public class BaoCaoBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	private Notify notify;
	private List<KhungNangLuc> khungNangLucs;
	private KhungNangLuc khungNangLuc;

	private LoaiKyDanhGia loaiKyDanhGiath;
	private LoaiKyDanhGia loaiKyDanhGiatc;
	private List<LoaiKyDanhGia> loaiKyDanhGias;

	private KyDanhGia kyDanhGiath;
	private KyDanhGia kyDanhGiatc;
	private List<KyDanhGia> kyDanhGiaths;
	private List<KyDanhGia> kyDanhGiatcs;

	private Department departmentth;
	private Department departmenttc;
	private List<Department> departments;

	private NangLuc nangLuc;
	private List<NangLuc> nangLucs;

	private int soluongnangluc;
	private String loaidanhgia;
	private String ketqua;

	private PositionJobDTO positionJobDTO;
	private List<PositionJobDTO> positionJobDTOs;

	EmployeeServicePublic employeeServicePublic;
	DepartmentServicePublic departmentServicePublic;
	PositionJobServicePublic positionJobServicePublic;
	@Inject
	private NangLucService nangLucService;
	@Inject
	private KhungNangLucService khungNangLucService;
	@Inject
	private ChiTietNangLucService chiTietNangLucService;
	@Inject
	private Logger logger;
	@Inject
	private LoaiKyDanhGiaService loaiKyDanhGiaService;
	@Inject
	private KyDanhGiaService kyDanhGiaService;
	@Inject
	private KetQuaDanhGiaService ketQuaDanhGiaService;

	List<BaoCaoTongHopDTO> baoCaoTongHopDTOs;
	List<BaoCaoTuyChonDTO> baoCaoTuyChonDTOs;

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	public void initItem() {
		loaidanhgia = "";
		ketqua = "";

		baoCaoTongHopDTOs = new ArrayList<BaoCaoTongHopDTO>();
		baoCaoTuyChonDTOs = new ArrayList<BaoCaoTuyChonDTO>();
		departmentServicePublic = new DepartmentServicePublicProxy();
		positionJobServicePublic = new PositionJobServicePublicProxy();
		employeeServicePublic = new EmployeeServicePublicProxy();
		departments = new ArrayList<Department>();
		nangLucs = new ArrayList<NangLuc>();

		loaiKyDanhGias = loaiKyDanhGiaService.findAll();
		if (loaiKyDanhGias.size() != 0) {
			loaiKyDanhGiath = loaiKyDanhGias.get(0);
			ajaxLoaiKyDanhGiath();
			loaiKyDanhGiatc = loaiKyDanhGias.get(0);
			ajaxLoaiKyDanhGiatc();
		}
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

	public void ajaxLoaiKyDanhGiath() {
		if (loaiKyDanhGiath != null) {
			try {
				kyDanhGiaths = kyDanhGiaService.findRange(loaiKyDanhGiath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void ajaxLoaiKyDanhGiatc() {
		if (loaiKyDanhGiatc != null) {
			try {
				kyDanhGiatcs = kyDanhGiaService.findRange(loaiKyDanhGiatc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void ajaxDeptc() {
		String codeDep = "";
		if (departmenttc != null)
			codeDep = departmenttc.getCode();
		try {
			nangLucs = nangLucService.findDepartment(codeDep);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void baocaotonghop() {
		notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (kyDanhGiath != null) {
				List<String> manvs = new ArrayList<String>();
				if (departmentth != null) {
					String[] codearr = { departmentth.getCode() };
					EmployeeDTO[] employeeDTOs = employeeServicePublic.findByDep(codearr);
					if (employeeDTOs != null)
						for (int j = 0; j < employeeDTOs.length; j++) {
							manvs.add(employeeDTOs[j].getCode());
						}
				}
				baoCaoTongHopDTOs = ketQuaDanhGiaService.baocaotonghop(kyDanhGiath.getId(), soluongnangluc, manvs,
						loaidanhgia);
				System.out.println("baoCaoTongHopDTOs: " + baoCaoTongHopDTOs.size());
			}
		} catch (Exception e) {
			writeLogError(e.getLocalizedMessage());
			notify.warning("Lá»—i khi lÆ°u: " + e.getLocalizedMessage());
		}
	}
	


	public void baocaotuychon() {
		notify = new Notify(FacesContext.getCurrentInstance());
		try {
			List<String> manvs = new ArrayList<String>();
			if (departmenttc != null) {
				List<String> codeDeps = new ArrayList<String>();
				codeDeps.add(departmenttc.getCode());
				Department[] departments = departmentServicePublic.getAllDepartSubByParent(departmenttc.getCode());
				if (departments != null)
					for (int j = 0; j < departments.length; j++) {
						codeDeps.add(departments[j].getCode());
					}
				
				String[] codearr = codeDeps.stream().toArray(String[]::new);
				EmployeeDTO[] employeeDTOs = employeeServicePublic.findByDep(codearr);
				if (employeeDTOs != null)
					for (int j = 0; j < employeeDTOs.length; j++) {
						manvs.add(employeeDTOs[j].getCode());
					}
			}
			String manl = null;
			if (nangLuc != null)
				manl = nangLuc.getMa();
			baoCaoTuyChonDTOs = ketQuaDanhGiaService.baocaotuychon(kyDanhGiatc.getId(), manvs, manl, ketqua);
			for (int i = 0; i < baoCaoTuyChonDTOs.size(); i++) {
				try {
					EmployeeDTO emp = employeeServicePublic.findByCode(baoCaoTuyChonDTOs.get(i).getManv());
					if (emp != null) {
						baoCaoTuyChonDTOs.get(i).setMaphongban(emp.getCodeDepart());
						baoCaoTuyChonDTOs.get(i).setPhongban(emp.getNameDepart());
						baoCaoTuyChonDTOs.get(i).setTennv(emp.getName());
					}
					PositionJobDTO pos = positionJobServicePublic.findByCode(baoCaoTuyChonDTOs.get(i).getMachucdanh());
					if (pos != null)
						baoCaoTuyChonDTOs.get(i).setChucdanh(pos.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			writeLogError(e.getLocalizedMessage());
			notify.warning("Lỗi khi tải: " + e.getLocalizedMessage());
		}
	}
	public void baocaotuychonxls() {
		try {
			executeScript("target='_blank';monitorDownload( showStatus , hideStatus)");
			List<Object[]> listObject = ToObjectFromClass.baocaotuychon(baoCaoTuyChonDTOs);
			String filename = "baocao";
			HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance()
					.getExternalContext().getResponse();
			httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + filename + ".xlsx");
			ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
			ToExcel.writeXLSX(listObject, servletOutputStream);
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void chitiet(BaoCaoTuyChonDTO baoCaoTuyChonDTO) {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpSession session = (HttpSession) externalContext.getSession(true);
			HttpServletRequest ht = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			ITextRenderer renderer = new ITextRenderer();
			String url = "http://" + ht.getServerName() + ":" + ht.getServerPort()
					+ "/danhgianangluc/pages/chitietdanhgia.htm?kydanhgia_id="
							+ baoCaoTuyChonDTO.getKyDanhGia().getId() + "&nv=" + baoCaoTuyChonDTO.getManv();
			
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
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

	public LoaiKyDanhGia getLoaiKyDanhGiath() {
		return loaiKyDanhGiath;
	}

	public void setLoaiKyDanhGiath(LoaiKyDanhGia loaiKyDanhGiath) {
		this.loaiKyDanhGiath = loaiKyDanhGiath;
	}

	public LoaiKyDanhGia getLoaiKyDanhGiatc() {
		return loaiKyDanhGiatc;
	}

	public void setLoaiKyDanhGiatc(LoaiKyDanhGia loaiKyDanhGiatc) {
		this.loaiKyDanhGiatc = loaiKyDanhGiatc;
	}

	public List<LoaiKyDanhGia> getLoaiKyDanhGias() {
		return loaiKyDanhGias;
	}

	public void setLoaiKyDanhGias(List<LoaiKyDanhGia> loaiKyDanhGias) {
		this.loaiKyDanhGias = loaiKyDanhGias;
	}

	public KyDanhGia getKyDanhGiath() {
		return kyDanhGiath;
	}

	public void setKyDanhGiath(KyDanhGia kyDanhGiath) {
		this.kyDanhGiath = kyDanhGiath;
	}

	public KyDanhGia getKyDanhGiatc() {
		return kyDanhGiatc;
	}

	public void setKyDanhGiatc(KyDanhGia kyDanhGiatc) {
		this.kyDanhGiatc = kyDanhGiatc;
	}

	public List<KyDanhGia> getKyDanhGiaths() {
		return kyDanhGiaths;
	}

	public void setKyDanhGiaths(List<KyDanhGia> kyDanhGiaths) {
		this.kyDanhGiaths = kyDanhGiaths;
	}

	public List<KyDanhGia> getKyDanhGiatcs() {
		return kyDanhGiatcs;
	}

	public void setKyDanhGiatcs(List<KyDanhGia> kyDanhGiatcs) {
		this.kyDanhGiatcs = kyDanhGiatcs;
	}

	public Department getDepartmentth() {
		return departmentth;
	}

	public void setDepartmentth(Department departmentth) {
		this.departmentth = departmentth;
	}

	public Department getDepartmenttc() {
		return departmenttc;
	}

	public void setDepartmenttc(Department departmenttc) {
		this.departmenttc = departmenttc;
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

	public ChiTietNangLucService getChiTietNangLucService() {
		return chiTietNangLucService;
	}

	public void setChiTietNangLucService(ChiTietNangLucService chiTietNangLucService) {
		this.chiTietNangLucService = chiTietNangLucService;
	}

	public List<BaoCaoTongHopDTO> getBaoCaoTongHopDTOs() {
		return baoCaoTongHopDTOs;
	}

	public void setBaoCaoTongHopDTOs(List<BaoCaoTongHopDTO> baoCaoTongHopDTOs) {
		this.baoCaoTongHopDTOs = baoCaoTongHopDTOs;
	}

	public List<BaoCaoTuyChonDTO> getBaoCaoTuyChonDTOs() {
		return baoCaoTuyChonDTOs;
	}

	public void setBaoCaoTuyChonDTOs(List<BaoCaoTuyChonDTO> baoCaoTuyChonDTOs) {
		this.baoCaoTuyChonDTOs = baoCaoTuyChonDTOs;
	}

	public int getSoluongnangluc() {
		return soluongnangluc;
	}

	public void setSoluongnangluc(int soluongnangluc) {
		this.soluongnangluc = soluongnangluc;
	}

	public String getLoaidanhgia() {
		return loaidanhgia;
	}

	public void setLoaidanhgia(String loaidanhgia) {
		this.loaidanhgia = loaidanhgia;
	}

	public String getKetqua() {
		return ketqua;
	}

	public void setKetqua(String ketqua) {
		this.ketqua = ketqua;
	}

	public NangLuc getNangLuc() {
		return nangLuc;
	}

	public void setNangLuc(NangLuc nangLuc) {
		this.nangLuc = nangLuc;
	}

	public List<NangLuc> getNangLucs() {
		return nangLucs;
	}

	public void setNangLucs(List<NangLuc> nangLucs) {
		this.nangLucs = nangLucs;
	}

}
