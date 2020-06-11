/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import model.Categories;
import org.springframework.stereotype.Service;
import model.DBUtil;

/**
 *
 * @author Leon
 */
@Service
public class CategoriesService {

    public List<Categories> getAllCategories() {
        EntityManager em = DBUtil.getEMF().createEntityManager();
        List<Categories> list = null;
        try {
            list = em.createNamedQuery("Categories.findAll")
                    .getResultList();
            if (list == null || list.isEmpty()) {
                list = null;
            }
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
        }
        return list;
    }

    public boolean addCategory(Categories c) {
        boolean result = false;
        EntityManager em = DBUtil.getEMF().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(c);
            trans.commit();
            result = true;
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
            return result;
        }
    }

    public Categories getCategoryById(int id) {
        EntityManager em = DBUtil.getEMF().createEntityManager();
        Categories cat = null;
        try {
            cat = em.createNamedQuery("Categories.findById", Categories.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
        }
        return cat;
    }

    public boolean editCategory(Categories c) {
        boolean result = false;
        EntityManager em = DBUtil.getEMF().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(c);
            trans.commit();
            result = true;
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
            return result;
        }
    }

    public boolean deleteCategory(Categories c) {
        boolean result = false;
        EntityManager em = DBUtil.getEMF().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.remove(em.merge(c));
            trans.commit();
            result = true;
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
            return result;
        }
    }
}
