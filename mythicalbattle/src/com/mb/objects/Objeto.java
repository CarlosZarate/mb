package com.mb.objects;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mb.actions.BallTargetSpell;
import com.mb.data.ObjectData;
import com.mb.screens.StartGameScreen;
import com.mb.utils.SpriteAccessor;
import com.mb.utils.Funciones;
public class Objeto {
	private Sprite ficha;
	public boolean move = true;
	private boolean selected = false;
	private boolean targeted = false;
	private boolean targetedFriend = false;
	private ObjectData data;
	
	private Sprite select;
	private Sprite rango;
	private Sprite target;
	private Sprite targetFriend;
	private Sprite area;
	
	private Vector2 posicionMat;
	public int IdFicha;
	private List<Vector2> AlcanceMov;
	private List<Vector2> AlcanceRango;
	public List<Vector2> AlcanceHabilidad;
	private List<Vector2> listTargetEnem;
	private List<Vector2> listTargetFriend;
	private List<Vector2> Pasos;
	
	private BallTargetSpell Effect;
	/*>>>>>>>>>>>>>>>>>>>>>>>>*/
	public float offsetH;
	public float offsetW;
	public int Size;//1 o 2
	public int Type;//Heroe = 1, reliquia = 2, Torre 3
	
	/*>>>>>>>>>>>>>>>>>>>>>>>>*/
	//private boolean Enemy;
	
	public int RH1=7;
	public int RH2=5;
	public int RH3=3;
	public int EP = 10;
	public int HP = 0;
	public int DP = 0;
	public int AP = 0;
	public int MRP = 0;
	public int AHPRP = 0;
	
	private int modEP = 0;
	private int modHP = 0;
	private int modDP = 0;
	private int modAP = 0;
	private int modMRP = 0;
	private int modAHPRP = 0;
	
	private int TypeAtack = 0;
	private static final int PHISIC = 1;
	private static final int MAGIC = 2;
	
	
	/*>>>>>>>>>>>>>>>>>>>>>>>*/
	public StartGameScreen startgame;
	
	public int size;
	
	private boolean readyButton = true;
	
	private ImageButton Move;
	private ImageButton Atack;
	private ImageButton butt3;
	private ImageButton butt4;
	
	private Funciones funciones;
	
	
	public Objeto(StartGameScreen startgamescreen, ObjectData Data){
		
		startgame = startgamescreen;
		data = Data;
		size = Data.size;
		Type = Data.Type;
		
		if(size == 2){
			if(Type == 1){
			offsetH = -11*startgame.factorH;
			offsetW = 50*startgame.factorH;
			}
			if(Type == 3){
				offsetH = -33*startgame.factorH;
				offsetW = 50*startgame.factorH;
			}
		}
		else{
			offsetH = 11*startgame.factorH;
			offsetW = 25*startgame.factorH;
		}
		
		IdFicha= startgame.indexFicha;
		funciones = new Funciones(startgame.factorH,startgame.Dimension);
		addTexture();
		
		AlcanceMov = new LinkedList<Vector2>();
		AlcanceRango = new LinkedList<Vector2>();
		AlcanceHabilidad = new LinkedList<Vector2>();
		listTargetEnem = new LinkedList<Vector2>();
		listTargetFriend = new LinkedList<Vector2>();
		
		addActors();
		
		Move.addListener(loadListener(2));
		Atack.addListener(loadListener(3));
		butt3.addListener(loadListener(5));
		butt4.addListener(loadListener(7));
		
		Effect = new BallTargetSpell(startgame.tweenManager);
	}
	
	public void FichaDraw(SpriteBatch spriteBatch, float delta){
		if(targeted){
			Vector2 pos = funciones.SeleccionarPos(posicionMat.x, posicionMat.y);
			target.setPosition(pos.x, pos.y);
			target.draw(spriteBatch);
			if(size == 2){
				pos = funciones.SeleccionarPos(posicionMat.x+1, posicionMat.y);
				target.setPosition(pos.x, pos.y);
				target.draw(spriteBatch);
				pos = funciones.SeleccionarPos(posicionMat.x, posicionMat.y+1);
				target.setPosition(pos.x, pos.y);
				target.draw(spriteBatch);
				pos = funciones.SeleccionarPos(posicionMat.x+1, posicionMat.y+1);
				target.setPosition(pos.x, pos.y);
				target.draw(spriteBatch);
			}
		}
		if(selected){
			Vector2 pos = funciones.SeleccionarPos(posicionMat.x, posicionMat.y);
			select.setPosition(pos.x, pos.y);
			select.draw(spriteBatch);
			if(size == 2){
				pos = funciones.SeleccionarPos(posicionMat.x+1, posicionMat.y);
				select.setPosition(pos.x, pos.y);
				select.draw(spriteBatch);
				pos = funciones.SeleccionarPos(posicionMat.x, posicionMat.y+1);
				select.setPosition(pos.x, pos.y);
				select.draw(spriteBatch);
				pos = funciones.SeleccionarPos(posicionMat.x+1, posicionMat.y+1);
				select.setPosition(pos.x, pos.y);
				select.draw(spriteBatch);
			}
		}
		if(targetedFriend){
			Vector2 pos = funciones.SeleccionarPos(posicionMat.x, posicionMat.y);
			targetFriend.setPosition(pos.x, pos.y);
			targetFriend.draw(spriteBatch);
			if(size == 2){
				pos = funciones.SeleccionarPos(posicionMat.x+1, posicionMat.y);
				targetFriend.setPosition(pos.x, pos.y);
				targetFriend.draw(spriteBatch);
				pos = funciones.SeleccionarPos(posicionMat.x, posicionMat.y+1);
				targetFriend.setPosition(pos.x, pos.y);
				targetFriend.draw(spriteBatch);
				pos = funciones.SeleccionarPos(posicionMat.x+1, posicionMat.y+1);
				targetFriend.setPosition(pos.x, pos.y);
				targetFriend.draw(spriteBatch);
			}
		}
		ficha.draw(spriteBatch);
		
	}
	
	public void addButton(Stage stage){
		stage.addActor(Move);
		stage.addActor(Atack);
		stage.addActor(butt3);
		stage.addActor(butt4);
	}
	
	public void EffectDraw(SpriteBatch spriteBatch, float delta){
		Effect.render(spriteBatch, delta);
	}
	
	public void RangoMovDraw(SpriteBatch spriteBatch){
		if(selected){
			if(startgame.ESTADO==2){
				Iterator<Vector2> iter = AlcanceRango.iterator();
				while(iter.hasNext()){
				Vector2 posicion = iter.next();				
					area.setPosition(posicion.x, posicion.y);
					area.draw(spriteBatch);
				}
			}
			else{
				Iterator<Vector2> iter = AlcanceMov.iterator();
				while(iter.hasNext()){
					Vector2 posicion = iter.next();	
					rango.setPosition(posicion.x, posicion.y);
					rango.draw(spriteBatch);
				}			
			}
		}
	}
	
	public void selected(){
		selected = true;
		funciones.Untarget(listTargetEnem,startgame.HABILIDAD, startgame.Nodos);
		funciones.Untarget(listTargetFriend,startgame.HABILIDAD, startgame.Nodos);
		funciones.CalcularAlcance(posicionMat,EP,AlcanceMov,startgame.Nodos);
		move=true;
	}
	
	public void unselected(){
		startgame.NODOSELECTED=null;
		funciones.Untarget(listTargetEnem,startgame.HABILIDAD, startgame.Nodos);
		funciones.Untarget(listTargetFriend,startgame.HABILIDAD, startgame.Nodos);
		selected = false;		
	}
	
	public void setPositionFicha(float x, float y){
		posicionMat= new Vector2(x,y);
		Vector2 fichaPosicion = new Vector2(funciones.SeleccionarPos(x,y));
		ficha.setPosition(fichaPosicion.x-offsetW, fichaPosicion.y+offsetH);
	}
	
	public Vector2 getPositionFicha(){
		return posicionMat;
	}
	
	public Vector2 getPositionCoorder(){
		return new Vector2(funciones.SeleccionarPos(posicionMat.x, posicionMat.y));
	}
	
	public Sprite getSprite(){
		return ficha;
	}
	
	public int getMP(){
		return EP;
	}
	
	public void targetF(){
		targetedFriend = true;
	}
	
	public void UntargetF(){
		targetedFriend = false;
	}
	
	public void target(){
		targeted = true;
	}
	public void Untarget(){
		targeted = false;
	}
	
	public void modHitsPoints(Vector2 index){
		if(modHP<0)
			switch(TypeAtack){
			case PHISIC:
				startgame.Nodos[(int)index.y][(int)index.x].ficha.HP+=(DP/100)*modHP*((AP/100)+1);
				break;
			case MAGIC:
				startgame.Nodos[(int)index.y][(int)index.x].ficha.HP+=(MRP/100)*modHP;
			}
			
		else
			startgame.Nodos[(int)index.y][(int)index.x].ficha.HP+=((AHPRP/100)+1)*modHP;
	}
	private void modDefencePoints(Vector2 index){
		startgame.Nodos[(int)index.y][(int)index.x].ficha.DP+=modDP;
	}
	
	private void modEnergyPoints(Vector2 index){
		startgame.Nodos[(int)index.y][(int)index.x].ficha.EP+=modEP;
	}
	
	private void modAtackPoints(Vector2 index){
		startgame.Nodos[(int)index.y][(int)index.x].ficha.AP+=modAP;
	}
	
	private void modMagicResistencePoints(Vector2 index){
		startgame.Nodos[(int)index.y][(int)index.x].ficha.MRP+=modMRP;
	}
	
	private void modAmpliHitsPointsRecoveriPoints(Vector2 index){
		startgame.Nodos[(int)index.y][(int)index.x].ficha.AHPRP+=modAHPRP;
	}

	private boolean buscarTarget(Vector2 indexTarget){
		Iterator<Vector2> iter = listTargetEnem.iterator();
		while(iter.hasNext()){
			Vector2 index = iter.next();
			if(index.x==indexTarget.x && index.y==indexTarget.y)
				return true;
		}
		return false;		
	}
	public void usarHabilidad(Vector2 Target){
		if(startgame.Nodos[(int)Target.y][(int)Target.x].ocupado){
			if(startgame.Nodos[(int)Target.y][(int)Target.x].Reflect)
				Target = startgame.Nodos[(int)Target.y][(int)Target.x].nodoReflect;
			if(buscarTarget(Target) && (posicionMat.x != Target.x || posicionMat.y != Target.y)){				
				readyButton=false;
				aplicarHabilidad(Target);
			}
			else{
				readyButton=true;
			}
		}
		else
			readyButton=true;
		
		startgame.ESTADO = 1;
		funciones.Untarget(listTargetEnem,startgame.HABILIDAD, startgame.Nodos);
		funciones.Untarget(listTargetFriend,startgame.HABILIDAD, startgame.Nodos);
		selected();
	}
	
	public void usarHabilidad(List<Vector2> indexList){
		if(indexList.size()>0){			
			readyButton=false;
			aplicarHabilidad(indexList);
		}
		else{
			readyButton=true;
		}
		startgame.ESTADO = 1;
		funciones.Untarget(listTargetEnem,startgame.HABILIDAD, startgame.Nodos);
		funciones.Untarget(listTargetFriend,startgame.HABILIDAD, startgame.Nodos);
		selected();
	}
	
	public void AnimHabilidad(){
		Tween.to(ficha, SpriteAccessor.TINT, 2f)
		.waypoint(1, 0, 0)
		.waypoint(1, 1, 1)
		.waypoint(1, 0, 0)
		.waypoint(1, 1, 1)
		.waypoint(1, 0, 0)
		.target(1, 1, 1)
		.path(TweenPaths.linear)
		.setCallback(callback2)
		.setCallbackTriggers(TweenCallback.START | TweenCallback.COMPLETE)
		.start(startgame.tweenManager);
		}
	
	private void aplicarHabilidad(Vector2 index){
		Effect.init(getPositionCoorder(), startgame.Nodos[(int)index.y][(int)index.x].ficha);
		AnimHabilidad();
		modDefencePoints(index);
		modEnergyPoints(index);
		modAtackPoints(index);
		modMagicResistencePoints(index);
		modAmpliHitsPointsRecoveriPoints(index);
		//startgame.Nodos[(int)index.y][(int)index.x].ficha.AnimHabilidad();
	}
	
	private void aplicarHabilidad(List<Vector2> indexList){
		AnimHabilidad();
		Iterator<Vector2> iter = indexList.iterator();
		while(iter.hasNext()){
			Vector2 index = iter.next();
			modDefencePoints(index);
			modEnergyPoints(index);
			modAtackPoints(index);
			modMagicResistencePoints(index);
			modAmpliHitsPointsRecoveriPoints(index);
			startgame.Nodos[(int)index.y][(int)index.x].ficha.AnimHabilidad();
		}
	}
	private void addActors(){
		
		TextureRegion textureMoveUp = new TextureRegion(new Texture(Gdx.files.internal("data/mover_up.png")),0,0,68,74);
		TextureRegion textureMoveDown = new TextureRegion(new Texture(Gdx.files.internal("data/mover_down.png")),0,0,68,74);
		
		ImageButtonStyle styleButtonMove= new ImageButtonStyle();
		styleButtonMove.up=new TextureRegionDrawable(textureMoveUp);
		styleButtonMove.down=new TextureRegionDrawable(textureMoveDown);
		
		Move=new ImageButton(styleButtonMove);
		Move.setPosition(200, 0);
		
		Atack = new ImageButton(styleButtonMove);
		Atack.setPosition(280, 0);
		
		butt3 = new ImageButton(styleButtonMove);
		butt3.setPosition(360, 0);
		
		butt4 = new ImageButton(styleButtonMove);
		butt4.setPosition(440, 0);
	}
	
	private void addTexture(){
		ficha = startgame.textures.createSprite(data.nombre);
		if(size == 2){
			ficha.setSize(300*startgame.factorH, 300*startgame.factorH);
		}
		else ficha.setSize(200*startgame.factorH, 200*startgame.factorH);
		
		select = startgame.textures.createSprite("loseta");
		select.setSize(100*startgame.factorH, 70*startgame.factorH);
		select.setColor(0, 0, 1, 0.7f);
		
		target = startgame.textures.createSprite("loseta");
		target.setSize(100*startgame.factorH, 70*startgame.factorH);
		target.setColor(1, 0, 0, 0.7f);
		
		targetFriend = startgame.textures.createSprite("loseta");
		targetFriend.setSize(100*startgame.factorH, 70*startgame.factorH);
		targetFriend.setColor(0, 1, 0, 0.7f);
		
		rango = startgame.textures.createSprite("loseta");
		rango.setSize(100*startgame.factorH, 70*startgame.factorH);
		rango.setColor(1, 1, 1, 0.25f);
		
		area = startgame.textures.createSprite("loseta");
		area.setSize(100*startgame.factorH, 70*startgame.factorH);
		area.setColor(255, 255, 0, 0.25f);
	}
	
	private InputListener loadListener(int tipo){
		InputListener Listener =new InputListener(); 
		switch (tipo){
			case 1:
				Listener = new InputListener(){
					public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			    		if(readyButton){
			    			selected();
			    			startgame.ESTADO=2;
			    			startgame.HABILIDAD=1;
				    		funciones.Target(posicionMat, size, listTargetFriend, AlcanceRango, AlcanceHabilidad, startgame.HABILIDAD, startgame.Nodos);
			    		}
			            return true;
			    }};
				return Listener;
				
			case 2:
				Listener = new InputListener(){
					public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			    		if(readyButton){
			    			selected();
			    			startgame.ESTADO=2;
			    			startgame.HABILIDAD=2;
			    			funciones.Target(posicionMat, size, listTargetEnem, AlcanceRango, AlcanceHabilidad, startgame.HABILIDAD, startgame.Nodos);
			    		}
			            return true;
			    }};
				return Listener;
				
			case 3:
				Listener = new InputListener(){
					public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			    		if(readyButton){
			    			startgame.ESTADO=2;
			    			startgame.HABILIDAD=3;
			    			funciones.CalcularAlcance(posicionMat,size,RH1,RH2,AlcanceRango,AlcanceHabilidad,startgame.Nodos);
			    		}
			            return true;
			    }};
				return Listener;
			case 4:
				Listener = new InputListener(){
					public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			    		if(readyButton){
			    			startgame.ESTADO=2;
			    			startgame.HABILIDAD=4;
			    			funciones.CalcularAlcance(posicionMat,size,RH1,RH2,AlcanceRango,AlcanceHabilidad,startgame.Nodos);
			    		}
			            return true;
			    }};
				return Listener;
			case 5:
				Listener = new InputListener(){
					public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			    		if(readyButton){
			    			startgame.ESTADO=2;
			    			startgame.HABILIDAD=5;
			    			funciones.CalcularAlcance(posicionMat,size,RH1,RH2,AlcanceRango,AlcanceHabilidad,startgame.Nodos);
			    		}
			            return true;
			    }};
				return Listener;				
			case 6:
				Listener = new InputListener(){
					public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			    		if(readyButton){
			    			startgame.ESTADO=2;
			    			startgame.HABILIDAD=6;
			    			funciones.CalcularAlcance(posicionMat,size,RH1,RH2,AlcanceRango,AlcanceHabilidad,startgame.Nodos);
			    		}
			            return true;
			    }};
				return Listener;	
			case 7:
				Listener = new InputListener(){
					public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			            return true;
			    }
					public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			    		/*if(readyButton){
			    			startgame.ESTADO=2;
			    			startgame.HABILIDAD=7;
			    			funciones.Target(posicionMat, size, listTargetFriend, AlcanceRango, AlcanceHabilidad, startgame.HABILIDAD, startgame.Nodos);
			    		}*/
						if(x>0 && x<68 && y>0 && y<74){
							System.out.println(true);
						}
						else
							System.out.println(false);
			    	}};
				return Listener;
		}
		return Listener;
	}
	
	
	TweenCallback callback2 = new TweenCallback() {
		@Override public void onEvent(int type, BaseTween source) {
			switch (type) {
				case START: System.out.println("Start");break;
				case COMPLETE:readyButton = true; break;
			}
		}
	};
	
	public boolean ifPasos(Vector2 puntero){
		if(move){		
			if(startgame.Nodos[(int)puntero.y][(int)puntero.x].closed){
				return true;
			}
		}
		unselected();
		return false;
	}
	public void MoveFicha(Vector2 Destino){
		if(size == 2){
			Destino = funciones.getReflect(Destino, startgame.Nodos);
			Pasos = funciones.getPasos(Destino, startgame.Nodos);
			
			Nodo nodoAUX = startgame.Nodos[(int)posicionMat.y][(int)posicionMat.x];
			
			startgame.Nodos[(int)posicionMat.y][(int)posicionMat.x] = new Nodo((int)posicionMat.x,(int)posicionMat.y);
			startgame.Nodos[(int)posicionMat.y][(int)posicionMat.x+1] = new Nodo((int)posicionMat.x+1,(int)posicionMat.y);
			startgame.Nodos[(int)posicionMat.y+1][(int)posicionMat.x] = new Nodo((int)posicionMat.x,(int)posicionMat.y+1);
			startgame.Nodos[(int)posicionMat.y+1][(int)posicionMat.x+1] = new Nodo((int)posicionMat.x+1,(int)posicionMat.y+1);
			
			startgame.Nodos[(int)Destino.y][(int)Destino.x] = nodoAUX;
			
			startgame.Nodos[(int)Destino.y][(int)Destino.x+1].ocupado = true;
			startgame.Nodos[(int)Destino.y+1][(int)Destino.x].ocupado = true;
			startgame.Nodos[(int)Destino.y+1][(int)Destino.x+1].ocupado = true;
        	
			startgame.Nodos[(int)Destino.y][(int)Destino.x+1].Reflect = true;
			startgame.Nodos[(int)Destino.y+1][(int)Destino.x].Reflect = true;
			startgame.Nodos[(int)Destino.y+1][(int)Destino.x+1].Reflect = true;
        	
			startgame.Nodos[(int)Destino.y][(int)Destino.x+1].nodoReflect = Destino;
			startgame.Nodos[(int)Destino.y+1][(int)Destino.x].nodoReflect = Destino;
			startgame.Nodos[(int)Destino.y+1][(int)Destino.x+1].nodoReflect = Destino;
        	
        	posicionMat = Destino;
        	startgame.NODOSELECTED = Destino;				
        	
        	MoveExe(Destino);			
        	startgame.Nodos[(int)posicionMat.y][(int)posicionMat.x].setPosicion((int)Destino.x,(int) Destino.y);
        	selected();
		}
		
		else{
			Pasos = funciones.getPasos(Destino, startgame.Nodos);			
			startgame.Nodos[(int)Destino.y][(int)Destino.x] = startgame.Nodos[(int)posicionMat.y][(int)posicionMat.x];
			startgame.Nodos[(int)posicionMat.y][(int)posicionMat.x] = new Nodo((int)posicionMat.x,(int)posicionMat.y);
			posicionMat = Destino;
			startgame.NODOSELECTED = Destino;
			MoveExe(Destino);
			startgame.Nodos[(int)posicionMat.y][(int)posicionMat.x].x = (int)posicionMat.x;
			startgame.Nodos[(int)posicionMat.y][(int)posicionMat.x].y = (int)posicionMat.y;
			selected();
		}
	}
	public void MoveExe(Vector2 indexTemp){
			posicionMat = new Vector2(indexTemp);
			funciones.CalcularAlcance(posicionMat,EP,AlcanceMov,startgame.Nodos);			
			Vector2 paso = 	Pasos.get(Pasos.size()-1);
			Pasos.remove(Pasos.size()-1);
			moveTween(paso.x,paso.y);			
	}
	
	TweenCallback callback = new TweenCallback() {
		@Override public void onEvent(int type, BaseTween source) {
			switch (type) {
				case START: move=false;break;
				case COMPLETE:
					if(Pasos.size()>0){
						Vector2 paso = Pasos.get(Pasos.size()-1);
						Pasos.remove(Pasos.size()-1);
						moveTween(paso.x, paso.y);
					}						
					else move = true;break;
			}
		}
	};
	
	private void moveTween(float x, float y){
		Timeline.createSequence()
		.push(Tween.to(ficha,SpriteAccessor.POS_XY, 0.1f).target(x-offsetW, y+offsetH).ease(Linear.INOUT))
		.setCallback(callback)
		.setCallbackTriggers(TweenCallback.START | TweenCallback.COMPLETE)
		.start(startgame.tweenManager);
	}
}
