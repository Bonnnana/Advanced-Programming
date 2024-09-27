// 29.

package SecondPartialExam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

class TeamPoints implements Comparable<TeamPoints>{
    int playedGames;
    int wins;
    int losts;
    int drawnGames;
    int scoredGoals;
    int lostGoals;

    public TeamPoints(int playedGames, int wins, int losts, int drawnGames, int scoredGoals, int lostGoals) {
        this.playedGames = playedGames;
        this.wins = wins;
        this.losts = losts;
        this.drawnGames = drawnGames;
        this.scoredGoals = scoredGoals;
        this.lostGoals = lostGoals;
    }

    public void updatePoints(int playedGames, int wins, int losts, int drawnGames, int scoredGoals, int lostGoals) {
        this.playedGames += playedGames;
        this.wins += wins;
        this.losts += losts;
        this.drawnGames += drawnGames;
        this.scoredGoals += scoredGoals;
        this.lostGoals += lostGoals;
    }

    public int getPoints() {
        return (this.wins * 3) + this.drawnGames;
    }
    public int getGoalDiff(){
        return scoredGoals-lostGoals;
    }


    @Override
    public int compareTo(TeamPoints o) {
        int res = Integer.compare(o.getPoints(),this.getPoints());
        if(res==0)
            res = Integer.compare(o.getGoalDiff(), this.getGoalDiff());

        return res;
    }
}

class FootballTable {
    Map<String, TeamPoints> tableOfPoints;

    public FootballTable() {
        this.tableOfPoints = new HashMap<>();
    }


    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        int homeWins = 0, awayWins = 0, drawnMatch = 0, homeLosts = 0, awayLosts = 0;
        int played = 1;
        if (homeGoals > awayGoals) {
            homeWins = 1;
            awayLosts = 1;
        } else if (homeGoals < awayGoals) {
            homeLosts = 1;
            awayWins = 1;
        } else {
            drawnMatch = 1;
        }
        tableOfPoints.putIfAbsent(homeTeam, new TeamPoints(0, 0, 0, 0, 0, 0));
        TeamPoints hTeam = tableOfPoints.get(homeTeam);
        hTeam.updatePoints(played, homeWins, homeLosts, drawnMatch, homeGoals, awayGoals);

        tableOfPoints.putIfAbsent(awayTeam, new TeamPoints(0, 0, 0, 0, 0, 0));
        TeamPoints aTeam = tableOfPoints.get(awayTeam);
        aTeam.updatePoints(played, awayWins, awayLosts, drawnMatch, awayGoals, homeGoals);
    }

    public void printTable() {
        Map<String, TeamPoints> sorted = tableOfPoints.entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<String, TeamPoints> a) -> a.getValue()).thenComparing(Map.Entry::getKey))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        int i=1;
        for(Map.Entry<String, TeamPoints> entry:sorted.entrySet()){
            String team = entry.getKey();
            TeamPoints points = entry.getValue();
            System.out.printf("%2d. %-15s%5d%5d%5d%5d%5d\n",
                    i++,
                    team,
                    points.playedGames,
                    points.wins,
                    points.drawnGames,
                    points.losts,
                    points.getPoints()
                    );
        }



    }
}
