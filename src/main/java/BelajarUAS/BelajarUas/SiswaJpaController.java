/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BelajarUAS.BelajarUas;

import BelajarUAS.BelajarUas.exceptions.NonexistentEntityException;
import BelajarUAS.BelajarUas.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ASUS
 */
public class SiswaJpaController implements Serializable {

    public SiswaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("BelajarUAS_BelajarUas_jar_0.0.1-SNAPSHOTPU");
    
    public SiswaJpaController(){}
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Siswa siswa) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(siswa);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSiswa(siswa.getId()) != null) {
                throw new PreexistingEntityException("Siswa " + siswa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Siswa siswa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            siswa = em.merge(siswa);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = siswa.getId();
                if (findSiswa(id) == null) {
                    throw new NonexistentEntityException("The siswa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Siswa siswa;
            try {
                siswa = em.getReference(Siswa.class, id);
                siswa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The siswa with id " + id + " no longer exists.", enfe);
            }
            em.remove(siswa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Siswa> findSiswaEntities() {
        return findSiswaEntities(true, -1, -1);
    }

    public List<Siswa> findSiswaEntities(int maxResults, int firstResult) {
        return findSiswaEntities(false, maxResults, firstResult);
    }

    private List<Siswa> findSiswaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Siswa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Siswa findSiswa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Siswa.class, id);
        } finally {
            em.close();
        }
    }

    public int getSiswaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Siswa> rt = cq.from(Siswa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
