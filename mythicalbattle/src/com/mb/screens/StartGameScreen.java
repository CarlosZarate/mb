package com.mb.screens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.mb.data.CardData;
import com.mb.data.DataStartGame;
import com.mb.data.DescriptionMapData;
import com.mb.data.ObjectData;
import com.mb.data.PropertiesMapData;
import com.mb.objects.MapLozeta;
import com.mb.objects.Nodo;
import com.mb.objects.Objeto;
import com.mb.utils.Funciones;
import com.mb.utils.SpriteAccessor;

public class StartGameScreen extends AbstractScreen{

	private OrthographicCamera camera;
	private SpriteBatch batch;
	AssetManager assetManager;
	
	float[] mod = new float[4];
	
	private float CamMaxX;
	private float CamMaxY;
	private float CamMinX;
	private float CamMinY;
	
	private TextureRegion contorno;
	private TextureRegion buttonTexture;
	
	public TextureAtlas textures;
	public TextureAtlas joypadtextures;
	
	public Sprite MapBack;
	
	private Sprite areaTarget;
	private DescriptionMapData MapaDescripcion;
	private PropertiesMapData MapaPropiedades;
	
	private float porcentx;
	private float porcenty;

	private Sprite DraggedFicha;
	private boolean dragged = false;
	private boolean atdragged = false;
	private boolean touchTarget = true;
	private boolean spelldragged = false;
	
	private Stage stage;
	private Stage cardStage;
	
	private Slider slider;
	private Touchpad joystick2;
	private ImageButton Deck;
	private ImageButton Card;
	private ImageButton Card2;
	private ImageButton Card3;
	private ImageButton CardMagic;
	private ImageButton CardEquip;
	
	public final TweenManager tweenManager = new TweenManager();
	
	public Funciones funciones;
	
	public List<CardData> listcards;
	
	private Vector2 TempPosicionMat;
	
	public int indexFicha = 0;
	
	/*>>>>>>>>>>>>> Datos de Mapa >>>>>>>>>>>>>>>>>>>*/
	
	public Nodo[][] Nodos;
	public MapLozeta[][] MapaLozetas;
	private int WidthLozetas;
	private int HeightLozetas;
	private int WidthMapTexture;
	private int HeightMapTexture;
	private Vector2 PositionTorreA;
	private Vector2 PositionTorreB;
	private String AssetPathMap;
	public float Dimension;
	
	/*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	
	public static final int UNSELECTED = 0;
	public static final int SELECTED = 1;
	public static final int READY = 2;
	
	public static final int STFTYPE = 1;
	public static final int STETYPE = 2;
	public static final int ATFTYPE = 3;
	public static final int ATETYPE = 4;
	public static final int ALFTYPE = 5;
	public static final int ALETYPE = 6;
	public static final int ATYPE = 7;
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*/
	
	public static final int HEROTYPE = 1;
	public static final int RELICTYPE = 2;
	public static final int SPELLTYPE = 3;
	public static final int EQUIPTYPE = 4;
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*/
	public int SPELLR =0;
	public int ESTADO = 0;
	public int HABILIDAD = 0;
	public Vector2 NODOSELECTED = new Vector2(); 
	public int SIZESELECTED;
	
	private List<Vector2> AlcanceMP;
	private List<Vector2> AlcanceHabilidad;
	private List<Vector2> listTarget;
	
	public float Width = 0;
	public float Height = 0;
	public float factorW = 0;
	public float factorH = 0;
	public float offsetW = 0;
	public float offsetH = 0;
	
	public float losetaW;
	public float losetaH;
	public CardData carddata;
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*/
	private DataStartGame Data;
	public StartGameScreen startgamescreen = this;
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*/
	
	public StartGameScreen(MainScreen game, DataStartGame data) {		
		super(game);
		// TODO Auto-generated constructor stub
		game.mNativeFunctions.getConnection();
		Data = data;
	}
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if((camera.position.x+porcentx)>CamMaxX || (camera.position.x+porcentx)<CamMinX)
        	porcentx = 0;
        if((camera.position.y+porcenty)>CamMaxY || (camera.position.y+porcenty)<CamMinY)
        	porcenty = 0;        
		camera.translate(porcentx, porcenty, 0);
		mod[2] -= porcentx;
		mod[3] -= porcenty;
		
		tweenManager.update(Gdx.graphics.getDeltaTime());
		InputHandler();
		
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		MapBack.draw(batch);		

		if(NODOSELECTED!=null){
			Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.RangoMovDraw(batch);
		}
		if(atdragged&&(touchTarget || spelldragged))
			AreaDraw();
		
		renderObjects(delta);
		renderObjectEffects(batch,delta);
		if(dragged)
			DraggedFicha.draw(batch);
		
		batch.end();
		
		stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        
        cardStage.act(Gdx.graphics.getDeltaTime());
        cardStage.draw();
	}
	
	public void renderObjectEffects(SpriteBatch spriteBatch, float delta){
		for(int i=0;i<37;i++)
			for(int j=0;j<37;j++){
				if(Nodos[i][j].ficha !=null)
				Nodos[i][j].ficha.EffectDraw(batch, delta);
			}
	}
	
	public void renderObjects(float delta){
		for(int i = 37; i>=0;i--)
			for(int j = 0; j <= 37-i; j++){
				if(Nodos[j][j+i].ficha!=null){
					Nodos[j][j+i].ficha.FichaDraw(batch,delta);
				}
			}
		
		for(int j = 1; j<37;j++)
			for(int i = 0; i <= 37-j; i++){
				if(Nodos[i+j][i].ficha!=null){
					Nodos[i+j][i].ficha.FichaDraw(batch,delta);
				}
			}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Width = Gdx.graphics.getWidth();
		Height = Gdx.graphics.getHeight();
		
		Json json = new Json();		
		
		Data.setIDMap(1);
		Data.setGold(150);
		Data.setTime(90);
		Data.idtowerA = 1;
		Data.idtowerB = 1;
		
		/*>>>>> Cargando datos de Mapa >>>>>>*/
		
		MapaDescripcion = game.mNativeFunctions.getMapData(1);
		MapaPropiedades = json.fromJson(PropertiesMapData.class, Gdx.files.internal("data/"+MapaDescripcion.data));
		MapaLozetas = MapaPropiedades.getMapLozetas();
		WidthLozetas = MapaPropiedades.getWidthLozetas();
		HeightLozetas = MapaPropiedades.getHeightLozetas();
		WidthMapTexture = MapaPropiedades.getWidth();
		HeightMapTexture = MapaPropiedades.getHeight();
		PositionTorreA = MapaPropiedades.getTorreA();
		PositionTorreB = MapaPropiedades.getTorreB();
		AssetPathMap = MapaPropiedades.getAssetPath();
		Dimension = WidthLozetas + HeightLozetas;
		
		System.out.println(WidthLozetas +"  "+ HeightLozetas);
		
		/*>>>>>>> Fin Cargado de Mapa >>>>>>>*/
		
		
		Nodos = new Nodo[38][38];
		for(int j=0;j<38;j++)
			for(int i=0;i<38;i++){
				Nodo nodo = new Nodo(i,j);
				if(MapaLozetas[j][i].getPropiedad()==-1)
					nodo.blocked = true;
				Nodos[j][i] = nodo;
			}	
		
		textures = new TextureAtlas(Gdx.files.internal("data/texturas.pack"));
		
		factorW = Width/1280;
		factorH = Height/800;
		
		losetaW = 100*factorH;
		losetaH = 70*factorH;
		
		CamMaxX = (1800*factorH-Width)/2;
		CamMinX = -(2200*factorH-Width)/2;
		
		CamMaxY = (1400*factorH-Height)/2-losetaW;
		CamMinY = -(1120*factorH-Height)/2-losetaH;
		
		camera = new OrthographicCamera(Width,Height);
		batch = new SpriteBatch();
		
		crearTorres();
				
		System.out.println(json.toJson(Data));
		
		funciones = new Funciones(factorH,Dimension);
	
		joypadtextures = new TextureAtlas(Gdx.files.internal("data/joypad.pack"));
		
		MapBack = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("data/"+AssetPathMap)),0,0,WidthMapTexture,HeightMapTexture));
		MapBack.setSize(MapBack.getWidth()*factorH, MapBack.getHeight()*factorH);
		
		areaTarget = textures.createSprite("loseta");
		areaTarget.setSize(100*factorH,70*factorH);
		areaTarget.setColor(1, 1, 1, 0.25f);	
		
		Skin skin = new Skin(Gdx.files.internal("data/controlui.json"));
		
		slider = new Slider(-25, 50, 1, false, skin);
		slider.setSize(100, 20);
		slider.setPosition(20, 50);
		slider.setValue(0);
		
		
		agregarActors();
		AgregarListener();
		
		cardStage = new Stage();
		stage = new Stage();
		stage.addActor(joystick2);
		stage.addActor(Deck);
		stage.addActor(Card);
		stage.addActor(Card2);
		stage.addActor(Card3);
		stage.addActor(CardMagic);
		stage.addActor(CardEquip);
		stage.addActor(slider);
		
		Tween.setWaypointsLimit(10);
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		
		multiplexer.addProcessor(cardStage);
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(inputProcessor);
		
		Gdx.input.setInputProcessor(multiplexer);	
		
		NODOSELECTED  = null;
		AlcanceMP = new LinkedList<Vector2>();
		AlcanceHabilidad = new LinkedList<Vector2>();
		listTarget = new LinkedList<Vector2>();
		
		MapBack.setPosition((-WidthMapTexture/2-100)*factorH, -HeightMapTexture*factorH/2);
        updateScreen();
		
	}
	
	private void agregarActors(){
		contorno = joypadtextures.findRegion("contorno800");
		buttonTexture = joypadtextures.findRegion("boton800");
		
		TouchpadStyle style = new TouchpadStyle(new TextureRegionDrawable(contorno),new TextureRegionDrawable(buttonTexture));
		joystick2 = new Touchpad(1,style );
		joystick2.setPosition(20, 150);
		
		TextureRegion textureDeckUp = new TextureRegion(new Texture(Gdx.files.internal("data/deck_up.png")),0,0,64,38);
		TextureRegion textureDeckDown = new TextureRegion(new Texture(Gdx.files.internal("data/deck_down.png")),0,0,64,38);
		
		ImageButtonStyle styleButtonDeck= new ImageButtonStyle();
		styleButtonDeck.up=new TextureRegionDrawable(textureDeckUp);
		styleButtonDeck.down=new TextureRegionDrawable(textureDeckDown);
		Deck=new ImageButton(styleButtonDeck);
		Deck.setPosition(3, 434);
		
		TextureRegion textureCardUp = new TextureRegion(new Texture(Gdx.files.internal("data/card_up.png")),0,0,42,60);
		TextureRegion textureCardDown = new TextureRegion(new Texture(Gdx.files.internal("data/card_down.png")),0,0,42,60);
		
		ImageButtonStyle styleButtonCard= new ImageButtonStyle();
		styleButtonCard.up=new TextureRegionDrawable(textureCardUp);
		styleButtonCard.down=new TextureRegionDrawable(textureCardDown);
		
		Card=new ImageButton(styleButtonCard);
		Card.setPosition(150, 420);
		
		Card2=new ImageButton(styleButtonCard);
		Card2.setPosition(200, 420);
		
		Card3=new ImageButton(styleButtonCard);
		Card3.setPosition(250, 420);
		
		TextureRegion textureCardMagicUp = new TextureRegion(new Texture(Gdx.files.internal("data/card_magic_up.png")),0,0,42,60);
		TextureRegion textureCardMagicDown = new TextureRegion(new Texture(Gdx.files.internal("data/card_magic_down.png")),0,0,42,60);
		
		ImageButtonStyle styleButtonMagicCard= new ImageButtonStyle();
		styleButtonMagicCard.up=new TextureRegionDrawable(textureCardMagicUp);
		styleButtonMagicCard.down=new TextureRegionDrawable(textureCardMagicDown);
		
		CardMagic=new ImageButton(styleButtonMagicCard);
		CardMagic.setPosition(300, 420);
		CardEquip=new ImageButton(styleButtonMagicCard);
		CardEquip.setPosition(350, 420);
	}
	
	private void updateScreen(){
		camera.zoom = 1+slider.getValue()/100;
	   	 CamMaxX = (1800*factorH-Width*camera.zoom)/2;
	   	 CamMinX = -(2200*factorH-Width*camera.zoom)/2;
			
	   	 CamMaxY = (1400*factorH-Height*camera.zoom)/2-70*factorH;
	   	 CamMinY = -(1120*factorH-Height*camera.zoom)/2-70*factorH;
	   	 
	   	 float x = 0;
	   	float y = 0;
	
	   	if((camera.position.x+1)>CamMaxX)
	   		x=(CamMaxX-1)-camera.position.x;
	   	if((camera.position.x-1)<CamMinX)
	   		x=(CamMinX+1)-camera.position.x;
	    if((camera.position.y+1)>CamMaxY)
	    	y=(CamMaxY-1)-camera.position.y;
	   	if((camera.position.y-1)<CamMinY)
	   		y=(CamMinY+1)-camera.position.y;
	   	
	   	camera.translate(x, y);
	   	
	   mod[2] -= x;
	   mod[3] -= y;

	}
	
	private void AgregarListener(){
		
		InputListener cardListener = new InputListener() {
	    	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            return true;
	    }
	    	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	    }
	};
		
		joystick2.addListener(new ChangeListener() {
	         @Override
	         public void changed (ChangeEvent event, Actor actor) {
	            Touchpad touchpad = (Touchpad) actor;
	            porcentx = (int)(touchpad.getKnobPercentX()*6);
	            porcenty = (int)(touchpad.getKnobPercentY()*6);
	         }
	         
	      });
		
		slider.addListener(new ChangeListener() {
	         @Override
	         public void changed (ChangeEvent event, Actor actor) {
		         updateScreen();
	        }
	      });
		
		
		Deck.addListener(new InputListener() {
	    	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	    		ObjectData hero = game.mNativeFunctions.getHeroData(1);
	    		System.out.println(hero.nombre);
	    		return true;
	    }});
		
		listcards = getListCard();
		
		CardData carddata1 = listcards.get(0);
		CardData carddata2 = listcards.get(1);
		CardData carddata3 = listcards.get(2);
		CardData carddata4 = listcards.get(3);
		CardData carddata5 = listcards.get(4);
		
		Card.addListener(getCardListener(carddata1));
		Card2.addListener(getCardListener(carddata2));
		Card3.addListener(getCardListener(carddata3));
		CardMagic.addListener(getCardListener(carddata4));
		CardEquip.addListener(getCardListener(carddata5));
		joystick2.addListener(cardListener);		
		Deck.addListener(cardListener);
		Card.addListener(cardListener);
	}
	

	@Override
	public void dispose() {
		batch.dispose();
	}

	
	private void enableDragged(){
		dragged = true;
	}
	
	private void desableDragged(){
		dragged = false;
	}
	
	private void InputHandler(){
		if(Gdx.input.isTouched()){
			Vector2 pos = funciones.convertirPuntero(Gdx.input.getX(), Gdx.input.getY(),camera,mod);
			Vector2 posMat = funciones.CalcularPosicionMat(pos.x, pos.y);
			if(dragged){
				Vector2 posFicha = funciones.SeleccionarPos(posMat.x, posMat.y);
				TempPosicionMat = new Vector2(posMat);
				DraggedFicha.setPosition(posFicha.x-offsetW, posFicha.y+offsetH);
			}
			if(atdragged){
				funciones.Untarget(listTarget,HABILIDAD,Nodos);
				if(spelldragged){
					funciones.targetTouch(posMat,listTarget,AlcanceMP,AlcanceHabilidad,HABILIDAD,Nodos,SPELLR);
				}
				else{touchTarget=verificarArea(posMat);
					if(touchTarget){
						funciones.targetTouch(posMat,listTarget,AlcanceMP,AlcanceHabilidad,HABILIDAD,Nodos,NODOSELECTED);
					}
				}
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.02;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.zoom -= 0.02;
		}
	}
	
	public void AreaDraw(){
		Iterator<Vector2> iter = AlcanceMP.iterator();
			while(iter.hasNext()){
				Vector2 posicion = iter.next();
				areaTarget.setPosition(posicion.x, posicion.y);
				areaTarget.draw(batch);			
		}
	}
	
	
	
	/*Con esta funcion se verifica que el puntero este sobre una pocicion en la matriz donde es posible seleccionar
	 *un area, si el puntero se encuentra fuera de rango entonces se retornala falso*/
	private boolean verificarArea(Vector2 posMat){
		Iterator<Vector2> iter = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.AlcanceHabilidad.iterator();
		while(iter.hasNext()){
			Vector2 posicion = iter.next();
			if(posMat.x==posicion.x&&posMat.y==posicion.y)
				return true;
		}
		return false;
	}
	
	@Override
	public void resize(int width, int height) {
		mod[0] = width/2;
		mod[1] = height/2;
	}
	/*El imputo procesor maneja los eventos de entrada sobre el campo de juego, esto no incluye los actores
	 * como botones o controles*/
	private final InputProcessor inputProcessor = new InputAdapter() {
		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			/*Se manejan 3 estados de juego:
			 * el primero UNSELECTED: que indica que ninguna ficha esta seleccionada
			 * el segundo SELECTED: que indica que se ha seleccionado una ficha, en este 
			 * estado tambien es posible mover la ficha tantos espacios como tenga disponibles
			 * el tercero READY: que indica que la ficha esta lista para ejecutar una habilidad*/
			
			/*Antes de interactuar con las fichas y el campo se verifica que la lista de fichas en
			 *el campo no este vacia*/
			if(indexFicha==0 || Gdx.input.isTouched(1) || Gdx.input.isTouched(2)||Gdx.input.isTouched(3)||Gdx.input.isTouched(4))
				return true;
			
			/*Se convierte el puntero a las coordenadas de el juego*/
			Vector2 puntero = funciones.convertirPuntero(x, y, camera, mod);
			
			/*Se recupera un puntero con la ubicacion en la matris del juego*/
			Vector2 indexTemp = funciones.CalcularPosicionMat(puntero.x, puntero.y);
			System.out.println(indexTemp);
			/*Se procede a ejecutar los eventes deacuerdo al estado actual del juego*/
			switch (ESTADO){
			/*Si el estado es UNSELECTED se verificara si la posicion seleccionada esta vacia o pertenece a una ficha,
			 * si pertenece a una ficha se le asignara  la variable de estado del juego y luego se agregan los actoren
			 * a la ficha seleccionada*/
			case UNSELECTED	:
					if(Nodos[(int)indexTemp.y][(int)indexTemp.x].ocupado){
						if(Nodos[(int)indexTemp.y][(int)indexTemp.x].size==2){
							NODOSELECTED =  new Vector2(indexTemp);
							ESTADO = SELECTED;
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.selected();
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.addButton(cardStage);
							SIZESELECTED = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].size;
						}else if(Nodos[(int)indexTemp.y][(int)indexTemp.x].Reflect){
								Vector2 reflectpos = Nodos[(int)indexTemp.y][(int)indexTemp.x].nodoReflect;
								NODOSELECTED =  new Vector2(reflectpos);
								ESTADO = SELECTED;
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.selected();
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.addButton(cardStage);
								SIZESELECTED = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].size;
							}else{
								NODOSELECTED =  new Vector2(indexTemp);
								ESTADO = SELECTED;
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.selected();
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.addButton(cardStage);
								SIZESELECTED = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].size;
							}
					}
					break;
			/*Si la ficha esta seleccionada, es posible que se este seleccionando a un espacio vacio, si la pocicion
			 * se encuentra dentro de el rango de mobilidad de la ficha, se puede ejecutar el movimiento, si se encuentra
			 * fuera del rando la ficha pasaria a un estado UNSELECTED, por lo que se verifica si la posicion seleccionada es tiene valor -1
			 * si no ocurre eso es posible que se este seleccionando otra ficha, por lo que se procede a seleccionar
			 * la ultima ficha seleccionada que estaria en la variable de estado y se limpia el Stage de los actores de la ficha
			 * luego se procede a asignar el nuevo valor a la variable de estado y se agregan los actores que pertenecen a la
			 * nueva ficha seleccionada */
			case SELECTED	:
					if(!Nodos[(int)indexTemp.y][(int)indexTemp.x].ocupado ){
						if(Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.ifPasos(indexTemp)&& Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.Type == 1){
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.MoveFicha(indexTemp);
						}
						else{ 
							Unselect();
						}
					}
					else 
						if(Nodos[(int)indexTemp.y][(int)indexTemp.x].Reflect){
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.unselected();
							cardStage.clear();
							Vector2 reflectpos = Nodos[(int)indexTemp.y][(int)indexTemp.x].nodoReflect;
							NODOSELECTED =  new Vector2(reflectpos);
							ESTADO = SELECTED;
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.selected();
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.addButton(cardStage);
							SIZESELECTED = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].size;
							}
						else{
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.unselected();
							cardStage.clear();
							NODOSELECTED = new Vector2(indexTemp);
							ESTADO = SELECTED;
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.selected();
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.addButton(cardStage);
							SIZESELECTED = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].size;
							}
					break;
			/*Si se ha seleccionado uno de los botones de habilidad de la ficha entra a un estado de READY que puede perderse al
			 * seleccionar un espacion en blanco con valor -1, se lo contrario se porcede a ejecutar la habilidad deacuerdo al tipo
			 * de habilidad*/
			case READY		:
					SelecetHabilidad(indexTemp);
					break;
			}
			return true;
		}
		
		public boolean touchUp(int x, int y, int pointer, int button) {
			if(atdragged){
				atdragged=false;
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(listTarget);
				funciones.Untarget(listTarget,HABILIDAD,Nodos);
			}
			return true;
		}
		
	};
	
	public Objeto getFichaById(int id){
		for(int i = 0 ; i< 38; i++)
			for(int j = 0; j<38; j++){
				if(Nodos[j][i].ficha.IdFicha == id)
					return Nodos[j][i].ficha;
			}
		return null;
	}
	
	public void SelecetHabilidad(Vector2 indexTemp){
		switch (HABILIDAD){
		/*Targeteo simple amistoso*/
			case STFTYPE	:
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(indexTemp);
				break;
				/*Targeteo simple para enemigo*/
			case STETYPE	:
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(indexTemp);
				break;
				/*Targeteo de area amistoso*/
			case ATFTYPE	:
				atdragged=true;			
				break;
				/*Targeteo de area para enemigos*/
			case ATETYPE	:
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(indexTemp);
				break;
				/*Targeteo de area local amistoso*/
			case ALFTYPE	:
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(indexTemp);
				break;
				/*Targeteo de area local para enemigos*/
			case ALETYPE	:
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(indexTemp);
				break;
				/*Auto lanzar habilidad*/
			case ATYPE	:
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(indexTemp);
				break;
		}
	}
	
	public void Unselect(){
		if(NODOSELECTED!=null)
			Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.unselected();
		cardStage.clear();
		ESTADO = UNSELECTED;
		NODOSELECTED = null;
	}
	
	public InputListener getCardListener(final CardData CardData){
		InputListener ObjectListener;
		switch (CardData.tipo){
			case HEROTYPE:
			case RELICTYPE:
				ObjectListener = new InputListener() {
			    	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
			    		ObjectData objetodatatemp = game.mNativeFunctions.getHeroData(CardData.idobjeto);
			    		Objeto objetotem = new Objeto(startgamescreen, objetodatatemp);
			    		DraggedFicha = new Sprite(objetotem.getSprite());
			    		DraggedFicha.setColor(1, 1, 1, 0.7f);
			    		enableDragged();
			    		SIZESELECTED = objetodatatemp.size;
			    		offsetW = objetotem.offsetW;
			    		offsetH = objetotem.offsetH;
			    		if(NODOSELECTED!=null)
			    			Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.unselected();
			    		Unselect();
			            return true;
				    }
			    	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			    		if(SIZESELECTED==2){
			    			if(	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].isWalkable() && 
			    				Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x+1].isWalkable() &&
			    				Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x].isWalkable() &&
			    				Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x+1].isWalkable() ){
			    					    				
			    				ObjectData objetodatatemp = game.mNativeFunctions.getHeroData(CardData.idobjeto);
			    	    		Objeto objetotem = new Objeto(startgamescreen, objetodatatemp);
			    	    		objetotem.setPositionFicha(TempPosicionMat.x,TempPosicionMat.y);
				            	
				            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ficha=objetotem;
				            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].size = objetotem.size;
				            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ficha.IdFicha = indexFicha;
				            	
				            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ocupado = true;
				            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x+1].ocupado = true;
				            	Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x].ocupado = true;
				            	Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x+1].ocupado = true;
				            	
				            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x+1].Reflect = true;
				            	Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x].Reflect = true;
				            	Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x+1].Reflect = true;
				            	
				            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x+1].nodoReflect = TempPosicionMat;
				            	Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x].nodoReflect = TempPosicionMat;
				            	Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x+1].nodoReflect =TempPosicionMat;
				            	indexFicha+=1;
			    			}
			    		}
			    		else{
			    			if(Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].isWalkable()){
			    				
			    				ObjectData objetodatatemp = game.mNativeFunctions.getHeroData(CardData.idobjeto);
			    	    		Objeto objetotem = new Objeto(startgamescreen, objetodatatemp);
			    	    		objetotem.setPositionFicha(TempPosicionMat.x,TempPosicionMat.y);
			    	    		
				            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ficha=objetotem;
				            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ficha.IdFicha = indexFicha;
				            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ocupado = true;
				            	indexFicha+=1;
					            }
							}
			            desableDragged();
					    }
				};
				return ObjectListener;
				
			case SPELLTYPE:
				ObjectListener = new InputListener() {
			    	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
			    		HABILIDAD = ATFTYPE;
			    		SPELLR = 3;
			    		atdragged=true;
			    		spelldragged = true;
			    		Unselect();
				        return true;
				    }
			    	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			    		if(atdragged){
							atdragged=false;
							spelldragged = false;
							Iterator<Vector2> iter = listTarget.iterator();
							while(iter.hasNext()){
								Vector2 item = iter.next();
								Nodos[(int)item.y][(int)item.x].ficha.AnimHabilidad();
						}
							
							funciones.Untarget(listTarget,HABILIDAD,Nodos);
						}
			    	}
				};
				return ObjectListener;
				
			case EQUIPTYPE:
				ObjectListener = new InputListener() {
			    	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
			    		HABILIDAD = ATFTYPE;
			    		SPELLR = 0;
			    		atdragged=true;
			    		spelldragged = true;
			    		Unselect();
				        return true;
				    }
			    	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			    		if(atdragged){
							atdragged=false;
							spelldragged = false;
							Iterator<Vector2> iter = listTarget.iterator();
							while(iter.hasNext()){
								Vector2 item = iter.next();
								Nodos[(int)item.y][(int)item.x].ficha.AnimHabilidad();
						}
							
							funciones.Untarget(listTarget,HABILIDAD,Nodos);
						}
			    	}
				};
				return ObjectListener;
			default :
				return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<CardData> getListCard(){
	      /*String input = "";
	
	      try {
	         URL httpurl = new URL(
	               "http://lazonanegativa.com/mb/deckdata.php");
	         URLConnection httpconn = httpurl.openConnection();
	         httpconn.setConnectTimeout(3000);
	         httpconn.connect();
	         BufferedReader br = new BufferedReader(new InputStreamReader(
	        		 httpconn.getInputStream()));
	         String line;
	         while ((line = br.readLine()) != null) {
	            input += line;
	         }
	         br.close();
	      } catch (IOException e) {
	         e.printStackTrace();
	         input = "";
	      }
	    if(input.equals(""))
	    	return null;
	    else{
	    	listcards = new ArrayList<CardData>();
	    	Json json2 = new Json();
			listcards = json2.fromJson(ArrayList.class, input);
	    }*/
		Json json2 = new Json();
	    listcards = new ArrayList<CardData>();
	    CardData cd1 = new CardData();
	    cd1.faccion = 1;
	    cd1.idobjeto = 1;
	    cd1.tipo = 1;
	    CardData cd2 = new CardData();
	    cd2.faccion = 1;
	    cd2.idobjeto = 2;
	    cd2.tipo = 1;
	    CardData cd3 = new CardData();
	    cd3.faccion = 1;
	    cd3.idobjeto = 3;
	    cd3.tipo = 1;
	    CardData cd4 = new CardData();
	    cd4.faccion = 1;
	    cd4.idobjeto = 1;
	    cd4.tipo = 3;
	    
	    CardData cd5 = new CardData();
	    cd5.faccion = 1;
	    cd5.idobjeto = 1;
	    cd5.tipo = 4;
	    
	    listcards.add(cd1);
	    listcards.add(cd2);
	    listcards.add(cd3);
	    listcards.add(cd4);
	    listcards.add(cd5);
	    
	    System.out.println(json2.toJson(listcards));
		return listcards;
	}
	
	private void crearTorres(){
		ObjectData tdataA = game.mNativeFunctions.getTowerData(1);
		Vector2 posA = PositionTorreA;
		System.out.println(tdataA.nombre);
		Objeto TorreA = new Objeto(this,tdataA);
		ObjectData tdataB = game.mNativeFunctions.getTowerData(1);
		Objeto TorreB = new Objeto(this,tdataB);
		Vector2 posB = PositionTorreB;
		
		TorreA.setPositionFicha(posA.x,posA.y);
    	
    	Nodos[(int)posA.y][(int)posA.x].ficha=TorreA;
    	Nodos[(int)posA.y][(int)posA.x].size = 2;
    	Nodos[(int)posA.y][(int)posA.x].ficha.IdFicha = indexFicha;
    	
    	Nodos[(int)posA.y][(int)posA.x].ocupado = true;
    	Nodos[(int)posA.y][(int)posA.x+1].ocupado = true;
    	Nodos[(int)posA.y+1][(int)posA.x].ocupado = true;
    	Nodos[(int)posA.y+1][(int)posA.x+1].ocupado = true;
    	
    	Nodos[(int)posA.y][(int)posA.x+1].Reflect = true;
    	Nodos[(int)posA.y+1][(int)posA.x].Reflect = true;
    	Nodos[(int)posA.y+1][(int)posA.x+1].Reflect = true;
    	
    	Nodos[(int)posA.y][(int)posA.x+1].nodoReflect = posA;
    	Nodos[(int)posA.y+1][(int)posA.x].nodoReflect = posA;
    	Nodos[(int)posA.y+1][(int)posA.x+1].nodoReflect =posA;
    	
    	TorreB.setPositionFicha(posB.x,posB.y);
    	
    	Nodos[(int)posB.y][(int)posB.x].ficha=TorreB;
    	Nodos[(int)posB.y][(int)posB.x].size = 2;
    	Nodos[(int)posB.y][(int)posB.x].ficha.IdFicha = indexFicha;
    	
    	Nodos[(int)posB.y][(int)posB.x].ocupado = true;
    	Nodos[(int)posB.y][(int)posB.x+1].ocupado = true;
    	Nodos[(int)posB.y+1][(int)posB.x].ocupado = true;
    	Nodos[(int)posB.y+1][(int)posB.x+1].ocupado = true;
    	
    	Nodos[(int)posB.y][(int)posB.x+1].Reflect = true;
    	Nodos[(int)posB.y+1][(int)posB.x].Reflect = true;
    	Nodos[(int)posB.y+1][(int)posB.x+1].Reflect = true;
    	
    	Nodos[(int)posB.y][(int)posB.x+1].nodoReflect = posB;
    	Nodos[(int)posB.y+1][(int)posB.x].nodoReflect = posB;
    	Nodos[(int)posB.y+1][(int)posB.x+1].nodoReflect =posB;		
				
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
}