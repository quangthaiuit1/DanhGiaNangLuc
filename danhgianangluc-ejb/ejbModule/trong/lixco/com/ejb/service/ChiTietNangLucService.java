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

import trong.lixco.com.jpa.entity.ChiTietNangLuc;
import trong.lixco.com.jpa.entity.NangLuc;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ChiTietNangLucService extends AbstractService<ChiTietNangLuc> {
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
	protected Class<ChiTietNangLuc> getEntityClass() {
		return ChiTietNangLuc.class;
	}

	public List<ChiTietNangLuc> findNangLuc(String ma, String codeDep) {
		try {
			if (ma != null || !"".equals(ma)) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ChiTietNangLuc> cq = cb.createQuery(ChiTietNangLuc.class);
				Root<ChiTietNangLuc> root = cq.from(ChiTietNangLuc.class);
				cq.select(root).where(
						cb.equal(root.get("nangLuc").get("ma"), ma),
						cb.or(cb.equal(root.get("nangLuc").get("codeDep"), codeDep),
								cb.isNull(root.get("nangLuc").get("codeDep"))));
				return em.createQuery(cq).getResultList();
			} else {
				return new ArrayList<ChiTietNangLuc>();
			}
		} catch (Exception e) {
			return new ArrayList<ChiTietNangLuc>();
		}
	}

	public List<ChiTietNangLuc> findSortNangLuc(long id) {
		try {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ChiTietNangLuc> cq = cb.createQuery(ChiTietNangLuc.class);
				Root<ChiTietNangLuc> root = cq.from(ChiTietNangLuc.class);
				cq.select(root)
						.where(cb.equal(root.get("nangLuc").get("id"), id))
						.orderBy(cb.asc(root.get("capdo")));
				return em.createQuery(cq).getResultList();
		
		} catch (Exception e) {
			return new ArrayList<ChiTietNangLuc>();
		}
	}
}
