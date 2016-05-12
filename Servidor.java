package VariosProcessos;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor{
	
	Monte m1, m2, m3, m4;
	Jogo jogo;
	ArrayList<Integer> b;
	ArrayList<Integer> baralho;

	
	public Servidor(){
		
		this.jogo = new Jogo();
		
		//cria as cartas, embaralha, cria os montes e permite que os jogadores peguem as suas cartas
		this.darCartas();
		
	}
	
	private void darCartas(){
		
		baralho = new ArrayList<Integer>(); //baralho embaralhado
		b = new ArrayList<Integer>(); //baralho temporario
		
		//cria as cartas
		for(int i=1; i<=6; i++){
			b.add(i);
			b.add(i);
			b.add(i);
			b.add(i);
		}

		//embaralha
		for(int i=0; i<24; i++){
			int indice = (int)(Math.random() * b.size()) ;
			baralho.add( b.remove( indice ));
		}
		
		//distribui as cartas aos montes
		this.m1 = new Monte(baralho.remove(0), baralho.remove(0));
		this.m2 = new Monte(baralho.remove(0), baralho.remove(0));
		this.m3 = new Monte(baralho.remove(0), baralho.remove(0));
		this.m4 = new Monte(baralho.remove(0), baralho.remove(0));
	}
	
	@SuppressWarnings("unchecked")
	public void run() throws Exception{
		
		ServerSocket serversocket;
		Socket conexao1,conexao2,conexao3,conexao4;
		ObjectInputStream recebe1,recebe2,recebe3,recebe4;
		ObjectOutputStream envia1,envia2,envia3,envia4;   
		boolean venceu1 = false,venceu2 = false,venceu3 = false,venceu4 = false;
		Integer carta = -1; //carta que quer devolver ou carta que comprou
		Integer monte; //monte que quer comprar ou que quer devolver
		String msg1,msg2,msg3,msg4; //acao (compra,devolucao,cartas,ganhador)
		ArrayList<Integer> cartas; //mao do jogador
		String nome; //nome do jogador que quer ter suas cartas verificadas
		cartas = new ArrayList<Integer>();
		
		//criando um socket servidor
		serversocket = new ServerSocket ( 5001,  100 );
		
		//while(true){
			
			System.out.println("AGUARDANDO CONEXAO");
			
			//aguardando uma conexao
			conexao1 = serversocket.accept();
			conexao2 = serversocket.accept();
			conexao3 = serversocket.accept();
			conexao4 = serversocket.accept();
			
			//obtendo os fluxos de entrada e de saida
			envia1 = new ObjectOutputStream( conexao1.getOutputStream());
			envia2 = new ObjectOutputStream( conexao2.getOutputStream());
			envia3 = new ObjectOutputStream( conexao3.getOutputStream());
			envia4 = new ObjectOutputStream( conexao4.getOutputStream());
			
			recebe1 = new ObjectInputStream( conexao1.getInputStream());
			recebe2 = new ObjectInputStream( conexao2.getInputStream());
			recebe3 = new ObjectInputStream( conexao3.getInputStream());
			recebe4 = new ObjectInputStream( conexao4.getInputStream());
			
			do{

				System.out.println("SERVER - ESPERANDO REQUISICAO");
				
				//esperando requisicao
				msg1 = (String)recebe1.readObject();
				msg2 = (String)recebe2.readObject();
				msg3 = (String)recebe3.readObject();
				msg4 = (String)recebe4.readObject();
				
				//caso seja uma compra de carta
				if (msg1.equals("Compra")){
					
					System.out.println("SERVER - COMPRA");
					
					//recebe um monte
					monte = (Integer)recebe1.readObject();
					
					//retira uma carta desse monte
					switch(monte){
					case 1: carta = m1.retirar(); break;
					case 2: carta = m2.retirar(); break;
					case 3: carta = m3.retirar(); break;
					case 4: carta = m4.retirar(); break;
					}
					
					//envia essa carta
					envia1.writeObject(carta);
					envia1.flush();
					
				}else if(msg1.equals("Devolucao")){
					
					System.out.println("SERVER - DEVOLUCAO");
					
					carta = (Integer)recebe1.readObject();
					monte = (Integer)recebe1.readObject();

					//devolve uma carta para esse monte
					switch(monte){
					case 1: m1.colocar(carta); break;
					case 2: m2.colocar(carta); break;
					case 3: m3.colocar(carta); break;
					case 4: m4.colocar(carta); break;
					}
					
				}else if(msg1.equals("VerificaVencedor")){
					
					System.out.println("SERVER - VERIFICACAO");
					
					cartas.clear();
					cartas =  (ArrayList<Integer>)recebe1.readObject();
					nome = (String)recebe1.readObject();
					
					//verifica a mao desse jogador
					venceu1 = jogo.verificarJogo(cartas, nome);
					
					//envia o resultado da verificacao
					envia1.writeObject(venceu1);
					envia1.flush();
				
			    }else if(msg1.equals("Cartas")){
			    	
			    	System.out.println("SERVER - DANDO AS CARTAS");
			    	
			    	cartas.clear();
			    	cartas.add(baralho.remove(0));
			    	cartas.add(baralho.remove(0));
			    	cartas.add(baralho.remove(0));
			    	cartas.add(baralho.remove(0));
			    	
			    	//envia o resultado da verificacao
					envia1.writeObject(cartas);
					envia1.flush();
				
				
			    }else{
					System.out.println("ERRO!!" + msg1);
				}
				//=========================DOIS================================
				//caso seja uma compra de carta
				if (msg2.equals("Compra")){
					
					System.out.println("SERVER - COMPRA");
					
					//recebe um monte
					monte = (Integer)recebe2.readObject();
					
					//retira uma carta desse monte
					switch(monte){
					case 1: carta = m1.retirar(); break;
					case 2: carta = m2.retirar(); break;
					case 3: carta = m3.retirar(); break;
					case 4: carta = m4.retirar(); break;
					}
					
					//envia essa carta
					envia2.writeObject(carta);
					envia2.flush();
					
				}else if(msg2.equals("Devolucao")){
					
					System.out.println("SERVER - DEVOLUCAO");
					
					carta = (Integer)recebe2.readObject();
					monte = (Integer)recebe2.readObject();

					//devolve uma carta para esse monte
					switch(monte){
					case 1: m1.colocar(carta); break;
					case 2: m2.colocar(carta); break;
					case 3: m3.colocar(carta); break;
					case 4: m4.colocar(carta); break;
					}
					
				}else if(msg2.equals("VerificaVencedor")){
					
					System.out.println("SERVER - VERIFICACAO");
					
					cartas.clear();
					cartas = (ArrayList<Integer>)recebe2.readObject();
					nome = (String)recebe2.readObject();
					
					//verifica a mao desse jogador
					venceu2 = jogo.verificarJogo(cartas, nome);
					
					//envia o resultado da verificacao
					envia2.writeObject(venceu2);
					envia2.flush();
				
			    }else if(msg2.equals("Cartas")){
			    	
			    	System.out.println("SERVER - DANDO AS CARTAS");
			    	
			    	cartas.clear();
			    	cartas.add(baralho.remove(0));
			    	cartas.add(baralho.remove(0));
			    	cartas.add(baralho.remove(0));
			    	cartas.add(baralho.remove(0));
			    	
			    	//envia o resultado da verificacao
					envia2.writeObject(cartas);
					envia2.flush();
				
				
			    }else{
					System.out.println("ERRO!" + msg2);
				}
				//===========================TRES==============================
				//caso seja uma compra de carta
				if (msg3.equals("Compra")){
					
					System.out.println("SERVER - COMPRA");
					
					//recebe um monte
					monte = (Integer)recebe3.readObject();
					
					//retira uma carta desse monte
					switch(monte){
					case 1: carta = m1.retirar(); break;
					case 2: carta = m2.retirar(); break;
					case 3: carta = m3.retirar(); break;
					case 4: carta = m4.retirar(); break;
					}
					
					//envia essa carta
					envia3.writeObject(carta);
					envia3.flush();
					
				}else if(msg3.equals("Devolucao")){
					
					System.out.println("SERVER - DEVOLUCAO");
					
					carta = (Integer)recebe3.readObject();
					monte = (Integer)recebe3.readObject();

					//devolve uma carta para esse monte
					switch(monte){
					case 1: m1.colocar(carta); break;
					case 2: m2.colocar(carta); break;
					case 3: m3.colocar(carta); break;
					case 4: m4.colocar(carta); break;
					}
					
				}else if(msg3.equals("VerificaVencedor")){
					
					System.out.println("SERVER - VERIFICACAO");
					
					cartas.clear();
					cartas = (ArrayList<Integer>)recebe3.readObject();
					nome = (String)recebe3.readObject();
					
					//verifica a mao desse jogador
					venceu3 = jogo.verificarJogo(cartas, nome);
					
					//envia o resultado da verificacao
					envia3.writeObject(venceu3);
					envia3.flush();
				
			    }else if(msg3.equals("Cartas")){
			    	
			    	System.out.println("SERVER - DANDO AS rsCARTAS");
			    	
			    	cartas.clear();
			    	cartas.add(baralho.remove(0));
			    	cartas.add(baralho.remove(0));
			    	cartas.add(baralho.remove(0));
			    	cartas.add(baralho.remove(0));
			    	
			    	//envia o resultado da verificacao
					envia3.writeObject(cartas);
					envia3.flush();
				
				
			    }else{
					System.out.println("ERRO!" + msg3);
				}
				//=============================QUATRO============================
				//caso seja uma compra de carta
				if (msg4.equals("Compra")){
					
					System.out.println("SERVER - COMPRA");
					
					//recebe um monte
					monte = (Integer)recebe4.readObject();
					
					//retira uma carta desse monte
					switch(monte){
					case 1: carta = m1.retirar(); break;
					case 2: carta = m2.retirar(); break;
					case 3: carta = m3.retirar(); break;
					case 4: carta = m4.retirar(); break;
					}
					
					//envia essa carta
					envia4.writeObject(carta);
					envia4.flush();
					
				}else if(msg4.equals("Devolucao")){
					
					System.out.println("SERVER - DEVOLUCAO");
					
					carta = (Integer)recebe4.readObject();
					monte = (Integer)recebe4.readObject();

					//devolve uma carta para esse monte
					switch(monte){
					case 1: m1.colocar(carta); break;
					case 2: m2.colocar(carta); break;
					case 3: m3.colocar(carta); break;
					case 4: m4.colocar(carta); break;
					}
					
				}else if(msg4.equals("VerificaVencedor")){
					
					System.out.println("SERVER - VERIFICACAO");
					
					cartas.clear();
					cartas = (ArrayList<Integer>)recebe4.readObject();
					nome = (String)recebe4.readObject();
					
					//verifica a mao desse jogador
					venceu4 = jogo.verificarJogo(cartas, nome);

					//envia o resultado da verificacao
					envia4.writeObject(venceu4);
					envia4.flush();
				
			    }else if(msg4.equals("Cartas")){
			    	
			    	System.out.println("SERVER - DANDO AS CARTAS");
			    	
			    	cartas.clear();
			    	cartas.add(baralho.remove(0));
			    	cartas.add(baralho.remove(0));
			    	cartas.add(baralho.remove(0));
			    	cartas.add(baralho.remove(0));
			    	
			    	//envia o resultado da verificacao
					envia4.writeObject(cartas);
					envia4.flush();
				
				
			    }else{
					System.out.println("ERRO!" + msg4);
				}
				//=========================================================
			}while(!venceu1 && !venceu2 && !venceu3 && !venceu4);
			
			//fechando a conexao
			envia1.close();
			recebe1.close();
			conexao1.close();
			envia2.close();
			recebe2.close();
			conexao2.close();
			envia3.close();
			recebe3.close();
			conexao3.close();
			envia4.close();
			recebe4.close();
			conexao4.close();
		}
		
	//}
	
}