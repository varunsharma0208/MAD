package com.techiguru.interviewai;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InterviewActivity extends AppCompatActivity {

    TextView tvCategory, tvQuestion, tvResult, tvQuestionCount;
    EditText etAnswer;

    Button btnSubmit, btnNext, btnFinish;

    Chronometer chronometer;

    int questionNumber = 1;

    String category;

    String API_KEY = "AQ.Ab8RN6IMNQNFo-AfCHmtDFcC5FgigAd58GZaS13-j9xlxZxAVw";

    StringBuilder interviewSummary = new StringBuilder();

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        tvCategory = findViewById(R.id.tvCategory);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvResult = findViewById(R.id.tvResult);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);

        etAnswer = findViewById(R.id.etAnswer);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnNext = findViewById(R.id.btnNext);
        btnFinish = findViewById(R.id.btnFinish);

        chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        btnNext.setVisibility(View.GONE);

        category = getIntent().getStringExtra("category");

        tvCategory.setText(category + " Interview");
        tvQuestionCount.setText("Question 1");

        generateQuestion();

        btnSubmit.setOnClickListener(v -> {

            String answer = etAnswer.getText().toString().trim();

            if (answer.isEmpty()) {
                etAnswer.setError("Enter your answer");
                return;
            }

            evaluateAnswer(answer);

        });

        btnNext.setOnClickListener(v -> {

            questionNumber++;

            tvQuestionCount.setText("Question " + questionNumber);

            etAnswer.setText("");
            tvResult.setText("");

            btnNext.setVisibility(View.GONE);

            generateQuestion();

        });

        btnFinish.setOnClickListener(v -> {

            if (interviewSummary.length() == 0) {

                tvResult.setText("Attempt at least one question first.");
                return;
            }

            Intent intent =
                    new Intent(InterviewActivity.this,
                            ResultActivity.class);

            intent.putExtra("summary",
                    interviewSummary.toString());

            startActivity(intent);
            finish();

        });

    }

    private void generateQuestion() {

        btnSubmit.setEnabled(false);

        tvQuestion.setText("Generating question...");

        String prompt =
                "Ask only one " + category +
                        " interview question. Do not provide answer.";

        sendRequest(prompt, true);

    }

    private void evaluateAnswer(String answer) {
        btnNext.setVisibility(View.GONE);

        tvResult.setText("Evaluating...");

        String prompt =
                "Question: " + tvQuestion.getText().toString()
                        + "\nAnswer: " + answer
                        + "\nEvaluate in plain text only.\n"
                        + "Format:\n"
                        + "Score: x/10\n"
                        + "Strengths:\n"
                        + "- point\n"
                        + "Improvements:\n"
                        + "- point\n"
                        + "Do not use markdown symbols.";

        sendRequest(prompt, false);

    }

    private void sendRequest(String prompt, boolean isQuestion) {

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

                    runOnUiThread(() -> {

                        if (isQuestion)
                            tvQuestion.setText("Error: " + e.getMessage());
                        else
                            tvResult.setText("Error: " + e.getMessage());

                    });

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

                        runOnUiThread(() -> {

                            if (isQuestion) {

                                tvQuestion.setText(responseText);

                                btnSubmit.setEnabled(true);

                            } else {

                                tvResult.setText(responseText);

                                interviewSummary.append("Question: ")
                                        .append(tvQuestion.getText())
                                        .append("\n");

                                interviewSummary.append("Answer: ")
                                        .append(etAnswer.getText())
                                        .append("\n");

                                interviewSummary.append("Feedback: ")
                                        .append(responseText)
                                        .append("\n\n");

                                btnNext.setVisibility(View.VISIBLE);

                            }

                        });

                    } catch (Exception e) {

                        runOnUiThread(() -> {

                            if (isQuestion)
                                tvQuestion.setText(result);
                            else
                                tvResult.setText(result);

                        });

                    }

                }

            });

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}