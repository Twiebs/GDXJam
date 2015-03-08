package com.gdxjam.utils;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.GameManager;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.SquadSpawnerSystem;

public class DeveloperTools {
	
	public static boolean ingoreResources = true;
	
	public static void spawnEnemyAtCursor(){
		PooledEngine engine = GameManager.getEngine();
		Vector2 pos = engine.getSystem(CameraSystem.class).screenToWorldCords(Gdx.input.getX(), Gdx.input.getY());
		
		SquadSpawnerSystem.spawnSquad(pos, Constants.enemyFaction, 1);
	}

}
