package hellojpa;

import entity.Member;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // 엔티티 매니저 팩토리 (DB당 하나만 생성해서 애플리케이션 전체에서 공유)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 엔티티 매니저 (쓰레드간에 공유를 하면 안되고 사용한 뒤 버려야함)
        // db 커넥션을 하나 받음, DB 다루는 것을 마치 자바컬렉션 다루는 것처럼 이해
        EntityManager em = emf.createEntityManager();
        // jpa 의 모든 데이터 변경 작업은 트랜잭션 안에서 작업을 해야함.
        EntityTransaction tx = em.getTransaction();     // 트랜잭션 객체 생성
        tx.begin();

        try {
            Member member = new Member();
            member.setName("홍길동");
            em.persist(member);
            // 1. EntityManager를 통해 객체(DB) 찾기, 삭제 등
//            Member findMember = em.find(Member.class, 1L); // 가져오는 순간 em이 관리하기 시작함.
//            System.out.println("member = " + findMember.getId());
//            System.out.println("member = " + findMember.getName());
//            em.remove(Member.class);

            // 2. JQPL 사용
            // 클래스(엔티티) 객체를 대상으로 쿼리를 짤 수 있음. (객체 지향 쿼리 언어를 제공함)
            // DB별 쿼리 방언을 신경쓰지 않아도 됨 (SQL 문으로 자동 변환)
            // 조건이 들어간 검색 쿼리가 문제임 (동적 쿼리)
            List<Member> members = em.createQuery("select m from Member m", Member.class)
                    .setFirstResult(3)
                    .setMaxResults(5)
                    .getResultList();

            for(Member m : members){
                System.out.println("member = " + m.getName());
            }

            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
