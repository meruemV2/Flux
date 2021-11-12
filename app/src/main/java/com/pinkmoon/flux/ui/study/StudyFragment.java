package com.pinkmoon.flux.ui.study;

import static android.os.VibrationEffect.DEFAULT_AMPLITUDE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import com.pinkmoon.flux.R;


public class StudyFragment extends Fragment {
    public StudyFragment() {
    }
    boolean isTimeRunning = false, isBreak = false;
    private static final long STUDY_TIME_IN_MILLIS = 1500000, REST_TIME_IN_MILLIS = 300000;

    static long startTime, breakTime, millisLeft;
    ImageButton playPauseButton, stopButton;
    CountDownTimer timer;
    ProgressBar timerProgressBar;
    TextView timerText;
    Vibrator vibrator;
    Ringtone ringtone;
    Button studyOrBreak;

    @Override
    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        if (outState != null) {
            millisLeft = outState.getLong("millisLeft");
            isTimeRunning = outState.getBoolean("isTimeRunning");
            isBreak = outState.getBoolean("isBreak");
        }


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_study, container, false);

        studyOrBreak = v.findViewById(R.id.switch1);

        playPauseButton = v.findViewById(R.id.play_pause_button);
        stopButton = v.findViewById(R.id.stop_button);
        timerProgressBar = v.findViewById(R.id.clock_progress);
        timerText = v.findViewById(R.id.timer_numbers);

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        ringtone = RingtoneManager.getRingtone(getActivity().getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        startTime = STUDY_TIME_IN_MILLIS;
        breakTime = REST_TIME_IN_MILLIS;

        millisLeft = (isBreak) ? breakTime : startTime;
        defineProgress();
        updateTimerProgress();

        if (millisLeft != startTime)
            updateResumePauseButton();

        if (isTimeRunning)
            startTimer();
        onStart();
        return v;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();

        defineProgress();
        updateTimerProgress();

        playPauseButton.setOnClickListener(v -> {
            if (isTimeRunning)
                pauseTimer();
            else
                startTimer();
        });

        stopButton.setOnClickListener(v -> resetTimer());

        studyOrBreak.setOnClickListener(v -> {
            if (isBreak) {
                isBreak = false;
                studyOrBreak.setText("Rest");

            } else {
                isBreak = true;
                studyOrBreak.setText("Study");
            }
            timerProgressBar.setMax((int)TimeUnit.MILLISECONDS.toSeconds((isBreak) ? breakTime : startTime));
            resetTimer();
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("isTimeRunning", isTimeRunning);
        outState.putLong("millisLeft", millisLeft);
        outState.putBoolean("isBreak", isBreak);

        if (isTimeRunning)
            destroyTimer();
    }

    private void startTimer() {
        isTimeRunning = true;

        timer = new CountDownTimer(millisLeft, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
                millisLeft = millisUntilFinished;

                updateTimerProgress();
            }

            @Override
            public void onFinish() {
                alertTimerFinish();
                changeTimerType();
                defineProgress();
                startTimer();
            }
        }.start();

        updateResumePauseButton();
    }

    public void defineProgress() {
        timerProgressBar.setMax((int)TimeUnit.MILLISECONDS.toSeconds((isBreak) ? breakTime : startTime));
        timerProgressBar.setProgress(timerProgressBar.getMax());
    }

    private void alertTimerFinish() {
        VibrationEffect vibe = VibrationEffect.createOneShot(200, DEFAULT_AMPLITUDE);
        vibrator.vibrate(vibe);
        ringtone.play();
    }

    private void changeTimerType() {
        millisLeft = (!isBreak) ? breakTime : startTime;
        isBreak = !isBreak;
    }

    private void destroyTimer() {
        timer.cancel();

        isTimeRunning = false;
    }

    private void pauseTimer() {
        destroyTimer();
        updateResumePauseButton();
    }

    private void resetTimer() {
        if (isTimeRunning)
            destroyTimer();

        millisLeft = (!isBreak) ? startTime : breakTime;

        updateTimerProgress();
        updateResumePauseButton();
    }

    private void updateTimerProgress() {
        int hours = (int) (millisLeft / 1000) / 3600;
        int mins = (int) ((millisLeft / 1000) % 3600) / 60;
        int secs = (int) (millisLeft / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, mins, secs);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", mins, secs);
        }

        timerText.setText(timeLeftFormatted);

        timerProgressBar.setProgress((int) TimeUnit.MILLISECONDS.toSeconds(millisLeft));
    }

    private void updateResumePauseButton() {
        playPauseButton.setImageResource(isTimeRunning ? R.drawable.pause_button: R.drawable.play_button);
    }
}
