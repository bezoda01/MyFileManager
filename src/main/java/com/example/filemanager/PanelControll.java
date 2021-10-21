package com.example.filemanager;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PanelControll implements Initializable {
    @FXML
    TableView<FileInfo> filesTable;

    @FXML
    ComboBox<String> diskBox;

    @FXML
    TextField pathField;


    public void btnExitAction(ActionEvent actionEvent){
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {//создаем таблицу с файлами
        TableColumn<FileInfo, String> filetypeColumn = new TableColumn<>();
        filetypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));//получаем имя тип
        filetypeColumn.setPrefWidth(24);//размер строчки где у нас будет сортировка по определенному типу имя тип и тд

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Имя");// создали колонку с именем файла
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        fileNameColumn.setPrefWidth(240);//размер колонки с именем файла

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");//создали колонку с размером файла
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setPrefWidth(120);
        fileSizeColumn.setCellFactory(column -> {

            return new TableCell<FileInfo, Long>(){
                @Override
                protected void updateItem(Long item, boolean empty){
                    super.updateItem(item, empty);
                    if(item == null || empty){
                        setText(null);
                        setStyle("");
                    }else{
                        String text = String.format("%,d bytes", item);
                        if(item == -1L){
                            text = "[Dir]";
                        }
                        setText(text);
                    }
                }

            };
        });
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> fileDateColumn = new TableColumn<>("Дата изменения");
        fileDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLasModified().format(dtf)));
        fileDateColumn.setPrefWidth(120);

        filesTable.getColumns().addAll(filetypeColumn, fileNameColumn, fileSizeColumn, fileDateColumn);//добавили обе колонки в таблицу
        filesTable.getSortOrder().add(filetypeColumn);

        diskBox.getItems().clear();
        for(Path p : FileSystems.getDefault().getRootDirectories()){
            diskBox.getItems().add(p.toString());

        }
        diskBox.getSelectionModel().select(0);

        filesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2){
                    Path path = Paths.get(pathField.getText()).resolve(filesTable.getSelectionModel().getSelectedItem().getFilename());
                    if(Files.isDirectory(path))
                    {
                        updateList(path);
                    }
                }
            }
        });

        updateList(Paths.get("."));// вызвали метод по выводу файлов в определенной директории

    }

    public void updateList(Path path){//метод по передаче всех путей устройства в таблицу
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            filesTable.getItems().clear();
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            filesTable.sort();
        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "По какой-то причине не удалось обновить список файлов", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void btnPathUpAction(ActionEvent actionEvent) {//кнопка по переходу в предыдущую директорию "Вверх"
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if(upperPath != null){
            updateList(upperPath);
        }
    }

    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }

    public String getSelectedFileName(){
        if(filesTable.isFocused())
        {
            return null;
        }
        return  filesTable.getSelectionModel().getSelectedItem().getFilename();
    }

    public String getCurrentPath(){
        return pathField.getText();
    }
}
