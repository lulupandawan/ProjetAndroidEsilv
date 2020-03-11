package fr.esilv.projetandroidesilv.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import fr.esilv.projetandroidesilv.R;

public class HomeFragment extends Fragment {
    private static final String TAG = "MainActivity";
    private static final String API_KEY = "9ab6ba97358e0a2c6f0174221921ddf2";

    private Button button;
    private EditText editText;
    private String query;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final EditText textView = root.findViewById(R.id.editText);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        editText = root.findViewById(R.id.editText);
        button = (Button)root.findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                query = editText.getText().toString();
                launchSearch(query);
            }
        });
        return root;
    }

    private void launchSearch(String query) {

    }
}