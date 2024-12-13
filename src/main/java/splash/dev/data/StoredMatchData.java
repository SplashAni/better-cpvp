package splash.dev.data;

import net.minecraft.client.network.AbstractClientPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StoredMatchData {
    private static final List<MatchStatsMenu> matches;

    static {
        matches = new ArrayList<>();
    }

    public static List<MatchStatsMenu> getMatchDataInCategory(Gamemode gamemode) {
        List<MatchStatsMenu> info = new ArrayList<>();
        matches.forEach(matchStatsMenu -> {
            if (matchStatsMenu.gamemode == gamemode) info.add(matchStatsMenu);
        });
        return info;
    }

    public static String getKDString(AbstractClientPlayerEntity entity) {
        return getKD(entity)[0] + "-" + getKD(entity)[1];
    }

    public static int[] getKD(AbstractClientPlayerEntity entity) {
        int enemyKills = 0;
        int kills = 0;

        for (MatchStatsMenu match : getMatches()) {
            if (Objects.equals(match.getMatchOutline().getTarget(), entity)) {
                if (match.getMatchOutline().isWon()) kills++;
                else enemyKills++;
            }
        }
        return new int[]{kills, enemyKills};
    }

    public static MatchStatsMenu getMatchId(int id) {
        for (MatchStatsMenu matchStatsMenu : matches) {
            if (matchStatsMenu.matchOutline.getId() == id) return matchStatsMenu;
        }
        return null;
    }


    public static List<MatchStatsMenu> getMatches() {
        return matches;
    }

    public static void addInfo(MatchStatsMenu matchStatsMenu) {
        matches.add(matchStatsMenu);
    }

    public static void addMatch(MatchStatsMenu matchStatsMenu) {
        matches.add(matchStatsMenu);
    }
}
