package business;

import domain.*;
import events.ChangeEvent;
import events.ChangeEventType;
import javafx.scene.control.TextFormatter;
import memory.*;
import observer.Observable;
import observer.Observer;
import validator.ValidationNota;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ServiceNota extends ServiceEntity<Pair, Nota> implements Observable<ChangeEvent> {

    private LocalDate startSemesterAux= LocalDate.of(2020, Month.FEBRUARY,24);
    private LocalDateTime d2 = LocalDateTime.of(startSemesterAux, LocalTime.MIDNIGHT);
    private LocalDateTime d1 = LocalDateTime.now();

    private List<Observer<ChangeEvent>> observers = new ArrayList<>();


    //private InMemoryRepository<Long, Student> repoS;
    private InXMLStudentRepository repoS;


    //private InMemoryRepository<Long, Tema> repoT;
    private InXMLTemaRepository repoT;

    private InXMLNotaRepository repoN;

    private StructuraSemestru ss;

    private void initSemestru() {
        int semestru = 2;
        if(d1.compareTo(d2) < 0)
            semestru = 1;
        ss = new StructuraSemestru(d1.getYear(),semestru);
    }

    public ServiceNota(InXMLNotaRepository repo, InXMLStudentRepository repoS, InXMLTemaRepository repoT, InXMLNotaRepository repoN) {
        super(repo);
        this.repoS = repoS;
        this.repoT = repoT;
        this.repoN = repoN;
        initSemestru();
        studentFileInit();
    }


    public List<Student> report1(int grupa) {
        List<Student> all = new ArrayList<>();
        for(Student s : this.repoS.findAll())
            all.add(s);

        Predicate<Student> p = x-> x.getGrupa() == grupa;

        return all.stream()
                .filter(p)
                .collect(Collectors.toList());

    }

    public List<Nota> report2(Long idTema) {
        List<Nota> all = new ArrayList<>();
        for(Nota s : this.getRepo().findAll())
            all.add(s);

        Predicate<Nota> p = x-> x.getIdTema().equals(idTema);

        return all.stream()
                .filter(p)
                .collect(Collectors.toList());
    }

    private List<NotaDTO> init() {
        List<NotaDTO> all = new ArrayList<>();

        for(Nota n : this.getRepo().findAll()) {
            Student s = repoS.findOne(n.getIdStudent());
            Tema t = repoT.findOne(n.getIdTema());
            all.add(new NotaDTO(s.getId(),t.getId(),s.getNume(),s.getProf(),n.getFeedback(),n.getValue(),ss.getWeek(n.getData())));
        }
        return all;
    }

    public List<NotaDTO> report3(Long idTema,String cadruDidactic) {
        List<NotaDTO> all = init();

        Predicate<NotaDTO> p1 = x-> x.getId().getFirst().equals(idTema);
        Predicate<NotaDTO> p2 = x-> x.getProf().equals(cadruDidactic);

        Predicate<NotaDTO> p = p1.and(p2);

        return all.stream()
                .filter(p)
                .collect(Collectors.toList());
    }

    public List<NotaDTO> report4(Long idTema,int week) {
        List<NotaDTO> all = init();

        Predicate<NotaDTO> p1 = x-> x.getId().getFirst().equals(idTema);
        Predicate<NotaDTO> p2 = x-> x.getSapt() == week;

        Predicate<NotaDTO> p = p1.and(p2);

        return all.stream()
                .filter(p)
                .collect(Collectors.toList());
    }

    public Student getStudent(Long idStudent) {
        return repoS.findOne(idStudent);
    }

    public int verificaST(Long idStudent,Long idTema) {
        Student stReturned = repoS.findOne(idStudent);
        Tema tReturned = repoT.findOne(idTema);
        if (stReturned == null || tReturned == null)
            return 0;
        return 1;
    }

    public int verificaSapt(Long idTema) {

        Tema tReturned = repoT.findOne(idTema);
        LocalDateTime currentDate = LocalDateTime.now();

        int currentWeek = ss.getWeek(currentDate);
        int deadlineWeek = tReturned.getDeadlineWeek();

        if(currentWeek != deadlineWeek)
            return 0;
        return 1;

    }

    public Float calcNota(Float motivare,Long idTema,Float value,LocalDateTime d1) {

        //LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime currentDate = d1;
        return calcNotaComun(motivare,idTema,value,currentDate);
    }

    public int verificaSaptIntarziere(Long idTema,LocalDateTime d) {

        Tema tReturned = repoT.findOne(idTema);
        //LocalDateTime currentDate = LocalDateTime.of(an,luna,zi,0,0);
        LocalDateTime currentDate = d;

        int currentWeek = ss.getWeek(currentDate);
        int deadlineWeek = tReturned.getDeadlineWeek();

        return currentWeek - deadlineWeek;

    }

    public float calcNotaComun(Float motivare,Long idTema,Float value,LocalDateTime currentDate) {
        Tema t = repoT.findOne(idTema);
        //Nota n = new Nota(idStudent, idTema, currentDate, value, feedback);

        int currentWeek = ss.getWeek(currentDate);
        int deadlineWeek = t.getDeadlineWeek();


        if(currentWeek - deadlineWeek - motivare > 2 )
            return 1f;
        else if(currentWeek - deadlineWeek - motivare > 0) {
            return value - currentWeek + deadlineWeek + motivare;
        }
        else
            return value;
    }

    public Float calcNotaIntarziere(Float motivare,Long idTema,Float value,int an,int luna, int zi) {

        LocalDateTime currentDate = LocalDateTime.of(an,luna,zi,0,0);
        return calcNotaComun(motivare,idTema,value,currentDate);
    }

    public Student addStudent(Long idStudent,String nume,int grupa,String email,String prof) {


        try {
            Student st = new Student(idStudent, nume, grupa, email, prof);
            Student returned = this.repoS.save(st);

            notifyObservers(new ChangeEvent(ChangeEventType.ADDNOTA,returned));

            return returned;
        } catch (Exception ex) {
            throw ex;
        }

    }

    private LocalDateTime getDataNota(LocalDate d1) {
        if (d1 == null)
            return LocalDateTime.now();
        else {
            LocalDateTime returned = LocalDateTime.of(d1, LocalTime.NOON);
            return returned;
        }
    }


    public Nota addNota(Long idStudent, Long idTema, String feedback, Float value,LocalDateTime d1) {

        LocalDateTime currentDate = d1;

        Tema t = repoT.findOne(idTema);

        int currentWeek = ss.getWeek(currentDate);
        int deadlineWeek = t.getDeadlineWeek();

        try{
            Nota n = new Nota(idStudent, idTema, currentDate, value, feedback);
            Nota returned = this.getRepo().save(n);
            if (returned == null)
                studentFile(n,currentWeek,deadlineWeek,feedback);

            notifyObservers(new ChangeEvent(ChangeEventType.ADDNOTA,returned));

            return returned;
        } catch (Exception ex) {
            throw ex;
        }
    }


    public Nota deleteNota(Long idStudent, Long idTema) {
        Pair id = new Pair(idStudent, idTema);
        Nota returned = this.getRepo().delete(id);
        notifyObservers(new ChangeEvent(ChangeEventType.DELETE,returned));
        return returned;
    }

    public Nota deleteNota(NotaDTO2 nota) {
        Pair id = new Pair(nota.getId().getFirst(),nota.getId().getSecond());
        Nota returned = this.getRepo().delete(id);
        notifyObservers(new ChangeEvent(ChangeEventType.ADDNOTA,returned));
        return returned;
    }

    public Student deleteStudent(Long idStudent) {

        Student returned = this.repoS.delete(idStudent);
        if(returned != null) {
            for(Nota n : this.getRepo().findAll()) {
                if(n.getId().getFirst() == idStudent)
                    this.getRepo().delete(new Pair(idStudent,n.getId().getSecond()));
            }

            this.fileDelete(returned);
        }
        notifyObservers(new ChangeEvent(ChangeEventType.DELETE,returned));
        return  returned;
    }

    public Student updateStudent(Long id,String nume,int grupa,String email,String cadruDidactic){
        try{
            Student returned = repoS.findOne(id);
            Student st = new Student(id,nume,grupa,email,cadruDidactic);
            Student rez = this.repoS.update(st);
            if(!returned.getNume().equals(nume)) {
                this.fileDelete(returned);
                this.getRepo().findAll().forEach(n -> {
                    if(n.getId().getFirst() == returned.getId()) {
                        Tema t = repoT.findOne(n.getIdTema());
                        int weekPredat = ss.getWeek(n.getData());
                        this.studentFile(n,weekPredat,t.getDeadlineWeek(),n.getFeedback());
                    }
                });
            }
            notifyObservers(new ChangeEvent(ChangeEventType.ADDNOTA,returned));
            return rez;
        }catch(Exception ex) {
            throw ex;
        }
    }

    public Tema deleteTema(Long idTema) {
        Tema returned = this.repoT.delete(idTema);
        if(returned != null) {
            for(Nota n : this.getRepo().findAll()) {
                if(n.getId().getSecond() == idTema)
                    this.getRepo().delete(new Pair(n.getId().getFirst(),idTema));
            }
        }
        notifyObservers(new ChangeEvent(ChangeEventType.DELETE,returned));
        return returned;
    }

    public Nota updateNota(Long idStudent, Long idTema, String feedback, Float value) {
        LocalDateTime currentDate = LocalDateTime.now();
        Nota n = new Nota(idStudent, idTema, currentDate, value, feedback);
        Nota returned =  this.getRepo().update(n);
        notifyObservers(new ChangeEvent(ChangeEventType.DELETE,returned));
        return returned;
    }

    public Iterable<Student> findAllStudents() {
        return this.repoS.findAll();
    }
    public List<Student> findAllStudents(String txt) {
        List<Student> stud = new ArrayList<>();
        for(Student s : repoS.findAll())
            if(s.getNume().contains(txt))
                stud.add(s);
         return stud;
    }

    public Iterable<Tema> findAllTema() {
        return this.repoT.findAll();
    }

    public List<Tema> findAllTema(Student s) {
        List<Tema> returned = new ArrayList<>();
        for(Nota n : this.getRepo().findAll())
            if(n.getIdStudent() == s.getId())
                returned.add(findTema(n.getIdTema()));

        return returned;
    }


    public Student findStudent(Long id) {
        return this.repoS.findOne(id);
    }

    public Tema findTema(Long id) {
        return this.repoT.findOne(id);
    }

    public void studentFileInit() {
        for(Nota n : this.getRepo().findAll()) {
            Tema t = repoT.findOne(n.getIdTema());
            studentFile(n,ss.getWeek(n.getData()),t.getDeadlineWeek(),n.getFeedback());
        }
    }

    private void studentFile (Nota n, int gradeWeek, int deadlineWeek, String feedback) {

        String path = "C:\\Users\\Victor\\Desktop\\filterGUISem8\\Studenti";

        /*
        for(Student s: repoS.findAll()){
            if(s.getId() == n.getIdStudent())
                nume=s.getNume();
        }
        */

        String nume = repoS.findOne(n.getId().getFirst()).getNume();

        path = path +"\\" + nume +".txt";
        FileWriter fw = null;
        try {
            fw = new FileWriter(path, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);

        out.print("ID-ul temei:");
        out.println(n.getIdTema());
        //
        out.print("Nota:");
        out.println(n.getValue());
        //
        out.print("Predata in saptamana:");
        out.println(gradeWeek);
        //
        out.print("Deadline:");
        out.println(deadlineWeek);
        //
        out.print("Feedback:");
        out.println(feedback);
        out.println();
        out.close();
    }

    private void fileDelete(Student s){

        String  path = "C:\\Users\\Mano\\Desktop\\Studenti";
        path = path + "\\" + s.getNume() + ".txt";

        File file = new File(path);
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addObserver(Observer<ChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<ChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(ChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }

    public Long getIdTema(String toString) {
        for(Tema t : repoT.findAll())
            if(t.getDesc().equals(toString))
                return t.getId();
         return 0L;
    }

    public Long getIdStudent(String studentName) {
        for(Student t : repoS.findAll())
            if(t.getNume().equals(studentName))
                return t.getId();
        return 0L;
    }

    public List<NotaDTO2> findAllNote(String txt) {
        List<NotaDTO2> stud = new ArrayList<>();
        for(Nota n : this.getRepo().findAll()) {
            Student s = findStudent(n.getIdStudent());
            if(s.getNume().contains(txt)) {
                LocalDateTime d = n.getData();
                stud.add(new NotaDTO2(s.getNume(),this.findTema(n.getIdTema()).getDesc(),ss.getWeek(d),n.getValue()));
            }
        }
        return stud;
    }

    public Nota findNota(NotaDTO2 selected) {

        Student s = this.findStudent(getIdStudent(selected.getStudentName()));
        Tema t = this.findTema(getIdTema(selected.getTemaName()));
        for(Nota n : this.getRepo().findAll()) {
            if(n.getIdStudent() == s.getId() && n.getIdTema() == t.getId())
                return n;
        }
        return  null;
    }

    public float medieStudent(Long studentId){
        Student stud = (Student) repoS.findOne(studentId);
        Iterable<Nota> note = getAll();
        Iterable<Tema> teme = repoT.findAll();
        float numarator=0;
        float numitor=0;
        float pondere=0;
        float pond=0;
        for (Nota n : note){
            if (repoS.findOne(n.getIdStudent()) == stud){
                pondere = repoT.findOne(n.getIdTema()).getDeadlineWeek() - repoT.findOne(n.getIdTema()).getStartWeek() + 1;
               // pondere=n.getTema().getDeadlineWeek()-n.getTema().getStartWeek();
                //numarator+=n.getNota()*pondere;
                numarator += n.getValue()*pondere;

            }

        }
        for (Tema t : teme){
            pond = t.getDeadlineWeek()-t.getStartWeek()+1;
            numitor += pond;
        }
        return (float) numarator / numitor;


    }

    public float medieTema(Long temaId) {
        Tema tema = (Tema) repoT.findOne(temaId);
        Iterable<Nota> note = getAll();
        float numarator = 0;

        for (Nota n : note) {
            if (repoT.findOne(n.getIdTema()) == tema) {
                numarator += n.getValue();
            }
        }
        List<Student> studenti = new ArrayList<Student>((Collection<? extends Student>) repoS.findAll());
        return (float)numarator / studenti.size();
    }
    public String worstTema(){
        Iterable<Tema> teme = repoT.findAll();
        float worst=11;
        String temarea="Toate temele de 10!";
        for(Tema t : teme){
            LocalDateTime local = LocalDateTime.of(startSemesterAux, LocalTime.MIDNIGHT);
            if (t.getDeadlineWeek()<ss.getWeek(local))
                if  (worst>medieTema(t.getId())){
                    worst = medieTema(t.getId());
                    temarea=t.getDesc();
                }

        }
        return temarea;
    }

    public Iterable<Student> studentiTrecuti(){
        List<Student> studs = (List<Student>) repoS.findAll();
        return studs.stream().filter(x -> medieStudent(x.getId()) >= 4).collect(Collectors.toList());
    }

    public boolean toateTeme(Long studentID){
        return StreamSupport.stream(repoN.findAll().spliterator(),false)
                .filter(nota ->{
                    return nota.getIdStudent() == studentID && ss.getWeek(nota.getData()) <= repoT.findOne(nota.getIdTema()).getDeadlineWeek();
                }).count() == StreamSupport.stream(repoT.findAll().spliterator(),false).count();
    }

}
