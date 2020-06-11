/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.springframework.stereotype.Service;
import model.DBUtil;
import model.Styles;

/**
 *
 * @author Leon
 */
@Service
public class StylesService {

    public List<Styles> getAllStyles() {
        EntityManager em = DBUtil.getEMF().createEntityManager();
        List<Styles> list = null;
        try {
            list = em.createNamedQuery("Styles.findAll")
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

    public List<Styles> getStylesByCategory(int categoryId) {
        EntityManager em = DBUtil.getEMF().createEntityManager();
        List<Styles> list = null;
        try {
            list = em.createNamedQuery("Styles.findByCatId")
                    .setParameter("catId", categoryId)
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

    public boolean addStyle(Styles s) {
        boolean result = false;
        EntityManager em = DBUtil.getEMF().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(s);
            trans.commit();
            result = true;
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
            return result;
        }
    }

    public Styles getStyleById(int id) {
        EntityManager em = DBUtil.getEMF().createEntityManager();
        Styles style = null;
        try {
            style = em.createNamedQuery("Styles.findById", Styles.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
        }
        return style;
    }

    public boolean editStyle(Styles style) {
        boolean result = false;
        EntityManager em = DBUtil.getEMF().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(style);
            trans.commit();
            result = true;
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
            return result;
        }
    }

    public boolean deleteStyle(Styles style) {
        boolean result = false;
        EntityManager em = DBUtil.getEMF().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.remove(em.merge(style));
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
