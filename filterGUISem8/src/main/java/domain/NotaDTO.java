package domain;

public class NotaDTO extends Entity<Pair> {
    private String numeElev;
    private String prof;
    private String feedback;
    private Float value;
    private int sapt;

    public NotaDTO(Long idStudent, Long idTema,String numeElev, String prof, String feedback,Float value, int sapt) {
        Pair p = new Pair(idStudent,idTema);
        this.setId(p);
        this.numeElev = numeElev;
        this.prof = prof;
        this.feedback = feedback;
        this.value = value;
        this.sapt = sapt;
    }

    public String getNumeElev() {
        return numeElev;
    }

    public void setNumeElev(String numeElev) {
        this.numeElev = numeElev;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public int getSapt() {
        return sapt;
    }

    public void setSapt(int sapt) {
        this.sapt = sapt;
    }

    @Override
    public String toString(){
        return numeElev + " | " + " saptamana = " + sapt + "|" +
                " feedback = " +  feedback + " | " +
                " nota = " + value;
    }
}

