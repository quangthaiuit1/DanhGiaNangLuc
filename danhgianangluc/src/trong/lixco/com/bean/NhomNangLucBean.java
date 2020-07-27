package trong.lixco.com.bean;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.ejb.service.NhomNangLucService;
import trong.lixco.com.jpa.entity.NhomNangLuc;
import trong.lixco.com.util.Notify;

@Named
@ViewScoped
public class NhomNangLucBean extends AbstractBean<NhomNangLuc> {
	private static final long serialVersionUID = 1L;
	private Notify notify;
	private List<NhomNangLuc> nhomNangLucs;
	private NhomNangLuc nhomNangLuc;
	private NhomNangLuc nhomNangLucEdit;

	@Inject
	private NhomNangLucService nhomNangLucService;
	@Inject
	private Logger logger;

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	public void initItem() {
		nhomNangLuc = new NhomNangLuc();
		search();
	}
	public void search(){
		nhomNangLucs=nhomNangLucService.findAll();
	}
	public void createOrUpdate() {
		notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (nhomNangLuc != null) {
				if (!"".equals(nhomNangLuc.getMa()) && !"".equals(nhomNangLuc.getTen())) {
					if (nhomNangLuc.getId() == null) {
						if (allowSave(null)) {
							nhomNangLuc=installSave(nhomNangLuc);
							nhomNangLuc = nhomNangLucService.create(nhomNangLuc);
							nhomNangLucs.add(0, nhomNangLuc);
							writeLogInfo("Tạo mới " + nhomNangLuc.toString());
							notify.success();
							search();
						} else {
							nhomNangLucEdit = new NhomNangLuc();
							notify.warningDetail();
						}
					} else {
						if (allowUpdate(null)) {
							nhomNangLuc=installUpdate(nhomNangLuc);
							nhomNangLuc = nhomNangLucService.update(nhomNangLuc);
							int index = nhomNangLucs.indexOf(nhomNangLuc);
							nhomNangLucs.set(index, nhomNangLuc);
							writeLogInfo("Cập nhật " + nhomNangLuc.toString());
							notify.success();
							search();
						} else {
							notify.warningDetail();
						}
					}
				} else {
					notify.warning("Điền đầy đủ thông tin!");
				}
			}
		} catch (Exception e) {
			writeLogError(e.getLocalizedMessage());
			notify.warning("Mã đã tồn tại!");
		}
	}

	public void reset() {
		nhomNangLuc = new NhomNangLuc();
		nhomNangLucEdit=null;

	}

	public void showEdit() {
		this.nhomNangLuc = nhomNangLucEdit;
		showDialog("chitiet");
	}

	public void delete() {
		notify = new Notify(FacesContext.getCurrentInstance());
		if (nhomNangLuc.getId() != null) {
			if (allowDelete(null)) {
				boolean status = nhomNangLucService.delete(nhomNangLuc);
				if (status) {
					nhomNangLucs.remove(nhomNangLuc);
					writeLogInfo("Xoá " + nhomNangLuc.toString());
					reset();
					notify.success();
					search();
				} else {
					writeLogError("Lỗi khi xoá " + nhomNangLuc.toString());
					notify.error();
				}
			} else {
				notify.warningDetail();
			}
		} else {
			notify.warning("Chưa chọn trong danh sách!");
		}
	}


	public List<NhomNangLuc> getNhomNangLucs() {
		return nhomNangLucs;
	}

	public void setNhomNangLucs(List<NhomNangLuc> NhomNangLucs) {
		this.nhomNangLucs = NhomNangLucs;
	}

	public NhomNangLuc getNhomNangLuc() {
		return nhomNangLuc;
	}

	public void setNhomNangLuc(NhomNangLuc NhomNangLuc) {
		this.nhomNangLuc = NhomNangLuc;
	}

	public NhomNangLuc getNhomNangLucEdit() {
		return nhomNangLucEdit;
	}

	public void setNhomNangLucEdit(NhomNangLuc NhomNangLucEdit) {
		this.nhomNangLucEdit = NhomNangLucEdit;
	}

	
}
