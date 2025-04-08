package com.example.advgame;


public class PlayerCharacter {
    private int hitPoints;
    private int strength;
    private int dexterity;
    private int intelligence;
    private int totalGold;

    public PlayerCharacter() {
        this.hitPoints = 20;
        this.strength = rollDice(6, 3);
        this.dexterity = rollDice(6, 3);
        this.intelligence = rollDice(6, 3);
        this.totalGold = 0;
    }

    private int rollDice(int sides, int rolls) {
        int result = 0;
        for (int i = 0; i < rolls; i++) {
            result += (int) (Math.random() * sides) + 1;
        }
        return result;
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

    public int getTotalGold() {
        return totalGold;
    }

    public void increaseTotalGold(int amount) {
        totalGold += amount;
    }

    public boolean runAway(NPC npc) {
        int roll = (int) (Math.random() * 20) + 1;
        return roll >= npc.getIntelligence();
    }

    public void takeDamage(int damage) {
        hitPoints -= damage;
    }
    public int attack(NPC npc) {
        int roll = (int) (Math.random() * 20) + 1;
        if (roll >= npc.getDexterity()) {
            int damage = this.getStrength() / 3;
            npc.setHitPoints(npc.getHitPoints() - damage);
            return damage;
        }
        return 0;
    }
}

