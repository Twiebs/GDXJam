
package com.gdxjam.ai.btree.task;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.components.TargetComponent;

public class HasTargetCondition extends LeafTask<Entity> {

	boolean checkSquad = false;
	
	@Override
	public void run (Entity entity) {
		boolean validTarget = false;
		Entity findTarget = entity;
		if(checkSquad){
			SquadMemberComponent squadMemberComp = Components.SQUAD_MEMBER.get(entity);
			findTarget = squadMemberComp.squad;
		}
		

		if (Components.TARGET.has(findTarget)) {
			TargetComponent targetComp = Components.TARGET.get(findTarget);
			if (targetComp.target != null) {
				validTarget = true;
			}
		}
		if (validTarget) {
			success();
		} else {
			fail();
		}
	}

	@Override
	protected Task<Entity> copyTo (Task<Entity> task) {
		// TODO Auto-generated method stub
		return null;
	}

}