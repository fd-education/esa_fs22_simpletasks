package com.example.simpletasks.data;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

public class AppDatabaseTest {
    @Test
    public void getAppDbAlwaysReturnsSameInstance() {
        final Context context = ApplicationProvider.getApplicationContext();
        final AppDatabase appDb = AppDatabase.getAppDb(context);

        final AppDatabase sameAppDb = AppDatabase.getAppDb(context);

        assertEquals(appDb, sameAppDb);
    }
}
