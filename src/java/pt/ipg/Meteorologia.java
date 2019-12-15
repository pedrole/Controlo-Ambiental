/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ipg;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lealp
 */
@Entity
@Table(name = "meteorologia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Meteorologia.findAll", query = "SELECT m FROM Meteorologia m")
    , @NamedQuery(name = "Meteorologia.findById", query = "SELECT m FROM Meteorologia m WHERE m.id = :id")
    , @NamedQuery(name = "Meteorologia.findByTemperatura", query = "SELECT m FROM Meteorologia m WHERE m.temperatura = :temperatura")
    , @NamedQuery(name = "Meteorologia.findByHumidade", query = "SELECT m FROM Meteorologia m WHERE m.humidade = :humidade")
    , @NamedQuery(name = "Meteorologia.findByData", query = "SELECT m FROM Meteorologia m WHERE m.data = :data")})
public class Meteorologia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "temperatura")
    private Double temperatura;
    @Column(name = "humidade")
    private Double humidade;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data = new Date();
    @JoinColumn(name = "local_id", referencedColumnName = "id_locais")
    @ManyToOne(optional = false)
    private Locais local;

    public Meteorologia() {
    }

    public Meteorologia(Integer id) {
        this.id = id;
    }

    public Meteorologia(Integer id, Date data) {
        this.id = id;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getHumidade() {
        return humidade;
    }

    public void setHumidade(Double humidade) {
        this.humidade = humidade;
    }

   

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Locais getLocal() {
        return local;
    }

    public void setLocal(Locais local) {
        this.local = local;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meteorologia)) {
            return false;
        }
        Meteorologia other = (Meteorologia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pt.ipg.Meteorologia[ id=" + id + " ]";
    }
    
}
