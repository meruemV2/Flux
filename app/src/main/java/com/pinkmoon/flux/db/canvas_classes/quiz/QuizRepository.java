package com.pinkmoon.flux.db.canvas_classes.quiz;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pinkmoon.flux.API.Quiz;
import com.pinkmoon.flux.API.Course;
import com.pinkmoon.flux.db.FluxDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizRepository
{
    private QuizDao quizDao;

    private LiveData<List<Quiz>> allQuizzes;

    private Application application; // to be used with Volley for API calls
    public MutableLiveData<List<Quiz>> listOfCanvasQuizzesByCourse;

    public static final String url = "https://canvas.instructure.com/api/v1/courses/";
    public static final String accessToken = "10284~nqtIdmzKxZtdTw324H6HQ3zZlG9TJSPNqagCfIlgjPwiErZttFv5Yj1ticxUT0xN";

    public QuizRepository(Application application){
        FluxDB fluxDB = FluxDB.getInstance(application);

        quizDao = fluxDB.quizDao();

        allQuizzes= quizDao.getAllQuizzes();

        this.application = application;
    }

    public void insertQuiz(Quiz... quiz) {
        new QuizRepository.InsertQuizAsync(quizDao).execute(quiz);
    }

    public void updateQuiz(Quiz quiz){
        new QuizRepository.UpdateQuizAsync(quizDao).execute(quiz);
    }

    public void deleteQuiz(Quiz quiz) {
        new QuizRepository.DeleteQuizAsync(quizDao).execute(quiz);
    }

    public LiveData<List<Quiz>> getAllQuizzes() {
        return allQuizzes;
    }

    public MutableLiveData<List<Quiz>> getListOfCanvasQuizzesByCourse(List<Course> courses) {
        if(listOfCanvasQuizzesByCourse == null){
            listOfCanvasQuizzesByCourse = new MutableLiveData<>();
        }
        loadCanvasQuizzes(courses);
        return listOfCanvasQuizzesByCourse;
    }

    // Async Task Operations
    public class InsertQuizAsync extends AsyncTask<Quiz, Void, Void> {

        QuizDao quizDao;
        public InsertQuizAsync(QuizDao quizDao) {
            this.quizDao = quizDao;
        }


        @Override
        protected Void doInBackground(Quiz... quizzes) {
            quizDao.insertQuiz(quizzes);
            return null;
        }
    }
    public class UpdateQuizAsync extends AsyncTask<Quiz, Void, Void>{

        QuizDao quizDao;
        public UpdateQuizAsync(QuizDao quizDao) {
            this.quizDao = quizDao;
        }

        @Override
        protected Void doInBackground(Quiz... quizzes) {
            quizDao.updateQuiz(quizzes[0]);
            return null;
        }

    }
    public class DeleteQuizAsync extends AsyncTask<Quiz, Void, Void> {

        QuizDao quizDao;
        public DeleteQuizAsync(QuizDao quizDao) {
            this.quizDao = quizDao;
        }

        @Override
        protected Void doInBackground(Quiz... quizzes) {
            quizDao.deleteQuiz(quizzes[0]);
            return null;
        }
    }

    // API Calls
    private void loadCanvasQuizzes(List<Course> courses) {
        for (Course course: courses)
        {
            String quizzesURL = url + course.getCourseId() + "/quizzes";
            RequestQueue queue = Volley.newRequestQueue(application);

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, quizzesURL,
                    null, new Response.Listener<JSONArray>()
            {
                @Override
                public void onResponse(JSONArray response)
                {
                    List<Quiz> canvasQuizList = convertJSONToListOfObject(response);
                    listOfCanvasQuizzesByCourse.setValue(canvasQuizList);
                }
            }, error ->
            {
                Toast.makeText(application, "ERROR: Volley could not satisfy the request for QUIZZES", Toast.LENGTH_SHORT).show();
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + accessToken);
                    return headers;
                }
            };
            queue.add(request);
        }
    }

    private List<Quiz> convertJSONToListOfObject(JSONArray response)
    {
        List<Quiz> list = new ArrayList<>();
        for (int i = 0; i < response.length(); i++)
        {
            try
            {
                JSONObject singleQuiz = response.getJSONObject(i);
                Gson gson = new Gson();
                Quiz quiz = gson.fromJson(singleQuiz.toString(), Quiz.class);
                if(quiz.getQuizName() != null && quiz.getQuizId() != null)
                {
                    list.add(quiz);
                }
                else
                {
                    Log.e("WARNING - EXPECTED", "A JSON Quiz Object did not have a Name nor ID and was ignored");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Log.e("ERROR","Could not trim some object from the JSONArray of quizzes");
            }
        }
        return list;
    }
}
