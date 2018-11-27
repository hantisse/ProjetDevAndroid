package com.judith.h.projetdevandroid;

import java.io.Serializable;
import java.util.ArrayList;

public class Card implements Serializable {

    private String name;
    private String scryfallID;
    private int cmc;
    private ArrayList<String> cardTypes;
    private String imgUrl;
    private String manaCost;
    private String color;
    private String description;

    public Card(){}

    public Card(String name, String scryfallID, int cmc, String manaCost, String color, ArrayList<String> cardTypes ){
        this.name = name;
        this.scryfallID = scryfallID;
        this.cmc = cmc;
        this.manaCost = manaCost;
        this.color = color;
        this.cardTypes = cardTypes;
    }

    public void setImgUrl(String url){
        imgUrl = url;
    }

    public void setDescription(String desc){
        description = description;
    }
    public String getName() {
        return name;
    }

    public int getCmc() {
        return cmc;
    }

    public ArrayList<String> getCardTypes() {
        return cardTypes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCmc(int cmc) {
        this.cmc = cmc;
    }

    public String getScryfallID() {
        return scryfallID;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getManaCost() {
        return manaCost;
    }

    public String getColor() {
        return color;
    }

    public void setScryfallID(String scryfallID) {
        this.scryfallID = scryfallID;
    }

    public void setCardTypes(ArrayList<String> cardTypes) {
        this.cardTypes = cardTypes;
    }

    public void setManaCost(String manaCost) {
        this.manaCost = manaCost;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
