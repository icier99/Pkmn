package ru.mirea.pkmn;

import java.io.Serializable;

public class AttackSkill implements Serializable {
    public static final long serialVersionUID = 1L;
    String cost;
    String name;
    int damage;

    public AttackSkill(String cost, String name, int damage) {

        this.cost = cost;
        this.name = name;
        this.damage = damage;
    }

    public AttackSkill() {
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public String toString() {
        return "AttackSkill{" +
                "cost='" + cost + '\'' +
                ", name='" + name + '\'' +
                ", damage=" + damage +
                '}';
    }
}
