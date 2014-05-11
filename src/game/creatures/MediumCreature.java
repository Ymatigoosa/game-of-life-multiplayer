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
public class MediumCreature extends Creature {

    public MediumCreature(Field field, Player owner) {
        super(field, owner);
    }
    
    public MediumCreature(Field field, Position position, Player owner) {
        super(field, position, owner);
    }

    @Override
    protected int[] creaturesAroundForSurvive() {
        int[] r = {2,3};
        return r;
    }

    @Override
    protected int creaturesNeedToBurn() {
        return 3;
    }
    
    @Override
    protected String typeName() {
        return "MediumCreature";
    }

    @Override
    public Creature deepCopy() {
        return new MediumCreature(field(), getPosition(), owner());
    }
}
