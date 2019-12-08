package main;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.utilities.GameTreeUtility;

/**
 * 
 * @author yezziga
 *
 */
public class AlphaBetaAgent extends Agent {

	public AlphaBetaAgent(PlayerTurn playerTurn) {
		super(playerTurn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AgentMove getMove(GameBoardState gameState) {

		MoveWrapper alpha = new MoveWrapper(null, Integer.MIN_VALUE);
		MoveWrapper beta = new MoveWrapper(null, Integer.MAX_VALUE);
		
		return minimax(gameState, searchDepth, alpha, beta, true);
	
	}

	/**
	 * Search algorithm based on alpha-beta pruning minimax algorithm with a cut-off
	 * after x(search depth) numbers of draws forward has been examined. Works
	 * recursively to get to the leaf nodes.
	 * 
	 * @param gameState
	 *            the current state of the game
	 * @param depth
	 *            the search depth
	 * @param alpha
	 *            a MoveWrapper with a initial worth of small integer, to represent
	 *            -infinity
	 * @param beta
	 *            a MoveWrapper with a initial worth of large integer, to represent
	 *            +infinity
	 * @param maximizingPlayer
	 *            a boolean whether the player wants to maximize or not
	 * @return
	 */
	public MoveWrapper minimax(GameBoardState gameState, int depth, MoveWrapper alpha, MoveWrapper beta,
			boolean maximizingPlayer) {

		MoveWrapper maxEval = new MoveWrapper(gameState.getLeadingMove(), Integer.MIN_VALUE); // -infinity

		if (gameState.isTerminal() || AgentController.getAvailableMoves(gameState, playerTurn).isEmpty() || depth == 0) {
			nodesExamined++;
			maxEval.setMoveReward((int) AgentController.getGameEvaluation(gameState, playerTurn));
//			System.out.println("in terminal statement " + maxEval.getMoveReward());
			return maxEval;
		}

		if (maximizingPlayer) {

			for (ObjectiveWrapper move : AgentController.getAvailableMoves(gameState, playerTurn)) {
				MoveWrapper eval = minimax(AgentController.getNewState(gameState, move), depth - 1, alpha, beta, false);

				if (eval.getMoveReward() > maxEval.getMoveReward()) { 
					maxEval.setObjectiveInformation(move);
					maxEval.setMoveReward(eval.getMoveReward());
//					System.out.println("Player: " + playerTurn + "Maximizing - Depth: " + depth + ", eval value: " + eval.getMoveReward() + ", maxEval value: " + maxEval.getMoveReward());
				}

				// alpha beta pruning
				 if (eval != null && alpha.compareTo(eval) < 0) {
				 alpha = eval;
				 }
				
				 if (beta.compareTo(alpha) <= 0) {
				 prunedCounter++;
				 break;
				 }
			}
			return maxEval;

		} else {
			MoveWrapper minEval = new MoveWrapper(gameState.getLeadingMove(), Integer.MAX_VALUE); // +infinity

			for (ObjectiveWrapper move : AgentController.getAvailableMoves(gameState, GameTreeUtility.getCounterPlayer(playerTurn))) {
				MoveWrapper eval = minimax(AgentController.getNewState(gameState, move), depth - 1, alpha, beta, true);

				if (eval.getMoveReward() < minEval.getMoveReward()) { 
					minEval.setObjectiveInformation(move);
					minEval.setMoveReward(eval.getMoveReward());
//					System.out.println("Player: " + playerTurn + "Minimizing - Depth: " + depth + ", eval value: " + eval.getMoveReward() + ", minEval value: " + minEval.getMoveReward());
				}

				// alpha beta pruning
				 if (eval != null && beta.compareTo(eval) > 0) {
				 beta = eval;
				 }
				 if (beta.compareTo(alpha) <= 0) {
				 prunedCounter++;
				 break;
				 }
			}
			return minEval;
		}

	}

}
