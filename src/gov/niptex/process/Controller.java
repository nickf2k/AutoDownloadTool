package gov.niptex.process;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextField tfTerm1;
    @FXML
    private TextField tfTerm2;
    @FXML
    private ChoiceBox cbField1;
    @FXML
    private ChoiceBox cbField2;
    @FXML
    private ChoiceBox cbAndOrXor;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnPrintResult;
    @FXML
    private Button btnDownload;
    @FXML
    private Button btnChooseFile;
    @FXML
    private Button btnChooseDirectory;
    @FXML
    private RadioButton rd5Patents;
    @FXML
    private RadioButton rd10Patents;
    @FXML
    private RadioButton rdAllPatents;
    @FXML
    private Label lbFile;
    @FXML
    private Label lbDirectory;
    @FXML
    private ListView<String> lvResult;
    @FXML
    private Pane pnOption;
    @FXML
    private Label lbResult;
    @FXML
    private Label lbDownResult;
    @FXML
    private MenuItem miClose;
    @FXML
    private MenuItem mnHelp;

    private String pathDownload = null;
    private Node[] listNode = null;
    private File selectDirectory = null;
    private Task<Void> taskDownload;
    private Task<Void> taskSearch;
    private Thread threadSearch;
    private Thread threadDownload;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> fieldList = FXCollections.observableArrayList("All Fields", "Title", "Abstract");
        ObservableList<String> andOrXorList = FXCollections.observableArrayList("AND", "OR", "ANDNOT");
        cbField1.setValue("All Fields");
        cbField1.setItems(fieldList);
        cbField2.setValue("All Fields");
        cbField2.setItems(fieldList);
        cbAndOrXor.setValue("AND");
        cbAndOrXor.setItems(andOrXorList);
        lbResult.setVisible(false);
        lbDownResult.setVisible(false);
    }

    public void search(ActionEvent event) {
        initOrDestroySearch(true);
        String firstPage = getFirstPageURL();
        if (firstPage.equals("")){
            alert("no data");
            initOrDestroySearch(false);
            lbResult.setText("Tim kiem loi!");
            lbResult.setVisible(true);
            return;
        }

        threadSearch = new Thread(new Runnable() {
            @Override
            public void run() {
                onSearching(firstPage);
            }
        });
        threadSearch.setDaemon(true);
        threadSearch.start();
    } // search

    private void initOrDestroySearch(boolean init) {
        if (init) {
            lbResult.setText("on searching");
            lbResult.setVisible(init);
        }
        cbField2.setDisable(init);
        cbField1.setDisable(init);
        cbAndOrXor.setDisable(init);
        btnDownload.setDisable(init);
        btnPrintResult.setDisable(init);
        btnReset.setDisable(init);
    }

    private void onSearching(String firstPage){
        System.out.println("on search");
        Query query = new Query(firstPage);
        try {
            this.listNode = query.query();
            System.out.println("query: end");
        } catch (IOException e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    alert("read time out");
                    initOrDestroySearch(false);
                }
            });
        }
        int sizeResult;
        if (this.listNode == null){
            sizeResult = 0;
        }else {
            ArrayList<String> listResultView = new ArrayList<String>();
            for (int i = 0; i < listNode.length; i++) {
                Node node = listNode[i];
                listResultView.add(node.getID() + "--" + node.getName());
            }
            ObservableList<String> observableList = FXCollections.observableList(listResultView);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lvResult.setItems(observableList);
                    initOrDestroySearch(false);
                }
            });
            sizeResult = query.getSize();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbResult.setText("Result of searching: " + sizeResult + " patents");
                lbResult.setVisible(true);
            }
        });
    }

    public void printResult(ActionEvent event) throws IOException {
        btnPrintResult.setDisable(true);
        String filePrintPath = lbFile.getText();
        if (filePrintPath.equals("File path")) {
            alert("no file choose");
            btnPrintResult.setDisable(false);
            return;
        }
        if (listNode==null){
            alert("no data");
            btnPrintResult.setDisable(false);
            return;
        }
        FileWriter fileWriter = new FileWriter(filePrintPath);
        for (int i = 0; i<listNode.length;i++){
            fileWriter.write(listNode[i].getID()+"-------"+listNode[i].getName()+"\n");
        }
        fileWriter.close();
        System.out.println("succes wirte file");
        btnPrintResult.setDisable(false);
    } // in ket qua

    public void chooseFile(ActionEvent event){
        Stage stage = (Stage) pnOption.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chon file in ket qua");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text Files","*.txt");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(stage);
        if (file==null){
            alert("no file choose");
            return;
        }
        lbFile.setText(file.getPath());
    }//chon file in ket qya

    public void chooseDirectory(ActionEvent event) {
        Stage stage = (Stage) pnOption.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Chon noi luu du lieu");
        this.selectDirectory = directoryChooser.showDialog(stage);
        if (selectDirectory == null) {
            alert("no file choose");
            return;
        }
        System.out.println(selectDirectory.getPath());
        lbDirectory.setText(selectDirectory.getPath());
        pathDownload = selectDirectory.getPath();
    } // chon noi luu

    public void download(ActionEvent event) {
        initOrDestroyDownload(true);
        if (pathDownload == null) {
            alert("pathdownload");
            initOrDestroyDownload(false);
            return;
        }
        if (listNode == null) {
            alert("no data");
            initOrDestroyDownload(false);
            return;
        }
        btnChooseDirectory.setDisable(true);
        threadDownload = new Thread(new Runnable() {
            @Override
            public void run() {
                onDownloading();
            }
        });
        threadDownload.setDaemon(true);
        threadDownload.start();
    } // download

    private void initOrDestroyDownload(boolean init) {
        if (init){
            lbDownResult.setText("Downloading...");
            lbDownResult.setVisible(init);
        }
        btnChooseDirectory.setDisable(init);
        btnSearch.setDisable(init);
        btnDownload.setDisable(init);
    }

    private void onDownloading() {
        Download download = new Download(pathDownload);
        ArrayList<Node> downList = null;
        if (rd10Patents.isSelected()) {
            downList = getDownList(10, "little");
            System.out.println(10);
        } else if (rd5Patents.isSelected()) {
            downList = getDownList(5, "little");
            System.out.println(5);
        } else{
            System.out.println(listNode.length);
            if (listNode.length>15){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        alert("oversize");
                    }
                });
                downList = getDownList(15, "little");
                System.out.println("15");
            }else {
                downList = getDownList(0,"full");
                System.out.println("full");
            }

//            } else return;
        }
        download.setListNode(downList);
        download.download();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbDownResult.setText("Download: done!");
                lbDownResult.setVisible(true);
                initOrDestroyDownload(false);
            }
        });
        System.out.println("Download xong");

    }

    public void newPlatform(ActionEvent event){
        alert("thread waiting");
        reset(event);
        lbDownResult.setVisible(true);
        lbFile.setText("File path");
        lbDirectory.setText("Directory path");
        lbDownResult.setVisible(false);
        lbResult.setVisible(false);
        rd5Patents.setSelected(true);
        rd10Patents.setSelected(false);
        rdAllPatents.setSelected(false);
        pathDownload = null;
        System.out.println("new plf");
        if (threadSearch!=null) threadSearch.interrupt();
        if (threadDownload!=null) threadDownload.interrupt();
        initOrDestroyDownload(false);
        initOrDestroySearch(false);
    }

    public void reset(ActionEvent event) {
        tfTerm1.setText("");
        tfTerm2.setText("");
        cbField1.setValue("All Fields");
        cbField2.setValue("All Fields");
        cbAndOrXor.setValue("AND");
        lvResult.setItems(null);
        lbResult.setVisible(false);

    }//reset

    public void closePlatForm(ActionEvent event){
        if (taskSearch!= null){
            taskSearch.cancel();
            taskSearch = null;
        }
        if (taskDownload!=null){
            taskDownload.cancel();
            taskDownload= null;
        }
        Platform.exit();
    }

    public void help(ActionEvent event) throws IOException {
        System.out.println("on action help");
        Parent root = FXMLLoader.load(getClass().getResource("help.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Help");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private int alert(String type) {
        int res = 0;
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (type == "oversize") {
            alert.setContentText("Du lieu qua lon, chi cho phep download 15 patents");
            alert.showAndWait();
        } else {
            if (type == "pathdownload") {
                alert.setContentText("Chon noi download!");
            }
            if (type == "no data") {
                alert.setContentText("Khong co du lieu!");
            }
            if (type == "no file choose") {
                alert.setContentText("No file choosen!");
            }
            if (type == "read time out"){
                alert.setContentText("Thoi gian ket noi qua dai. Can internet on dinh hoac du lieu nhap vao sai quy chuan");
            }
            if (type == "thread waiting"){
                alert.setContentText("Tien trinh download hoac tim kiem se tiep tuc tien hanh!");
            }
            if (type == "du lieu sai"){
                alert.setContentText("Loi tim kiem (ki tu khong dau, khong co ki tu dac biet)!");
            }
            alert.showAndWait();

        }
        return res;
    }

    private ArrayList<Node> getDownList(int size, String option) {
        ArrayList<Node> nodeArrayList = new ArrayList<>(Arrays.asList(this.listNode));
        ArrayList<Node> downList = new ArrayList<>();
        if (size>nodeArrayList.size()) {size = nodeArrayList.size();}
        System.out.println(size);
        if (option.equals("little")) {
            for (int i = 0; i < size; i++) {
                downList.add(nodeArrayList.get(i));
                System.out.println(nodeArrayList.get(i).getID() + " stt:" + i);
            }
            return downList;
        }
        return nodeArrayList;
    }

    private String getFirstPageURL() {
        String firstPage = "http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&p=1&u=%2Fnetahtml%2FPTO%2Fsearch-bool.html&r=0&f=S&l=50";
        if (tfTerm1.getText().equals("") && tfTerm2.getText().equals("")) {
            return "";
        }
        String[] parameters = new String[5];
        parameters[0] = "&TERM1=" + tfTerm1.getText();
        parameters[1] = "&FIELD1=" + convertToParameter(cbField1.getValue().toString());
        parameters[2] = "&co1=" + convertToParameter(cbAndOrXor.getValue().toString());
        parameters[3] = "&TERM2=" + tfTerm2.getText();
        parameters[4] = "&FIELD2=" + convertToParameter(cbField2.getValue().toString());
        for (int i = 0; i < 5; i++) {
            firstPage += parameters[i];
        }
        firstPage += "&d=PTXT";
        System.out.println(firstPage);
        try {
            URL url = new URL(firstPage);
        } catch (MalformedURLException e) {
            alert("du lieu sai");
            return "";
        }
        return firstPage;
    }

    private String convertToParameter(String value) {
        if (value.equals("All Fields")) {
            value = "";
        } else if (value.equals("Title")) {
            value = "TI";
        } else if (value.equals("Abstract")) {
            value = "ABTX";
        } else if (value.equals("ANDNOT")) {
            value = "NOT";
        }
        return value;
    }

}
