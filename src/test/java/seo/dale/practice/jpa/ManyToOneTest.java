package seo.dale.practice.jpa;

import org.junit.*;
import seo.dale.practice.jpa.model.Member;
import seo.dale.practice.jpa.model.Order;
import seo.dale.practice.jpa.model.OrderStatus;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ManyToOneTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    EntityTransaction tx;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        emf = Persistence.createEntityManagerFactory("jpa-practice");
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
        order.setMember(member);
        order.setStatus(OrderStatus.ORDER);

        em.persist(order);

        System.out.println(">>> " + order);

        assertEquals(order.getId(),  member.getOrders().get(0).getId());

        order = em.find(Order.class, order.getId());
        member = order.getMember();

        assertEquals(member.getId(), order.getMember().getId());
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