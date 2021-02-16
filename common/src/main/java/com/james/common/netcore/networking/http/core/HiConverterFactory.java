package com.james.common.netcore.networking.http.core;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.james.common.netcore.networking.http.exception.DataErrorException;
import com.james.common.netcore.networking.http.annotations.WithHiResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author elson, email:liuqi2elson@163.com
 * @date 2020/7/10
 * @Desc
 */
public class HiConverterFactory extends Converter.Factory {

    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static HiConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static HiConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new HiConverterFactory(gson);
    }

    private final Gson gson;

    private HiConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof WithHiResponse && ((WithHiResponse) annotation).value()) {
                TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
                return new HiResponseBodyConverter<>(gson, adapter);
            }
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new HiRequestBodyConverter<>(gson, adapter);
    }


    // ---------------------------- Converter -------------------------------

    final static class HiResponseBodyConverter<T> implements Converter<ResponseBody, HiResponse<T>> {
        private final Gson gson;
        private final TypeAdapter<T> adapter;

        HiResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public HiResponse<T> convert(ResponseBody value) throws IOException {
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            try {
                T result = adapter.read(jsonReader);
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw new JsonIOException("JSON document was not fully consumed.");
                }
                return new HiResponse<T>(0, "", result);
            } catch (IOException e) {
                throw new DataErrorException(null);
            } finally {
                value.close();
            }
        }
    }

    final static class HiRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");
        private static final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        HiRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }
}
