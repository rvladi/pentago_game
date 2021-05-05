package student_player;

import java.util.Iterator;
import java.util.List;

import boardgame.Board;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

public class Node implements Iterable<Node> {

	private static final PentagoHeuristic HEURISTIC = getHeuristic();

	private final PentagoMove previousMove;
	private final PentagoBoardState boardState;
	private final int studentPlayerID;

	private final List<PentagoMove> legalMoves;
	private final int depthLimit;

	public Node(PentagoMove previousMove, PentagoBoardState boardState, int studentPlayerID) {
		this.previousMove = previousMove;
		this.boardState = boardState;
		this.studentPlayerID = studentPlayerID;

		legalMoves = boardState.getAllLegalMoves();
		depthLimit = calculateDepthLimit(legalMoves.size());
	}

	private static PentagoHeuristic getHeuristic() {
		CompositePentagoHeuristic heuristic = new CompositePentagoHeuristic();
		heuristic.add(new PentagoHeuristic4And1EmptyOr5());
		heuristic.add(new PentagoHeuristic3And2Empty());
		return heuristic;
	}

	private static int calculateDepthLimit(int numLegalMoves) {
		if (numLegalMoves <= 4 * 6) {
			// 4 or fewer pieces left to play
			return 4;
		}
		if (numLegalMoves <= 10 * 6) {
			// 10 or fewer pieces left to play
			return 3;
		}
		return 2;
	}

	/**
	 * @return the board state associated with this node
	 */
	public PentagoBoardState getBoardState() {
		return boardState;
	}

	/**
	 * @return true if the board state associated with this node corresponds to an
	 *         ended game (student player win, opponent win or draw); otherwise
	 *         false
	 */
	public boolean isTerminal() {
		return boardState.getWinner() != Board.NOBODY;
	}

	/**
	 * @return the score of the board state associated with this node; if the node
	 *         is terminal, the score is 1 for student player win, 0 for opponent
	 *         win and 0.5 for draw; otherwise it is calculated using heuristics
	 */
	public double getScore() {
		int winner = boardState.getWinner();
		if (winner == Board.NOBODY) {
			return HEURISTIC.calculateScore(boardState, studentPlayerID);
		}
		if (winner == Board.DRAW) {
			return 0.5;
		}
		return (winner == studentPlayerID) ? 1 : 0;
	}

	/**
	 * @return the move that generated the board state associated with this node
	 */
	public PentagoMove getPreviousMove() {
		return previousMove;
	}

	/**
	 * @return the next best move for the student player
	 */
	public PentagoMove getNextMove() {
		Pair<Node, Double> maxScore = alphaBetaSearch();
		return maxScore.key.getPreviousMove();
	}

	private Pair<Node, Double> alphaBetaSearch() {
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		return maxScore(0, alpha, beta);
	}

	private Pair<Node, Double> maxScore(int depth, double alpha, double beta) {
		if (depth >= depthLimit || isTerminal()) {
			return new Pair<>(this, getScore());
		}

		Pair<Node, Double> maxScore = new Pair<>(null, Double.NEGATIVE_INFINITY);
		for (Node child : this) {
			Pair<Node, Double> childScore = child.minScore(depth + 1, alpha, beta);
			if (childScore.value > maxScore.value) {
				maxScore.value = childScore.value;
				maxScore.key = child;
			}
			if (maxScore.value > alpha) {
				alpha = maxScore.value;
			}
			if (alpha >= beta) {
				break;
			}
		}
		return maxScore;
	}

	private Pair<Node, Double> minScore(int depth, double alpha, double beta) {
		if (depth >= depthLimit || isTerminal()) {
			return new Pair<>(this, getScore());
		}

		Pair<Node, Double> minScore = new Pair<>(null, Double.POSITIVE_INFINITY);
		for (Node child : this) {
			Pair<Node, Double> childScore = child.maxScore(depth + 1, alpha, beta);
			if (childScore.value < minScore.value) {
				minScore.value = childScore.value;
				minScore.key = child;
			}
			if (minScore.value < beta) {
				beta = minScore.value;
			}
			if (alpha >= beta) {
				break;
			}
		}
		return minScore;
	}

	@Override
	public Iterator<Node> iterator() {
		return new NodeIterator();
	}

	private class NodeIterator implements Iterator<Node> {

		private final Iterator<PentagoMove> movesIter = legalMoves.iterator();

		@Override
		public boolean hasNext() {
			return movesIter.hasNext();
		}

		@Override
		public Node next() {
			PentagoMove move = movesIter.next();
			PentagoBoardState state = (PentagoBoardState) boardState.clone();
			state.processMove(move);
			return new Node(move, state, studentPlayerID);
		}

	}

}
