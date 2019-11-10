package com.xeniac.selectalgorithm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText mainET;
    private TextView initialEmptyTV, sortedEmptyTV;

    private RecyclerView initialRV, sortedRV;
    private ArrayList<Integer> mainArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        mainInitializer();
    }

    private void mainInitializer() {
        mainET = findViewById(R.id.ti_main_edit);
        initialEmptyTV = findViewById(R.id.tv_main_initial_empty);
        sortedEmptyTV = findViewById(R.id.tv_main_sorted_empty);

        initialRV = findViewById(R.id.rv_main_initial);
        sortedRV = findViewById(R.id.rv_main_sorted);

        mainArray = new ArrayList<>();

        mainCondition();
        editTextActionDone();
    }

    private void mainCondition() {
        if (mainArray.isEmpty()) {
            initialRV.setVisibility(View.GONE);
            sortedRV.setVisibility(View.GONE);

            initialEmptyTV.setVisibility(View.VISIBLE);
            sortedEmptyTV.setVisibility(View.VISIBLE);
        } else {
            initialEmptyTV.setVisibility(View.GONE);
            sortedRV.setVisibility(View.GONE);

            initialRV.setVisibility(View.VISIBLE);
            sortedEmptyTV.setVisibility(View.VISIBLE);

            mainRecyclerView();
        }
    }

    private void mainRecyclerView() {
        MainAdapter initialAdapter = new MainAdapter(this, mainArray);
        initialRV.setAdapter(initialAdapter);
    }

    private void editTextActionDone() {
        mainET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addOnClick(v);
            }
            return false;
        });
    }

    public void addOnClick(View view) {
        String input = Objects.requireNonNull(mainET.getText()).toString();

        if (TextUtils.isEmpty(input)) {
            mainET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (methodManager != null) {
                methodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } else {
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (methodManager != null) {
                methodManager.hideSoftInputFromWindow(mainET.getWindowToken(), 0);
            }
            mainET.setText(null);
            mainET.clearFocus();

            mainArray.add(Integer.parseInt(input));
            mainCondition();
            sortedEmptyTV.setText(R.string.string_main_sorted_msg);
        }
    }

    public void sortOnClick(View view) {
        sortedEmptyTV.setVisibility(View.GONE);
        sortedRV.setVisibility(View.VISIBLE);

//        quickSort(mainArray, 0, mainArray.size() - 1);

        MainAdapter sortedAdapter = new MainAdapter(this, mainArray);
        sortedRV.setAdapter(sortedAdapter);
    }
}