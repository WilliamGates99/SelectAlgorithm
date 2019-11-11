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
                    foundNumberTV.setVisibility(View.GONE);
                    foundEmptyTV.setVisibility(View.VISIBLE);
                    foundEmptyTV.setText(R.string.string_main_found_msg_error_greater);
                } else if (Integer.parseInt(input) <= 0) {
                    foundNumberTV.setVisibility(View.GONE);
                    foundEmptyTV.setVisibility(View.VISIBLE);
                    foundEmptyTV.setText(R.string.string_main_found_msg_error_less);
                } else {
                    foundEmptyTV.setVisibility(View.GONE);
                    foundNumberTV.setVisibility(View.VISIBLE);

                    int foundNumber = quickSelect(mainArray, 0,
                            mainArray.size() - 1, Integer.parseInt(input));
                    foundNumberTV.setText(String.valueOf(foundNumber));
                }
            }
        }
    }

    private int partition(ArrayList<Integer> array, int left, int right) {
        int x = array.get(right);
        int i = left;

        for (int j = left; j <= right - 1; j++) {
            if (array.get(j) <= x) {
                //Swapping array[i] and array[j]
                int temp = array.get(i);
                array.set(i, array.get(j));
                array.set(j, temp);

                i++;
            }
        }

        //Swapping array[i] and array[r]
        int temp = array.get(i);
        array.set(i, array.get(right));
        array.set(right, temp);

        return i;
    }

    private int quickSelect(ArrayList<Integer> array, int left, int right, int k) {

        if (k > 0 && k <= right - left + 1) {
            int pos = partition(array, left, right);

            if (pos - left == k - 1) {
                return array.get(pos);
            } else if (pos - left > k - 1) {
                return quickSelect(array, left, pos - 1, k);
            } else {
                return quickSelect(array, pos + 1, right, k - pos + left - 1);
            }
        }

        return Integer.MAX_VALUE;
    }
}