/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import model.Breweries;
import org.springframework.stereotype.Service;
import model.DBUtil;

/**
 *
 * @author Leon
 */
@Service
public class BreweriesService {

    public List<Breweries> getAllBreweries() {
        EntityManager em = DBUtil.getEMF().createEntityManager();
        List<Breweries> list = null;
        try {
            list = em.createNamedQuery("Breweries.findAll")
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

    public boolean addBrewery(Breweries brewery) {
        boolean result = false;
        EntityManager em = DBUtil.getEMF().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(brewery);
            trans.commit();
            result = true;
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
            return result;
        }
    }

    public Breweries getBreweryById(int id) {
        EntityManager em = DBUtil.getEMF().createEntityManager();
        Breweries brewery = null;
        try {
            brewery = em.createNamedQuery("Breweries.findById", Breweries.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
        }
        return brewery;
    }

    public boolean editBrewery(Breweries brewery) {
        boolean result = false;
        EntityManager em = DBUtil.getEMF().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(brewery);
            trans.commit();
            result = true;
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
            return result;
        }
    }

    public boolean deleteBrewery(Breweries brewery) {
        boolean result = false;
        EntityManager em = DBUtil.getEMF().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.remove(em.merge(brewery));
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
