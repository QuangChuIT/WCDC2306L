package vn.aptech.servlet.student;

import vn.aptech.servlet.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class StudentService {

    public List<Student> getAllStudents() {

        EntityManager em = JPAUtil.getInstance().getEntityManager();
        try {
            Query query = em.createQuery("select s from STUDENTS s", Student.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error get all students: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            em.close();
        }

    }

    public boolean existByEmail(String email) {
        EntityManager em = JPAUtil.getInstance().getEntityManager();
        try {
            Query query = em.createQuery("select s from STUDENTS s where s.email = :email", Student.class);
            query.setParameter("email", email);
            return query.getSingleResult() != null;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }

    public void addStudent(Student student) {
        EntityManager em = JPAUtil.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(student);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error adding student: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void updateStudent(Student student) {
        EntityManager em = JPAUtil.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(student);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error update student: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void deleteStudent(Long id) {
        EntityManager em = JPAUtil.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Student student = em.find(Student.class, id);
            em.remove(student);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error delete student: " + e.getMessage());
        }
    }
}
