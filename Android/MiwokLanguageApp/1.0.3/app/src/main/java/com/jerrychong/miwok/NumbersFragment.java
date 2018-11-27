package com.jerrychong.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {

                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Pause playback and reset player to the start of the file. That way, we play the word from the beginning
                        // when we resume playback.
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback
                        mMediaPlayer.start();
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // The AUDIOFOCUS_LOSS case means we've lost audio focus and stop playback and cleanup resources
                        releaseMediaPlayer();
                    }
                }
            };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    public NumbersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.jerrychong.miwok.R.layout.word_list, container, false);

        /** TODO: Insert all the code from the NumberActivity’s onCreate() method after the setContentView method call */
        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create an arrayList of words
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("one", "lutti", com.jerrychong.miwok.R.drawable.number_one, com.jerrychong.miwok.R.raw.number_one));
        words.add(new Word("two","otiiko", com.jerrychong.miwok.R.drawable.number_two, com.jerrychong.miwok.R.raw.number_two));
        words.add(new Word("three","tolookosu", com.jerrychong.miwok.R.drawable.number_three, com.jerrychong.miwok.R.raw.number_three));
        words.add(new Word("four","oyyisa", com.jerrychong.miwok.R.drawable.number_four, com.jerrychong.miwok.R.raw.number_four));
        words.add(new Word("five","massokka", com.jerrychong.miwok.R.drawable.number_five, com.jerrychong.miwok.R.raw.number_five));
        words.add(new Word("six","temmokka", com.jerrychong.miwok.R.drawable.number_six, com.jerrychong.miwok.R.raw.number_six));
        words.add(new Word("seven","kenekaku", com.jerrychong.miwok.R.drawable.number_seven, com.jerrychong.miwok.R.raw.number_seven));
        words.add(new Word("eight","kawinta", com.jerrychong.miwok.R.drawable.number_eight, com.jerrychong.miwok.R.raw.number_eight));
        words.add(new Word("nine","wo'e", com.jerrychong.miwok.R.drawable.number_nine, com.jerrychong.miwok.R.raw.number_nine));
        words.add(new Word("ten","na'aacha", com.jerrychong.miwok.R.drawable.number_ten, com.jerrychong.miwok.R.raw.number_ten));

        WordAdapter adapter = new WordAdapter(getActivity(), words, com.jerrychong.miwok.R.color.category_numbers);
        ListView listView = (ListView) rootView.findViewById(com.jerrychong.miwok.R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);
                Log.d("NumbersActivity", "Current word: " + word);

                // Release the media player if it currently exists because we are about to play a different sound file
                releaseMediaPlayer();

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music sream
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now

                    // Create and setup the {@link MediaPlayer} for the audio resource associated with the current word
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getmAudioResourceId());

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the media player once the sounds has
                    // finished playing
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Abandon audio focus when playback complete
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
