package domain;

public class Tema extends Entity<Long> {
    private String desc;
    private int startWeek;
    private int deadlineWeek;

    public Tema(Long id,String desc, int startWeek, int deadlineWeek) {
        this.setId(id);
        this.desc = desc;
        this.startWeek = startWeek;
        this.deadlineWeek = deadlineWeek;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getDeadlineWeek() {
        return deadlineWeek;
    }

    public void setDeadlineWeek(int deadlineWeek) {
        this.deadlineWeek = deadlineWeek;
    }

    @Override
    public String toString() {
        return getId() + "." +
                " desc = " + desc + " | " +
                " startWeek = " + startWeek + " | " +
                " deadlineWeek = " + deadlineWeek;
    }
}
