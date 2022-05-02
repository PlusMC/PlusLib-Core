package org.plusmc.pluslib.mongo;

import org.mongodb.morphia.annotations.Embedded;

import java.util.HashMap;
import java.util.Map;

@Embedded
public class UserMH {
    private Map<String, Long> personalBests;
    private int totalKills;
    private int totalDeaths;
    private int totalWins;
    private int totalLosses;

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

    public void addTotalKills(int value) {
        totalKills += value;
    }

    public void addTotalDeaths(int value) {
        totalDeaths += value;
    }

    public void addTotalWins(int value) {
        totalWins += value;
    }

    public void addTotalLosses(int value) {
        totalLosses += value;
    }
}
