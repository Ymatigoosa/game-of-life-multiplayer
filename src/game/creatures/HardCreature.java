/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.creatures;

import game.Field;
import game.Player;
import game.Position;

/**
 * Существо с конкретными условиями для жизни
 * @author Пахомов Дмитрий
 */
public class HardCreature extends Creature {

    public HardCreature(Field field, Player owner) {
        super(field, owner);
    }
    
    public HardCreature(Field field, Position position, Player owner) {
        super(field, position, owner);
    }

    @Override
    protected int[] creaturesAroundForSurvive() {
        int[] r = {3,4};
        return r;
    }

    @Override
    protected int creaturesNeedToBurn() {
        return 4;
    }
    
    @Override
    protected String typeName() {
        return "HardCreature";
    }
    
    @Override
    public Creature deepCopy() {
        return new HardCreature(field(), getPosition(), owner());
    }
}
