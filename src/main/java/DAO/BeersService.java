/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import model.Beers;
import org.springframework.stereotype.Service;
import model.DBUtil;

/**
 *
 * @author Leon
 */
@Service
public class BeersService {

    public List<Beers> getAllBeers() {
        EntityManager em = DBUtil.getEMF().createEntityManager();
        List<Beers> list = null;
        try {
            list = em.createNamedQuery("Beers.findAll")
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

    public boolean addBeer(Beers b) {
        boolean result = false;
        EntityManager em = DBUtil.getEMF().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(b);
            trans.commit();
            result = true;
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
            return result;
        }
    }

    public Beers getBeerById(int id) {
        EntityManager em = DBUtil.getEMF().createEntityManager();
        Beers beer = null;
        try {
            beer = em.createNamedQuery("Beers.findById", Beers.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
        }
        return beer;
    }

    public List<Beers> getBeersByBreweryId(int breweryId) {
        EntityManager em = DBUtil.getEMF().createEntityManager();
        List<Beers> beers = null;
        try {
            beers = em.createNamedQuery("Beers.findByBreweryId", Beers.class)
                    .setParameter("breweryId", breweryId)
                    .getResultList();
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
        }
        return beers;
    }

    public boolean editBeer(Beers beer) {
        boolean result = false;
        EntityManager em = DBUtil.getEMF().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(beer);
            trans.commit();
            result = true;
        } catch (Exception ex) {
            System.out.println("DB: " + ex);
        } finally {
            em.close();
            return result;
        }
    }

    public boolean deleteBeer(Beers beer) {
        boolean result = false;
        EntityManager em = DBUtil.getEMF().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.remove(em.merge(beer));
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
