/*
 * Copyright (C) 2009 The Android Open Source Project
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

package com.yxst.epic.yixin.utils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;

import com.miicaa.home.R;

/**
 * A class for annotating a CharSequence with spans to convert textual emoticons
 * to graphical ones.
 */
public class SmileyParser {
	
	private static final String TAG = "SmileyParser";
	
    // Singleton stuff
    private static SmileyParser sInstance;
    public static SmileyParser getInstance() { return sInstance; }
    public static void init(Context context) {
        sInstance = new SmileyParser(context);
    }

    private final Context mContext;
    private final String[] mSmileyTexts;
    private final Pattern mPattern;
    private final HashMap<String, Integer> mSmileyToRes;

    private SmileyParser(Context context) {
    	TypedArray ta = context.getResources().obtainTypedArray(R.array.smiley_drawable);
    	int N = ta.length();
    	DEFAULT_SMILEY_RES_IDS = new int[N];
    	
    	for (int i = 0; i < N; i++) {
    		DEFAULT_SMILEY_RES_IDS[i] = ta.getResourceId(i, -1);
    	}
    	
    	ta.recycle();
    	
        mContext = context;
        mSmileyTexts = mContext.getResources().getStringArray(DEFAULT_SMILEY_TEXTS);
        mSmileyToRes = buildSmileyToRes();
        mPattern = buildPattern();
    }

//    public static class Smileys {
//    	
//    	public final int[] sIconIds;
//    	public final String[] sWhichs;
//    	
//    	public Smileys(Context context) {
//    		sIconIds = context.getResources().getIntArray(R.array.smiley_drawable);
//    		sWhichs = context.getResources().getStringArray(R.array.smiley_code);
//    	}
//
//    	/*public static int getSmileyResource(int which) {
//            return sIconIds[which];
//        }*/
//    }

    // NOTE: if you change anything about this array, you must make the corresponding change
    // to the string arrays: default_smiley_texts and default_smiley_names in res/values/arrays.xml
    public final int[] DEFAULT_SMILEY_RES_IDS;
 
    public static final int DEFAULT_SMILEY_TEXTS = R.array.smiley_code;
    public static final int DEFAULT_SMILEY_NAMES = R.array.smiley_code;
 
    /***
     * Builds the hashtable we use for mapping the string version
     * of a smiley (e.g. ":-)") to a resource ID for the icon version.
     */
    private HashMap<String, Integer> buildSmileyToRes() {
    	Log.d(TAG, "buildSmileyToRes() DEFAULT_SMILEY_RES_IDS:" + DEFAULT_SMILEY_RES_IDS);
    	Log.d(TAG, "buildSmileyToRes() mSmileyTexts:" + mSmileyTexts);
        if (DEFAULT_SMILEY_RES_IDS.length != mSmileyTexts.length) {
            // Throw an exception if someone updated DEFAULT_SMILEY_RES_IDS
            // and failed to update arrays.xml
            throw new IllegalStateException("Smiley resource ID/text mismatch");
        }
 
        HashMap<String, Integer> smileyToRes =
                            new HashMap<String, Integer>(mSmileyTexts.length);
        for (int i = 0; i < mSmileyTexts.length; i++) {
            smileyToRes.put(mSmileyTexts[i], DEFAULT_SMILEY_RES_IDS[i]);
        }
 
        return smileyToRes;
    }
    
    /**
     * Builds the regular expression we use to find smileys in {@link #addSmileySpans}.
     */
    public static Pattern buildPattern(String[] array) {
        // Set the StringBuilder capacity with the assumption that the average
        // smiley is 3 characters long.
        StringBuilder patternString = new StringBuilder(array.length * 3);

        // Build a regex that looks like (:-)|:-(|...), but escaping the smilies
        // properly so they will be interpreted literally by the regex matcher.
        patternString.append('(');
        for (String s : array) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        // Replace the extra '|' with a ')'
        patternString.replace(patternString.length() - 1, patternString.length(), ")");

        return Pattern.compile(patternString.toString());
    }


    /**
     * Adds ImageSpans to a CharSequence that replace textual emoticons such
     * as :-) with a graphical version.
     *
     * @param text A CharSequence possibly containing emoticons
     * @return A CharSequence annotated with ImageSpans covering any
     *         recognized emoticons.
     */
    public static CharSequence addSmileySpans(Context context, CharSequence text, Pattern pattern, HashMap<String, Integer> resMap) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);

        Matcher matcher = pattern.matcher(text);
       
        while (matcher.find()) {
            int resId = resMap.get(matcher.group());
            builder.setSpan(new ImageSpan(context, resId),
                            matcher.start(), matcher.end(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }
    
    public Pattern buildPattern() {
    	return buildPattern(mSmileyTexts);
    }
    
    public CharSequence addSmileySpans(CharSequence text) {
    	return addSmileySpans(mContext, text, mPattern, mSmileyToRes);
    }
    
    public HashMap<String, Integer> getSmileyToRes() {
    	return mSmileyToRes;
    }
}


