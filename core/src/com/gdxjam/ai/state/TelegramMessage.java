package com.gdxjam.ai.state;

public enum TelegramMessage {
	
	//Squad relevant messages
	SQUAD_DISCOVERED_ENEMY,
	SQUAD_DISCOVERED_RESOURCE,
	SQUAD_INPUT_SELECTED,
	
	TARGET_REQUEST,
	TARGET_REMOVED,
	
	//Unit relevant messages
	UNIT_ADDED_TO_SQUAD,
	
	//Construction System relevant messages
	CONSTRUCT_UNIT_REQUEST,
	CONSTRUCT_UNIT_CONFRIM,
	
	//GUI relevant messages
	GUI_INSUFFICIENT_RESOURCES,
	
	;

}
