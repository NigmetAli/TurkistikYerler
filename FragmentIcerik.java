package nmt.turkistikyerler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by NigmetAli on 4.05.2016.
 */
public class FragmentIcerik extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_icerik,container,false);

        Button  btnMap = (Button) rootView.findViewById(R.id.btnMap);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ( ((MainActivity) getActivity()).getYerKontrol()) {
                    Intent i = new Intent(getContext(), Harita.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getActivity(), "Lütfen Bir Yer Seçin", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return rootView;
    }
}
