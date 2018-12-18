package com.example.android.courtcounterapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int scoreTeamA = 0;
    int scoreTeamB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayForTeamA(scoreTeamA);
        displayForTeamB(scoreTeamB);
    }

    public void resetScore(View view) {
        scoreTeamA = 0;
        scoreTeamB = 0;
        displayForTeamA(scoreTeamA);
        displayForTeamB(scoreTeamB);
    }

    public void plusTeamAThreePoints(View view) {
        scoreTeamA += 3;
        displayForTeamA(scoreTeamA);
    }

    public void plusTeamATwoPoints(View view) {
        scoreTeamA += 2;
        displayForTeamA(scoreTeamA);
    }

    public void plusTeamAOnePoint(View view) {
        scoreTeamA += 1;
        displayForTeamA(scoreTeamA);
    }

    /**
     * Displays the given score for Team A.
     */
    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }

    public void plusTeamBThreePoints(View view) {
        scoreTeamB += 3;
        displayForTeamB(scoreTeamB);
    }

    public void plusTeamBTwoPoints(View view) {
        scoreTeamB += 2;
        displayForTeamB(scoreTeamB);
    }

    public void plusTeamBOnePoint(View view) {
        scoreTeamB += 1;
        displayForTeamB(scoreTeamB);
    }

    /**
     * Displays the given score for Team B.
     */
    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }
}