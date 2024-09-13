package org.zerock.myapp;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.zerock.myapp.entity.Member;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JPAMain_1 {
	
	public static void main(String...args) {
		log.trace("main({}) invoked.", Arrays.toString(args));
		
		
		// Step1. 엔티티관리자를 생성할 수 있는 공장객체를 획듯
		// -- 엔티티 클래스의 매핑정보대로, DB에 테이블을 어떻게 누가 생성해주는걸까
		@Cleanup EntityManagerFactory emf = // 자원객체
				Persistence.createEntityManagerFactory("study-jpa-1");
		log.info("\tStep1. emf : {}", emf);
		
		// Step2. Step1. 에서 얻어낸 공장(Factory)으로부터, 엔티티관리자 획득
		@Cleanup EntityManager em = emf.createEntityManager(); // 자원객체
		log.info("\tStep2. em : {}", em);
		
		// Step3. Step2. 에서 얻어낸 엔티티관리자로부터 트랜잭션관리 객체를 획득
		EntityTransaction tx = em.getTransaction();
		log.info("\tStep3. tx : {}", tx);
		
		// Step4. Step3. 에서 얻어낸 엔티티 트랜잭션을 통해, 
		//		  트랜잭션을 시작합니다.
		tx.begin();		// 트랜잭션 시작
		
		JPAMain_1.businessLogic(em);

		// 이 트랜잭션의 시작과 종료 사이에서 우리가 하고 싶은, JPA작업을 하면 됩니다. -> 지금시점 : 회원테이블 생
		
		
		// StepN. Step4 에서 시작한 트랜잭션을 종료
		tx.commit();	// 트랜잭션 종료 with Commit
		
//		tx.rollback(); // 트랜잭션 종료 with Rollback
	} // main
	
	static void businessLogic(EntityManager em) {
		log.trace("businessLogic({}) invoked.", em);
		
		// TEST1. 엔티티에 새로운 튜플 생성
		//		  즉, 엔티티 클래스의 새로운 인스턴스 생성을 의미합니다.
		Member newMember1 = new Member();

		newMember1.setId("ID1");
		newMember1.setUserName("NAME_1");
		newMember1.setAge(23);
		
		// Entity Manager 를 통해서, CRUD 합니다.
		em.persist(newMember1);
		
		Member newMember2 = new Member();
		
		newMember2.setId("ID2");
		newMember2.setUserName("NAME_2");
		newMember2.setAge(24);
		
		// Entity Manager 를 통해서, CRUD 합니다.
		em.persist(newMember2);
		
		Member newMember3 = new Member();
		
		newMember3.setId("ID3");
		newMember3.setUserName("NAME_3");
		newMember3.setAge(25);
		
		// Entity Manager 를 통해서, CRUD 합니다.
		em.persist(newMember3);
		
		Member newMember4 = new Member();
		
		newMember4.setId("ID4");
		newMember4.setUserName("NAME_4");
		newMember4.setAge(26);
		
		// Entity Manager 를 통해서, CRUD 합니다.
		em.persist(newMember4);
		
		Member newMember5 = new Member();
		
		newMember5.setId("ID5");
		newMember5.setUserName("NAME_5");
		newMember5.setAge(27);
		
		// Entity Manager 를 통해서, CRUD 합니다.
		em.persist(newMember5);
		
	} // businessLogic
} // end class
