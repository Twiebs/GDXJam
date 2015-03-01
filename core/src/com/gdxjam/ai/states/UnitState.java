package com.gdxjam.ai.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.GameManager;
import com.gdxjam.components.Components;
import com.gdxjam.components.ResourceComponent;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.utils.Constants;

public enum UnitState implements State<Entity> {

	FORMATION (){
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			
			SteeringBehaviorComponent behavior = Components.STEERING_BEHAVIOR.get(entity);
			SteerableComponent steerable = Components.STEERABLE.get(entity);
			SquadMemberComponent squadMember = Components.SQUAD_MEMBER.get(entity);

			Arrive<Vector2> arriveSB = new Arrive<Vector2>(steerable, squadMember.getTargetLocation())
				.setTimeToTarget(0.001f)
				.setArrivalTolerance(0.01f)
				.setDecelerationRadius(2f);
			
			RaycastObstacleAvoidance<Vector2> raycastSB = new RaycastObstacleAvoidance<Vector2>(steerable)
				.setDistanceFromBoundary(Constants.unitRadius * 4);
			
			BlendedSteering<Vector2> blendSB = new BlendedSteering<Vector2>(steerable);
			blendSB.add(raycastSB, 1000);
			blendSB.add(arriveSB, 0.1f);
			behavior.setBehavior(arriveSB);
		}
		
	},
	
    HARVEST(){
   	 @Override
   	 public void enter(Entity entity){
      	 //Test code
   		 ImmutableArray<Entity> entities = GameManager.getEngine().getEntitiesFor(Family.all(ResourceComponent.class).get());
    		 Entity resource = entities.first();
    		 
    		 Arrive<Vector2> arrive = new Arrive<Vector2>(Components.STEERABLE.get(entity))
    			 .setTarget((Location<Vector2>)Components.STEERABLE.get(resource).getBody().getPosition())
    			 .setTimeToTarget(0.01f)
    			 .setArrivalTolerance(0.002f)
    			 .setDecelerationRadius(2f);
    		 
    		 SteeringBehaviorComponent behavior = Components.STEERING_BEHAVIOR.get(entity);
    		 behavior.setBehavior(arrive);
   	 }
   	 
   	 @SuppressWarnings("unchecked")
		public void update(Entity entity){
//   		 
//   		 if(unit.target == null){
////   			 Components.STATE_MACHINE.get(entity).stateMachine.changeState(IDLE);
//   		 }
//   		 else{
//      		 SteerableComponent steerable = Components.STEERABLE.get(entity);
//      		 SteeringBehaviorComponent behavior = Components.STEERING_BEHAVIOR.get(entity);
//
//      		 Arrive arrive = (Arrive) behavior.getBehavior();
//      		 Vector2 distance = steerable.getBody().getPosition();
//      		 distance.sub((Vector2) arrive.getTarget().getPosition());
//      		 
//      		 if(distance.len2() < 1.5f){
//      			 if(Components.HEALTH.has(unit.target)){
//      				 HealthComponent health = Components.HEALTH.get(unit.target);
//         			 health.value -= 1;
//         			 if(health.value <= 0){
//         				 unit.target = null;
//         			 }
//      			 }
//      			
//      		 }
//   		 }
//   		 
   		 
   	 }
	
    },
    
    COMBAT(){
   	 
    };



    @Override
    public void enter(Entity entity) {

    }

    @Override
    public void update(Entity entity) {

    }


    @Override
    public void exit(Entity entity) {

    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
//        switch (telegram.message){
//            case Messages.REGROUP_ORDER:
//                if(!Components.STATE_MACHINE.get(entity).stateMachine.isInState(REGROUP))
//                    Components.STATE_MACHINE.get(entity).stateMachine.changeState(REGROUP);
//                break;
//            case Messages.RESOUCES_ORDER:
//                Components.STATE_MACHINE.get(entity).stateMachine.changeState(COLLECT_RESOURCES);
//        }

        return false;
    }
}
