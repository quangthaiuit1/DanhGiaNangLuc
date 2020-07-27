package trong.lixco.com.ejb.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import trong.lixco.com.jpa.entity.ChiTietKyDanhGia;
import trong.lixco.com.jpa.entity.KyDanhGia;
import trong.lixco.com.jpa.entity.NangLuc;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ChiTietKyDanhGiaService extends AbstractService<ChiTietKyDanhGia> {
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
	protected Class<ChiTietKyDanhGia> getEntityClass() {
		return ChiTietKyDanhGia.class;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean saveOrUpdate(List<ChiTietKyDanhGia> cts) {
		try {
			Query query = em
					.createQuery("DELETE FROM ChiTietKyDanhGia WHERE KyDanhGia_id = :id AND  codeDep = :codeDep");
			query.setParameter("id", cts.get(0).getKyDanhGia().getId());
			query.setParameter("codeDep", cts.get(0).getCodeDep());
			query.executeUpdate();
			for (int i = 0; i < cts.size(); i++) {
				em.persist(cts.get(i));
			}
			em.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			ct.setRollbackOnly();
			return false;
		}
	}

	public ChiTietKyDanhGia findRange(long kyDanhGia_id, String chucdanh, long nangluc_id) {
		try {
			if (kyDanhGia_id != 0 && chucdanh != null && nangluc_id != 0) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ChiTietKyDanhGia> cq = cb.createQuery(ChiTietKyDanhGia.class);
				Root<ChiTietKyDanhGia> root = cq.from(ChiTietKyDanhGia.class);
				cq.select(root)
						.where(cb.equal(root.get("kyDanhGia").get("id"), kyDanhGia_id),
								cb.equal(root.get("mavitri"), chucdanh),
								cb.equal(root.get("nangLuc").get("id"), nangluc_id))
						.orderBy(cb.asc(root.get("nangLuc").get("nhomNangLuc").get("ma")),
								cb.asc(root.get("nangLuc").get("ma")));
				return em.createQuery(cq).getSingleResult();
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public List<ChiTietKyDanhGia> findRange(long kyDanhGia_id, String chucdanh) {
		try {
			if (kyDanhGia_id != 0 && chucdanh != null) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ChiTietKyDanhGia> cq = cb.createQuery(ChiTietKyDanhGia.class);
				Root<ChiTietKyDanhGia> root = cq.from(ChiTietKyDanhGia.class);
				cq.select(root)
						.where(cb.equal(root.get("kyDanhGia").get("id"), kyDanhGia_id),
								cb.equal(root.get("mavitri"), chucdanh))
						.orderBy(cb.asc(root.get("nangLuc").get("nhomNangLuc").get("ma")),
								cb.asc(root.get("nangLuc").get("ma")));
				return em.createQuery(cq).getResultList();
			} else {
				return new ArrayList<ChiTietKyDanhGia>();
			}
		} catch (Exception e) {
			return new ArrayList<ChiTietKyDanhGia>();
		}
	}
	

}
