package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.sun.javafx.charts.Legend;
import components.Planet;
import exceptions.IncorrectDistanceException;
import exceptions.IncorrectEaException;
import exceptions.IncorrectEccentricityException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import methods.*;

public class ProjectController {

    private final ObservableList<String> methodsList = FXCollections.observableArrayList("Bisection", "Regula Falsi", "Fixed point iteration", "Newtons Method", "Secant Method");
    private static int n=0;
    private ArrayList<String> legendStyles = new ArrayList<>();
    private ArrayList<String> seriesNames = new ArrayList<>();
    private Alert warningAlert = new Alert(Alert.AlertType.WARNING, "Something went wrong");
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Something went wrong");


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldDistance;

    @FXML
    private TextField textFieldEccentricity;

    @FXML
    private ChoiceBox<String> methodBox;

    @FXML
    private TextField textFieldEa;

    @FXML
    private ColorPicker color;

    @FXML
    private LineChart<Number, Number> chart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Button btnDraw;

    @FXML
    private Button btnMercury;

    @FXML
    private Button btnVenus;

    @FXML
    private Button btnEarth;

    @FXML
    private Button btnMars;

    @FXML
    private Button btnJupiter;

    @FXML
    private Button btnSaturn;

    @FXML
    private Button btnUranus;

    @FXML
    private Button btnNeptune;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnSave;

    @FXML
    void btnClearPressed(ActionEvent event) {
        chart.getData().clear();
        legendStyles.clear();
        seriesNames.clear();
        n=0;
    }

    @FXML
    void btnDrawPressed(ActionEvent event) {
        try {
            n++;
            String name = textFieldName.getText();
            double distance = Double.parseDouble(textFieldDistance.getText());
            if (distance<=0)
                throw new IncorrectDistanceException();
            double eccentricity = Double.parseDouble(textFieldEccentricity.getText());
            if (eccentricity<=0)
                throw new IncorrectEccentricityException();
            Planet planet = new Planet(name, eccentricity, distance);

            double ea = Double.parseDouble(textFieldEa.getText());
            if (ea<=0)
                throw new IncorrectEaException();
            if (ea > 1)
                showWarning("Error value you entered is high, you should consider changing it to smaller value.");

            String method = methodBox.getValue();
            SolvingMethods solvingMethods;

            if (method.equals("Bisection"))
                solvingMethods = new Bisection((x) -> 1+(eccentricity*Math.sin(x))-x, 0, Math.PI*2);
            else if (method.equals("Regula Falsi"))
                solvingMethods = new RegulaFalsi((x) -> 1+(eccentricity*Math.sin(x))-x, 0, Math.PI*2);
            else if (method.equals("Fixed point iteration"))
                solvingMethods = new FixedPointIteration((x) -> 1+(eccentricity*Math.sin(x))-x, 0);
            else if (method.equals("Newtons Method"))
                solvingMethods = new NewtonsMethod((x) -> 1+(eccentricity*Math.sin(x))-x, 0);
            else
                solvingMethods = new SecantMethod((x) -> 1+(eccentricity*Math.sin(x))-x, 1, 0);

            ArrayList<double[]> trajectory = planet.trajectory(solvingMethods, ea);

            XYChart.Series series = new XYChart.Series();

            for (double[] tab : trajectory)
                series.getData().add( new XYChart.Data(tab[0],tab[1]));

            series.setName(name);
            seriesNames.add(name);

            chart.getData().add(series);

            String rgb = String.format("%d, %d, %d",
                    (int) (color.getValue().getRed() * 255),
                    (int) (color.getValue().getGreen() * 255),
                    (int) (color.getValue().getBlue() * 255));

            for(int index = 0; index<chart.getData().get(n-1).getData().size(); index++){
                XYChart.Data dataPoint = chart.getData().get(n-1).getData().get(index);
                Node lineSymbol = dataPoint.getNode().lookup(".chart-line-symbol");
                lineSymbol.setStyle("-fx-background-color: rgb(" + rgb + "), rgb(" + rgb + ");" +
                        "-fx-padding: 1px 1px 1px 1px;");
            }

            for(Node node : chart.getChildrenUnmodifiable()){
                if(node instanceof Legend) {
                    legendStyles.add("-fx-background-color: rgb(" + rgb + "), rgb(" + rgb + ");");
                    for (int i=0; i<n; i++) {
                            ((Legend) node).getItems().get(i).getSymbol().setStyle(legendStyles.get(i));
                    }
                }
            }
        }
        catch (IncorrectDistanceException e1) {
            showError(e1.getMessage());
            textFieldDistance.setText("1");
            n--;
        }
        catch (IncorrectEccentricityException e2) {
            showError(e2.getMessage());
            textFieldEccentricity.setText("0.0167");
            n--;
        }
        catch (IncorrectEaException e3) {
            showError(e3.getMessage());
            textFieldEa.setText("0.01");
            n--;
        }
        catch (Exception e) {n--;}
    }

    @FXML
    void btnSavePressed(ActionEvent event) {
        if (chart.getData().size() == 0)
            showWarning("Before saving your trajectory you have to add a series to the chart! Draw trajectory for your planet.");
        else
            openModalAndGetMessage();
    }

    private void openModalAndGetMessage(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/save.fxml"));
            Parent root = loader.load();
            SaveController saveController = loader.getController();
            Stage saveStage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/fxml/project.css");
            saveStage.setScene(scene);
            saveStage.initModality(Modality.WINDOW_MODAL);
            saveStage.setTitle("Save your trajectory");
            saveStage.getIcons().add(new Image(this.getClass().getResource("/icons/saveIcon.jpg").toString()));
            saveStage.showAndWait();

            String fileName = saveController.getFileName();

            if (!(fileName.endsWith(".txt")))
                fileName = (fileName + ".txt");

            File file = new File(fileName);
            FileWriter fileWriter = new FileWriter(file);

            for(int s = 0; s<n; s++){
                fileWriter.write(chart.getData().get(s).getName() + "\n");
                fileWriter.write("x\ty\n");

                for (int i=0; i < chart.getData().get(s).getData().size(); i++)
                    fileWriter.write(chart.getData().get(s).getData().get(i).getXValue() + "\t" + chart.getData().get(s).getData().get(i).getYValue() + "\n");

                fileWriter.write("\n");
            }

            fileWriter.close();

        } catch (IOException e) {
            showError("Incorrect path and / or file name.");
            System.err.println(e);
        }

    }

    void showWarning(String message) {
        warningAlert.setContentText(message);
        warningAlert.show();
    }

    void showError(String message) {
        errorAlert.setContentText(message);
        errorAlert.show();
    }

    @FXML
    void btnEarthPressed(ActionEvent event) {

        textFieldDistance.setText("1");
        textFieldEa.setText("0.1");
        textFieldEccentricity.setText("0.0167");
        textFieldName.setText("Earth");

    }

    @FXML
    void btnJupiterPressed(ActionEvent event) {

        textFieldDistance.setText("5.203");
        textFieldEa.setText("0.1");
        textFieldEccentricity.setText("0.0484");
        textFieldName.setText("Jupiter");

    }

    @FXML
    void btnMarsPressed(ActionEvent event) {

        textFieldDistance.setText("1.524");
        textFieldEa.setText("0.1");
        textFieldEccentricity.setText("0.0934");
        textFieldName.setText("Mars");

    }

    @FXML
    void btnMercuryPressed(ActionEvent event) {

        textFieldDistance.setText("0.387");
        textFieldEa.setText("0.1");
        textFieldEccentricity.setText("0.2056");
        textFieldName.setText("Mercury");

    }

    @FXML
    void btnNeptunePressed(ActionEvent event) {

        textFieldDistance.setText("30.069");
        textFieldEa.setText("0.1");
        textFieldEccentricity.setText("0.0086");
        textFieldName.setText("Neptune");

    }

    @FXML
    void btnSaturnPressed(ActionEvent event) {

        textFieldDistance.setText("9.537");
        textFieldEa.setText("0.1");
        textFieldEccentricity.setText("0.0542");
        textFieldName.setText("Saturn");

    }

    @FXML
    void btnUranusPressed(ActionEvent event) {

        textFieldDistance.setText("19.191");
        textFieldEa.setText("0.1");
        textFieldEccentricity.setText("0.0472");
        textFieldName.setText("Uranus");

    }

    @FXML
    void btnVenusPressed(ActionEvent event) {

        textFieldDistance.setText("0.723");
        textFieldEa.setText("0.1");
        textFieldEccentricity.setText("0.0068");
        textFieldName.setText("Venus");

    }

    @FXML
    void initialize() {
        assert textFieldName != null : "fx:id=\"textFieldName\" was not injected: check your FXML file 'project.fxml'.";
        assert textFieldDistance != null : "fx:id=\"textFieldDistance\" was not injected: check your FXML file 'project.fxml'.";
        assert textFieldEccentricity != null : "fx:id=\"textFieldEccentricity\" was not injected: check your FXML file 'project.fxml'.";
        assert methodBox != null : "fx:id=\"methodBox\" was not injected: check your FXML file 'project.fxml'.";
        assert textFieldEa != null : "fx:id=\"textFieldEa\" was not injected: check your FXML file 'project.fxml'.";
        assert color != null : "fx:id=\"color\" was not injected: check your FXML file 'project.fxml'.";
        assert chart != null : "fx:id=\"chart\" was not injected: check your FXML file 'project.fxml'.";
        assert xAxis != null : "fx:id=\"xAxis\" was not injected: check your FXML file 'project.fxml'.";
        assert yAxis != null : "fx:id=\"yAxis\" was not injected: check your FXML file 'project.fxml'.";
        assert btnDraw != null : "fx:id=\"btnDraw\" was not injected: check your FXML file 'project.fxml'.";
        assert btnMercury != null : "fx:id=\"btnMercury\" was not injected: check your FXML file 'project.fxml'.";
        assert btnVenus != null : "fx:id=\"btnVenus\" was not injected: check your FXML file 'project.fxml'.";
        assert btnEarth != null : "fx:id=\"btnEarth\" was not injected: check your FXML file 'project.fxml'.";
        assert btnMars != null : "fx:id=\"btnMars\" was not injected: check your FXML file 'project.fxml'.";
        assert btnJupiter != null : "fx:id=\"btnJupiter\" was not injected: check your FXML file 'project.fxml'.";
        assert btnSaturn != null : "fx:id=\"btnSaturn\" was not injected: check your FXML file 'project.fxml'.";
        assert btnUranus != null : "fx:id=\"btnUranus\" was not injected: check your FXML file 'project.fxml'.";
        assert btnNeptune != null : "fx:id=\"btnNeptune\" was not injected: check your FXML file 'project.fxml'.";
        assert btnClear != null : "fx:id=\"btnClear\" was not injected: check your FXML file 'project.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'project.fxml'.";

        methodBox.setItems(methodsList);
        methodBox.setValue("Regula Falsi");

        chart.setLegendSide(Side.RIGHT);
        n = 0;

        color.setValue(new Color(0.35, 0.45, 0.85, 1));

        warningAlert.setTitle("Warning");
        warningAlert.setHeaderText(null);
        warningAlert.setGraphic(new ImageView(new Image("/icons/warningIcon.png")));
        DialogPane dialogPane = warningAlert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/fxml/project.css").toExternalForm());
        Stage stage = (Stage) warningAlert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/icons/warningIcon.png").toString()));

        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(null);
        errorAlert.setGraphic(new ImageView(new Image("/icons/errorIcon.png")));
        DialogPane dialogPane2 = errorAlert.getDialogPane();
        dialogPane2.getStylesheets().add(getClass().getResource("/fxml/project.css").toExternalForm());
        Stage stage2 = (Stage) errorAlert.getDialogPane().getScene().getWindow();
        stage2.getIcons().add(new Image(this.getClass().getResource("/icons/errorIcon.png").toString()));

    }
}
