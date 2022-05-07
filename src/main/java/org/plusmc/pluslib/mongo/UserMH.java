package org.plusmc.pluslib.mongo;

import org.mongodb.morphia.annotations.Embedded;

import java.util.HashMap;
import java.util.Map;

@Embedded
public class UserMH {
    private Map<String, Long> personalBests;


    private int winsAsRunner;
    private int lossAsRunner;
    private int killsAsRunner;
    private int deathsAsRunner;


    private int winsAsHunter;
    private int lossAsHunter;
    private int killsAsHunter;
    private int deathsAsHunter;

    public int getKillsAsRunner() {
        return killsAsRunner;
    }

    public int getDeathsAsRunner() {
        return deathsAsRunner;
    }

    public int getKillsAsHunter() {
        return killsAsHunter;
    }

    public int getDeathsAsHunter() {
        return deathsAsHunter;
    }

    public void addPersonalBest(String key, long value) {
        if (personalBests == null)
            personalBests = new HashMap<>();

        personalBests.computeIfAbsent(key, k -> personalBests.put(k, value));

        personalBests.put(key, Math.max(value, personalBests.get(key)));
    }

    public void removePersonalBest(String key) {
        if (personalBests != null) {
            personalBests.remove(key);
        }
    }

    public void addKillRunner() {
        killsAsRunner++;
    }

    public void addDeathRunner() {
        deathsAsRunner++;
    }

    public void addKillHunter() {
        killsAsHunter++;
    }

    public void addDeathHunter() {
        deathsAsHunter++;
    }

    public void addWinRunner() {
        winsAsRunner++;
    }

    public void addWinHunter() {
        winsAsHunter++;
    }

    public void addLossRunner() {
        lossAsRunner++;
    }

    public void addLossHunter() {
        lossAsHunter++;
    }

    public int getTotalKills() {
        return killsAsHunter + killsAsRunner;
    }

    public int getTotalDeaths() {
        return deathsAsHunter + deathsAsRunner;
    }

    public int getTotalWins() {
        return winsAsRunner + winsAsHunter;
    }

    public int getWinsAsRunner() {
        return winsAsRunner;
    }

    public int getWinsAsHunter() {
        return winsAsHunter;
    }

    public int getTotalLosses() {
        return lossAsRunner + lossAsHunter;
    }

    public int getLossAsRunner() {
        return lossAsRunner;
    }

    public int getLossAsHunter() {
        return lossAsHunter;
    }

    public int getTotalGames() {
        return winsAsHunter + winsAsRunner + lossAsHunter + lossAsRunner;
    }

    public Map<String, Long> getPersonalBests() {
        return Map.copyOf(personalBests);
    }
}
