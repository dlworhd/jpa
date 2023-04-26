package org.example.jpa.jpql;

import org.example.jpa.model.p1.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class JPQL_EX {
	public static void main(String[] args) {
		// member.toString()할 때 team.name도 같이 출력되기 때문에, JOIN FETCH 사용

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
		EntityManager em = emf.createEntityManager();

		//like
		List<Member> membersLike = em.createQuery("SELECT m FROM Member m JOIN FETCH m.team WHERE  m.name like '%1'", Member.class).getResultList();
		for (Member member : membersLike) {
			System.out.println(member.toString());
		}

		List<Member> membersAge = em.createQuery("SELECT m FROM Member m JOIN FETCH m.team WHERE  m.age > 18", Member.class).getResultList();
		for (Member member : membersAge) {
			System.out.println(member.toString());
		}

		TypedQuery<Long> query = em.createQuery("SELECT COUNT(m) FROM Member m WHERE m.age > 18", Long.class);
		Long singleResult = query.getSingleResult();
		System.out.println(singleResult);

		List<Member> memberRetrieve = em.createQuery("SELECT m FROM Member m JOIN FETCH m.team WHERE m.name = :username", Member.class)
				.setParameter("username", "TEST4").getResultList();
		if(memberRetrieve.size() > 0){
			for (Member member : memberRetrieve) {
				System.out.println(member.toString());
			}
		}

		List<Address> addresses = em.createQuery("SELECT m.address FROM Member m", Address.class).getResultList();

		for (Address address : addresses) {
			System.out.println(address.toString());
		}

		List<MemberDto> resultList = em.createQuery("SELECT new org.example.jpa.model.p1.MemberDto(m.name, m.age) FROM Member m", MemberDto.class).getResultList();

		for (MemberDto memberDto : resultList) {
			System.out.println(memberDto.toString());
		}

		List<MemberDto> resultListPaging = em.createQuery("SELECT new org.example.jpa.model.p1.MemberDto(m.name, m.age) FROM Member m", MemberDto.class)
				.setFirstResult(0)
				.setMaxResults(10)
				.getResultList();

		for (MemberDto memberDto : resultListPaging) {
			System.out.println(memberDto.toString());
		}


		/**
		 * Join ON절
		 * 1. 조인 **대상** 필터링 (WHERE절은 Join 끝나고 나서 설정하는 듯?)
		 * 2. 연관 관계 없는 Entity 외부 조인
		 */

		System.out.println("---------------------------------------------------------");

		// #1
		List<Member> resultList1 = em.createQuery("SELECT m FROM Member m LEFT JOIN  m.team t ON t.name = 'Team07'", Member.class).getResultList();
		for (Member member : resultList1) {
			System.out.println(member.toString());
		}
		System.out.println("---------------------------------------------------------");

		// #2
		List<Object[]> resultList2 = em.createQuery("SELECT m, t FROM Member m Join Team t ON t.name = m.name", Object[].class)
				.getResultList();

		for (Object[] objects : resultList2) {
			Member member = (Member) objects[0];
			Team team = (Team) objects[1];

			System.out.println(member.toString());
			System.out.println(team.toString());
		}
		System.out.println("---------------------------------------------------------");

		// #3
		List<Member> resultList3 = em.createQuery("SELECT m FROM Member m WHERE m.age > (SELECT AVG(m.age) FROM Member m)", Member.class).getResultList();

		for (Member member : resultList3) {
			System.out.println(member.toString());
		}

		System.out.println("---------------------------------------------------------");

//		// #4 Order 만들면 써보기
//		List<Member> resultList4 = em.createQuery("SELECT m FROM Member m WHERE (SELECT COUNT(o) FROM Order o WHERE m = o.member) > 0", Member.class).getResultList();
//		for (Member member : resultList4) {
//			System.out.println(member.toString());
//		}
//		System.out.println("---------------------------------------------------------");

		// #5
		List<Member> resultList5 = em.createQuery("SELECT m FROM Member m WHERE m.team = ANY(SELECT t FROM Team t)", Member.class).getResultList();
		for (Member member : resultList5) {
			System.out.println(member.toString());
		}

		System.out.println("---------------------------------------------------------");


		/**
		 * 아래의 #6과 #7의 차이가 뭘까 생각해봤다.
		 * 내가 exists를 잘 몰랐기 때문이다. 확실하게 정리해보려고 한다.
		 * #6번 쿼리는 이러한 의미를 가지고 있다. "너가 서브쿼리를 실행하고 나서 서브쿼리에 결과가 존재하면 조건은 참이 된다.
		 * "즉, 참이 되는 Member는 다 가져온다."라는 뜻이 되는데, 잘 이해해야 한다.
		 * 1. 서브 쿼리의 SELECT문이 참이 되면 2. Member를 가져오게 된다.
		 * #6번 쿼리를 다시 보자. Team을 다 가져와서 Team이름을 순회한다. Team이름이 "Team01"인 것만!
		 * 그런데? 어쨋든 하나라도 존재하기 때문에 결과는 참이 된다. 왜냐하면 Team은 Team01~Team10까지 실제 존재하기 때문
		 * 그래서 모든 Member들이 다 출력이 되는 문제가 발생한다.
		 * 이러한 문제를 막기 위해 #7번처럼 쿼리를 짠다.
		 * 1. Member의 하나뿐인 Team을 가져와서 그 Member의 소속팀이 Team01이여야만 서브쿼리가 무조건 참이 아닌, 참과 거짓으로 결과를 가져오게 된다.
		 * 그래서 모든 멤버를 다 출력하는 게 아니다. 왜냐하면 서브 쿼리가 거짓일 때도 있기 때문에!
		 * 이러한 문제점을 잘 캐치해야 한다. 쉽게 요약하자면, 서브 쿼리가 항상 참이 나오는지, 아니면 참과 거짓 구분해서 나오는지를 잘 파악하라는 것이다.
		 *
		 * 아래는 GPT에게 위의 내용 그대로 질문했을 때 돌아오는 답변
		 *
		 * 예, 맞습니다. 첫 번째 쿼리는 'Team01'이라는 이름을 가진 팀이 있는지 확인하고 해당 이름을 가진 팀이 하나 이상 있으므로 쿼리는 모든 구성원을 반환합니다.
		 * 반면 두 번째 쿼리는 각 구성원이 'Team01'이라는 이름의 팀을 가지고 있는지 확인하고 해당 팀을 가진 구성원만 반환합니다.
		 * 따라서 'Team01'에 속하지 않은 구성원이 있으면 결과 집합에 포함되지 않습니다. 그렇기 때문에 이 두 쿼리의 출력이 다를 수 있습니다.
		 *
		 */

		// #6
		List<Member> resultList6 = em.createQuery("SELECT m FROM Member m WHERE exists (SELECT t FROM Team t WHERE t.name = 'Team01')", Member.class).getResultList();
		for (Member member : resultList6) {
			System.out.println(member.toString());
		}

		// #7(#6과 비교 -> Team에서 m.team으로 변경)
		List<Member> resultList7 = em.createQuery("SELECT m FROM Member m WHERE exists (SELECT t FROM m.team t WHERE t.name = 'Team01')", Member.class).getResultList();
		for (Member member : resultList7) {
			System.out.println(member.toString());
		}

		// #8
		List<String> resultList8 = em.createQuery("SELECT CASE t.name " +
				"WHEN 'Team01' THEN '인센티브 10%' " +
				"WHEN 'Team02' THEN '인센티브 20%' " +
				"END " +
				"FROM Team t", String.class).getResultList();

		for (String team : resultList8) {
			System.out.println(team);
		}

		// #9
		List<String> resultList9 = em.createQuery("SELECT coalesce(m.name, '이름 없음') FROM Member m", String.class).getResultList();

		for (String s : resultList9) {
			System.out.println(s);
		}

		// #10
		List<String> resultList10 = em.createQuery("SELECT nullif(m.name, 'TEST4') FROM Member m", String.class).getResultList();

		for (String s : resultList10) {
			System.out.println(s);
		}


		/**
		 * 경로를 탐색할 때, m.name 이런 식으로 상태 필드에 접근할 수 있고,
		 * member.team(단일 값)이나 team.members(컬렉션 값)처럼 Entity를 탐색할 수도 있음
		 * 단일 값과 컬렉션의 경우 묵시적 조인이 발생하며, 명시적 조인을 사용해서도 Join을 할 수 있다.
		 * 단일 값은 탐색 가능하고, 컬렉션 값은 탐색이 불가능하다. team.members.xxx이런 게 불가능(다만 From절에서 별칭을 얻으면 가능)
		 * 묵시적 조인은 Inner Join만 가능하고, 명시적 조인은 Outer Join이 가능하다
		 *
		 * TODO: 가급적이면 실무에서 명시적 조인을 사용할 것 -> 묵시적 조인은 파악하기 어려움
		 */

		em.clear();

		// #11 쿼리 확인 -> inner join 발생 (묵시적 조인) -> Outer Join도 가능
		List<Team> resultList11 = em.createQuery("SELECT m.team FROM Member m", Team.class).getResultList();
		for (Team team : resultList11) {
			System.out.println(team);
		}


		// #12 쿼리 확인 -> inner join 발생 (묵시적 조인) -> 탐색을 하기 위해서는 명시적 조인을 사용하고, 별칭을 얻어야 한다. #13에서 확인
		Collection resultList12 = em.createQuery("SELECT t.members FROM Team t", Object.class).getResultList();

		for (Object object : resultList12) {
			Member member = (Member) object;
			System.out.println(member.toString());
		}


		// #13 쿼리 확인 -> 명시적 조인으로 컬렉션 값(team.members) 탐색 가능 (tm.name)
		List<String> resultList13 = em.createQuery("SELECT tm.name FROM Team t JOIN t.members tm ", String.class).getResultList();

		for (String name : resultList13) {
			System.out.println(name);
		}

		em.clear();
	}
}
