package business;

import domain.Nota;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import validator.ValidationException;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EditNotaController {

    private ServiceNota serv;
    private Stage dialogStage;
    private Nota nota;
    private Long idStudent;
    private Long idTema;

    @FXML
    TextField textFieldIdStudent;
    @FXML
    TextField textFieldNota;
    @FXML
    TextField textFieldData;
    @FXML
    TextField textFieldIntarziere;
    @FXML
    TextArea textAreaFeedback;

    private LocalDate data = LocalDate.now();

    @FXML
    private void initialize() {
        textFieldData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                data = LocalDate.parse(textFieldData.getText());
                setFields2();
            }
        });
        textFieldIntarziere.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setFields2();
            }
        });
    }

    public void setService(ServiceNota serv, Stage stage, Nota nota, Long idTema, Long idStudent) {
        this.serv = serv;
        this.dialogStage = stage;
        this.nota = nota;
        this.idTema = idTema;
        this.idStudent = idStudent;
        if (null != nota) {
            data = nota.getData().toLocalDate();
            setFields(nota);
        } else {
            setFieldsData();
        }
    }

    private void setFieldsData() {
        textFieldData.setText(String.valueOf(data));
        setFields2();
    }

    private void setFields2() {

        textAreaFeedback.setText("");

        if (this.idStudent != 0)
            textFieldIdStudent.setText(String.valueOf(idStudent));
        Float motivare;


        LocalDateTime d1 = LocalDateTime.of(data, LocalTime.NOON);
        Long idT = this.idTema;
        int intarziere = serv.verificaSaptIntarziere(idT, d1);


        if (intarziere > 0) {
            try {
                motivare = Float.valueOf(textFieldIntarziere.getText());
            } catch (Exception ignored) {
                motivare = 0f;
                textFieldIntarziere.setText(String.valueOf(0));
            }

            float dif = intarziere - motivare;

            if (dif > 0 && dif < 3) {

                float nota = serv.calcNotaComun(motivare, idT, 10f, d1);
                textAreaFeedback.setText("NOTA A FOST DIMINUATA DIN CAUZA INTARZIERILOR\n" + "NOTA MAXIMA:" + String.valueOf(nota));
            }
            if (dif >= 3)
                textAreaFeedback.setText("TEMA A FOST PREDATA PREA TARZIU");
        }
    }

    private void clearFields() {
        textFieldIdStudent.setText("");
        textFieldNota.setText("");
        textFieldData.setText("");
        textFieldIntarziere.setText("");
        textAreaFeedback.setText("");
    }


    private void setFields(Nota s) {

        textFieldIdStudent.setText(String.valueOf(s.getIdStudent()));
        LocalDate d = s.getData().toLocalDate();
        data = d;
        textFieldData.setText(String.valueOf(d));
        textFieldNota.setText(String.valueOf(s.getValue()));
        textAreaFeedback.setText(s.getFeedback());
    }

    public void handleAddNota(ActionEvent actionEvent) {
        try {
            Long idS = Long.valueOf(textFieldIdStudent.getText());
            Long idT = this.idTema;
            Float nota = Float.valueOf(textFieldNota.getText());
            String feedback = textAreaFeedback.getText();
            LocalDate d = LocalDate.parse(textFieldData.getText());
            LocalDateTime d1 = LocalDateTime.of(d, LocalTime.NOON);

            int intarziere = serv.verificaSaptIntarziere(idT, d1);


            if (intarziere > 0) {
                Float motivare = Float.valueOf(textFieldIntarziere.getText());
                nota = serv.calcNotaComun(motivare, idT, nota, d1);
            }

            Nota n = new Nota(idS, idT, d1, nota, feedback);
            if (null == this.nota)
                saveMessage(n);
            else
                updateMessage(n);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void updateMessage(Nota n) {
    }

    private void saveMessage(Nota n) {
        try {

            Nota s = serv.addNota(n.getIdStudent(), n.getIdTema(), n.getFeedback(), n.getValue(), n.getData());
            if (s == null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvare nota", "Nota a fost salvata");
            else
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvare nota", "Studentul are deja nota la tema!");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleCancelNota(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
