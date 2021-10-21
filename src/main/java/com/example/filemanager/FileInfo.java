package com.example.filemanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class FileInfo {
    public enum FileType{
        FILE("F"), DIRECTORY("D");

        private String name;

        String getName(){
            return name;
        }

        FileType(String name){
            this.name = name;
        }
    }

    private String filename;
    private FileType type;
    private long size;
    private LocalDateTime lasModified;

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public LocalDateTime getLasModified() {
        return lasModified;
    }
    public void setLasModified(LocalDateTime lasModified) {
        this.lasModified = lasModified;
    }
    public FileType getType() {
        return type;
    }
    public void setType(FileType type) {
        this.type = type;
    }

    public FileInfo(Path path){
        try {
            this.filename = path.getFileName().toString();
            this.size = Files.size(path);
            this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;
            if(this.type == FileType.DIRECTORY){
                this.size = -1L;
            }
            this.lasModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(0));
        }catch (IOException E){
            throw new RuntimeException("Unable to create file info from path");
        }
    }
}
