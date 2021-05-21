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
            List<Member> resultList = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            for(Member member : resultList){
                System.out.println(member);
            }

            Member member = em.find(Member.class, 1L);
            member.setName("바저호");

            // 꼭 해줘야한다.
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
