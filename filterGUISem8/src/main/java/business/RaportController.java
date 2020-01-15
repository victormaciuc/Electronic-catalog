package business;

import domain.NotaDTO2;
import domain.Student;
import domain.StudentDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RaportController {

    private ServiceNota serv;
    private Student student;
    private Stage dialogStage;
    private ObservableList<StudentDTO> modelStud = FXCollections.observableArrayList();

    Iterable<Student> studs;

    @FXML
    TableColumn<StudentDTO, String> tableColumStudent;

    @FXML
    TableColumn<StudentDTO, String> testari2;

    @FXML
    TableColumn<StudentDTO, Float> coloana4;

    @FXML
    TableColumn<StudentDTO, Boolean> coloana3;

    @FXML
    TableView<StudentDTO> studentTableView;

    @FXML
    TextField testare;

    @FXML
    private void initialize() {
        tableColumStudent.setCellValueFactory(new PropertyValueFactory<StudentDTO, String>("nume"));
        testari2.setCellValueFactory(new PropertyValueFactory<StudentDTO, String>("trecut"));
        coloana4.setCellValueFactory(new PropertyValueFactory<StudentDTO, Float>("medie"));
        coloana3.setCellValueFactory(new PropertyValueFactory<StudentDTO, Boolean>("temele"));
        studentTableView.setItems(modelStud);
    }

    public void setService(ServiceNota serv, Stage stage) {
        this.serv = serv;
        this.dialogStage = stage;
    }

    public void updateView(){
        List<StudentDTO> studentDTOS = new ArrayList<>();
        Iterable<Student> stud = serv.findAllStudents();
        String trecut;
        Boolean temele;

        for (Student s : stud){
            String nume = s.getNume();
            float medie = serv.medieStudent(s.getId());
            if (medie >= 4)
                trecut = "Da";
            else
                trecut = "Nu";
            temele = serv.toateTeme(s.getId());
            StudentDTO clona = new StudentDTO(nume, medie, trecut, temele);
            studentDTOS.add(clona);
        }

        modelStud.setAll(studentDTOS);
    }


    public void init(){
        Iterable<Student> students = serv.findAllStudents();
        List<Student> studentList = StreamSupport.stream(students.spliterator(), false)
                .collect(Collectors.toList());

//        modelStud.setAll(studentList);

        testare.setText(serv.worstTema());

        updateView();
    }
}
