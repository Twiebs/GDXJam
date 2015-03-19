package com.gdxjam.ai.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.ecs.Components;

public enum SquadHarvestState implements State<Entity>{
	IDLE(){
		@Override
		public void enter (Entity entity) {
			SquadComponent squadComp = Components.SQUAD.get(entity);
			
			for (Entity member : squadComp.members) {
				Components.FSM.get(member).changeState(UnitState.IDLE);
			}
			
			// If we have targets available we don't need to be idle
			if (squadComp.resourcesInRange.size > 0)
				Components.FSM.get(entity).changeState(HARVEST);
		}
		
		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
		TelegramMessage telegramMsg = TelegramMessage.values()[telegram.message];
		switch(telegramMsg){
		case SQUAD_DISCOVERED_RESOURCE:
			Components.FSM.get(entity).changeState(HARVEST);
			return true;
			
		default:
			return false;
			}
		}
	},
	
	HARVEST(){
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			
			SquadComponent squadComp = Components.SQUAD.get(entity);
			for (Entity member : squadComp.members) {
				Components.FSM.get(member).changeState(UnitState.IDLE);
			}
		}
		
		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			SquadComponent squadComp = Components.SQUAD.get(entity);
			TelegramMessage telegramMsg = TelegramMessage.values()[telegram.message];
			
			switch (telegramMsg) {
			case TARGET_REQUEST:
				//First we check if we have any target resources to delegate to our members
				if(squadComp.resourcesInRange.size <= 0){
					//There were no more targets
					Components.FSM.get(entity).changeState(SquadHarvestState.IDLE);
				} else {
					Entity unit = (Entity)telegram.extraInfo;
					int index = squadComp.members.indexOf(unit, true);

					// Get a target and set it
					Entity target = squadComp.resourcesInRange.get(index % squadComp.resourcesInRange.size);
					Components.TARGET.get(unit).setTarget(target);
				}
				return true;

			default:
				return false;
			}
		}
	}
	
	;

	@Override
	public void enter (Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update (Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit (Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage (Entity entity, Telegram telegram) {
		// TODO Auto-generated method stub
		return false;
	}

}
