package pt.ipg.controloambiental;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lealp on 02/07/2017.
 */

public class Local implements Parcelable {

    private int id;
    private String descricao;
    private int frequencia;
    private boolean obtemDados;
    private double temperaturaMinima, temperaturaMaxima;

    public Local(int id, String descricao, int frequencia, boolean obtemDados, double temperaturaMinima, double temperaturaMaxima) {
        this.id = id;
        this.descricao = descricao;
        this.frequencia = frequencia;
        this.obtemDados = obtemDados;
        this.temperaturaMinima = temperaturaMinima;
        this.temperaturaMaxima = temperaturaMaxima;
    }

    public double getTemperaturaMaxima() {
        return temperaturaMaxima;
    }

    public void setTemperaturaMaxima(double temperaturaMaxima) {
        this.temperaturaMaxima = temperaturaMaxima;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public boolean isObtemDados() {
        return obtemDados;
    }

    public void setObtemDados(boolean obtemDados) {
        this.obtemDados = obtemDados;
    }

    public double getTemperaturaMinima() {
        return temperaturaMinima;
    }

    public void setTemperaturaMinima(double temperaturaMinima) {
        this.temperaturaMinima = temperaturaMinima;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.descricao);
        dest.writeInt(this.frequencia);
        dest.writeByte(this.obtemDados ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.temperaturaMinima);
        dest.writeDouble(this.temperaturaMaxima);
    }

    protected Local(Parcel in) {
        this.id = in.readInt();
        this.descricao = in.readString();
        this.frequencia = in.readInt();
        this.obtemDados = in.readByte() != 0;
        this.temperaturaMinima = in.readDouble();
        this.temperaturaMaxima = in.readDouble();
    }

    public static final Creator<Local> CREATOR = new Creator<Local>() {
        @Override
        public Local createFromParcel(Parcel source) {
            return new Local(source);
        }

        @Override
        public Local[] newArray(int size) {
            return new Local[size];
        }
    };
}
