package student_player;

import java.util.ArrayList;
import java.util.List;

import pentago_swap.PentagoBoardState;

public class CompositePentagoHeuristic implements PentagoHeuristic {

	private final List<PentagoHeuristic> heuristics = new ArrayList<>();

	public void add(PentagoHeuristic heuristic) {
		heuristics.add(heuristic);
	}

	@Override
	public double calculateScore(PentagoBoardState boardState, int studentPlayerID) {
		if (heuristics.isEmpty()) {
			return 0.5;
		}

		boolean win = false;
		double sum = 0;
		for (PentagoHeuristic heuristic : heuristics) {
			double score = heuristic.calculateScore(boardState, studentPlayerID);
			if (score == 0) {
				return 0;
			}
			if (score == 1) {
				win = true;
			}
			sum += score;
		}
		return win ? 1 : (sum / heuristics.size());
	}

}
