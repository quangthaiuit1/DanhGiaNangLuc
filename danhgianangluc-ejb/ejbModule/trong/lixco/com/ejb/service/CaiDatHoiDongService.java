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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import trong.lixco.com.jpa.entity.CaiDatHoiDong;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaiDatHoiDongService extends AbstractService<CaiDatHoiDong> {
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
	protected Class<CaiDatHoiDong> getEntityClass() {
		return CaiDatHoiDong.class;
	}

	public CaiDatHoiDong taidulieu(String manhanvien) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CaiDatHoiDong> cq = cb.createQuery(CaiDatHoiDong.class);
			List<Predicate> predicates = new LinkedList<Predicate>();
			Root<CaiDatHoiDong> root = cq.from(CaiDatHoiDong.class);
			predicates.add(cb.equal(root.get("manhanvien"), manhanvien));
			cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
			List<CaiDatHoiDong> datas = em.createQuery(cq).setMaxResults(1).getResultList();
			if (datas.size() != 0)
				return datas.get(0);
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	// thai
	public List<CaiDatHoiDong> find(String departmentCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CaiDatHoiDong> cq = cb.createQuery(CaiDatHoiDong.class);
		Root<CaiDatHoiDong> root = cq.from(CaiDatHoiDong.class);
		List<Predicate> queries = new ArrayList<>();
		if (departmentCode != null) {
			departmentCode = "%" + departmentCode + "%";
			Predicate employeeCodeQuery = cb.like(root.get("phongban"), departmentCode);
			queries.add(employeeCodeQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<CaiDatHoiDong> query = em.createQuery(cq);
		List<CaiDatHoiDong> abc = query.getResultList();
		if (!abc.isEmpty()) {
			return abc;
		} else {
			return new ArrayList<>();
		}

	}
	// end thai
}
