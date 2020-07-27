package trong.lixco.com.bean;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.ejb.service.LoaiKyDanhGiaService;
import trong.lixco.com.jpa.entity.LoaiKyDanhGia;
import trong.lixco.com.util.Notify;

@Named
@ViewScoped
public class LoaiKyDanhGiaBean extends AbstractBean<LoaiKyDanhGia> {
	private static final long serialVersionUID = 1L;
	private Notify notify;
	private List<LoaiKyDanhGia> loaiKyDanhGias;
	private LoaiKyDanhGia loaiKyDanhGia;
	private LoaiKyDanhGia loaiKyDanhGiaEdit;

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
		loaiKyDanhGia = new LoaiKyDanhGia();
		search();
	}
	public void search(){
		loaiKyDanhGias=loaiKyDanhGiaService.findAll();
	}
	public void createOrUpdate() {
		notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (loaiKyDanhGia != null) {
				if (!"".equals(loaiKyDanhGia.getMa()) && !"".equals(loaiKyDanhGia.getTen())) {
					if (loaiKyDanhGia.getId() == null) {
						if (allowSave(null)) {
							loaiKyDanhGia=installSave(loaiKyDanhGia);
							loaiKyDanhGia = loaiKyDanhGiaService.create(loaiKyDanhGia);
							loaiKyDanhGias.add(0, loaiKyDanhGia);
							writeLogInfo("Tạo mới " + loaiKyDanhGia.toString());
							notify.success();
							search();
						} else {
							loaiKyDanhGiaEdit = new LoaiKyDanhGia();
							notify.warningDetail();
						}
					} else {
						if (allowUpdate(null)) {
							loaiKyDanhGia=installUpdate(loaiKyDanhGia);
							loaiKyDanhGia = loaiKyDanhGiaService.update(loaiKyDanhGia);
							int index = loaiKyDanhGias.indexOf(loaiKyDanhGia);
							loaiKyDanhGias.set(index, loaiKyDanhGia);
							writeLogInfo("Cập nhật " + loaiKyDanhGia.toString());
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
		loaiKyDanhGia = new LoaiKyDanhGia();
		loaiKyDanhGiaEdit=null;

	}

	public void showEdit() {
		this.loaiKyDanhGia = loaiKyDanhGiaEdit;
		showDialog("chitiet");
	}

	public void delete() {
		notify = new Notify(FacesContext.getCurrentInstance());
		if (loaiKyDanhGia.getId() != null) {
			if (allowDelete(null)) {
				boolean status = loaiKyDanhGiaService.delete(loaiKyDanhGia);
				if (status) {
					loaiKyDanhGias.remove(loaiKyDanhGia);
					writeLogInfo("Xoá " + loaiKyDanhGia.toString());
					reset();
					notify.success();
					search();
				} else {
					writeLogError("Lỗi khi xoá " + loaiKyDanhGia.toString());
					notify.error();
				}
			} else {
				notify.warningDetail();
			}
		} else {
			notify.warning("Chưa chọn trong danh sách!");
		}
	}


	public List<LoaiKyDanhGia> getLoaiKyDanhGias() {
		return loaiKyDanhGias;
	}

	public void setLoaiKyDanhGias(List<LoaiKyDanhGia> LoaiKyDanhGias) {
		this.loaiKyDanhGias = LoaiKyDanhGias;
	}

	public LoaiKyDanhGia getLoaiKyDanhGia() {
		return loaiKyDanhGia;
	}

	public void setLoaiKyDanhGia(LoaiKyDanhGia LoaiKyDanhGia) {
		this.loaiKyDanhGia = LoaiKyDanhGia;
	}

	public LoaiKyDanhGia getLoaiKyDanhGiaEdit() {
		return loaiKyDanhGiaEdit;
	}

	public void setLoaiKyDanhGiaEdit(LoaiKyDanhGia LoaiKyDanhGiaEdit) {
		this.loaiKyDanhGiaEdit = LoaiKyDanhGiaEdit;
	}

	
}
