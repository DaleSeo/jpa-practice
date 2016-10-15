package seo.dale.practice.jpa;

import seo.dale.practice.jpa.model.Member;
import seo.dale.practice.jpa.util.TableCreator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MemberMain {

    public static void main(String[] args) throws SQLException {
        TableCreator.createTable();

        //엔티티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("commerce");
        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득

        try {

            tx.begin(); //트랜잭션 시작
            logic(em);  //비즈니스 로직
            tx.commit();//트랜잭션 커밋

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); //트랜잭션 롤백
        } finally {
            em.close(); //엔티티 매니저 종료
        }

        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void logic(EntityManager em) {

        Member member = new Member();
        member.setName("Dale");
        member.setCity("Seoul");

        //등록
        em.persist(member);

        //수정
        member.setCity("Busan");

        //한 건 조회
        Member findMember = em.find(Member.class, 1L);
        System.out.println("findMember=" + findMember.getName() + ", city=" + findMember.getCity());

        //목록 조회
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        System.out.println("members.size=" + members.size());

        //삭제
        em.remove(member);
    }

}