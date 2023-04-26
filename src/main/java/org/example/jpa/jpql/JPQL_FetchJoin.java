package org.example.jpa.jpql;

import org.example.jpa.model.p1.Member;
import org.example.jpa.model.p1.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JPQL_FetchJoin {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
/**
 * Fetch Join 특징
 * 1. Fetch Join에 별칭을 부여할 순 x 하이버네이트는 가능 그러나 웬만하면 사용 x
 * 2. 컬렉션 값을 Fetch Join하는 경우 페이징 API를 사용할 수 없음, @ToOne을 Fetch Join하는 경우는 가능
 * 3. Fetch 전략을 무시한다.
 * 4. 최적화가 필요한 곳에는 Fetch Join 적용
 * 5. 객체 그래프를 유지할 때 사용하면 좋음
 * But, 모든 것을 Fetch Join으로 해결할 수는 없음,
 * 여러 테이블을 조인해서 전혀 다른 결과를 내야 하는 경우에는 일반 조인해서 필요한 데이터만
 * DTO타입으로 반환하는 게 좋을 수도 있음
 *
 *
 */

//		// #1 Fetch Join 하기 전 -> 쿼리 N+1 문제 발생
//		List<Member> resultList1 = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
//
//
//		for (Member member : resultList1) {
//			Team team = member.getTeam();
//			System.out.println("------------------------------------");
//			System.out.println(team.getName());
//		}

		// #2 Fetch Join 적용 후 -> N+1 문제 해결
		List<Member> resultList2 = em.createQuery("SELECT m, mt FROM Member m join fetch m.team mt", Member.class).getResultList();


		for (Member member : resultList2) {
			Team team = member.getTeam();
			System.out.println("------------------------------------");
			System.out.println(team.getName());
		}


		// #3
		/**
		 *  DISTINCT로 Entity 중복 제거하기 -> Why?
		 *  Join이 된 테이블은 각 로우가 다 다른 데이터라고 본다.
		 *  그래서 Team01 멤버인 TEST1와 TEST2가 로우 2개를 차지하는데, 같은 Team이지만 각각 다른 데이터라고 보기에
		 *  중복되는 TEAM Data를 제거할 수 없다.
		 *  그래서 애플리케이션으로 가져와서 JPQL이 제공하는 DISTINCT를 사용하면,
		 *  Entity 중복이 제거되어 하나의 참조 변수만 사용하고, 메모리 낭비를 줄일 수 있다.
		 *  제거하지 않는다면, 똑같은 Team01을 두 개의 변수가 참조하게 되어 메모리의 낭비를 가져올 수 있음
		 */

		em.clear();

		List<Member> resultList3 = em.createQuery("SELECT DISTINCT m FROM Member m JOIN FETCH m.team", Member.class).getResultList();

		for (Member member : resultList3) {
			System.out.println(member.getTeam());
		}


		transaction.commit();
		em.close();
		emf.close();

	}

}
