package com.travel721;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialogFragment;

public class SelectLocationDiscoverFragment extends RoundedBottomSheetDialogFragment {
    int discoverFragmentId;
    String accessToken;

    public static SelectLocationDiscoverFragment newInstance(int discoverFragment, String accessToken) {
        SelectLocationDiscoverFragment discoverFragmentSelectLocation = new SelectLocationDiscoverFragment();
        discoverFragmentSelectLocation.discoverFragmentId = discoverFragment;
        discoverFragmentSelectLocation.accessToken = accessToken;
        return discoverFragmentSelectLocation;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover_bottom_sheet, container,
                false);
        TextView title = view.findViewById(R.id.discoverTitle);
        title.setOnClickListener(v -> {
            dismiss();
        });
        SeekBar daysSeekBar = view.findViewById(R.id.daysSeekBar);
        SeekBar radiusSeekBar = view.findViewById(R.id.radiusSeekBar);
        TextView radTextView = view.findViewById(R.id.radiusTextView);
        EditText editText = view.findViewById(R.id.editText);
        TextView textView = view.findViewById(R.id.textView5);
        textView.setText(getString(R.string.up_to_days_from_today, 5));
        daysSeekBar.setProgress(5);
        radiusSeekBar.setProgress(5);
        radTextView.setText(getString(R.string.search_x_miles, 5));
        Button button = view.findViewById(R.id.discover_button);
        daysSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView.setText(getString(R.string.up_to_days_from_today, i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radTextView.setText(getString(R.string.search_x_miles, i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                LoadingDiscoverFragment loadingFragment = LoadingDiscoverFragment.newInstance(accessToken, editText.getText().toString(), String.valueOf(radiusSeekBar.getProgress()), String.valueOf(daysSeekBar.getProgress()));
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, loadingFragment).commit();
                dismiss();
            }
        });
        // get the views and attach the listener

        return view;

    }
}
