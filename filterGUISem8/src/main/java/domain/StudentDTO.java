package domain;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class StudentDTO {
    private String nume;
    private float medie;
    private String trecut;
    private Boolean temele;

    public StudentDTO(String nume, float medie, String trecut, Boolean temele) {
        this.nume = nume;
        this.medie = medie;
        this.trecut = trecut;
        this.temele = temele;
    }

    public Boolean getTemele() {
        return temele;
    }

    public void setTemele(Boolean temele) {
        this.temele = temele;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public float getMedie() {
        return medie;
    }

    public void setMedie(float medie) {
        this.medie = medie;
    }

    public String getTrecut() {
        return trecut;
    }

    public void setTrecut(String trecut) {
        this.trecut = trecut;
    }
}
