package student_player;

import pentago_swap.PentagoBoardState;

public interface PentagoHeuristic {

	double calculateScore(PentagoBoardState boardState, int studentPlayerID);

}
