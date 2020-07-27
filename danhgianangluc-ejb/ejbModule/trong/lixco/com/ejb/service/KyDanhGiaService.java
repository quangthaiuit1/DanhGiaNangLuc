package trong.lixco.com.ejb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import trong.lixco.com.jpa.entity.KhungNangLuc;
import trong.lixco.com.jpa.entity.KyDanhGia;
import trong.lixco.com.jpa.entity.LoaiKyDanhGia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class KyDanhGiaService extends AbstractService<KyDanhGia> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	@Override
	protected Class<KyDanhGia> getEntityClass() {
		return KyDanhGia.class;
	}

	public List<KyDanhGia> kiemtrakydanhgia(Date date, String codeEmp) {
		if (date != null) {
			try {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<KyDanhGia> cq = cb.createQuery(KyDanhGia.class);
				List<Predicate> predicates = new LinkedList<Predicate>();
				Root<KyDanhGia> root = cq.from(KyDanhGia.class);
				predicates.add(cb.lessThanOrEqualTo(root.get("ngaybatdau"), date));
				predicates.add(cb.greaterThanOrEqualTo(root.get("ngayketthuc"), date));
				predicates.add(cb.like(root.get("nhanviendanhgia"), "%" + codeEmp + "%"));
				cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
				return em.createQuery(cq).getResultList();
			} catch (Exception e) {
				return new ArrayList<KyDanhGia>();
			}
		} else {
			return new ArrayList<KyDanhGia>();
		}
	}

	public List<KyDanhGia> kiemtrakydanhgia(Date date) {
		if (date != null) {
			try {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<KyDanhGia> cq = cb.createQuery(KyDanhGia.class);
				List<Predicate> predicates = new LinkedList<Predicate>();
				Root<KyDanhGia> root = cq.from(KyDanhGia.class);
				predicates.add(cb.lessThanOrEqualTo(root.get("ngaybatdau"), date));
				predicates.add(cb.greaterThanOrEqualTo(root.get("ngayketthuc"), date));
				cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
				return em.createQuery(cq).getResultList();
			} catch (Exception e) {
				return new ArrayList<KyDanhGia>();
			}
		} else {
			return new ArrayList<KyDanhGia>();
		}
	}

	public List<KyDanhGia> findRange(LoaiKyDanhGia loaiKyDanhGia) {
		if (loaiKyDanhGia != null) {
			try {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<KyDanhGia> cq = cb.createQuery(KyDanhGia.class);
				List<Predicate> predicates = new LinkedList<Predicate>();
				Root<KyDanhGia> root = cq.from(KyDanhGia.class);
				predicates.add(cb.equal(root.get("loaiKyDanhGia"), loaiKyDanhGia));
				cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
				return em.createQuery(cq).getResultList();
			} catch (Exception e) {
				return new ArrayList<KyDanhGia>();
			}
		} else {
			return new ArrayList<KyDanhGia>();
		}
	}
}
