package student_player;

import static pentago_swap.PentagoBoardState.BOARD_SIZE;

import java.util.ArrayList;
import java.util.List;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;

public abstract class PentagoHeuristicBase implements PentagoHeuristic {

	protected enum Condition {
		THREE_AND_TWO_EMPTY, FOUR_AND_ONE_EMPTY, FOUR_AND_ONE_EMPTY_OR_FIVE, FIVE
	}

	protected Piece studentPiece;
	protected Piece opponentPiece;

	protected void initPieces(int studentPlayerID) {
		if (studentPlayerID == 0) {
			// student played first with white
			studentPiece = Piece.WHITE;
			// opponent played second with black
			opponentPiece = Piece.BLACK;
		} else {
			// opponent played second with white
			opponentPiece = Piece.WHITE;
			// student played second with black
			studentPiece = Piece.BLACK;
		}
	}

	protected Piece[][] getBoard(PentagoBoardState boardState) {
		Piece[][] board = new Piece[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				board[i][j] = boardState.getPieceAt(i, j);
			}
		}
		return board;
	}

	// top left, top right
	protected Piece[][] getBoardSwapTopLeftTopRight(PentagoBoardState boardState) {
		Piece[][] board = getBoard(boardState);
		swap(board, 0, 0, 0, BOARD_SIZE / 2);
		return board;
	}

	// top left, bottom left
	protected Piece[][] getBoardSwapTopLeftBottomLeft(PentagoBoardState boardState) {
		Piece[][] board = getBoard(boardState);
		swap(board, 0, 0, BOARD_SIZE / 2, 0);
		return board;
	}

	// top left, bottom right
	protected Piece[][] getBoardSwapTopLeftBottomRight(PentagoBoardState boardState) {
		Piece[][] board = getBoard(boardState);
		swap(board, 0, 0, BOARD_SIZE / 2, BOARD_SIZE / 2);
		return board;
	}

	// top right, bottom right
	protected Piece[][] getBoardSwapTopRightBottomRight(PentagoBoardState boardState) {
		Piece[][] board = getBoard(boardState);
		swap(board, 0, BOARD_SIZE / 2, BOARD_SIZE / 2, BOARD_SIZE / 2);
		return board;
	}

	// top right, bottom left
	protected Piece[][] getBoardSwapTopRightBottomLeft(PentagoBoardState boardState) {
		Piece[][] board = getBoard(boardState);
		swap(board, 0, BOARD_SIZE / 2, BOARD_SIZE / 2, 0);
		return board;
	}

	// bottom left, bottom right
	protected Piece[][] getBoardSwapBottomLeftBottomRight(PentagoBoardState boardState) {
		Piece[][] board = getBoard(boardState);
		swap(board, 0, 0, BOARD_SIZE / 2, BOARD_SIZE / 2);
		return board;
	}

	protected void swap(Piece[][] board, int i1, int j1, int i2, int j2) {
		for (int i = 0; i < BOARD_SIZE / 2; i++) {
			for (int j = 0; j < BOARD_SIZE / 2; j++) {
				Piece p = board[i1 + i][j1 + j];
				board[i1 + i][j1 + j] = board[i2 + i][j2 + j];
				board[i2 + i][j2 + j] = p;
			}
		}
	}

	protected List<Piece[][]> getSwapBoards(PentagoBoardState boardState) {
		Piece[][] board;
		List<Piece[][]> boards = new ArrayList<>();

		// swap top left with top right
		board = getBoardSwapTopLeftTopRight(boardState);
		boards.add(board);

		// swap top left with bottom left
		board = getBoardSwapTopLeftBottomLeft(boardState);
		boards.add(board);

		// swap top left with bottom right
		board = getBoardSwapTopLeftBottomRight(boardState);
		boards.add(board);

		// swap top right with bottom right
		board = getBoardSwapTopRightBottomRight(boardState);
		boards.add(board);

		// swap top right with bottom left
		board = getBoardSwapTopRightBottomLeft(boardState);
		boards.add(board);

		// swap bottom left with bottom right
		board = getBoardSwapBottomLeftBottomRight(boardState);
		boards.add(board);

		return boards;
	}

	protected List<Piece[][]> getSwapBoardsWithExcludeCondition(PentagoBoardState boardState, Piece otherPiece,
			Condition excludeCondition) {
		Piece[][] board;
		List<Piece[][]> boards = new ArrayList<>();

		// swap top left with top right
		board = getBoardSwapTopLeftTopRight(boardState);
		if (!checkConditionOnBoard(board, otherPiece, excludeCondition)) {
			boards.add(board);
		}

		// swap top left with bottom left
		board = getBoardSwapTopLeftBottomLeft(boardState);
		if (!checkConditionOnBoard(board, otherPiece, excludeCondition)) {
			boards.add(board);
		}

		// swap top left with bottom right
		board = getBoardSwapTopLeftBottomRight(boardState);
		if (!checkConditionOnBoard(board, otherPiece, excludeCondition)) {
			boards.add(board);
		}

		// swap top right with bottom right
		board = getBoardSwapTopRightBottomRight(boardState);
		if (!checkConditionOnBoard(board, otherPiece, excludeCondition)) {
			boards.add(board);
		}

		// swap top right with bottom left
		board = getBoardSwapTopRightBottomLeft(boardState);
		if (!checkConditionOnBoard(board, otherPiece, excludeCondition)) {
			boards.add(board);
		}

		// swap bottom left with bottom right
		board = getBoardSwapBottomLeftBottomRight(boardState);
		if (!checkConditionOnBoard(board, otherPiece, excludeCondition)) {
			boards.add(board);
		}

		return boards;
	}

	protected boolean checkConditionOnBoards(List<Piece[][]> boards, Piece turnPiece, Condition condition) {
		for (Piece[][] bboard : boards) {
			if (checkConditionOnBoard(bboard, turnPiece, condition)) {
				return true;
			}
		}

		return false;
	}

	protected boolean checkConditionOnBoard(Piece[][] board, Piece piece, Condition condition) {
		if (checkConditionOnRows(board, piece, condition)) {
			return true;
		}
		if (checkConditionOnColumns(board, piece, condition)) {
			return true;
		}
		if (checkConditionOnDiagonals(board, piece, condition)) {
			return true;
		}

		return false;
	}

	protected boolean checkConditionOnRows(Piece[][] board, Piece piece, Condition condition) {
		Piece[] pieces = new Piece[5];

		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < 5; j++) {
				pieces[j] = board[i][j];
			}
			if (checkCondition(pieces, piece, condition)) {
				return true;
			}

			for (int j = 1; j < 6; j++) {
				pieces[j - 1] = board[i][j];
			}
			if (checkCondition(pieces, piece, condition)) {
				return true;
			}
		}

		return false;
	}

	protected boolean checkConditionOnColumns(Piece[][] board, Piece piece, Condition condition) {
		Piece[] pieces = new Piece[5];

		for (int j = 0; j < BOARD_SIZE; j++) {
			for (int i = 0; i < 5; i++) {
				pieces[i] = board[i][j];
			}
			if (checkCondition(pieces, piece, condition)) {
				return true;
			}

			for (int i = 1; i < 6; i++) {
				pieces[i - 1] = board[i][j];
			}
			if (checkCondition(pieces, piece, condition)) {
				return true;
			}
		}

		return false;
	}

	protected boolean checkConditionOnDiagonals(Piece[][] board, Piece piece, Condition condition) {
		// descending
		if (checkConditionOnDescendingDiagonal(board, piece, 0, 0, condition)) {
			return true;
		}
		if (checkConditionOnDescendingDiagonal(board, piece, 1, 1, condition)) {
			return true;
		}
		if (checkConditionOnDescendingDiagonal(board, piece, 0, 1, condition)) {
			return true;
		}
		if (checkConditionOnDescendingDiagonal(board, piece, 1, 0, condition)) {
			return true;
		}

		// ascending
		if (checkConditionOnAscendingDiagonal(board, piece, BOARD_SIZE - 1, 0, condition)) {
			return true;
		}
		if (checkConditionOnAscendingDiagonal(board, piece, BOARD_SIZE - 2, 1, condition)) {
			return true;
		}
		if (checkConditionOnAscendingDiagonal(board, piece, BOARD_SIZE - 2, 0, condition)) {
			return true;
		}
		if (checkConditionOnAscendingDiagonal(board, piece, BOARD_SIZE - 1, 1, condition)) {
			return true;
		}

		return false;
	}

	protected boolean checkConditionOnDescendingDiagonal(Piece[][] board, Piece piece, int i, int j,
			Condition condition) {
		Piece[] pieces = new Piece[5];
		for (int k = 0; k < 5; k++) {
			pieces[k] = board[i + k][j + k];
		}
		if (checkCondition(pieces, piece, condition)) {
			return true;
		}

		return false;
	}

	protected boolean checkConditionOnAscendingDiagonal(Piece[][] board, Piece piece, int i, int j,
			Condition condition) {
		Piece[] pieces = new Piece[5];
		for (int k = 0; k < 5; k++) {
			pieces[k] = board[i - k][j + k];
		}
		if (checkCondition(pieces, piece, condition)) {
			return true;
		}

		return false;
	}

	protected int countConditionOnBoards(List<Piece[][]> boards, Piece turnPiece, Condition condition) {
		int nCount = 0;
		int nMaxCount = 0;

		for (Piece[][] bboard : boards) {
			nCount = countConditionOnBoard(bboard, turnPiece, condition);
			if (nCount > nMaxCount) {
				nMaxCount = nCount;
			}
		}

		return nMaxCount;
	}

	protected int countConditionOnBoard(Piece[][] board, Piece piece, Condition condition) {
		int counter = 0;
		counter += countConditionOnRows(board, piece, condition);
		counter += countConditionOnColumns(board, piece, condition);
		counter += countConditionOnDiagonals(board, piece, condition);
		return counter;
	}

	protected int countConditionOnRows(Piece[][] board, Piece piece, Condition condition) {
		int counter = 0;

		Piece[] pieces = new Piece[5];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < 5; j++) {
				pieces[j] = board[i][j];
			}
			if (checkCondition(pieces, piece, condition)) {
				counter++;
			}

			for (int j = 1; j < 6; j++) {
				pieces[j - 1] = board[i][j];
			}
			if (checkCondition(pieces, piece, condition)) {
				counter++;
			}
		}

		return counter;
	}

	protected int countConditionOnColumns(Piece[][] board, Piece piece, Condition condition) {
		int counter = 0;

		Piece[] pieces = new Piece[5];
		for (int j = 0; j < BOARD_SIZE; j++) {
			for (int i = 0; i < 5; i++) {
				pieces[i] = board[i][j];
			}
			if (checkCondition(pieces, piece, condition)) {
				counter++;
			}

			for (int i = 1; i < 6; i++) {
				pieces[i - 1] = board[i][j];
			}
			if (checkCondition(pieces, piece, condition)) {
				counter++;
			}
		}

		return counter;
	}

	protected int countConditionOnDiagonals(Piece[][] board, Piece piece, Condition condition) {
		int counter = 0;

		// descending
		counter += countConditionOnDescendingDiagonal(board, piece, 0, 0, condition);
		counter += countConditionOnDescendingDiagonal(board, piece, 1, 1, condition);
		counter += countConditionOnDescendingDiagonal(board, piece, 0, 1, condition);
		counter += countConditionOnDescendingDiagonal(board, piece, 1, 0, condition);

		// ascending
		counter += countConditionOnAscendingDiagonal(board, piece, BOARD_SIZE - 1, 0, condition);
		counter += countConditionOnAscendingDiagonal(board, piece, BOARD_SIZE - 2, 1, condition);
		counter += countConditionOnAscendingDiagonal(board, piece, BOARD_SIZE - 2, 0, condition);
		counter += countConditionOnAscendingDiagonal(board, piece, BOARD_SIZE - 1, 1, condition);

		return counter;
	}

	protected int countConditionOnDescendingDiagonal(Piece[][] board, Piece piece, int i, int j, Condition condition) {
		int counter = 0;

		Piece[] pieces = new Piece[5];
		for (int k = 0; k < 5; k++) {
			pieces[k] = board[i + k][j + k];
		}
		if (checkCondition(pieces, piece, condition)) {
			counter++;
		}

		return counter;
	}

	protected int countConditionOnAscendingDiagonal(Piece[][] board, Piece piece, int i, int j, Condition condition) {
		int counter = 0;

		Piece[] pieces = new Piece[5];
		for (int k = 0; k < 5; k++) {
			pieces[k] = board[i - k][j + k];
		}
		if (checkCondition(pieces, piece, condition)) {
			counter++;
		}

		return counter;
	}

	protected Pair<Integer, Integer> countNumberOfPiecesLike(Piece targetPiece, Piece[] pieces) {
		Pair<Integer, Integer> pair = new Pair<>(0, 0);
		for (Piece piece : pieces) {
			if (piece == targetPiece) {
				pair.key++;
			} else if (piece == Piece.EMPTY) {
				pair.value++;
			}
		}
		return pair;
	}

	protected boolean checkCondition(Piece[] pieces, Piece piece, Condition condition) {
		Pair<Integer, Integer> pair = countNumberOfPiecesLike(piece, pieces);

		switch (condition) {
		case THREE_AND_TWO_EMPTY:
			return pair.key == 3 && pair.value == 2;
		case FOUR_AND_ONE_EMPTY:
			return pair.key == 4 && pair.value == 1;
		case FOUR_AND_ONE_EMPTY_OR_FIVE:
			return pair.key == 4 && pair.value == 1 || pair.key == 5;
		case FIVE:
			return pair.key == 5;
		}

		return false;
	}

}
