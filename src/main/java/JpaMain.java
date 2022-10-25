import entity.MemberTest;

import javax.persistence.*;

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
            // 1. EntityManager를 통해 객체(DB) 찾기, 삭제 등
//            Member member = new Member();
//            member.setName("홍길동");
//            Member findMember = em.find(Member.class, 1L); // 가져오는 순간 em이 관리하기 시작함.
//            System.out.println("member = " + findMember.getId());
//            System.out.println("member = " + findMember.getName());
//            em.remove(Member.class);

            // 2. JQPL 사용
            // 클래스(엔티티) 객체를 대상으로 쿼리를 짤 수 있음. (객체 지향 쿼리 언어를 제공함)
            // DB별 쿼리 방언을 신경쓰지 않아도 됨 (SQL 문으로 자동 변환)
            // 조건이 들어간 검색 쿼리가 문제임 (동적 쿼리)
//            List<Member> members = em.createQuery("select m from Member m", Member.class)
//                    .setFirstResult(3)
//                    .setMaxResults(5)
//                    .getResultList();
//
//            for(Member m : members){
//                System.out.println("member = " + m.getName());
//            }
//===========================================================================================
            /* 비영속, 영속, 준영속 차이 */
            // 비영속 (한번도 영속상태가 되지 않음)
//            Member member = new Member();
//            member.setName("홍길동");

            // 영속 (entityManager에 의해 영속성 컨텍스트에 저장 및 관리)
            // 영속성 컨텍스트 내 1차 캐시에 저장됨. (key-value로 key는 @id 값이되고 value는 해당 엔티티 객체가 됨)
            // 여기서 말하는 1차 캐시는 em을 생성한 시점의 트랜잭션 에서만 존재함
            // 이후 해당 엔티티를 영속성 컨텍스트에서 호출할 때 1차 캐시에서 먼저 호출함 (쿼리 호출x)
//            em.persist(member);

//            Member member1 = em.find(Member.class, 1L);
//            System.out.println("member1 = " + member1);

            // 준영속 (한번 영속 상태가 되었다가 분리된 상태)
//            em.detach(member);

            // 제거
//            em.remove(member);
// ==========================================================================================
            /* 변경감지 (dirty checking) */

//            Member member = new Member();
//            member.setName("홍길동");

            // 1차 캐시에 저장
//            em.persist(member);

//            Member member1 = em.find(Member.class, 1L);
            // 변경감지 대상
//            member1.setName("change");

            // 트랜잭션 커밋 시점에서 1차 캐시에 저장된 entity 객체의 스냅샷과 현재 entity 객체를 비교 (변경감지 동작)
            // 변경점이 있다면 update 쿼리를 날림
            // 정리하면 jpa는 영속성 컨텍스트에 관리되는 객체의 속성이 변경되어 커밋되면 알아서 update 쿼리 날려줌 (따로 persist 를 안써도됨)
//            tx.commit();
// ==========================================================================================
            /* 트랜잭션 커밋 시 플러시 발동
            * 1. 변경 감지가 일어남
            * 2. 수정된 엔티티를 쓰기 지연 SQL 저장소에 등록함.
            * 3. 쓰기 지연 SQL 저장소의 쿼리를 DB에 날림 (등록, 수정, 삭제 쿼리)
            * */

            /* 영속성 컨텍스트 플러시 하는 방법
            * 1. em.flush() - 직접 엔티티매니저에서 호출
            * 2. tx.commit() - 트랜잭션 커밋 시 플러시가 자동으로 호출 됨
            * 3. JPQL 쿼리 실행시 플러시 자동 호출
            * */

            MemberTest member = new MemberTest();
            member.setName("홍길동");

            em.persist(member);

            em.flush(); // 직접 호출
            System.out.println("===================");

            tx.commit();    // 자동 호출
        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
