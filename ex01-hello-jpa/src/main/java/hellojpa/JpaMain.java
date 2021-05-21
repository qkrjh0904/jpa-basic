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
            // 비영속 상태
            Member member = new Member();
            member.setId(100L);
            member.setName("바저호랑이");

            // 영속 상태, 이 때 DB에 저장되는게 아니다.
            // 영속성 컨텍스트에 저장이된다.
            // 이 때 JPA는 insert sql을 생성해 쓰기지연 SQL 저장소에 쌓아둔다.
            em.persist(member);

            // 영속성 컨텍스트에서 분리함.
            // em.detach(member);

            // find 는 1차캐시에 있는 영속성 컨텍스트에서 가져오기때문에 select 쿼리를 날리지 않는다.
            // 같은 id로 두번 find 하면 쿼리는 한번만 나가야한다.
            // 처음 db에서 조회하면 영속성 컨텍스트에 올린다.
            // 그럼 1차캐시에서 조회해서 가져온다.
            // 영속 엔터티의 동일성을 보장한다. 다시말해 객체 == 비교하면 true이다.
            Member findMember = em.find(Member.class, 100L);
            System.out.println(findMember);

            // 이 때 영속성 컨텍스트에 저장된 부분을 쿼리로 날린다.
            // 쓰기지연 SQL 저장소의 쿼리들을 flush 한다.
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
