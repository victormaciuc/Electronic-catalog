package domain;

public class Entity<ID> {
    private ID id;
    public ID getId() {
        return id;
    }
    public void setId(ID id) {
        this.id = id;
    }

    public boolean isStudent() {
        try{
            Student nou = (Student) this;
            return true;
        } catch(Exception ex) {
            return false;
        }
    }

    public boolean isTema() {
        try{
            Tema nou = (Tema) this;
            return true;
        } catch(Exception ex) {
            return false;
        }
    }

    public boolean isNota() {
        try{
            Nota nou = (Nota) this;
            return true;
        } catch(Exception ex) {
            return false;
        }
    }

}

