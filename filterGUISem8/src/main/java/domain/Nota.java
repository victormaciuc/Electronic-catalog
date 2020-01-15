package domain;

import java.time.LocalDateTime;


public class Nota extends Entity<Pair> implements Comparable<Nota> {

    private LocalDateTime data;
    private float value;
    private String feedback;


    public Nota(Long idStudent, Long idTema, LocalDateTime data, float value, String feedback) {
        Pair p = new Pair(idStudent,idTema);
        this.setId(p);
        this.data = data;
        this.value = value;
        this.feedback = feedback;
    }

    public Long getIdStudent() {
        return getId().getFirst();
    }

    public void setIdStudent(Long idStudent) {
        Pair p = new Pair(idStudent,this.getId().getSecond());
        this.setId(p);
    }

    public Long getIdTema() {
        return getId().getSecond();
    }

    public void setIdTema(Long idTema) {
        Pair p = new Pair(this.getId().getFirst(),idTema);
        this.setId(p);
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return getId().getFirst() + " " +getId().getSecond() + " | " +
                " feedback = " +  feedback + " | " +
                " nota = " + value;
    }

    @Override
    public int compareTo(Nota o) {
        return 0;
    }
}
