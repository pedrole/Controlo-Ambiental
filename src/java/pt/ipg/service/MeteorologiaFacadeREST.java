/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ipg.service;

import com.sun.faces.action.RequestMapping;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pt.ipg.Meteorologia;

/**
 *
 * @author lealp
 */
@Stateless
@Path("pt.ipg.meteorologia")
public class MeteorologiaFacadeREST extends AbstractFacade<Meteorologia> {

    @PersistenceContext(unitName = "ControloAmbientalPU")
    private EntityManager em;

    public MeteorologiaFacadeREST() {
        super(Meteorologia.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Meteorologia entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Meteorologia entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Meteorologia find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET

    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Meteorologia> findAll(@QueryParam("local") int localID) {
        if (localID == 0) {
            return super.findAll();
        } else {
            Query createQuery = getEntityManager().createQuery("Select m FROM Meteorologia m WHERE m.local.id = :localID"
                    + " ORDER BY m.data desc");
            createQuery.setParameter("localID", localID);
            createQuery.setMaxResults(1);
            return createQuery.getResultList();
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Meteorologia> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

  /*  @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Meteorologia getMeteorologia(@QueryParam("local") int localID) {
        if (localID == 0) {
            findAll();

        }
        /*TypedQuery<Tarefas> query = getEntityManager().createQuery(querySearchAll+ "AND t.projetoId.id = :projeto_id"
        + orderSearchALL,
        Tarefas.class);
        query.setParameter("projeto_id", projeto_id);
        query.setParameter("nome", nome.trim() + "%");*/
  /*      Query createQuery = getEntityManager().createQuery("Select m FROM Meteorologia m WHERE m.localId.idLocais = :localID"
                + " LIMIT 1");
        createQuery.setParameter("localID", localID);
        return (Meteorologia) createQuery.getSingleResult();

    }*/

    /*String querySearchAll = "SELECT t FROM Tarefas t where "
                + "t.nome LIKE :nome and t.concluida = 0 ";
    String orderSearchALL = " order by t.prioridade, t.dataRealizacao";*/

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
