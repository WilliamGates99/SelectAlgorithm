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

    private TextInputEditText addET, findET;
    private TextView listEmptyTV, foundEmptyTV, foundNumberTV;

    private RecyclerView mainRV;
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
        addET = findViewById(R.id.ti_main_edit_add);
        findET = findViewById(R.id.ti_main_edit_find);
        listEmptyTV = findViewById(R.id.tv_main_list_empty);
        foundEmptyTV = findViewById(R.id.tv_main_found_empty);
        foundNumberTV = findViewById(R.id.tv_main_found_number);

        mainRV = findViewById(R.id.rv_main);

        mainArray = new ArrayList<>();

        mainCondition();
        addActionDone();
        findActionDone();
    }

    private void mainCondition() {
        if (mainArray.isEmpty()) {
            mainRV.setVisibility(View.GONE);
            foundNumberTV.setVisibility(View.GONE);

            listEmptyTV.setVisibility(View.VISIBLE);
            foundEmptyTV.setVisibility(View.VISIBLE);
        } else {
            listEmptyTV.setVisibility(View.GONE);
            foundNumberTV.setVisibility(View.GONE);

            mainRV.setVisibility(View.VISIBLE);
            foundEmptyTV.setVisibility(View.VISIBLE);

            mainRecyclerView();
        }
    }

    private void mainRecyclerView() {
        MainAdapter initialAdapter = new MainAdapter(this, mainArray);
        mainRV.setAdapter(initialAdapter);
    }

    private void addActionDone() {
        addET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addOnClick(v);
            }
            return false;
        });
    }

    private void findActionDone() {
        findET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findOnClick(v);
            }
            return false;
        });
    }

    public void addOnClick(View view) {
        String input = Objects.requireNonNull(addET.getText()).toString();

        if (TextUtils.isEmpty(input)) {
            addET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (methodManager != null) {
                methodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } else {
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (methodManager != null) {
                methodManager.hideSoftInputFromWindow(addET.getWindowToken(), 0);
            }
            addET.setText(null);
            addET.clearFocus();

            mainArray.add(Integer.parseInt(input));
            mainCondition();
            foundEmptyTV.setText(R.string.string_main_found_msg);
            foundEmptyTV.setMaxLines(10);
        }
    }

    public void findOnClick(View view) {
        if (listEmptyTV.getVisibility() != View.VISIBLE) {
            String input = Objects.requireNonNull(findET.getText()).toString();

            if (TextUtils.isEmpty(input)) {
                findET.requestFocus();
                InputMethodManager methodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (methodManager != null) {
                    methodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            } else {
                InputMethodManager methodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (methodManager != null) {
                    methodManager.hideSoftInputFromWindow(findET.getWindowToken(), 0);
                }
                findET.setText(null);
                findET.clearFocus();

                if (Integer.parseInt(input) > mainArray.size()) {
                    foundEmptyTV.setText(R.string.string_main_found_msg_error);
                } else {
                    foundEmptyTV.setVisibility(View.GONE);
                    foundNumberTV.setVisibility(View.VISIBLE);

                    int foundNumber = quickSelect(mainArray, 0,
                            mainArray.size() - 1, Integer.parseInt(input) - 1);
                    foundNumberTV.setText(foundNumber);
                }
            }
        }
    }

    private int partition(ArrayList<Integer> array, int low, int high) {
        int pivot = array.get(high);
        int pivotLocation = low;

        for (int i = low; i <= high; i++) {
            if (array.get(i) < pivot) {
                int temp = array.get(i);
                array.set(i, array.get(pivotLocation));
                array.set(pivotLocation, temp);
                pivotLocation++;
            }
        }

        int temp = array.get(high);
        array.set(high, array.get(pivotLocation));
        array.set(pivotLocation, temp);

        return pivotLocation;
    }

    private int quickSelect(ArrayList<Integer> array, int low, int high, int k) {
        int partitionIndex = partition(array, low, high);

        if (partitionIndex == k) {
            return array.get(partitionIndex);
        } else if (partitionIndex < k) {
            return quickSelect(array, partitionIndex + 1, high, k);
        } else {
            return quickSelect(array, low, partitionIndex + 1, k);
        }
    }
}