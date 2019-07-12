package com.QUeM.TreGStore.GiocoPacman.entity;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.QUeM.TreGStore.GiocoPacman.Direction;
import com.QUeM.TreGStore.GiocoPacman.PacmanGame;
import com.QUeM.TreGStore.GiocoPacman.entity.CutsceneActor.Cutscene.Sequence;
import com.QUeM.TreGStore.GiocoPacman.entity.CutsceneActor.Cutscene.StartPoint;

public class CutsceneSteak extends CutsceneActor {

    private static final Map<Integer, Cutscene> CUTSCENES;
    static {
        Map<Integer, Cutscene> css = new HashMap<Integer, Cutscene>();
        css.put(
            Integer.valueOf(2),
            new Cutscene(
                new StartPoint(32, 9.5f),
                new Sequence[] {
                    new Sequence(
                        new Cutscene.Move(Direction.NONE, 0)),
                    new Sequence(
                        new Cutscene.Move(Direction.NONE, 0)),
                    new Sequence(
                        new Cutscene.Move(Direction.NONE, 0)),
                    new Sequence(
                        new Cutscene.Move(Direction.NONE, 0)),
                    new Sequence(
                        new Cutscene.Move(Direction.NONE, 0)),
                }));
        CUTSCENES = Collections.unmodifiableMap(css);
    }
    
    CutsceneSteak(Bitmap sourceImage, PacmanGame game) {
        super(sourceImage, game);
    }
    
    @Override
    public void init() {
        super.init();
        getAppearance().setOrder(109);
    }

    @Override
    Cutscene getCutscene() {
        return CUTSCENES.get(Integer.valueOf(game.getCutsceneId()));
    }

    @Override
    void setMode(Cutscene cutscene) {
    }

    @Override
    public int[] getImagePos() {
        int sequenceId = game.getCutsceneSequenceId();
        double time = game.getCutsceneTime();
        int x = sequenceId == 1
                    ? time > 60
                        ? 1
                        : time > 45
                            ? 2
                            : 3
                    : sequenceId == 2
                        ? 3
                        : sequenceId == 3 || sequenceId == 4
                            ? 4
                            : 0;
        int y = 13;
        
        return new int[] { y, x };
    }
}
