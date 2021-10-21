package com.example.filemanager;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller {
    @FXML
    VBox LeftPanel, RightPanel;



    public void btnExitAction(ActionEvent actionEvent){
        Platform.exit();
    }

    public void copyButtonAction(ActionEvent actionEvent) {
        PanelControll LeftPC = (PanelControll) LeftPanel.getProperties().get("ctrl");
        PanelControll RightPC = (PanelControll) RightPanel.getProperties().get("ctrl");

        if(LeftPC.getSelectedFileName() == null && RightPC.getSelectedFileName() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ни один файл не выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        PanelControll srcPC = null, dstPC = null;
        if(LeftPC.getSelectedFileName() != null){
            srcPC = LeftPC;
            dstPC = RightPC;
        }
        if(RightPC.getSelectedFileName() != null){
            srcPC = RightPC;
            dstPC = LeftPC;
        }

        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFileName());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            Files.copy(srcPath, dstPath);
            dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ни удалось скопировать указанный файл", ButtonType.OK);
            alert.showAndWait();
        }

    }
/*
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

        updateList(Paths.get("."));// вызвали метод по выводу файлов в определенной директории

    }

    public void updateList(Path path){
        filesTable.getItems().clear();
        try {
            filesTable.getItems().clear();
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            filesTable.sort();
        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "По какой-то причине не удалось обновить список файлов", ButtonType.OK);
            alert.showAndWait();
        }
    }*/
}