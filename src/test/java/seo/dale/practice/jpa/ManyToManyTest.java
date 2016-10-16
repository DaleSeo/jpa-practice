package seo.dale.practice.jpa;

import org.junit.*;
import seo.dale.practice.jpa.model.Category;
import seo.dale.practice.jpa.model.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.*;

public class ManyToManyTest {

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
        Item item = new Item();
        item.setName("Apple");
        item.setPrice(500);
        item.setStockQuantity(1000000);

        em.persist(item);

        Item item2 = new Item();
        item2.setName("Banana");
        item2.setPrice(3000);
        item2.setStockQuantity(3000000);

        em.persist(item2);

        Category category = new Category();
        category.setName("Fruits");
        category.addItem(item);
        category.addItem(item2);

        em.persist(category);

        category = em.find(Category.class, category.getId());
        assertEquals(category.getItems().get(0).getId(), item.getId());
        assertEquals(category.getItems().get(1).getId(), item2.getId());
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