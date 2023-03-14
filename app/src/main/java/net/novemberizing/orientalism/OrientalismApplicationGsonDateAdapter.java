package net.novemberizing.orientalism;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

public class OrientalismApplicationGsonDateAdapter extends TypeAdapter<Date> {
    private static final String Tag = "OrientalismApplicationGsonDateAdapter";

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        out.value("");
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if(in.hasNext()) {
            String value = in.nextString();
            Log.e(Tag, value);
        }

        return null;
    }
}
