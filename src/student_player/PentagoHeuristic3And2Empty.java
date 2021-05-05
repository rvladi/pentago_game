package student_player;

import java.util.List;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;

public class PentagoHeuristic3And2Empty extends PentagoHeuristicBase {

	@Override
	public double calculateScore(PentagoBoardState boardState, int studentPlayerID) {
		initPieces(studentPlayerID);

		double score;
		if (boardState.getTurnPlayer() == studentPlayerID) {
			// student's turn
			score = calculateScore(boardState, studentPiece, opponentPiece);
		} else {
			// opponent's turn
			score = 1 - calculateScore(boardState, opponentPiece, studentPiece);
		}

		return score;
	}

	private double calculateScore(PentagoBoardState boardState, Piece turnPiece, Piece nonturnPiece) {
		List<Piece[][]> boards = getSwapBoards(boardState);

		int countTurnPlayer = countConditionOnBoards(boards, turnPiece, Condition.THREE_AND_TWO_EMPTY);
		int countNonturnPlayer = countConditionOnBoards(boards, nonturnPiece, Condition.THREE_AND_TWO_EMPTY);
		return Math.max(0, Math.min(1, 0.5 + 0.01 * (countTurnPlayer - countNonturnPlayer)));
	}

}
