package com.example.advgame;

public class NPC {
    private int hitPoints;
    private int strength;
    private int dexterity;
    private int intelligence;

    public NPC() {
        int randomNumber = (int) (Math.random() * 6) + 1;
        this.hitPoints = randomNumber;
        this.strength = randomNumber * 2;
        this.dexterity = randomNumber * 2;
        this.intelligence = randomNumber * 2;
    }


    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }
    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }


    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void takeDamage(int damage) {
        hitPoints -= damage;
        if (hitPoints < 0) {
            hitPoints = 0;
        }
    }

    public int attack(PlayerCharacter player) {
        int roll = (int) (Math.random() * 20) + 1;
        if (roll >= player.getDexterity()) {
            int damage = this.getStrength() / 3;
            player.takeDamage(damage);
            return damage;
        }
        return 0;
    }
}