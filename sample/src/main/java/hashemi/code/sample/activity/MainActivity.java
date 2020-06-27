package hashemi.code.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hashemi.code.sample.App;
import hashemi.code.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnShowList.setOnClickListener(v ->
                startActivity(new Intent(this, PostListActivity.class))
        );

        binding.btnDeleteLocal.setOnClickListener(v ->
        {
            App.deleteRealm();
            Toast.makeText(this, "Local Data is cleared.", Toast.LENGTH_SHORT).show();
        });
    }
}
