package seo.dale.practice.jpa;

import org.junit.*;
import seo.dale.practice.jpa.model.Member;
import seo.dale.practice.jpa.util.TableCreator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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

    @After
    public void tearDown() {
        tx.commit();
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
