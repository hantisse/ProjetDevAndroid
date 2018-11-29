package com.judith.h.projetdevandroid;

import java.io.Serializable;
import java.util.ArrayList;

public class Card implements Serializable {

    private int cardId;
    private String name;
    private String scryfallID;
    private int cmc;
    private ArrayList<String> cardTypes;
    private String imgUrl;
    private String manaCost;
    private String colorIdentity;

    public Card(){cardId = -1;}

    public Card(String name, String scryfallID, int cmc, String manaCost, String color, ArrayList<String> cardTypes ){
        this.name = name;
        this.scryfallID = scryfallID;
        this.cmc = cmc;
        this.manaCost = manaCost;
        this.colorIdentity = color;
        this.cardTypes = cardTypes;
    }

    public int getCardId() {
        return this.cardId;
    }

    public void setCardId(int id) {
        this.cardId = id;
    }

    public void setImgUrl(String url){
        imgUrl = url;
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

    public String getColorIdentity() {
        return colorIdentity;
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

    public void setColorIdentity(String color) {
        this.colorIdentity = color;
    }
}
