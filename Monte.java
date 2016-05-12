package VariosProcessos;

import java.util.Stack;

public class Monte {
	
	Stack<Integer> cartas = new Stack<Integer>();
	
	public Monte(Integer carta1, Integer carta2){
		
		cartas.push(carta1);
		cartas.push(carta2);
	}
	
	public synchronized int retirar(){
		
		//caso nao tenha cartas para retirar
		if (cartas.empty()){
			try {
				
				//espera ate que tenha
				wait();
			} catch (InterruptedException e) {}
		}
		
		//retorna a carta
		return cartas.pop();
	}
	
	public synchronized void colocar(int carta){

		//coloca a carta
		cartas.push(carta);
		
		//avisa um possivel jogador
		notify();
		
	}
}
