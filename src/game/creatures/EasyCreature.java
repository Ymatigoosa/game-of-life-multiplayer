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
public class EasyCreature extends Creature {

    public EasyCreature(Field field, Player owner) {
        super(field, owner);
    }
    
    public EasyCreature(Field field, Position position, Player owner) {
        super(field, position, owner);
    }

    @Override
    protected int[] creaturesAroundForSurvive() {
        int[] r = {1,2};
        return r;
    }

    @Override
    protected int creaturesNeedToBurn() {
        return 2;
    }

    @Override
    protected String typeName() {
        return "EasyCreature";
    }
    
    @Override
    public Creature deepCopy() {
        return new EasyCreature(field(), getPosition(), owner());
    }
}
