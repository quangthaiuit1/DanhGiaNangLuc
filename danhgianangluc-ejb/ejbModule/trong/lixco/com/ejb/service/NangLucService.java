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

import trong.lixco.com.jpa.entity.ChiTietNangLuc;
import trong.lixco.com.jpa.entity.NangLuc;
import trong.lixco.com.jpa.entity.NhomNangLuc;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NangLucService extends AbstractService<NangLuc> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;
	@Inject
	NhomNangLucService nhomNangLucService;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	@Override
	protected Class<NangLuc> getEntityClass() {
		return NangLuc.class;
	}

	public NangLuc findByIdAll(long id) {
		try {
			NangLuc nangLuc = em.find(NangLuc.class, id);
			nangLuc.getChiTietNangLucs().size();
			return nangLuc;
		} catch (Exception e) {
			return null;
		}

	}

	public NangLuc findByCode(String ma) {
		if (ma != null) {
			try {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<NangLuc> cq = cb.createQuery(NangLuc.class);
				List<Predicate> predicates = new LinkedList<Predicate>();
				Root<NangLuc> root = cq.from(NangLuc.class);
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

	public NangLuc findByCode(String ma, String codeDep) {
		if (ma != null) {
			try {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<NangLuc> cq = cb.createQuery(NangLuc.class);
				List<Predicate> predicates = new LinkedList<Predicate>();
				Root<NangLuc> root = cq.from(NangLuc.class);
				predicates.add(cb.equal(root.get("ma"), ma));
				predicates.add(cb.equal(root.get("codeDep"), codeDep));
				cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
				return em.createQuery(cq).getSingleResult();
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	public List<NangLuc> findDepartment(String code) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<NangLuc> cq = cb.createQuery(NangLuc.class);
			List<Predicate> predicates = new LinkedList<Predicate>();
			Root<NangLuc> root = cq.from(NangLuc.class);
			if (code == null || "".equals(code)) {
				predicates.add(cb.isNull(root.get("codeDep")));
			} else {
				predicates.add(cb.equal(root.get("codeDep"), code));
			}
			predicates.add(cb.equal(root.get("nhomNangLuc").get("nangluccongty"), true));
			cq.select(root).where(cb.or(predicates.toArray(new Predicate[0])))
					.orderBy(cb.asc(root.get("nhomNangLuc").get("ma")), cb.asc(root.get("ma")));
			return em.createQuery(cq).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public void luunangluctuexcel(List<NangLuc> nangLucs) {
		for (int i = 0; i < nangLucs.size(); i++) {
			try {
				NangLuc nl = nangLucs.get(i);
				NangLuc nlDataBase = findByCode(nangLucs.get(i).getMa());
				if (nlDataBase == null) {
					String manhom = nl.getMa().substring(0, 1);
					NhomNangLuc nhomnl = nhomNangLucService.findByCode(manhom);
					if (nhomnl != null) {
						nl.setNhomNangLuc(nhomnl);
						List<ChiTietNangLuc> ctnls = nl.getChiTietNangLucs();
						nl.setChiTietNangLucs(null);
						em.persist(nl);
						for (int j = 0; j < ctnls.size(); j++) {
							ChiTietNangLuc ct = ctnls.get(j);
							ct.setNangLuc(nl);
							em.persist(ct);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void luunanglucchuyenmontuexcel(String codeDep, List<NangLuc> nangLucs) {
		for (int i = 0; i < nangLucs.size(); i++) {
			try {
				NangLuc nl = nangLucs.get(i);
				if (codeDep != null) {
					NangLuc nlDataBase = findByCode(nangLucs.get(i).getMa(), codeDep);
					if (nlDataBase == null) {
						String manhom = nl.getMa().substring(0, 1);
						if ("D".equals(manhom)) {
							NhomNangLuc nhomnl = nhomNangLucService.findByCode(manhom);
							if (nhomnl != null) {
								nl.setCodeDep(codeDep);
								nl.setNhomNangLuc(nhomnl);
								List<ChiTietNangLuc> ctnls = nl.getChiTietNangLucs();
								nl.setChiTietNangLucs(null);
								em.persist(nl);
								for (int j = 0; j < ctnls.size(); j++) {
									ChiTietNangLuc ct = ctnls.get(j);
									ct.setNangLuc(nl);
									em.persist(ct);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void capnhat(NangLuc nangluc) {
		try {
			List<ChiTietNangLuc> ctnls = nangluc.getChiTietNangLucs();
			nangluc.setChiTietNangLucs(null);
			em.equals(nangluc);
			for (int j = 0; j < ctnls.size(); j++) {
				ChiTietNangLuc ct = ctnls.get(j);
				ct.setNangLuc(nangluc);
				em.merge(ct);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
