package com.techiguru.interviewai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {

    TextView tvScore;

    TextView tvCommunicationScore;
    TextView tvCommunicationFeedback;

    TextView tvTechnicalScore;
    TextView tvTechnicalFeedback;

    TextView tvConfidenceScore;
    TextView tvConfidenceFeedback;

    TextView tvStrength;
    TextView tvGrowth;
    TextView tvRecommendation;

    Button btnHome;

    String API_KEY = "AQ.Ab8RN6IMNQNFo-AfCHmtDFcC5FgigAd58GZaS13-j9xlxZxAVw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvScore = findViewById(R.id.tvScore);

        tvCommunicationScore = findViewById(R.id.tvCommunicationScore);
        tvCommunicationFeedback = findViewById(R.id.tvCommunicationFeedback);

        tvTechnicalScore = findViewById(R.id.tvTechnicalScore);
        tvTechnicalFeedback = findViewById(R.id.tvTechnicalFeedback);

        tvConfidenceScore = findViewById(R.id.tvConfidenceScore);
        tvConfidenceFeedback = findViewById(R.id.tvConfidenceFeedback);

        tvStrength = findViewById(R.id.tvStrength);
        tvGrowth = findViewById(R.id.tvGrowth);
        tvRecommendation = findViewById(R.id.tvRecommendation);

        btnHome = findViewById(R.id.btnHome);

        btnHome.setOnClickListener(v -> {

            Intent intent =
                    new Intent(ResultActivity.this,
                            MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);

            finish();

        });

        String summary = getIntent().getStringExtra("summary");

        generateFinalEvaluation(summary);
    }

    private void generateFinalEvaluation(String summary) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        String prompt =
                "Analyze this interview summary:\n\n"
                        + summary
                        + "\n\nReturn ONLY in this format:\n\n"

                        + "OVERALL_SCORE: x/10\n\n"

                        + "COMMUNICATION_SCORE: x/10\n"
                        + "COMMUNICATION_FEEDBACK: text\n\n"

                        + "TECHNICAL_SCORE: x/10\n"
                        + "TECHNICAL_FEEDBACK: text\n\n"

                        + "CONFIDENCE_SCORE: x/10\n"
                        + "CONFIDENCE_FEEDBACK: text\n\n"

                        + "KEY_STRENGTH: text\n\n"

                        + "GROWTH_AREA: text\n\n"

                        + "FINAL_RECOMMENDATION: text\n\n"

                        + "Do not use markdown symbols.";

        try {

            JSONObject textObject = new JSONObject();
            textObject.put("text", prompt);

            JSONArray partsArray = new JSONArray();
            partsArray.put(textObject);

            JSONObject contentObject = new JSONObject();
            contentObject.put("parts", partsArray);

            JSONArray contentsArray = new JSONArray();
            contentsArray.put(contentObject);

            JSONObject bodyJson = new JSONObject();
            bodyJson.put("contents", contentsArray);

            RequestBody body = RequestBody.create(
                    bodyJson.toString(),
                    MediaType.parse("application/json")
            );

            String url =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key="
                            + API_KEY;

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {

                    runOnUiThread(() ->
                            tvRecommendation.setText(e.getMessage()));

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String result = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(result);

                        JSONArray candidates =
                                jsonObject.getJSONArray("candidates");

                        JSONObject candidate =
                                candidates.getJSONObject(0);

                        JSONObject content =
                                candidate.getJSONObject("content");

                        JSONArray parts =
                                content.getJSONArray("parts");

                        String responseText =
                                parts.getJSONObject(0)
                                        .getString("text");

                        runOnUiThread(() -> fillUI(responseText));

                    } catch (Exception e) {

                        runOnUiThread(() ->
                                tvRecommendation.setText(result));

                    }

                }
            });

        } catch (Exception e) {

            tvRecommendation.setText(e.getMessage());

        }
    }

    private void fillUI(String text) {

        tvScore.setText(extractScore(text, "OVERALL_SCORE") + "%");

        tvCommunicationScore.setText(
                extractScore(text, "COMMUNICATION_SCORE") + "%");

        tvTechnicalScore.setText(
                extractScore(text, "TECHNICAL_SCORE") + "%");

        tvConfidenceScore.setText(
                extractScore(text, "CONFIDENCE_SCORE") + "%");

        tvCommunicationFeedback.setText(
                extractText(text, "COMMUNICATION_FEEDBACK"));

        tvTechnicalFeedback.setText(
                extractText(text, "TECHNICAL_FEEDBACK"));

        tvConfidenceFeedback.setText(
                extractText(text, "CONFIDENCE_FEEDBACK"));

        tvStrength.setText(
                extractText(text, "KEY_STRENGTH"));

        tvGrowth.setText(
                extractText(text, "GROWTH_AREA"));

        tvRecommendation.setText(
                extractText(text, "FINAL_RECOMMENDATION"));
    }

    private int extractScore(String text, String key) {

        Pattern pattern =
                Pattern.compile(key + ":\\s*(\\d+)/10");

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {

            int score = Integer.parseInt(matcher.group(1));

            return score * 10;
        }

        return 0;
    }

    private String extractText(String text, String key) {

        Pattern pattern =
                Pattern.compile(key + ":\\s*(.*)");

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {

            return matcher.group(1);
        }

        return "";
    }
}