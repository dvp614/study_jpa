package org.zerock.myapp.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.zerock.myapp.util.PersistencesUnits;

import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQL1_MemberTests {
	private EntityManagerFactory emf;
	private EntityManager em;

	@BeforeAll
	void beforeAll() {
		log.trace("beforeAll() invoked.");

		this.emf = Persistence.createEntityManagerFactory(PersistencesUnits.MYSQL);
		assertNotNull(emf);
		
		this.em = this.emf.createEntityManager();
		assert this.em != null;
	} // beforeAll

	@AfterAll
	void afterAll() {
		log.trace("afterAll() invoked.");

		assert this.emf != null;

		// 참고로, emf 는 자원객체이기는 하지만, 자바언어에서 말하는
		// 자원객체가 되려면 Autocloseable 해야한다
		// 따라서 try-with-resurce block을 사용할 수 없다.
//		try(this.emf) {}  // XX

		this.emf.close();
	} // afterAll

//	@Disabled
	@Order(1)
	@Test
//	@RepeatedTest(1)
	@DisplayName("1. createTable")
	@Timeout(value = 1L, unit = TimeUnit.MINUTES)
	void createTable() {
		log.trace("createTable() invoked.");

		@Cleanup
		EntityManager em = emf.createEntityManager();

		log.info("\t+ Done.");
	} // createTable

//	@Disabled
	@Order(2)
	@Test
//	@RepeatedTest(1)
	@DisplayName("2. createEntities")
	@Timeout(value = 1L, unit = TimeUnit.MINUTES)
	void createEntities() {
		log.trace("createEntities() invoked.");

		@Cleanup
		EntityManager em = this.emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			IntStream.range(1, 5).forEach(seq -> {
				Member m = new Member();
				m.setId("ID_" + seq);
				m.setUserName("NAME_" + seq);
				m.setAge(seq);

				em.persist(m); // 엔티티 인스턴스 저장 with Entity Manager
			}); // forEach

			tx.commit();
		} catch (Exception _original) {
			tx.rollback();
			throw new IllegalStateException(_original);
		}
	} // createEntities

//	@Disabled
	@Order(3)
//	@RepeatedTest(1)
	@Test
	@DisplayName("3. readEntities")
	@Timeout(value = 1L, unit = TimeUnit.MINUTES)
	void readEntities() {
		log.trace("readEntities() invoked.");

		@Cleanup
		EntityManager em = this.emf.createEntityManager();

		IntStream.range(1, 4).forEach(seq -> {
			Member foundMember = em.<Member>find(Member.class, "ID_"+seq);
			log.info("\t+ foundMember : {}", foundMember);
		}); // forEach

//		String jpql = "SELECT m FROM Member m ORDER BY m.id ASC";
//		TypedQuery<Member> jpqlQuery = em.<Member>createQuery(jpql, Member.class);
//		
//		assertNotNull(jpqlQuery);
//		
//		List<Member> list = jpqlQuery.getResultList();
//		list.forEach(log::info);

//		String nativeSQL = "SELECT * FROM Member ORDER BY id DESC";
//		Query nativeQuery = em.createNativeQuery(nativeSQL, Member.class);
//		@SuppressWarnings("rawtypes")
//		List list = nativeQuery.getResultList();
//		list.forEach(log::info);
	} // readEntities

//	@Disabled
	@Order(4)
	@Test
//	@RepeatedTest(1)
	@DisplayName("4. updateEntities")
	@Timeout(value = 1L, unit = TimeUnit.MINUTES)
	void updateEntities() {
		log.trace("updateEntities() invoked.");

		@Cleanup
		EntityManager em = this.emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		final String idToFind = "ID_3";
		Member foundMemberWithID3 = em.<Member>find(Member.class, idToFind);

		Objects.requireNonNull(foundMemberWithID3);
		log.info("\t+ foundMemberWithID3 before : {}", foundMemberWithID3);

		foundMemberWithID3.setUserName("Yoon");
		foundMemberWithID3.setAge(25);
		log.info("\t+ foundMemberWithID3 after : {}", foundMemberWithID3);

		tx.commit();
	} // updateEntities

//	@Disabled
	@Order(5)
	@Test
//	@RepeatedTest(1)
	@DisplayName("5. removeEntities")
	@Timeout(value = 1L, unit = TimeUnit.MINUTES)
	void removeEntities() {
		log.trace("removeEntities() invoked.");

		@Cleanup
		EntityManager em = this.emf.createEntityManager();
		try {
			em.getTransaction().begin();

			IntStream.rangeClosed(1, 6).forEach(seq -> {
				String pk = "ID" + seq;
				Member foundMember = em.<Member>find(Member.class, pk);

				if (foundMember != null) {
					em.remove(foundMember);
				} // if
			}); // forEach

			em.getTransaction().commit();
		} catch (Exception _original) {
			em.getTransaction().rollback();
			throw new IllegalStateException(_original);
		}
	} // removeEntities

//	@Disabled
	@Order(6)
	@Test
//	@RepeatedTest(1)
	@DisplayName("6. testDetachEntities")
	@Timeout(value = 1L, unit = TimeUnit.MINUTES)
	void testDetachEntities() {
		log.trace("testDetachEntities() invoked.");
		
//		IntStream.of(1, 2, 3).forEachOrdered(seq -> {
//			Member transientMember = new Member();
//
//			transientMember.setId("ID_" + seq);
//			transientMember.setAge(seq);
//			transientMember.setUserName("NAME_" + seq);
//			
//			try {
//				this.em.getTransaction().begin();
//				
//				this.em.persist(transientMember);
//				
//				this.em.getTransaction().commit();
//			}catch(Exception _original) {
//				this.em.getTransaction().rollback();
//				throw new IllegalStateException(_original);
//			} // try-catch
//		}); // forEachOrdered
		
		final String id = "ID_3";
		
		Member foundMember = this.em.<Member>find(Member.class, id);
		
		assertNotNull(foundMember);
//		
		log.info("\t+ foundMember : {}, isContains : {}", foundMember, em.contains(foundMember));
		
		em.detach(foundMember);
		log.info("\t+ is contains after detached : {}", em.contains(foundMember));
		
		foundMember.setAge(23);
		foundMember.setUserName("yoon");
		
		
		em.clear();
	} // testDetachEntities
} // end class
