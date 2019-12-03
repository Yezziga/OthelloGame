package main;


import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;

/**
 * 
 * @author yezziga
 *
 */
public class AlphaBetaAgent extends Agent {

	public AlphaBetaAgent() {
		super(PlayerTurn.PLAYER_ONE);
		// TODO Auto-generated constructor stub
	}

	private AlphaBetaAgent(PlayerTurn playerTurn) {
		super(playerTurn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AgentMove getMove(GameBoardState gameState) {

		MoveWrapper alpha = new MoveWrapper(null, Integer.MIN_VALUE);
		MoveWrapper beta = new MoveWrapper(null, Integer.MAX_VALUE);

		if (playerTurn == PlayerTurn.PLAYER_ONE) {
			return minimax(gameState, searchDepth, alpha, beta, true);
		} else {
			return minimax(gameState, searchDepth, alpha, beta, false);
		}
	}

	/**
	 * Search algorithm based on alpha-beta pruning minimax algorithm with a cut-off
	 * after x(search depth) numbers of draws forward has been examined.
	 * 
	 * @param gameState
	 *            the current state of the game
	 * @param depth
	 *            the search depth
	 * @param alpha
	 *            a MoveWrapper with a initial worth of small integer, closing in to
	 *            -infinity
	 * @param beta
	 *            a MoveWrapper with a initial worth of large integer, closing in to
	 *            +infinity
	 * @param maximizingPlayer
	 *            a boolean whether the player wants to maximize or not
	 * @return
	 */
	public MoveWrapper minimax(GameBoardState gameState, int depth, MoveWrapper alpha, MoveWrapper beta,
			boolean maximizingPlayer) {
		if (depth == 0 || gameState.isTerminal()) {
			return new MoveWrapper(gameState.getLeadingMove());
		}

		if (maximizingPlayer) {
			MoveWrapper maxEval = new MoveWrapper(null, Integer.MIN_VALUE); // -infinity
			
			for (ObjectiveWrapper move : AgentController.getAvailableMoves(gameState, PlayerTurn.PLAYER_ONE)) {
				MoveWrapper eval = minimax(AgentController.getNewState(gameState, move), depth - 1, alpha, beta, false);
				if (eval == null) {
					maxEval = new MoveWrapper(gameState.getLeadingMove());
				} else if (maxEval == null || eval.compareTo(maxEval) > 0) {
					maxEval = new MoveWrapper(move);
				}

				
				if (eval != null && alpha.compareTo(eval) < 0) {
					alpha = eval;
				}
				
				if (beta.compareTo(alpha) <= 0) {
					prunedCounter++;
					break;
				}
				nodesExamined++;
			}
			return maxEval;

		} else {
			MoveWrapper minEval = new MoveWrapper(null, Integer.MAX_VALUE); // +infinity
			
			for (ObjectiveWrapper move : AgentController.getAvailableMoves(gameState, PlayerTurn.PLAYER_TWO)) {
				MoveWrapper eval = minimax(AgentController.getNewState(gameState, move), depth - 1, alpha, beta, true);
				if (eval == null) {
					minEval = new MoveWrapper(gameState.getLeadingMove());
				} else if (minEval == null || eval.compareTo(minEval) > 0) {
					minEval = new MoveWrapper(move);
				}

				if (eval != null && beta.compareTo(eval) > 0) {
					beta = eval;
				}
				if (beta.compareTo(alpha) <= 0) {
					prunedCounter++;
					break;
				}
				nodesExamined++;
			}
			return minEval;
		}

	}

}
