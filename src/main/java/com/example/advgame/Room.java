package com.example.advgame;

import java.util.ArrayList;

public class Room {
    private int row;
    private int col;
    private boolean blocked;
    private ArrayList<NPC> npcs;
    private int gold;

    public Room(int row, int col, boolean blocked, int gold) {
        this.row = row;
        this.col = col;
        this.blocked = blocked;
        this.npcs = new ArrayList<>();
        this.gold = gold;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public ArrayList<NPC> getNPCs() {
        return npcs;
    }

    public void addNPC(NPC npc) {
        npcs.add(npc);
    }

    public void removeNPC(NPC npc) {
        npcs.remove(npc);
    }

    public boolean hasNPC() {
        return !npcs.isEmpty();
    }

    public NPC getRandomNPC() {
        if (hasNPC()) {
            int randomIndex = (int) (Math.random() * npcs.size());
            return npcs.get(randomIndex);
        }
        return null;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
