package gamelogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.tools.DocumentationTool.Location;

import model.Chess;
import model.ChessBoard;
import model.Operation;

public class ComputerComponent{
	
 public static void Operate(int player, Game game) {
	 if(Game.msgToOp(game.msg).player!=player) return;
	 List<Operation> oplist=allAvailableOperate(player,game);
	 if(Game.msgToOp(game.msg).optype==Operation.EAT) {game.toEat(randomOperation(oplist));}
	 game.toDo(randomOperation(oplist));
 }
 
 public static List<Operation> allAvailableOperate(int player, Game game) {
	 Operation op= Game.msgToOp(game.msg);
	 if(op.player!=player)return null;
	 ArrayList<Operation> opList = new ArrayList<Operation>();
	 
	 if(op.optype==Operation.PLACE) 
	   {
		 for(model.Location l:model.Location.values()) {
			 Operation op0=new Operation();
			 op0.optype=op.optype;
			 op0.player=op.player;
			 op0.lstart=l;
			 if(game.csbd.placable(op0)) {opList.add(op0);}
			 }
		 } 
	 if(op.optype==Operation.MOVE) {
		 for(Chess a: game.csbd.chessonboard) 
		 {
			 if(a.player!=player)continue;
			 for(model.Location l:model.Location.values()) {
				 Operation op0=new Operation();
				 op0.optype=op.optype;
				 op0.player=op.player;
				 op0.lstart=a.l;
				 op0.lfinish=l;
				 if(game.csbd.movable(op0)&&model.Location.isAdjacent(op0.lstart, op0.lfinish)) {opList.add(op0);}
				 if(game.csbd.movable(op0)&&game.csbd.flyable(op0)) {opList.add(op0);}
				 }
		 }
	     }
	 if(op.optype==Operation.EAT) 
	 {
		 for(Chess a: game.csbd.chessonboard) 
		 {
			 if(a.player==player)continue;
			 Operation op0=new Operation();
			 op0.optype=op.optype;
			 op0.player=op.player;
			 op0.lstart=a.l;
			 if(game.csbd.eatable(op0))opList.add(op0);
		 }
	 }
        return opList;
	 } 
 public static Operation randomOperation(List<Operation> opList) 
 {
	 Random rand = new Random();
	   return opList.get(rand.nextInt(opList.size()));
 }


}
