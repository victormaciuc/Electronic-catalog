package business;

import configuration.ApplicationContext;
import domain.*;
import events.ChangeEvent;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import memory.InXMLNotaRepository;
import memory.InXMLStudentRepository;
import memory.InXMLTemaRepository;
import observer.Observer;
import validator.ValidationNota;
import validator.ValidationStudent;
import validator.ValidationTema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class MainViewController2 implements Observer<ChangeEvent> {


    private StructuraSemestru ss = new StructuraSemestru(2019, 1);


    private ValidationStudent validS = new ValidationStudent();
    private ValidationTema validT = new ValidationTema();
    private ValidationNota validN = new ValidationNota();

    private InXMLStudentRepository repoS = new InXMLStudentRepository(validS, "data/student.xml");
    private InXMLTemaRepository repoT = new InXMLTemaRepository(validT, "data/tema.xml");
    private InXMLNotaRepository repoN = new InXMLNotaRepository(validN, "data/nota.xml");

    private ValidationNota validN2 = new ValidationNota();
    InXMLNotaRepository repoN2 = new InXMLNotaRepository(validN2, "data/nota.xml");
    private ServiceNota serv = new ServiceNota(repoN, repoS, repoT, repoN2);

    private ObservableList<Student> modelS = FXCollections.observableArrayList();
    private ObservableList<NotaDTO2> modelN = FXCollections.observableArrayList();
    private ObservableList<Tema> modelT = FXCollections.observableArrayList();
    private ObservableList<String> modelComboBox = FXCollections.observableArrayList();


    @FXML
    TableView<Student> tableViewStudent;
    @FXML
    TableColumn<Student, String> tableColumID;
    @FXML
    TableColumn<Student, String> tableColumNume;
    @FXML
    TableColumn<Student, String> tableColumGrupa;
    @FXML
    TableColumn<Student, String> tableColumMail;

    @FXML
    TableView<Tema> tableViewTema;
    @FXML
    TableColumn<Tema, String> tableColumID2;
    @FXML
    TableColumn<Tema, String> tableColumDesc;
    @FXML
    TableColumn<Tema, String> tableColumStartWeek;
    @FXML
    TableColumn<Tema, String> tableColumDeadlineWeek;

    @FXML
    TableView<NotaDTO2> tableViewNote;
    @FXML
    TableColumn<NotaDTO2, String> tableColumStudent;
    @FXML
    TableColumn<NotaDTO2, String> tableColumTema;
    @FXML
    TableColumn<NotaDTO2, String> tableColumNota;

    @FXML
    TextField textField1;
    @FXML
    RadioButton buttonStudent;
    @FXML
    RadioButton buttonTema;
    @FXML
    RadioButton buttonNota;

    @FXML
    ComboBox comboBoxTema;

    @FXML
    public void initialize() {
        //
        tableColumID.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
        tableColumNume.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        tableColumGrupa.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        tableColumMail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        tableViewStudent.setItems(modelS);

        tableColumID2.setCellValueFactory(new PropertyValueFactory<Tema, String>("id"));
        tableColumDesc.setCellValueFactory(new PropertyValueFactory<Tema, String>("desc"));
        tableColumStartWeek.setCellValueFactory(new PropertyValueFactory<Tema, String>("startWeek"));
        tableColumDeadlineWeek.setCellValueFactory(new PropertyValueFactory<Tema, String>("deadlineWeek"));
        tableViewTema.setItems(modelT);

        tableColumStudent.setCellValueFactory(new PropertyValueFactory<NotaDTO2, String>("studentName"));
        tableColumTema.setCellValueFactory(new PropertyValueFactory<NotaDTO2, String>("temaName"));
        tableColumNota.setCellValueFactory(new PropertyValueFactory<NotaDTO2, String>("nota"));
        tableViewNote.setItems(modelN);

        comboBoxTema.setItems(modelComboBox);

        textField1.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                initModelStudent2(textField1.getText());
                if (buttonNota.isSelected())
                    initModelNote2(textField1.getText());
            }
        });


    }

    private void initModelNote2(String txt) {
        List<NotaDTO2> students = serv.findAllNote(txt);
        modelN.setAll(students);
    }

    private void initModelStudent2(String txt) {
        List<Student> students = serv.findAllStudents(txt);
        modelS.setAll(students);
    }

    public void init() {
        serv.addObserver(this);
        initModelStudent();
        initModelTema();
        initModelNote();
        initComboBox();
        comboBoxTema.getSelectionModel().selectLast();
    }

    private void initComboBox() {


        Iterable<Tema> tema;
        List<Tema> temaList;

        List<String> rez;


        tema = serv.findAllTema();
        temaList = StreamSupport.stream((tema.spliterator()), false)
                .collect(Collectors.toList());
        //}

        rez = temaList.stream().map(x -> x.getDesc()).collect(Collectors.toList());
        modelComboBox.setAll(rez);
    }

    private void initModelStudent() {
        Iterable<Student> students = serv.findAllStudents();
        List<Student> studentList = StreamSupport.stream(students.spliterator(), false)
                .collect(Collectors.toList());
        modelS.setAll(studentList);
    }

    private void initModelTema() {
        Iterable<Tema> note = serv.findAllTema();
        List<Tema> temeList = StreamSupport.stream(note.spliterator(), false)
                .collect(Collectors.toList());
        modelT.setAll(temeList);
    }

    private void initModelNote() {
        Iterable<Nota> note = serv.getAll();
        List<NotaDTO2> noteList = StreamSupport.stream(note.spliterator(), false)
                .map(nota -> new NotaDTO2(serv.findStudent(nota.getIdStudent()).getNume(), serv.findTema(nota.getIdTema()).getDesc(), ss.getWeek(nota.getData()), nota.getValue()))
                .collect(Collectors.toList());
        modelN.setAll(noteList);
    }


    @Override
    public void update(ChangeEvent changeEvent) {
        initModelStudent();
        initModelTema();
        initModelNote();
        initComboBox();
    }

    public void showStudentEditDialog(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/EditStudentView.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Student");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditStudentController editStudentController = loader.getController();
            editStudentController.setService(serv, dialogStage, student);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showNotaEditDialog(Nota nota) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/EditNotaView.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Nota");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditNotaController editNotaController = loader.getController();

            Student selected = (Student) tableViewStudent.getSelectionModel().getSelectedItem();
            Long idS;
            if (selected == null)
                idS = 0L;
            else
                idS = selected.getId();

            editNotaController.setService(serv, dialogStage, nota, serv.getIdTema(comboBoxTema.getValue().toString()), idS);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handeAdd(ActionEvent actionEvent) {
        if (buttonStudent.isSelected()) {
            showStudentEditDialog(null);
        }
        if (buttonNota.isSelected()) {
            showNotaEditDialog(null);
        }
    }

    public void handleRaport(ActionEvent actionEvent){
        try{

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RaportView.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Rapoarte");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            RaportController raportController = loader.getController();
            raportController.setService(serv, dialogStage);
            raportController.init();
            dialogStage.show();


        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handeDelete(ActionEvent actionEvent) {

        boolean ok = false;

        if (!buttonNota.isSelected() && !buttonStudent.isSelected() && !buttonTema.isSelected()) {

            try {
                Entity selected = (Entity) tableViewStudent.getSelectionModel().getSelectedItem();
                Entity selected2 = (Entity) tableViewTema.getSelectionModel().getSelectedItem();
                Entity selected3 = (Entity) tableViewNote.getSelectionModel().getSelectedItem();
                if (selected == null && selected2 == null & selected3 == null)
                    MessageAlert.showErrorMessage(null, "Nu ati selectat nici o entitate!");
                else {
                    if (selected != null) {
                        serv.deleteStudent(((Student) selected).getId());
                        ok = true;
                    }
                    if (selected2 != null) {
                        serv.deleteTema(((Tema) selected2).getId());
                        ok = true;
                    }
                    if (!ok) {
                        NotaDTO2 selected4 = (NotaDTO2) selected3;
                        Long idT = serv.getIdTema(selected4.getTemaName());
                        Long idS = serv.getIdStudent(selected4.getStudentName());
                        serv.deleteNota(idS, idT);
                    }
                }
            } catch (Exception ignored) {

            }
        } else if (buttonStudent.isSelected()) {
            Student selected = (Student) tableViewStudent.getSelectionModel().getSelectedItem();
            if (selected == null)
                MessageAlert.showErrorMessage(null, "Nu ati selectat niciun student!");
            else
                serv.deleteStudent(selected.getId());
        } else if (buttonTema.isSelected()) {
            Tema selected = (Tema) tableViewTema.getSelectionModel().getSelectedItem();
            if (selected == null)
                MessageAlert.showErrorMessage(null, "Nu ati selectat nicio tema!");
            else
                serv.deleteTema(selected.getId());
        } else {
            NotaDTO2 selected = (NotaDTO2) tableViewNote.getSelectionModel().getSelectedItem();
            if (selected == null)
                MessageAlert.showErrorMessage(null, "Nu ati selectat nicio nota!");
            else {
                Long idT = serv.getIdTema(selected.getTemaName());
                Long idS = serv.getIdStudent(selected.getStudentName());
                serv.deleteNota(idS, idT);
            }

        }
    }

    public void handeUpdate(ActionEvent actionEvent) {
        if (buttonStudent.isSelected()) {
            Student selected = tableViewStudent.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showStudentEditDialog(selected);
            } else
                MessageAlert.showErrorMessage(null, "Nu ati selectat nici un student");
        }
        if (buttonNota.isSelected()) {
            NotaDTO2 selected = tableViewNote.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Nota selected1 = serv.findNota(selected);
                showNotaEditDialog(selected1);
            } else
                MessageAlert.showErrorMessage(null, "Nu ati selectat nici un student");
        }
    }

}
