package trong.lixco.com.bean;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.classInfor.NhanVienKyDanhGia;
import trong.lixco.com.ejb.service.ChiTietKyDanhGiaService;
import trong.lixco.com.ejb.service.ChiTietNangLucService;
import trong.lixco.com.ejb.service.KetQuaDanhGiaService;
import trong.lixco.com.ejb.service.KyDanhGiaService;
import trong.lixco.com.ejb.service.NangLucService;
import trong.lixco.com.jpa.entity.ChiTietKyDanhGia;
import trong.lixco.com.jpa.entity.ChiTietNangLuc;
import trong.lixco.com.jpa.entity.KetQuaDanhGia;
import trong.lixco.com.jpa.entity.KyDanhGia;
import trong.lixco.com.servicepublic.EmpPJobDTO;
import trong.lixco.com.servicepublic.EmpPJobServicePublic;
import trong.lixco.com.servicepublic.EmpPJobServicePublicProxy;
import trong.lixco.com.servicepublic.EmployeeDTO;
import trong.lixco.com.servicepublic.EmployeeServicePublic;
import trong.lixco.com.servicepublic.EmployeeServicePublicProxy;
import trong.lixco.com.thai.mail.CONFIG_MAIL;
import trong.lixco.com.thai.mail.Mail;
import trong.lixco.com.util.Notify;

@Named
@ViewScoped
public class KetQuaDanhGiaBean extends AbstractBean<KetQuaDanhGia> {
	private static final long serialVersionUID = 1L;
	private Notify notify;
	List<KetQuaDanhGia> ketQuaDanhGias;

	EmpPJobServicePublic empPJobServicePublic;
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

	@Override
	protected Logger getLogger() {
		return logger;
	}

	Gson gson;

	@Override
	public void initItem() {
		try {
			employeeServicePublic = new EmployeeServicePublicProxy();
			empPJobServicePublic = new EmpPJobServicePublicProxy();
			ketQuaDanhGias = new ArrayList<KetQuaDanhGia>();
			Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap();
			String kydanhgia_id = params.get("kydanhgia_id");
			kyDanhGia = kyDanhGiaService.findById(Long.parseLong(kydanhgia_id));
			trong.lixco.com.account.servicepublics.Member member = getAccount().getMember();

			boolean status = ketQuaDanhGiaService.findRange(kyDanhGia.getId(), member.getCode());
			if (status) {
				redirect();
			} else {
				gson = new Gson();
				Type listType = new TypeToken<List<NhanVienKyDanhGia>>() {
				}.getType();
				List<NhanVienKyDanhGia> nvs = gson.fromJson(this.kyDanhGia.getNhanviendanhgia(), listType);

				String codePjob = "";
				String codeDep = "";
				for (int i = 0; i < nvs.size(); i++) {
					if (nvs.get(i).getManhanvien().equals(member.getCode())) {
						tenchucdanh=nvs.get(i).getTenchucdanh();
						codePjob = nvs.get(i).getMachucdanh();
						codeDep = nvs.get(i).getMaphongban();
						break;
					}
				}
				List<ChiTietKyDanhGia> chitiets = chiTietKyDanhGiaService.findRange(Long.parseLong(kydanhgia_id),
						codePjob);
				if (!"".equals(codePjob) && !"".equals(codeDep)) {
					for (int i = 0; i < chitiets.size(); i++) {
						List<ChiTietNangLuc> ctnls = chiTietNangLucService.findNangLuc(chitiets.get(i).getNangLuc()
								.getMa(), codeDep);
						Collections.shuffle(ctnls);
						KetQuaDanhGia kq = new KetQuaDanhGia();
						kq.setKyDanhGia(kyDanhGia);
						kq.setNangLuc(chitiets.get(i).getNangLuc());
						kq.setManl(chitiets.get(i).getNangLuc().getMa());
						kq.setTennl(chitiets.get(i).getNangLuc().getTen());
						kq.setChiTietNangLucs(ctnls);
						kq.setManhanvien(member.getCode());
						kq.setMachucdanh(chitiets.get(i).getMavitri());
						ketQuaDanhGias.add(kq);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	EmployeeServicePublic employeeServicePublic;
	
	public void luuketqua() {
		try {
			boolean checkdanhgia = true;
			for (int i = 0; i < ketQuaDanhGias.size(); i++) {
				if (ketQuaDanhGias.get(i).getChiTietNangLuc() == null) {
					checkdanhgia = false;
					break;
				} else {
					ketQuaDanhGias.get(i).setDiem(ketQuaDanhGias.get(i).getChiTietNangLuc().getCapdo());
				}
			}
			if (checkdanhgia) {
				boolean status = ketQuaDanhGiaService.saveOrUpdate(ketQuaDanhGias);
				if (status) {
					//Thai
					//handle send mail
					trong.lixco.com.account.servicepublics.Member member = getAccount().getMember();
					String managerCode = member.getDepartment().getCodeMem();
					EmployeeDTO manager = employeeServicePublic.findByCode(managerCode);
					//Create list mail destination
					List<String> listMailDestinations = new ArrayList<>();
					String tenChucDanh = tenchucdanh;
					String tenNhanVien = member.getName();
					
					//add mail manager
//					listMailDestination.add(manager.getEmail());
					//test mail employee temp // k co du lieu
					if(member.getEmail().isEmpty()) {
						listMailDestinations.add("thai-dinhquang@lixco.com");
					}
					//process send mail
					if(!listMailDestinations.isEmpty()) {
						//danh gia nang luc tuyen dung
						if(kyDanhGia.getLoaiKyDanhGia().getId() == 3) {
							Mail.processSendMailAfterSuccessEvaluate(CONFIG_MAIL.mailSend, CONFIG_MAIL.passMailSend, listMailDestinations, this.kyDanhGia, tenNhanVien, tenChucDanh);
						}
						//danh gia nang luc toan cong ty
						if(kyDanhGia.getLoaiKyDanhGia().getId() == 1) {
							Mail.processSendMailManagerCompany(CONFIG_MAIL.mailSend, CONFIG_MAIL.passMailSend, listMailDestinations, this.kyDanhGia, tenNhanVien, tenChucDanh);
						}
						//danh gia nang luc quy hoach can bo
						if(kyDanhGia.getLoaiKyDanhGia().getId() == 2) {
							Mail.processSendMailQuyHoachCanBoQuanLy(CONFIG_MAIL.mailSend, CONFIG_MAIL.passMailSend, listMailDestinations, this.kyDanhGia, tenNhanVien, tenChucDanh);
						}
						
					}
					//End Thai
					redirect();
				} else {
					errorDialog("Xảy ra lỗi khi lưu");
				}
			} else {
				warnDialog("Một số câu hỏi bạn chưa trả lời.");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void redirect() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/danhgianangluc/pages/kydanhgianangluc.htm");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public KyDanhGia getKyDanhGia() {
		return kyDanhGia;
	}

	public void setKyDanhGia(KyDanhGia kyDanhGia) {
		this.kyDanhGia = kyDanhGia;
	}

	public List<KetQuaDanhGia> getKetQuaDanhGias() {
		return ketQuaDanhGias;
	}

	public void setKetQuaDanhGias(List<KetQuaDanhGia> ketQuaDanhGias) {
		this.ketQuaDanhGias = ketQuaDanhGias;
	}

	public String getTenchucdanh() {
		return tenchucdanh;
	}

	public void setTenchucdanh(String tenchucdanh) {
		this.tenchucdanh = tenchucdanh;
	}

}
