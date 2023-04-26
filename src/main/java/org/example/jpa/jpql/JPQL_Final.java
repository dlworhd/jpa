package org.example.jpa.jpql;

import org.example.jpa.model.p1.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JPQL_Final {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
//
//		// #1 엔티티를 직접 사용할 수 있다.
//		Long result1 = em.createQuery("SELECT COUNT(m.id) FROM Member m", Long.class).getSingleResult();
//		System.out.println(result1);
//		// #2 엔티티를 직접 사용할 수 있다.
//		Long result2 = em.createQuery("SELECT COUNT(m) FROM Member m", Long.class).getSingleResult();
//		System.out.println(result2);
//
//
//		// #3 Set Parameter 가능
//		String username = "TEST2";
//		List<Member> resultList1 = em.createQuery("SELECT m FROM Member m WHERE m.name = :username", Member.class)
//				.setParameter("username", username)
//				.getResultList();
//
//		for (Member member : resultList1) {
//			System.out.println(member.getId());
//		}
//
//		// #4 Set Parameter 가능 - 식별자도 가능
//		Long userId = 5L;
//		List<Member> resultList2 = em.createQuery("SELECT m FROM Member m WHERE m.id = :userId", Member.class)
//				.setParameter("userId", userId)
//				.getResultList();
//
//		for (Member member : resultList2) {
//			System.out.println(member.getName());
//		}
//
//		// #5 Member Class 확인해보기 - @NamedQuery 추가 + em.create**Name**Query() 사용 주의
//		List<String> findUsername = em.createNamedQuery("Member.findByUsername", String.class).setParameter("username", username).getResultList();
//		System.out.println(findUsername);
//

		/**
		 * 주의해야 할 점은 영속성 컨텍스트를 무시하고 DB를 먼저 실행한 후에 영속성 컨텍스트 초기화한다는 점이다.
		 */
		// #6 벌크 연산 - 쿼리 한 번으로 UPDATE/DELETE 가능, 하이버네이트에서는 Insert into도 가능
		em.createQuery("UPDATE Member m SET m.age = m.age + 10 WHERE m.age > :customAge")
				.setParameter("customAge", 10)
				.executeUpdate();

		transaction.commit();
		em.close();
		emf.close();
	}
}
