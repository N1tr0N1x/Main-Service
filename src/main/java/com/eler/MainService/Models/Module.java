package com.eler.MainService.Models;


import org.springframework.boot.autoconfigure.domain.EntityScan;


@EntityScan
public class Module {

   
    private Long idModule;

    private String name;
 
    private int credit;

    private int coefficient;

    private int vhCours;

    private int vhTd;

    private int vhTp;

    private String emailEnsCours;

    private String emailEnsTd;

    private String emailEnsTp;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;


    }
    

    public Module(){
    }

    public Long getIdModule() {
        return idModule;
    }

    public void setIdModule(Long idModule) {
        this.idModule = idModule;
    }

    public int getVhCours() {
        return vhCours;
    }

    public void setVhCours(int vhCours) {
        this.vhCours = vhCours;
    }

    public int getVhTd() {
        return vhTd;
    }

    public void setVhTd(int vhTd) {
        this.vhTd = vhTd;
    }

    public int getVhTp() {
        return vhTp;
    }

    public void setVhTp(int vhTp) {
        this.vhTp = vhTp;
    }

    public String getEmailEnsCours() {
        return emailEnsCours;
    }

    public void setEmailEnsCours(String emailEnsCours) {
        this.emailEnsCours = emailEnsCours;
    }

    public String getEmailEnsTd() {
        return emailEnsTd;
    }

    public void setEmailEnsTd(String emailEnsTd) {
        this.emailEnsTd = emailEnsTd;
    }

    public String getEmailEnsTp() {
        return emailEnsTp;
    }

    public void setEmailEnsTp(String emailEnsTp) {
        this.emailEnsTp = emailEnsTp;
    }

    public Module(Long idModule, String name, int credit, int coefficient, int vhCours, int vhTd, int vhTp,
            String emailEnsCours, String emailEnsTd, String emailEnsTp) {
        this.idModule = idModule;
        this.name = name;
        this.credit = credit;
        this.coefficient = coefficient;
        this.vhCours = vhCours;
        this.vhTd = vhTd;
        this.vhTp = vhTp;
        this.emailEnsCours = emailEnsCours;
        this.emailEnsTd = emailEnsTd;
        this.emailEnsTp = emailEnsTp;
    }
    
}
