package domain;

public class Student extends Entity<Long> implements Comparable<Student>{
    private String nume;
    private int grupa;
    private String email;
    private String prof;

    public Student(Long id,String nume, int grupa, String email, String prof) {
        this.setId(id);
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.prof = prof;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getGrupa() {
        return grupa;
    }

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    @Override
    public String toString() {
        return getId() + "." +
                " nume = " + nume + " | " +
                " grupa = " + grupa + " | " +
                " email =" + email + " | " +
                " cadruDidactic = " + prof;
    }

    @Override
    public int compareTo(Student student) {
        return this.getNume().compareTo(student.getNume());
    }
}
