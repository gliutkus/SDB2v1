package fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.g.sdb.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import static com.example.g.sdb.R.layout.fragment_home;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageButton btnLock;
    private ImageButton btnMic;
    private ImageButton btnAlert;
    private VideoView piStream;
    private RelativeLayout homeFragmentLayout;
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private String src;
    private VideoView videoView;
    //String url = "http://www.ebookfrenzy.com/android_book/movie.mp4";

    //refferencing the firebase database getting instance of the child and parent dabase entries
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("Motor/state");
//    DatabaseReference mConditionRefMic= mRootRef.child("")


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //stating that the root is the fragment home fragment
        //it is done so that it would be possible to link the fragment components to their R value
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        //stating the components
        final VideoView videoView = (VideoView) root.findViewById(R.id.dashboard_videoview);
        homeFragmentLayout = (RelativeLayout) root.findViewById(R.id.dashboard_relative_layout);
        btnLock = (ImageButton) root.findViewById(R.id.dashboard_btn_unlock);
        btnAlert = (ImageButton) root.findViewById(R.id.dashboard_btn_emergency);
        btnMic=(ImageButton)root.findViewById(R.id.dashboard_btn_microphone);
        src = "http://192.168.0.136:8090/"; //the ip for the pi stream
        videoView.setVideoPath("http://www.ebookfrenzy.com/android_book/movie.mp4"); // this code was used for the testing while the live stream was not available
        /*videoView.setVideoPath("http://192.168.0.136:8090/");

        videoView.start();*/

        Uri UriSrc = Uri.parse(src);//parsing the url
        videoView.setVideoURI(UriSrc);//setting that parsed url to the uri
        mediaController = new MediaController(getActivity());
        videoView.setMediaController(mediaController);
        videoView.start();//starting the stream

        return root;


    }

    public void onStart() {
        super.onStart();
        //this is used for linking the firebase database between the variables and the parent directory
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
            }//taken as a snapshot because the firebase connectivity transfers the variable in a snapshot form

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





            btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConditionRef.setValue("UNLOCK");//the value that the button is linked to send to the DB
                Snackbar snackBar = Snackbar.make(homeFragmentLayout, "You have opened the door", Snackbar.LENGTH_SHORT);
                snackBar.show();//show the snackbar(alert message)
            }
        });


        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);//the button calls an intent for action dial
                intent.setData(Uri.parse("tel:112"));//the phone number that is being called during an emergency
                startActivity(intent);//starts the intent

            }
        });

        //btnMic.setOnClickListener




    }









    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* public void onClick (View view){
        if(view.getId()==R.id.dashboard_btn_unlock)
        {
            Snackbar snackBar = Snackbar.make(homeFragmentLayout,"You have opened the door",Snackbar.LENGTH_SHORT);
            snackBar.show();
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }






    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
