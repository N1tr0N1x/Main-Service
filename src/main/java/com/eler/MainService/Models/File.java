package com.eler.MainService.Models;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import java.util.Date;

@EntityScan
public class File {

 private long idFile;
private String title;
private Long moduleID;
private String teacher_email;
private String description;
private String link;
private Date createdDate;
     public long getIdFile() {
        return idFile;
    }

    public void setIdFile(long idFile) {
        this.idFile = idFile;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public void setModuleID(Long moduleID) {
        this.moduleID = moduleID;
    }

    public Long getModuleID() {
        return moduleID;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }
    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description=description;
    }

      public String getLink(){
        return link;
    }

    public void setLink(String link){
        this.link=link;
    }

      public Date getCreatedDate(){
        return createdDate;
    }

    public void setCreatedDate(Date createdDate){
        this.createdDate=createdDate;
    }

    public File() {
    }

    public File(long idFile, String title, Long moduleID, String teacher_email, String description, String link,
     Date createdDate){
        super();
        this.idFile = idFile;
        this.title = title;
        this.moduleID = moduleID;
        this.teacher_email = teacher_email;
        this.description = description;
        this.link = link;
        this.createdDate = createdDate;
    }

}
