package com.example.lovecalculator.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Random;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Result implements Serializable{
    
    private String fname;
    private String sname;
    private String percentage;
    private String result;
    private String id;
    private String compatibility;

    public String getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(String compatibility) {
        this.compatibility = compatibility;
    }

    public String getId() {
        return id;
    }

    public Result() {
        this.id = this.generateId(8);
    }

    private synchronized String generateId(int numChars) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numChars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, numChars);
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getSname() {
        return sname;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }
    public String getPercentage() {
        return percentage;
    }
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public static Result create(String json) throws IOException{
        Result r = new Result();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader reader = Json.createReader(is);
            JsonObject o = reader.readObject();
            r.setFname(o.getString("fname"));
            r.setSname(o.getString("sname"));
            r.setResult(o.getString("result"));
            r.setPercentage(o.getString("percentage"));
            if (Integer.parseInt(o.getString("percentage"))>75){
                r.setCompatibility("compatible!");
            } else {
                r.setCompatibility("not compatible!");
            }
    }
    return r;
    
}


}
