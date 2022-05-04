package org.plusmc.pluslib.mongo;

import org.mongodb.morphia.annotations.Embedded;

import java.util.HashMap;
import java.util.Map;

@Embedded
public class UserMH {
    private Map<String, Long> personalBests;
    private int totalKills, totalDeaths;
    private int totalWins, winsAsRunner, winsAsHunter;
    private int totalLosses, lossAsRunner, lossAsHunter;
    private int totalGames;

    public UserMH() {
    }

    public void addPersonalBest(String key, long value) {
        if (personalBests == null)
            personalBests = new HashMap<>();

        if(!personalBests.containsKey(key)) {
            personalBests.put(key, value);
            return;
        }

        personalBests.put(key, Math.max(value, personalBests.get(key)));
    }

    public void removePersonalBest(String key) {
        if (personalBests != null) {
            personalBests.remove(key);
        }
    }

    public void addKill() {
        totalKills++;
    }

    public void addDeath() {
        totalDeaths++;
    }

    public void addWinRunner() {
        winsAsRunner++;
        totalWins++;
    }

    public void addWinHunter() {
        winsAsHunter++;
        totalWins++;
    }

    public void addLossRunner() {
        lossAsRunner++;
        totalLosses++;
    }

    public void addLossHunter() {
        lossAsHunter++;
        totalLosses++;
    }

    public void addGame() {
        totalGames++;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getWinsAsRunner() {
        return winsAsRunner;
    }

    public int getWinsAsHunter() {
        return winsAsHunter;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public int getLossAsRunner() {
        return lossAsRunner;
    }

    public int getLossAsHunter() {
        return lossAsHunter;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public Map<String, Long> getPersonalBests() {
        return Map.copyOf(personalBests);
    }
}
