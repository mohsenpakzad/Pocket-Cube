package com.github.mohsenpakzad;

import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        PocketCube pocketCube = getInitialCube();
        findMovesToSolution(pocketCube).forEach(m -> System.out.print(m + " "));
        System.out.println();
    }

    private static PocketCube getInitialCube() {

        Color[][] cubeSides = new Color[6][4];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {

                switch (scanner.next().charAt(0)) {
                    case 'w':
                        cubeSides[i][j] = Color.WHITE;
                        break;
                    case 'b':
                        cubeSides[i][j] = Color.BLUE;
                        break;
                    case 'r':
                        cubeSides[i][j] = Color.RED;
                        break;
                    case 'g':
                        cubeSides[i][j] = Color.GREEN;
                        break;
                    case 'y':
                        cubeSides[i][j] = Color.YELLOW;
                        break;
                    case 'o':
                        cubeSides[i][j] = Color.ORANGE;
                        break;
                }
            }
        }
        return new PocketCube(cubeSides);
    }

    private static List<Integer> findMovesToSolution(PocketCube pocketCube) {

        if (pocketCube.isSolved()) return Collections.emptyList();

        Queue<SearchStep> queue = new LinkedList<>();

        // add initial moves to queue
        for (int i = 1; i <= 12; i++) {
            PocketCube movedCube = pocketCube.copy();
            movedCube.move(i);
            if (movedCube.isSolved()) return Collections.singletonList(i);
            queue.add(new SearchStep(null, i, movedCube));
        }

        while (queue.size() != 0) {
            SearchStep searchStep = queue.poll();
            for (int i = 1; i <= 12; i++) {
                PocketCube movedCube = searchStep.getPocketCube().copy();
                movedCube.move(i);
                if (movedCube.isSolved()) {
                    List<Integer> movesToThisRes = getMovesToSearchStep(searchStep);
                    movesToThisRes.add(i);
                    return movesToThisRes;
                }
                queue.add(new SearchStep(searchStep, i, movedCube));
            }
        }

        throw new RuntimeException("Can not find solution");
    }

    private static List<Integer> getMovesToSearchStep(SearchStep searchStep) {

        List<Integer> moves = new ArrayList<>();

        while (searchStep != null) {
            moves.add(searchStep.getMoveNumber());
            searchStep = searchStep.getParent();
        }

        Collections.reverse(moves);
        return moves;
    }
}

enum Color {
    WHITE,
    BLUE,
    RED,
    GREEN,
    YELLOW,
    ORANGE
}

class PocketCube {

    private static final int LEFT = 0;
    private static final int TOP = 1;
    private static final int FRONT = 2;
    private static final int BOTTOM = 3;
    private static final int RIGHT = 4;
    private static final int BACK = 5;

    private Color[][] cubeSides;

    public PocketCube(Color[][] cubeSides) {
        this.cubeSides = cubeSides;
    }

    public static Color[][] deepCopy(Color[][] original) {

        if (original == null) {
            throw new RuntimeException("Can not copy null array");
        }

        final Color[][] result = new Color[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    public void move(int moveNumber) {

        switch (moveNumber) {

            case 1:
            case 2:
//              ('f3', 'u3'), ('f1', 'u1'),
//              ('b2', 'd3'), ('b4', 'd1'),
//              ('u3', 'b2'), ('u1', 'b4'),
//              ('d3', 'f3'), ('d1', 'f1'),
//              ('l2', 'l1'), ('l1', 'l3'), ('l4', 'l2'), ('l3', 'l4')
                AssigningValues(new HashMap<Index, Index>() {{
                    put(new Index(FRONT, 3), new Index(TOP, 3));
                    put(new Index(FRONT, 1), new Index(TOP, 1));

                    put(new Index(BACK, 2), new Index(BOTTOM, 3));
                    put(new Index(BACK, 4), new Index(BOTTOM, 1));

                    put(new Index(TOP, 3), new Index(BACK, 2));
                    put(new Index(TOP, 1), new Index(BACK, 4));

                    put(new Index(BOTTOM, 3), new Index(FRONT, 3));
                    put(new Index(BOTTOM, 1), new Index(FRONT, 1));

                    put(new Index(LEFT, 2), new Index(LEFT, 1));
                    put(new Index(LEFT, 1), new Index(LEFT, 3));
                    put(new Index(LEFT, 4), new Index(LEFT, 2));
                    put(new Index(LEFT, 3), new Index(LEFT, 4));
                }}, moveNumber % 2 == 1);
                break;

            case 3:
            case 4:
//              ('f4', 'u4'), ('f2', 'u2'),
//              ('u4', 'b1'), ('u2', 'b3'),
//              ('b1', 'd4'), ('b3', 'd2'),
//              ('d4', 'f4'), ('d2', 'f2'),
//              ('r1', 'r2'), ('r2', 'r4'), ('r4', 'r3'), ('r3', 'r1')
                AssigningValues(new HashMap<Index, Index>() {{
                    put(new Index(FRONT, 4), new Index(TOP, 4));
                    put(new Index(FRONT, 2), new Index(TOP, 2));

                    put(new Index(TOP, 4), new Index(BACK, 1));
                    put(new Index(TOP, 2), new Index(BACK, 3));

                    put(new Index(BACK, 1), new Index(BOTTOM, 4));
                    put(new Index(BACK, 3), new Index(BOTTOM, 2));

                    put(new Index(BOTTOM, 4), new Index(FRONT, 4));
                    put(new Index(BOTTOM, 2), new Index(FRONT, 2));

                    put(new Index(RIGHT, 1), new Index(RIGHT, 2));
                    put(new Index(RIGHT, 2), new Index(RIGHT, 4));
                    put(new Index(RIGHT, 4), new Index(RIGHT, 3));
                    put(new Index(RIGHT, 3), new Index(RIGHT, 1));
                }}, moveNumber % 2 == 1);
                break;

            case 5:
            case 6:
//              ('u4', 'l2'), ('u3', 'l4'),
//              ('r1', 'u3'), ('r3', 'u4'),
//              ('d2', 'r1'), ('d1', 'r3'),
//              ('l2', 'd1'), ('l4', 'd2'),
//              ('f1', 'f3'), ('f2', 'f1'), ('f4', 'f2'), ('f3', 'f4')
                AssigningValues(new HashMap<Index, Index>() {{
                    put(new Index(TOP, 4), new Index(LEFT, 2));
                    put(new Index(TOP, 3), new Index(LEFT, 4));

                    put(new Index(RIGHT, 1), new Index(TOP, 3));
                    put(new Index(RIGHT, 3), new Index(TOP, 4));

                    put(new Index(BOTTOM, 2), new Index(RIGHT, 1));
                    put(new Index(BOTTOM, 1), new Index(RIGHT, 3));

                    put(new Index(LEFT, 2), new Index(BOTTOM, 1));
                    put(new Index(LEFT, 4), new Index(BOTTOM, 2));

                    put(new Index(FRONT, 1), new Index(FRONT, 3));
                    put(new Index(FRONT, 2), new Index(FRONT, 1));
                    put(new Index(FRONT, 4), new Index(FRONT, 2));
                    put(new Index(FRONT, 3), new Index(FRONT, 4));
                }}, moveNumber % 2 == 1);
                break;

            case 7:
            case 8:
//              ('u2', 'l1'), ('u1', 'l3'),
//              ('r2', 'u1'), ('r4', 'u2'),
//              ('d4', 'r2'), ('d3', 'r4'),
//              ('l1', 'd3'), ('l3', 'd4'),
//              ('b1', 'b2'), ('b2', 'b4'), ('b4', 'b3'), ('b3', 'b1')
                AssigningValues(new HashMap<Index, Index>() {{
                    put(new Index(TOP, 2), new Index(LEFT, 1));
                    put(new Index(TOP, 1), new Index(LEFT, 3));

                    put(new Index(RIGHT, 2), new Index(TOP, 1));
                    put(new Index(RIGHT, 4), new Index(TOP, 2));

                    put(new Index(BOTTOM, 4), new Index(RIGHT, 2));
                    put(new Index(BOTTOM, 3), new Index(RIGHT, 4));

                    put(new Index(LEFT, 1), new Index(BOTTOM, 3));
                    put(new Index(LEFT, 3), new Index(BOTTOM, 4));

                    put(new Index(BACK, 1), new Index(BACK, 2));
                    put(new Index(BACK, 2), new Index(BACK, 4));
                    put(new Index(BACK, 4), new Index(BACK, 3));
                    put(new Index(BACK, 3), new Index(BACK, 1));
                }}, moveNumber % 2 == 1);
                break;

            case 9:
            case 10:
//              ('f1', 'r1'), ('f2', 'r2'),
//              ('r1', 'b1'), ('r2', 'b2'),
//              ('b1', 'l1'), ('b2', 'l2'),
//              ('l1', 'f1'), ('l2', 'f2'),
//              ('u1', 'u3'), ('u2', 'u1'), ('u4', 'u2'), ('u3', 'u4')
                AssigningValues(new HashMap<Index, Index>() {{
                    put(new Index(FRONT, 1), new Index(RIGHT, 1));
                    put(new Index(FRONT, 2), new Index(RIGHT, 2));

                    put(new Index(RIGHT, 1), new Index(BACK, 1));
                    put(new Index(RIGHT, 2), new Index(BACK, 2));

                    put(new Index(BACK, 1), new Index(LEFT, 1));
                    put(new Index(BACK, 2), new Index(LEFT, 2));

                    put(new Index(LEFT, 1), new Index(FRONT, 1));
                    put(new Index(LEFT, 2), new Index(FRONT, 2));

                    put(new Index(TOP, 1), new Index(TOP, 3));
                    put(new Index(TOP, 2), new Index(TOP, 1));
                    put(new Index(TOP, 4), new Index(TOP, 2));
                    put(new Index(TOP, 3), new Index(TOP, 4));
                }}, moveNumber % 2 == 1);
                break;

            case 11:
            case 12:
//             ('f3', 'r3'), ('f4', 'r4'),
//             ('r3', 'b3'), ('r4', 'b4'),
//             ('b3', 'l3'), ('b4', 'l4'),
//             ('l3', 'f3'), ('l4', 'f4'),
//             ('d1', 'd2'), ('d2', 'd4'), ('d4', 'd3'), ('d3', 'd1')
                AssigningValues(new HashMap<Index, Index>() {{
                    put(new Index(FRONT, 3), new Index(RIGHT, 3));
                    put(new Index(FRONT, 4), new Index(RIGHT, 4));

                    put(new Index(RIGHT, 3), new Index(BACK, 3));
                    put(new Index(RIGHT, 4), new Index(BACK, 4));

                    put(new Index(BACK, 3), new Index(LEFT, 3));
                    put(new Index(BACK, 4), new Index(LEFT, 4));

                    put(new Index(LEFT, 3), new Index(FRONT, 3));
                    put(new Index(LEFT, 4), new Index(FRONT, 4));

                    put(new Index(BOTTOM, 1), new Index(BOTTOM, 2));
                    put(new Index(BOTTOM, 2), new Index(BOTTOM, 4));
                    put(new Index(BOTTOM, 4), new Index(BOTTOM, 3));
                    put(new Index(BOTTOM, 3), new Index(BOTTOM, 1));
                }}, moveNumber % 2 == 1);
                break;
        }
    }

    /**
     * Assign right index value to left index value and if @param reverse is true do vise versa
     *
     * @param indexesToAssign Indexes to assign
     * @param reverse         Change order of assigning
     */
    private void AssigningValues(Map<Index, Index> indexesToAssign, boolean reverse) {
        Color[][] temp = deepCopy(cubeSides);

        for (Map.Entry<Index, Index> indexToAssign : indexesToAssign.entrySet()) {

            int destSideIndex, destNumberIndex, srcSideIndex, srcNumberIndex;

            destSideIndex = indexToAssign.getKey().getSide();
            destNumberIndex = indexToAssign.getKey().getNumber() - 1;

            srcSideIndex = indexToAssign.getValue().getSide();
            srcNumberIndex = indexToAssign.getValue().getNumber() - 1;

            if (!reverse) temp[destSideIndex][destNumberIndex] = cubeSides[srcSideIndex][srcNumberIndex];
            else temp[srcSideIndex][srcNumberIndex] = cubeSides[destSideIndex][destNumberIndex];
        }
        cubeSides = temp;
    }

    public boolean isSolved() {

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && cubeSides[i][j] != Color.WHITE) return false;
                if (i == 1 && cubeSides[i][j] != Color.BLUE) return false;
                if (i == 2 && cubeSides[i][j] != Color.RED) return false;
                if (i == 3 && cubeSides[i][j] != Color.GREEN) return false;
                if (i == 4 && cubeSides[i][j] != Color.YELLOW) return false;
                if (i == 5 && cubeSides[i][j] != Color.ORANGE) return false;
            }
        }
        return true;
    }

    public PocketCube copy() {
        return new PocketCube(deepCopy(cubeSides));
    }

    @Override
    public String toString() {

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < 6; i++) {

            if (i == 0)
                res.append("Left: \n");
            else if (i == 1)
                res.append("Top: \n");
            else if (i == 2)
                res.append("Front: \n");
            else if (i == 3)
                res.append("Bottom: \n");
            else if (i == 4)
                res.append("Right: \n");
            else
                res.append("Back: \n");


            for (int j = 0; j < 4; j++) {
                if (j % 2 == 0) res.append("\t").append(cubeSides[i][j]).append(" ");
                else res.append(cubeSides[i][j]).append("\n");
            }
        }
        return res.toString();
    }

    private static class Index {
        private final int side;
        private final int number;

        public Index(int side, int number) {
            this.side = side;
            this.number = number;
        }

        public int getSide() {
            return side;
        }

        public int getNumber() {
            return number;
        }
    }
}

class SearchStep {

    private SearchStep parent;
    private int moveNumber;
    private PocketCube pocketCube;

    public SearchStep(SearchStep parent, int moveNumber, PocketCube pocketCube) {
        this.parent = parent;
        this.moveNumber = moveNumber;
        this.pocketCube = pocketCube;
    }

    public SearchStep getParent() {
        return parent;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public PocketCube getPocketCube() {
        return pocketCube;
    }
}