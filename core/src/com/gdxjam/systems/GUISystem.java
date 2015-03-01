package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.gdxjam.Assets;
import com.gdxjam.ai.Squad;
import com.gdxjam.ui.HotkeyTable;
import com.gdxjam.ui.SquadCommandTable;
import com.gdxjam.ui.HotkeyTable.HotkeyTableStyle;
import com.gdxjam.ui.SquadManagmentTable;
import com.gdxjam.utils.Constants;

public class GUISystem extends EntitySystem implements Disposable {

	private Stage stage;
	private Skin skin;
	private Table squadSidebar;
	
	private SquadManagmentTable squadManagment;
	
	private final ObjectIntMap<Squad> squadKeyMap = new ObjectIntMap<Squad>(Constants.maxSquads);
	
	private Label resourceLabel;

	public GUISystem() {
		this.stage = new Stage();
		this.skin = Assets.skin;

		initGUI();
	}

	public void initGUI() {
		/** Resource Table		 */
		resourceLabel = new Label("Resources: XXX", skin);
		Table resourceTable = new Table();
		resourceTable.add(resourceLabel);
		
		

		squadManagment = new SquadManagmentTable(skin);
		
		Table squadManagmentContainer = new Table();
		squadManagmentContainer.setFillParent(true);
		squadManagmentContainer.add(squadManagment).padTop(30);
		squadManagmentContainer.center().bottom();
		stage.addActor(squadManagmentContainer);
		
		Table topTable = new Table();
		topTable.setFillParent(true);
		topTable.top().right();
		
		topTable.defaults().pad(5);
		topTable.add(resourceTable);

		stage.addActor(topTable);
		
	}

	public HotkeyTable createActionTable() {
		HotkeyTable table = new HotkeyTable();
		table.addHotkey(Keys.Q, "Q");
		table.addHotkey(Keys.W, "W");
		table.addHotkey(Keys.E, "E");
		table.addHotkey(Keys.R, "R");
		table.addHotkey(Keys.T, "T");
		return table;
	}

	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
	}
	
	public void addSquad(Squad squad) {
		String strIndex = String.valueOf(squad.index + 1);
		int keycode = Keys.valueOf(strIndex);
		squadKeyMap.put(squad, keycode);
	
		squadManagment.addSquad(squad);
	}
	
	public void updateSquad(Squad squad){
		squadManagment.updateSquadTable(squad);
	}

	public void setSelected(Squad squad) {
		squadManagment.setSelected(squad.index, squad.isSelected());
	}

	public void resize(int screenWidth, int screenHeight) {
		stage.getViewport().update(screenWidth, screenHeight);
	}
	
	public void updateResource(int amount){
		resourceLabel.setText("Resources: " + amount);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		stage.act();
		stage.draw();
	}

	public Stage getStage() {
		return stage;
	}

//	public HotkeyTable getHotkeyTable() {
//		return hotkeyTable;
//	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}

}
