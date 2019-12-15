/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ipg;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lealp
 */
@Entity
@Table(name = "locais")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Locais.findAll", query = "SELECT l FROM Locais l")
    , @NamedQuery(name = "Locais.findByIdLocais", query = "SELECT l FROM Locais l WHERE l.id = :id")
    , @NamedQuery(name = "Locais.findByDescricao", query = "SELECT l FROM Locais l WHERE l.descricao = :descricao")})
public class Locais implements Serializable {

    @Column(name = "temperatura_maxima")
    private Double temperaturaMaxima;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "temperatura_minima")
    private Double temperaturaMinima;

    @Basic(optional = false)
    @NotNull
    @Column(name = "frequencia")
    private int frequencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "obtem_dados")
    private boolean obtemDados;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_locais")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descricao")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "local")
    private Collection<Meteorologia> meteorologiaCollection;

    public Locais() {
    }

    public Locais(Integer id) {
        this.id = id;
    }

    public Locais(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public Collection<Meteorologia> getMeteorologiaCollection() {
        return meteorologiaCollection;
    }

    public void setMeteorologiaCollection(Collection<Meteorologia> meteorologiaCollection) {
        this.meteorologiaCollection = meteorologiaCollection;
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
        if (!(object instanceof Locais)) {
            return false;
        }
        Locais other = (Locais) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pt.ipg.Locais[ id=" + id + " ]";
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public boolean getObtemDados() {
        return obtemDados;
    }

    public void setObtemDados(boolean obtemDados) {
        this.obtemDados = obtemDados;
    }

    public Double getTemperaturaMinima() {
        return temperaturaMinima;
    }

    public void setTemperaturaMinima(Double temperaturaMinima) {
        this.temperaturaMinima = temperaturaMinima;
    }

    public Double getTemperaturaMaxima() {
        return temperaturaMaxima;
    }

    public void setTemperaturaMaxima(Double temperaturaMaxima) {
        this.temperaturaMaxima = temperaturaMaxima;
    }
    
}
