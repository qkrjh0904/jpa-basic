package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        // entity manager factory 는 app Loading 시점에 딱 하나만 만들어야한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("h2db");
        EntityManager em = emf.createEntityManager();

        // JPA에서 모든 데이터의 변경은 트랜잭션 안에서 일어나야한다.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member2 member1 = new Member2();
            member1.setUserName("가");

            Member2 member2 = new Member2();
            member2.setUserName("나");

            Member2 member3 = new Member2();
            member3.setUserName("나");

            // GeneratedValue의 경우는 DB에 넣어야 id값을 알 수 있기때문에 쿼리가 바로 반영된다.
            em.persist(member1); //1, 51 seq
            em.persist(member2); //memory 내에서
            em.persist(member3); //memory 내에서
            System.out.println(member1);
            System.out.println(member2);
            System.out.println(member3);
            tx.commit();
        } catch (Exception e){
            // 에러 발생시 롤백 시켜준다.
            tx.rollback();
        } finally {
            // 자원 반납해줘야한다.
            em.close();
        }
        emf.close();

    }
}
