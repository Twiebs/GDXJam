package com.gdxjam.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdxjam.ai.state.SquadState;
import com.gdxjam.ai.state.SquadTatics;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SquadComponent.PatternType;
import com.gdxjam.ecs.Components;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityFactory;

public class SquadCommandCard extends CommandCard{
	

	
	private SelectBox<SquadTatics> tatics;
	private SelectBox<PatternType> formationPatternSelect;
	private final Entity squad;
	private final int index;
	private BitmapFontCache squadText;
	

	public SquadCommandCard(final Entity squad, int index, Skin skin){
		super(skin);
		
		this.squad = squad;
		this.index = index;

		squadText = new BitmapFontCache(skin.getFont("default-font"));
		squadText.setMultiLineText("Squad " + (index + 1), 0, 0);
		squadText.setColor(Color.WHITE);

		tatics = new SelectBox<SquadTatics>(skin);
		tatics.setItems(SquadTatics.values());
		tatics.setSelected(SquadTatics.COMBAT);
		tatics.addListener(new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				State<Entity> state;
				switch(tatics.getSelected()){
				default:
				case HARVEST:
					state = SquadState.HARVEST_IDLE;
					break;
				case COMBAT:
					state = SquadState.COMBAT_IDLE;
					break;
				}
				Components.FSM.get(squad).changeState(state);
			}
		});
		
//		formationPatternSelect = new SelectBox<PatternType>(skin);
//		formationPatternSelect.setItems(PatternType.values());
//		formationPatternSelect.setSelected(SquadComponent.DEFAULT_PATTERN);
//		formationPatternSelect.addListener(new ChangeListener() {
//			
//			@Override
//			public void changed (ChangeEvent event, Actor actor) {
//				Components.SQUAD.get(squad).setFormationPattern(formationPatternSelect.getSelected());
//			}
//		});
		
		FormationPatternTable formationTable = new FormationPatternTable(squad, skin);
		
		TextButton addMemberButton = new TextButton(" + ", skin);
		addMemberButton.addListener(new ChangeListener(){
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				EntityFactory.createUnit(squad);
			}
		});
		
		add(tatics).pad(5);
		add(addMemberButton);
		row();
		add(formationTable).colspan(2);
	}
	
	public void update(){
		SquadComponent squadComp = Components.SQUAD.get(squad);
		squadText.setMultiLineText("Squad " + (index + 1) + "   (" + squadComp.members.size + " / " + Constants.maxSquadMembers + ")", 0, 0);
	}
	
	public void setSelected(boolean selected){
		squadText.tint(selected ? Color.CYAN : Color.WHITE);
		setColor(selected ? selectedColor : defaultColor);
	}
	
	@Override
	protected void drawBackground (Batch batch, float parentAlpha, float x, float y) {
		super.drawBackground(batch, parentAlpha, x, y);
		TextBounds bounds = squadText.getBounds();
		float xPos = x + ((getWidth() * 0.5f) - (bounds.width * 0.5f));
		float yPos = y + ((getHeight()) - (bounds.height * 0.5f) ) + 4;	//The constant should not be needed
		squadText.setPosition(xPos, yPos);
		squadText.draw(batch, parentAlpha);
	}
	
	
}