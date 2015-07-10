package test;

import model.Question;
import model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by joseotti on 7/10/15.
 */
public class QueryTest {
    private EntityManager manager;

    public QueryTest(EntityManager manager) {
        this.manager = manager;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        EntityManager manager = factory.createEntityManager();
        QueryTest test = new QueryTest(manager);

        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            test.createData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();

        test.listData();

        System.out.println(".. done");
    }

    @SuppressWarnings("unchecked")
    private void listData() {
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            List<User> users = manager.createQuery("select u from User u").getResultList();
            for (User u : users) {
                System.out.println(u);
            }
            List<Object[]> objs = manager.
                    createQuery("select u.name, q.name from User u, Question q where q member of u.questions")
                    .getResultList();

            for (Object[] o : objs) {
                System.out.println(Arrays.toString(o));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();
    }

    private void createData() {
        String[] uNames = {"Jack", "Joe", "Barney"};
        String[] qNames = {"q1", "q2", "q3"};
        Map<String, User> users = new HashMap<>();
        Map<String, Question> questions = new HashMap<>();

        for (String uName : uNames) {
            User user = new User();
            user.setName(uName);
            manager.persist(user);
            users.put(user.getName(), user);
        }
        for (String qName : qNames) {
            Question q = new Question();
            q.setName(qName);
            manager.persist(q);
            questions.put(q.getName(), q);
        }
        users.get("Jack").getQuestions().add(questions.get("q1"));
        users.get("Jack").getQuestions().add(questions.get("q2"));
        users.get("Joe").getQuestions().add(questions.get("q1"));
        users.get("Barney").getQuestions().add(questions.get("q3"));
    }

}
