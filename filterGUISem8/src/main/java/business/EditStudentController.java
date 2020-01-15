package business;

import domain.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.awt.*;
import javafx.scene.control.TextField;
import validator.ValidationException;


public class EditStudentController {

    private ServiceNota serv;
    private Stage dialogStage;
    private Student student;

    @FXML
    private TextField textFieldID;
    @FXML
    private TextField textFieldNume;
    @FXML
    private TextField textFieldGrupa;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldProf;

    @FXML
    private void initialize() {

    }


    public void setService(ServiceNota serv,Stage stage,Student student) {
        this.serv = serv;
        this.dialogStage = stage;
        this.student = student;
        if(null != student) {
            setFields(student);
        }
    }

    private void clearFields() {
        textFieldID.setText("");
        textFieldNume.setText("");
        textFieldGrupa.setText("");
        textFieldEmail.setText("");
        textFieldProf.setText("");
    }

    private void setFields(Student s)
    {
        textFieldID.setText(String.valueOf(s.getId()));
        textFieldNume.setText(s.getNume());
        textFieldGrupa.setText(String.valueOf(s.getGrupa()));
        textFieldEmail.setText(s.getEmail());
        textFieldProf.setText(s.getProf());
    }

    private void saveMessage(Student m)
    {
        try {

            Student s = this.serv.addStudent(m.getId(),m.getNume(),m.getGrupa(),m.getEmail(),m.getProf());
            if (s==null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Salvare student","Studentul a fost salvat");
            else
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Salvare student","ID este deja folosit!");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }

    private void updateMessage(Student m)
    {
        try {
            Student s= this.serv.updateStudent(m.getId(),m.getNume(),m.getGrupa(),m.getEmail(),m.getProf());
            if (s!=null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Modificare student","Studentul a fost modificat");
        }catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleAdd(ActionEvent actionEvent) {
        try {
            Long id =  Long.valueOf(textFieldID.getText());
            String nume = textFieldNume.getText();
            int grupa = Integer.valueOf(textFieldGrupa.getText());
            String email = textFieldEmail.getText();
            String cadruDidactic = textFieldProf.getText();
            Student m = new Student(id,nume, grupa, email, cadruDidactic);
           if (null == this.student)
               saveMessage(m);
            else
                updateMessage(m);
        }catch (Exception e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
