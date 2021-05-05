package student_player;

import java.util.List;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;

public class PentagoHeuristic4And1EmptyOr5 extends PentagoHeuristicBase {

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
		// swap boards which do not contain 5 non-turn pieces
		List<Piece[][]> boards = getSwapBoardsWithExcludeCondition(boardState, nonturnPiece, Condition.FIVE);
		if (boards.isEmpty()) {
			// non-turn player wins on next move or draw
			// all swap boards contain 5 non-turn pieces (row, column or diagonal)
			return 0;
		}

		if (checkConditionOnBoards(boards, turnPiece, Condition.FOUR_AND_ONE_EMPTY_OR_FIVE)) {
			// turn player wins: found a swap board which contains 4 and 1 empty or 5 turn pieces
			return 1;
		}

		boards = getSwapBoardsWithExcludeCondition(boardState, nonturnPiece, Condition.FOUR_AND_ONE_EMPTY_OR_FIVE);
		if (boards.isEmpty()) {
			// non-turn player wins on next move or draw
			// all swap boards contain 4 and 1 empty or 5 non-turn pieces (row, column or diagonal)
			return 0;
		}

		return 0.5;
	}

}
