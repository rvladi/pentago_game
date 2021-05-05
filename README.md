# Artificial Intelligence Agent for the Pentago Game

Pentago belongs to the Moku family of games. Other popular games in this category include Tic-Tac-Toe and Connect-4, although Pentago is significantly more complex. In this variation of the game, the board is divided into quadrants which can be swapped around during the game.

## Setup

Pentago is a two-player game played on a 6×6 board which consists of four 3×3 quadrants. Initially, the board is empty. One player plays as white and the other one plays as black.

## Objective

In order to win, each player tries to achieve 5 pieces in a row before their opponent does. A winning row can be achieved horizontally, vertically, or diagonally. If all of the spaces on the board are occupied without a winner, a draw is declared. If swapping two quadrants results in a winning row for both players, the game also ends in a draw.

## Playing

Moves consist of two phases: placing and swapping. On a given player's turn, a piece is first placed in an empty slot on the board. The player then selects two quadrants which switch position. A complete move therefore consists of placing a piece and selecting two quadrants to swap.

## Strategy

Allowing quadrants to be swapped introduces significant complexity and AI agents will need to contend with this high branching complexity. Since quadrants can be swapped, blocking an opponent's row is not as easy as simply placing an adjacent piece. A good AI agent might consider balancing seeking to win with preventing their opponent from achieving the same.
