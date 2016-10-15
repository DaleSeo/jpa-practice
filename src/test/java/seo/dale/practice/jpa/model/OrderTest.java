package seo.dale.practice.jpa.model;

import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.Date;

public class OrderTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    EntityTransaction tx;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        emf = Persistence.createEntityManagerFactory("commerce");
    }

    @Before
    public void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @Test
    public void test() {
        Member member = new Member();
        member.setName("Dale");
        member.setCity("Seoul");
        member.setStreet("Eonju");
        member.setZipcode("06229");

        em.persist(member);

        System.out.println(">>> " + member);

        Order order = new Order();
        order.setOrderDate(new Date());
        order.setMemberId(member.getId());
        order.setStatus(OrderStatus.ORDER);

        em.persist(order);

        System.out.println(">>> " + order);
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

}