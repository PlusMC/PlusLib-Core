package org.plusmc.pluslib.bukkit.managed;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// a scoreboard that is used to display information to players
public abstract class PlusBoard implements Tickable {
    private final Scoreboard scoreboard;
    private final Objective objective;

    private final List<Score> scores;

    protected PlusBoard(String title) {
        scores = new ArrayList<>();
        if(Bukkit.getScoreboardManager() == null)
            throw new IllegalStateException("A world has not loaded yet so a scoreboard cannot be created.");
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective(title, "dummy", title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    // this will be called when the scoreboard is updated
    public abstract List<String> getEntries(long tick);

    //added a bunch of comments cause this shit is pretty confusing
    //im going to cry if this doesn't work
    @Override
    public void tick(long tick) {
        List<Score> newScores = new ArrayList<>();
        List<Score> scoresToRemove = new ArrayList<>();
        List<String> entries = getEntries(tick);
        resolveRepeats(entries);
        for(int i = 0; i < entries.size(); i++) {
            String entry = entries.get(i);

            //if the score isn't already in the scoreboard, add it
            if(i >= scores.size()) {
                Score score = objective.getScore(entry);
                newScores.add(score);
                continue;
            }

            //if the score is already in the scoreboard, re-add it
            Score score = scores.get(i);
            if(!score.getEntry().equals(entry)) {
                scoresToRemove.add(score);
                Score newScore = objective.getScore(entry);
                newScores.add(newScore);
            } else newScores.add(score);
        }
        scores.clear();
        scores.addAll(newScores);
        clearScores(scoresToRemove);
        addScores(scores);
    }

    private void clearScores(List<Score> scores) {
        for(Score score : scores) {
            scoreboard.resetScores(score.getEntry());
        }
    }

    private void addScores(List<Score> scores) {
        int i = scores.size() - 1;
        for(Score score : scores) {
            score.setScore(i);
            i--;
        }
    }

    private void resolveRepeats(List<String> entries) {
        Map<String, Integer> repeats = new HashMap<>();
        for(int i = 0; i < entries.size(); i++) {
            String entry = entries.get(i);
            if(repeats.containsKey(entry)) {
                entry = entry + "§r".repeat(repeats.get(entry));
            }
            repeats.put(entry, repeats.getOrDefault(entry, 0) + 1);
            entries.set(i, entry);
        }
    }

    public void setTitle(String title) {
        objective.setDisplayName(title);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

}
