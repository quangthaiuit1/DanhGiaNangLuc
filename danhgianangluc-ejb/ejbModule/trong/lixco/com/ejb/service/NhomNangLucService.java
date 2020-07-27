package trong.lixco.com.ejb.service;

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

import trong.lixco.com.jpa.entity.NangLuc;
import trong.lixco.com.jpa.entity.NhomNangLuc;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NhomNangLucService extends AbstractService<NhomNangLuc> {
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
	protected Class<NhomNangLuc> getEntityClass() {
		return NhomNangLuc.class;
	}
	public NhomNangLuc findByCode(String ma) {
		if (ma != null) {
			try {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<NhomNangLuc> cq = cb.createQuery(NhomNangLuc.class);
				List<Predicate> predicates = new LinkedList<Predicate>();
				Root<NhomNangLuc> root = cq.from(NhomNangLuc.class);
				predicates.add(cb.equal(root.get("ma"), ma));
				cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
				return em.createQuery(cq).getSingleResult();
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}
}
