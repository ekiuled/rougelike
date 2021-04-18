package roguelike.view;

import roguelike.model.Level;
import roguelike.model.LevelMap;
import roguelike.model.Player;
import roguelike.model.util.Cell;
import roguelike.util.Position;
import roguelike.util.ViewMessage;

import java.util.*;

public class View {
    private static final Queue<LevelView> queue = new ArrayDeque<>();

    public static boolean isEmpty() {
        return queue.isEmpty();
    }

    public static LevelView getLevelView() {
        return queue.remove();
    }

    public static void addView(Level newLevel) {
        LevelMap currentMap = newLevel.getMap();
        Cell[][] currentCells = currentMap.getCells();
        Collection<Player> players = newLevel.getPlayers().values();
        int currentWight = currentMap.getWidth();
        int currentHeight = currentMap.getHeight();
        Character[][] currentView = new Character[currentWight][currentHeight];
        for (int i = 0; i < currentWight; i++) {
            for (int j = 0; j < currentHeight; j++) {
                switch (currentCells[i][j].getKind()) {
                    case WALL -> currentView[i][j] = '#';
                    case START -> currentView[i][j] = '^';
                    case END -> currentView[i][j] = '_';
                    case GROUND -> currentView[i][j] = ' ';
                }
            }
        }
        Map<UUID, Position> map = new HashMap<>();
        for (var player : players) {
            map.put(player.getId(), player.getPosition());
            currentView[player.getPosition().getX()][player.getPosition().getY()] = '@';
        }

        queue.add(new LevelView(newLevel.getNumber(), currentView, map));
    }

    public static class LevelView {
        private final int number;
        private final Character[][] view;
        private final Map<UUID, Position> players;

        public LevelView(int num, Character[][] view, Map<UUID, Position> pls) {
            number = num;
            this.view = view;
            players = pls;
        }

        public int getNumber() {
            return number;
        }

        public Character[][] getView() {
            return view;
        }

        public ViewMessage buildViewMessage() {
            return new ViewMessage(number, view, players);
        }

    }

}
