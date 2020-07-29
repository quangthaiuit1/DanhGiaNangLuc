package trong.lixco.com.ejb.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import trong.lixco.com.jpa.dto.BaoCaoTongHopDTO;
import trong.lixco.com.jpa.dto.BaoCaoTuyChonDTO;
import trong.lixco.com.jpa.entity.KetQuaDanhGia;
import trong.lixco.com.jpa.entity.KyDanhGia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class KetQuaDanhGiaService extends AbstractService<KetQuaDanhGia> {
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
	protected Class<KetQuaDanhGia> getEntityClass() {
		return KetQuaDanhGia.class;
	}

	public boolean saveOrUpdate(List<KetQuaDanhGia> cts) {
		try {
			for (int i = 0; i < cts.size(); i++) {
				em.persist(cts.get(i));
			}
			return true;
		} catch (Exception e) {
			System.out.println("loi khi luu danh gia nang luc");
			e.printStackTrace();
			return false;
		}
	}

//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean update(List<KetQuaDanhGia> cts) {
		try {
			for (int i = 0; i < cts.size(); i++) {
				if (cts.get(i).getId() != null) {
					em.merge(cts.get(i));
				} else {
					em.persist(cts.get(i));
				}
			}
//			em.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
//			ct.setRollbackOnly();
			return false;
		}
	}

	public KetQuaDanhGia ketquadanhgianhanvien(long kyDanhGia_id, String manv, long idnangluc) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<KetQuaDanhGia> cq = cb.createQuery(KetQuaDanhGia.class);
			Root<KetQuaDanhGia> root = cq.from(KetQuaDanhGia.class);
			cq.select(root).where(cb.equal(root.get("KyDanhGia").get("id"), kyDanhGia_id),
					cb.equal(root.get("nangLuc").get("id"), idnangluc), cb.equal(root.get("manhanvien"), manv));
			List<KetQuaDanhGia> kqs= em.createQuery(cq).setMaxResults(1).getResultList();
			if(kqs.size()!=0){
				return kqs.get(0);
			}else{
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public List<BaoCaoTongHopDTO> baocaotonghop(long kyDanhGia_id, int soluongnangluc, List<String> manv,
			String loaidanhgia) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BaoCaoTongHopDTO> cq = cb.createQuery(BaoCaoTongHopDTO.class);
			Root<KetQuaDanhGia> root = cq.from(KetQuaDanhGia.class);
			List<Predicate> predicates = new LinkedList<Predicate>();
			predicates.add(cb.equal(root.get("KyDanhGia").get("id"), kyDanhGia_id));
			if (manv.size() != 0)
				predicates.add(cb.in(root.get("manhanvien")).value(manv));
			if ("daotao".equals(loaidanhgia)) {
				predicates.add(cb.lessThan(root.get("ketqua"), 100));
			} else if ("tuyenduong".equals(loaidanhgia)) {
				predicates.add(cb.greaterThan(root.get("ketqua"), 100));
			}
			cq.select(
					cb.construct(BaoCaoTongHopDTO.class, root.get("KyDanhGia"), root.get("nangLuc"),
							cb.count(root.get("id")).alias("countid")))
					.where(cb.and(predicates.toArray(new Predicate[0])))
					.groupBy(root.get("KyDanhGia"), root.get("nangLuc")).orderBy(cb.desc(cb.count(root.get("id"))));
			List<BaoCaoTongHopDTO> datas = em.createQuery(cq).setMaxResults(soluongnangluc).getResultList();
			return datas;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<BaoCaoTongHopDTO>();
		}
	}

	public List<BaoCaoTuyChonDTO> baocaotuychon(long kyDanhGia_id, List<String> manv, String manl, String ketqua) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BaoCaoTuyChonDTO> cq = cb.createQuery(BaoCaoTuyChonDTO.class);
			Root<KetQuaDanhGia> root = cq.from(KetQuaDanhGia.class);
			List<Predicate> predicates = new LinkedList<Predicate>();
			predicates.add(cb.equal(root.get("KyDanhGia").get("id"), kyDanhGia_id));
			predicates.add(cb.notEqual(root.get("diemdat"), 0));
			if (manv.size() != 0)
				predicates.add(cb.in(root.get("manhanvien")).value(manv));
			if (manl != null) {
				predicates.add(cb.equal(root.get("nangLuc").get("ma"), manl));
			}
			if ("khongdat".equals(ketqua)) {
				predicates.add(cb.lessThan(root.get("tongdiem"), 100));
			} else if ("dat".equals(ketqua)) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("tongdiem"), 100));
			}
//			predicates.add(cb.greaterThan(root.get("tongdiem"), 0));
			cq.select(
					cb.construct(BaoCaoTuyChonDTO.class, root.get("KyDanhGia"), root.get("manhanvien"),
							root.get("tongdiem"),root.get("machucdanh"))).where(cb.and(predicates.toArray(new Predicate[0]))).groupBy(root.get("manhanvien"));
			List<BaoCaoTuyChonDTO> datas = em.createQuery(cq).getResultList();
			return datas;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<BaoCaoTuyChonDTO>();
		}
	}

	public boolean findRange(long kyDanhGia_id, String manv) {
		try {
			if (kyDanhGia_id != 0 && manv != null) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<KetQuaDanhGia> cq = cb.createQuery(KetQuaDanhGia.class);
				Root<KetQuaDanhGia> root = cq.from(KetQuaDanhGia.class);
				cq.select(root).where(cb.equal(root.get("KyDanhGia").get("id"), kyDanhGia_id),
						cb.equal(root.get("manhanvien"), manv));
				if (em.createQuery(cq).getResultList().size() != 0)
					return true;
				return false;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean kiemtradanhgianv(long kyDanhGia_id, String manv) {
		try {
			if (kyDanhGia_id != 0 && manv != null) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<KetQuaDanhGia> cq = cb.createQuery(KetQuaDanhGia.class);
				Root<KetQuaDanhGia> root = cq.from(KetQuaDanhGia.class);
				cq.select(root)
						.where(cb.notEqual(root.get("diem"), 0),
								cb.equal(root.get("KyDanhGia").get("id"), kyDanhGia_id),
								cb.equal(root.get("manhanvien"), manv));
				if (em.createQuery(cq).getResultList().size() != 0)
					return true;
				return false;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean kiemtradanhgiaql(long kyDanhGia_id, String manv) {
		try {
			if (kyDanhGia_id != 0 && manv != null) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<KetQuaDanhGia> cq = cb.createQuery(KetQuaDanhGia.class);
				Root<KetQuaDanhGia> root = cq.from(KetQuaDanhGia.class);
				cq.select(root)
						.where(cb.notEqual(root.get("diemql"), 0),
								cb.equal(root.get("KyDanhGia").get("id"), kyDanhGia_id),
								cb.equal(root.get("manhanvien"), manv));
				if (em.createQuery(cq).setMaxResults(1).getResultList().size() != 0)
					return true;
				return false;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean kiemtradanhgiahd(long kyDanhGia_id, String manv) {
		try {
			if (kyDanhGia_id != 0 && manv != null) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<KetQuaDanhGia> cq = cb.createQuery(KetQuaDanhGia.class);
				Root<KetQuaDanhGia> root = cq.from(KetQuaDanhGia.class);
				cq.select(root)
						.where(cb.notEqual(root.get("diemhd"), 0),
								cb.equal(root.get("KyDanhGia").get("id"), kyDanhGia_id),
								cb.equal(root.get("manhanvien"), manv));
				if (em.createQuery(cq).getResultList().size() != 0)
					return true;
				return false;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Thai
	public List<KetQuaDanhGia> findByResultLessThan(int result,KyDanhGia kyDanhGia){
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<KetQuaDanhGia> cq = cb.createQuery(KetQuaDanhGia.class);
			Root<KetQuaDanhGia> root = cq.from(KetQuaDanhGia.class);
			List<Predicate> queries = new ArrayList<>();
			if(result != 0) {
				Predicate resultQuery = cb.lessThan(root.get("ketqua"),result);
				queries.add(resultQuery);
			}
			if(result != 0) {
				Predicate resultQueryGreater = cb.greaterThan(root.get("ketqua"),0);
				queries.add(resultQueryGreater);
			}
			if(kyDanhGia != null) {
				Predicate resultQuery = cb.equal(root.get("KyDanhGia"),kyDanhGia);
				queries.add(resultQuery);
			}
			
			Predicate data[] = new Predicate[queries.size()];
			for (int i = 0; i < queries.size(); i++) {
				data[i] = queries.get(i);
			}
			Predicate finalPredicate = cb.and(data);
			cq.where(finalPredicate);
			TypedQuery<KetQuaDanhGia> query = em.createQuery(cq);
			List<KetQuaDanhGia> results = query.getResultList();
			return results;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<KetQuaDanhGia>();
		}
			
	}
	
	public List<KetQuaDanhGia> findByResultGreaterThan(int result,KyDanhGia kyDanhGia){
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<KetQuaDanhGia> cq = cb.createQuery(KetQuaDanhGia.class);
			Root<KetQuaDanhGia> root = cq.from(KetQuaDanhGia.class);
			List<Predicate> queries = new ArrayList<>();
			if(result != 0) {
				Predicate resultQuery = cb.greaterThan(root.get("ketqua"),result);
				queries.add(resultQuery);
			}
			if(kyDanhGia != null) {
				Predicate resultQuery = cb.equal(root.get("KyDanhGia"),kyDanhGia);
				queries.add(resultQuery);
			}
			
			Predicate data[] = new Predicate[queries.size()];
			for (int i = 0; i < queries.size(); i++) {
				data[i] = queries.get(i);
			}
			Predicate finalPredicate = cb.and(data);
			cq.where(finalPredicate);
			TypedQuery<KetQuaDanhGia> query = em.createQuery(cq);
			List<KetQuaDanhGia> results = query.getResultList();
			return results;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<KetQuaDanhGia>();
		}
			
	}
	//End Thai

}
