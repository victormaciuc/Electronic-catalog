package events;

import domain.Nota;
import domain.Student;
import domain.Tema;

public class ChangeEvent implements Event {
    private ChangeEventType type;
    private Student data, oldData;
    private Tema dataT,oldDataT;
    private Nota dataN,oldDataN;

    public ChangeEvent(ChangeEventType type, Student data) {
        this.type = type;
        this.data = data;
    }

    public ChangeEvent(ChangeEventType type, Tema data) {
        this.type = type;
        this.dataT = data;
    }

    public ChangeEvent(ChangeEventType type, Nota dataN) {
        this.type = type;
        this.dataN = dataN;
    }

    public ChangeEvent(ChangeEventType type, Nota dataN, Nota oldDataN) {
        this.type = type;
        this.dataN = dataN;
        this.oldDataN = oldDataN;
    }

    public ChangeEvent(ChangeEventType type, Student data, Student oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Student getData() {
        return data;
    }

    public Student getOldData() {
        return oldData;
    }
}