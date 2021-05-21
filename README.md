# JPA 기초

## 1. JPA 시작하기
1. entity manager factory 는 app Loading 시점에 딱 하나만 만들어야한다.
2. JPA에서 모든 데이터의 변경은 트랜잭션 안에서 일어나야한다.
3. EntityManagerFactory 설정시 resource > META-INF > persistence.xml > persistence-uint > name 으로 db설정

## 2. 영속성 관리 - 내부 동작 방식
```java
// 비영속 상태
Member member = new Member();
member.setId(100L);
member.setName("바저호랑이");

// 영속 상태, 이 때 DB에 저장되는게 아니다.
// 영속성 컨텍스트에 저장이된다.
// 이 때 JPA는 insert sql을 생성해 쓰기지연 SQL 저장소에 쌓아둔다.
em.persist(member);

// 영속성 컨텍스트에서 분리함.
em.detach(member);

// find 는 1차캐시에 있는 영속성 컨텍스트에서 가져오기때문에 select 쿼리를 날리지 않는다.
// 같은 id로 두번 find 하면 쿼리는 한번만 나가야한다.
// 처음 db에서 조회하면 영속성 컨텍스트에 올린다.
// 그럼 1차캐시에서 조회해서 가져온다.
// 영속 엔터티의 동일성을 보장한다. 다시말해 객체 == 비교하면 true이다.
Member findMember = em.find(Member.class, 100L);
System.out.println(findMember);

// em.update(); 같은것은 없다.
// 이렇게 하면 update쿼리가 나간다.
// Dirty Checking 이라는건데, 처음 1차캐시에 저장될 때 스냅샷이 저장되는데, 
// 1차캐시의 내용과 비교해 달라진게 있다면 쓰기지연 SQL 저장소에 update 쿼리가 쌓여진다. 
findMember.setName("AAA");

// 이 때 영속성 컨텍스트에 저장된 부분을 쿼리로 날린다.
// 쓰기지연 SQL 저장소의 쿼리들을 flush 한다.
tx.commit();
```
### flush
 - 영속성 컨텍스트의 변경내용을 DB에 반영
 - 영속성 컨텍스트를 비우는게 아니라 DB에 동기화 하는거다!
 - 변경감지
 - 수정된 Entity의 update 쿼리를 쓰기 지연 SQL 저장소에 등록
 - 쓰기 지연 SQL 저장소의 쿼리를 DB에 전송(등록, 수정 삭제 쿼리)
 - em.flush() - 직접 호출
 - 트랜잭션 커밋 - 플러시 자동 호출
 - JPQL 쿼리 실행 - 플러시 자동 호출 (쿼리 실행 후 조회할 때 반영이 안되면 문제가 생길 수 있기때문)
 
### 준영속 상태
 - 일단 1차 캐시에 올라간 상태가 영속 상태이다.
 - find해서 1차캐시에 없어 DB에서 가져와 1차 캐시에 들어가면 영속상태가 된다.
 - 영속 -> 준영속
 - 영속 상태의 Entity가 영속성 컨텍스트에서 분리(detached)
 - 영속성 컨텍스트 기능을 사용 못함
```java
// 1차캐시에 들어와 영속상태가 됨
Member member = em.find(Member.class, 150L);
member.setName("AAA");

// 특정 Entity만 준영속 상태로 변경
em.detach(member);

// 영속성 컨텍스트 안에있는 모든 영속성을 날림
em.clear();
```
 
 ## 3. Entity 매핑
 1. DB 스키마 자동 생성7
 - 운영 장비에는 절대 create, create-drop, update 사용하면 안된다.
 - 개발 초기단계는 create 또는 update
 - 테스트 서버는 update 또는 validate
 - 스테이징과 운영 서버는 validate 또는 none
 
 2. DDL 생성기능 
 - DDL 생성 기능은 DDL을 자도 ㅇ생성할 때만 사용되고 JPA의 실행 로직에는 영향을 주지 않는다.
 - @Column(unique = true, nullable = false, length = 10) 뭐 이런것
 
 3. 필드와 컬럼 매핑
 - @Enumerated : STRING, ORDINAL
 - @Temporal : DATE, TIME, TIMESTAMP
 - @Lob : 제한없이 글쓰는 경우, 문자(String)면 CLOB, 나머지는 BLOB
 - @Transient : DB와 관계없이 메모리에서만 쓰고싶을때 
 
 4. 기본 키 매핑
 - GenerationType.IDENTITY : auto increment와 같다. 
 - 기본 키 권장 : Long형 + 대체키(auto increment), 비지니스 로직과 전혀 관계없는 키 권장
 
 