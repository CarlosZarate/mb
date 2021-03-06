package com.mb.objects;

import com.badlogic.gdx.math.Vector2;

public class Nodo {
	
	public Objeto ficha;
	public Nodo parent;
	public boolean ocupado;
	public boolean closed;
	public boolean opened;
	public boolean target;
	public boolean blocked;
	public int x;
	public int y;
	public int size = 1;
	public Vector2 [][] espace = new Vector2[2][2];
	public boolean Reflect;//1 = single, 2 = reflect
	public boolean ReflectThis;
	public Vector2 nodoReflect;
	public boolean guia;
	public Vector2 posicion = new Vector2();
	
	public Nodo(int initx, int inity){
		ficha = null;
		parent = null;
		ocupado = false;
		opened = false;
		closed = false;
		x=initx;
		y=inity;
		posicion.x = initx;
		posicion.y = inity;
		Reflect = false;
		nodoReflect=null;
		guia=false;
		target = false;
		blocked = false;
	}
	
	public void setPosicion(int setx, int sety){
		posicion.x= setx;
		x = setx;
		posicion.y= sety;
		y = sety;
	}
	
	public void clean(){
		opened = false;
		closed = false;
		ReflectThis=false;
		guia=false;
		target = false;
	}
	
	public boolean isWalkable(){
		return !blocked && !ocupado;
	}

}
