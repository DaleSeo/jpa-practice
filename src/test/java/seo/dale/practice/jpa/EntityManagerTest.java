package seo.dale.practice.jpa;

import org.junit.*;
import seo.dale.practice.jpa.model.Member;
import seo.dale.practice.jpa.util.TableCreator;

import javax.persistence.*;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class EntityManagerTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    EntityTransaction tx;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        TableCreator.createTable();
        emf = Persistence.createEntityManagerFactory("commerce");
    }

    @Before
    public void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @Test
    public void testLifeCycle() {
        // New
        Member member = createMember(1L, "dale0713", 34);
        assertFalse(em.contains(member));

        // Managed
        em.persist(member);
        assertTrue(em.contains(member));
        assertSame(member, em.find(Member.class, 1L));

        // Though you flush, it stays in the persistence context
        em.flush();
        assertTrue(em.contains(member));

        // Detached
        em.detach(member);
        member.setAge(17);
        em.flush();
        assertFalse(em.contains(member));
        assertEquals(34, em.find(Member.class, 1L).getAge().intValue());

        // Managed again
        member = em.merge(member);
        em.flush();
        assertTrue(em.contains(member));
        assertEquals(17, em.find(Member.class, 1L).getAge().intValue());

        // Removed
        em.remove(member);
        em.flush();
        assertFalse(em.contains(member));
        assertNull(em.find(Member.class, 1L));
    }

    @Test
    public void tesDirtyChecking() {
        Member member = createMember(1L, "dale0713", 34);
        em.persist(member);
        assertTrue(em.contains(member));

        em.flush();
        assertSame(member, em.find(Member.class, 1L));

        member.setUsername("DaleSeo");
        em.flush();
        assertEquals("DaleSeo", em.find(Member.class, 1L).getUsername());
    }

    @Test
    public void testInsertAndUpdate() {
        Member member = createMember(1L, "dale0713", 34);
        em.persist(member);
        member.setAge(17);

        Member found = em.find(Member.class, 1L);
        assertEquals(member.getAge(), found.getAge());
    }

    @Test(expected = EntityExistsException.class)
    public void testPersistDifferentObjectWithTheSameId() {
        em.persist(createMember(1L, "dale0713", 34));
        em.persist(createMember(1L, "kate0308", 28)); // the same id
        fail();
    }

    @Test(expected = PersistenceException.class)
    public void testInsertDuplicate() {
        Member member = createMember(1L, "dale0713", 34);
        em.persist(member);
        em.flush();
        em.detach(member);

        em.persist(createMember(1L, "DaleSeo", 34)); // primary key violation
        em.flush();
        fail();
    }

    @After
    public void tearDown() {
        if (tx.getRollbackOnly()) {
            tx.rollback();
        } else {
            tx.commit();
        }
        em.close();
    }

    @AfterClass
    public static void tearDownClass() {
        emf.close();
    }

    private static Member createMember(Long id, String username, Integer age) {
        Member member = new Member();
        member.setId(id);
        member.setUsername(username);
        member.setAge(age);
        return member;
    }

}
