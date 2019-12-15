package pt.ipg.controloambiental;

import java.util.Date;

/**
 * Created by lealp on 02/07/2017.
 */

public class Meteorologia {
    private int id;
    private double temperatura, humidade;
    private Local local;
    private Date data;

    public Meteorologia(int id, double temperatura, double humidade, Local local, Date data) {
        this.id = id;
        this.temperatura = temperatura;
        this.humidade = humidade;
        this.local = local;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getHumidade() {
        return humidade;
    }

    public void setHumidade(double humidade) {
        this.humidade = humidade;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
