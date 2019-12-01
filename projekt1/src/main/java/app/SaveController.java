package app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SaveController {

    private String fileName;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtFileName;

    @FXML
    private Button buttonSave;

    @FXML
    void buttonSavePressed(ActionEvent event) {
        fileName = txtFileName.getText();
        Stage stage = (Stage) buttonSave.getScene().getWindow();
        stage.close();
    }

    @FXML
    public String getFileName () {
        return fileName;
    }

    @FXML
    void initialize() {
        assert txtFileName != null : "fx:id=\"txtFileName\" was not injected: check your FXML file 'save.fxml'.";
        assert buttonSave != null : "fx:id=\"buttonSave\" was not injected: check your FXML file 'save.fxml'.";

    }
}
