
package com.gdxjam;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdxjam.ai.states.Messages;
import com.gdxjam.components.Components;
import com.gdxjam.components.FactionComponent;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.components.ProjectileComponent;
import com.gdxjam.components.TargetFinderComponent;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityUtils;

public class GameContactListener implements ContactListener {
	
	private static final String TAG = GameContactListener.class.getSimpleName();

	@Override
	public void beginContact (Contact contact) {
		Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();

		if (Components.PROJECTILE.has(entityA)) 
			processProjectile(entityA, entityB);
		if (Components.PROJECTILE.has(entityB)) 
			processProjectile(entityB, entityA);
		
		if(Components.SQUAD.has(entityA))
			processTargetFinder(entityA, entityB, false);
		if(Components.SQUAD.has(entityB))
			processTargetFinder(entityB, entityA, false);
	
	}

	@Override
	public void endContact (Contact contact) {
		Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();

		if(Components.SQUAD.has(entityA))
			processTargetFinder(entityA, entityB, true);
		if(Components.SQUAD.has(entityB))
			processTargetFinder(entityB, entityA, true);
	}

	@Override
	public void preSolve (Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve (Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
	

	public void processTargetFinder(Entity squad, Entity target, boolean contactEnd){
		//return if the two entities are allies
		if(EntityUtils.isSameFaction(squad, target)) return;
		
		TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(squad);
		if(Components.SQUAD.has(target)){
			targetFinder.squad(target, contactEnd);
			//Sends a message to the squad that a new target has been identified.
			MessageManager.getInstance().dispatchMessage(null, Components.FSM.get(squad), Messages.foundEnemy);
		} else if (Components.RESOURCE.has(target)){
			targetFinder.resource(target, contactEnd);
			//Sends a message to the squad that a new resource has been identified.
			MessageManager.getInstance().dispatchMessage(null, Components.FSM.get(squad), Messages.foundResource);
		}
	}
	

	public void processProjectile (Entity projectile, Entity target) {
		if (Components.HEALTH.has(target)) {
			if(!Constants.friendlyFire){
				FactionComponent projectileFactionComp = Components.FACTION.get(projectile);
				FactionComponent targetFactionComp = Components.FACTION.get(target);
				if(projectileFactionComp.faction == targetFactionComp.faction)
					return;
			}
			

			
			ProjectileComponent projectileComp = Components.PROJECTILE.get(projectile);
			HealthComponent healthComp = Components.HEALTH.get(target);

			healthComp.value -= projectileComp.damage;
		}

		EntityUtils.removeEntity(projectile);
	}

}
