package appafzar.dataservice.web.entity;


import java.io.File;

public class MultiPartFile {
    public String name;
    public String filename;
    public String type;
    public File file;

    public MultiPartFile(String name, String filename, String type, File file) {
        this.name = name;
        this.filename = filename;
        this.type = type;
        this.file = file;
    }
}
