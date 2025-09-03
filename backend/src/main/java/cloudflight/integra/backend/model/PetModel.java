package cloudflight.integra.backend.model;

import java.io.Serializable;

public class PetModel implements Serializable {
    private int id;
    private String specie;
    private String rasa;
    private String nume;
    private String locatia;
    private int varsta;
    private String descriere;
    private String imgURL;

    public PetModel() {
    }

    public PetModel(int id, String specie, String rasa, String nume, String locatia, int varsta, String descriere, String imgURL) {
        this.id = id;
        this.specie = specie;
        this.rasa = rasa;
        this.nume = nume;
        this.locatia = locatia;
        this.varsta = varsta;
        this.descriere = descriere;
        this.imgURL = imgURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public String getRasa() {
        return rasa;
    }

    public void setRasa(String rasa) {
        this.rasa = rasa;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getLocatia() {
        return locatia;
    }

    public void setLocatia(String locatia) {
        this.locatia = locatia;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    @Override
    public String toString() {
        return "PetModel{" +
                "id=" + id +
                ", specie='" + specie + '\'' +
                ", rasa='" + rasa + '\'' +
                ", nume='" + nume + '\'' +
                ", locatia='" + locatia + '\'' +
                ", varsta=" + varsta +
                ", descriere='" + descriere + '\'' +
                ", imgURL='" + imgURL + '\'' +
                '}';
    }
}
