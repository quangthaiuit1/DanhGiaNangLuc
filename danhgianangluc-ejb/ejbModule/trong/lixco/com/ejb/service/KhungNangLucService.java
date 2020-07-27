package trong.lixco.com.ejb.service;

import java.util.ArrayList;
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

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class KhungNangLucService extends AbstractService<KhungNangLuc> {
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
	protected Class<KhungNangLuc> getEntityClass() {
		return KhungNangLuc.class;
	}

	public List<KhungNangLuc> findByPos(String code) {
		if (code != null) {
			try {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<KhungNangLuc> cq = cb.createQuery(KhungNangLuc.class);
				List<Predicate> predicates = new LinkedList<Predicate>();
				Root<KhungNangLuc> root = cq.from(KhungNangLuc.class);
				predicates.add(cb.equal(root.get("mavitri"), code));
				cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
				return em.createQuery(cq).getResultList();
			} catch (Exception e) {
				return new ArrayList<KhungNangLuc>();
			}
		} else {
			return new ArrayList<KhungNangLuc>();
		}
	}

	public KhungNangLuc findByPosNangLuc(String code, long nangluc_id) {
		if (code != null && nangluc_id != 0) {
			try {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<KhungNangLuc> cq = cb.createQuery(KhungNangLuc.class);
				List<Predicate> predicates = new LinkedList<Predicate>();
				Root<KhungNangLuc> root = cq.from(KhungNangLuc.class);
				predicates.add(cb.equal(root.get("mavitri"), code));
				predicates.add(cb.equal(root.get("nangLuc").get("id"), nangluc_id));
				predicates.add(cb.greaterThan(root.get("trongsonhom"), 0));
				cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
				return em.createQuery(cq).getSingleResult();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	public KhungNangLuc taikhungnangluc(String code, long nangluc_id) {
		if (code != null && nangluc_id != 0) {
			try {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<KhungNangLuc> cq = cb.createQuery(KhungNangLuc.class);
				List<Predicate> predicates = new LinkedList<Predicate>();
				Root<KhungNangLuc> root = cq.from(KhungNangLuc.class);
				predicates.add(cb.equal(root.get("mavitri"), code));
				predicates.add(cb.equal(root.get("nangLuc").get("id"), nangluc_id));
				cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
				List<KhungNangLuc> datas = em.createQuery(cq).setMaxResults(1).getResultList();
				if (datas.size() != 0)
					return datas.get(0);
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

}
