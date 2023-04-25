package org.example.jpa;

import org.example.jpa.model.p1.Member;
import org.example.jpa.model.p1.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class Main {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
		EntityManager em = emf.createEntityManager();

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();


		em.clear();


		//------------------------------------------------------------------------------//

		/**
		 * 지연 로딩이 발생하는 경우(Member(N):Team(1)을 가정)
		 * Member를 가져올 때, Team은 프록시 객체로 가져옴
		 * Team team = Member.getTeam();을 할 때까진 쿼리 실행 안 함
		 * team.getName();을 하는 순간 실제 DB를 방문한 후에
		 * 1차 쿼리에 team에 대한 정보를 저장하고 team을 가져와서 name을 반환함
		 */

		List<Member> members  = em.createQuery("SELECT m FROM Member m join m.team", Member.class).getResultList();

		for (Member member : members) {
			Team team = member.getTeam();
			System.out.println(team.toString());
		}

		//------------------------------------------------------------------------------//
		transaction.commit();
		em.close();
		emf.close();
	}
}