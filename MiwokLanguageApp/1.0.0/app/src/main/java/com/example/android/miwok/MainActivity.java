/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        // find the view that show numbers category
        TextView numbers = (TextView)findViewById(R.id.numbers);
        TextView colors = (TextView)findViewById(R.id.colors);
        TextView family  = (TextView)findViewById(R.id.family);
        TextView phrases = (TextView)findViewById(R.id.phrases);
        // Set a clickListener on that view

        numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creat new intent for open (( NumbersActivity
                Intent numbersIntent = new Intent(MainActivity.this,NumbersActivity.class);
                // Start to new Activity
                startActivity(numbersIntent);
            }
        });

        colors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creat new intent for open (( ColorsActivity
                Intent colorsIntent = new Intent(MainActivity.this,ColorsActivity.class);
                // Start to new Activity
                startActivity(colorsIntent);
            }
        });

        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creat new intent for open (( FamilyActivity
                Intent familyIntent = new Intent(MainActivity.this,FamilyActivity.class);
                // Start to new Activity
                startActivity(familyIntent);
            }
        });

        phrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creat new intent for open (( PhrasesActivity
                Intent phrasesIntent = new Intent(MainActivity.this,PhrasesActivity.class);
                // Start to new Activity
                startActivity(phrasesIntent);
            }
        });
    }
}
