package com.jordanrifaey.galaga;

import com.badlogic.gdx.Game;

public class Galaga extends Game {

    @Override
    public void create() {
        this.setScreen(new PlayScreen(this));
        this.dispose();
    }
}
