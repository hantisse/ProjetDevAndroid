package com.judith.h.projetdevandroid;

public class Card {

    public enum CardType{
        CREATURE,
        ARTIFACT,
        ENCHANTMENT,
        INSTANT,
        SORCERY,
    }

    private String name;
    private String extensionTAG;
    private String cmc;
    private CardType[]cardTypes;

    public Card(String name, String extensionTAG, String cmc, CardType[] cardTypes){
        this.name = name;
        this.extensionTAG = extensionTAG;
        this.cmc = cmc;
        this.cardTypes = cardTypes;
    }

    public String getName() {
        return name;
    }

    public String getExtensionTAG() {
        return extensionTAG;
    }

    public String getCmc() {
        return cmc;
    }

    public CardType[] getCardTypes() {
        return cardTypes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExtensionTAG(String extensionTAG) {
        this.extensionTAG = extensionTAG;
    }

    public void setCmc(String cmc) {
        this.cmc = cmc;
    }

    public void setCardTypes(CardType[] cardTypes) {
        this.cardTypes = cardTypes;
    }
}
