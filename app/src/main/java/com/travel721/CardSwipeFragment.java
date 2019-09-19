package com.travel721;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.travel721.AnalyticsHelper.USER_NEGATIVE_FEEDBACK;
import static com.travel721.AnalyticsHelper.USER_POSITIVE_FEEDBACK;
import static com.travel721.Constants.API_ROOT_URL;
import static com.travel721.Constants.SLIDE_ANIMATION_DURATION;

/**
 * A placeholder fragment containing a simple view.
 */
public class CardSwipeFragment extends Fragment implements CardStackListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    private LoadingFragment callingLoader;

    // This is where to make the bundle info
    public static CardSwipeFragment newInstance(Bundle bundle, LoadingFragment callingLoader) {
        CardSwipeFragment fragment = new CardSwipeFragment();
        fragment.setArguments(bundle);
        fragment.callingLoader = callingLoader;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cardArrayList = getArguments().getParcelableArrayList("events");
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

    }

    View root;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_event_swipe, container, false);

        AnalyticsHelper.logEvent(getContext(), AnalyticsHelper.TEST_RELEASE_ANALYTICS_EVENT, null);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_event_swipe);


        FloatingActionButton likeButton = root.findViewById(R.id.thumbupButton);
        FloatingActionButton dislikeButton = root.findViewById(R.id.thumbdownButton);
        FloatingActionButton shareEventButton = root.findViewById(R.id.shareEventButton);
        likeButton.setOnClickListener(this::likesTopCard);
        dislikeButton.setOnClickListener(this::dislikesTopCard);
        shareEventButton.setOnClickListener(view -> {
            try {
                Log.v("INDEX", String.valueOf(cardStackLayoutManager.getTopPosition()));

                int index = cardStackLayoutManager.getTopPosition();
                Card card = cardArrayList.get(index);
                if (card instanceof EventCard) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_TEXT, ((EventCard) card).getEventHyperLink());
                    i.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(i, null);
                    startActivity(shareIntent);
                } else if (card instanceof FeedbackCard) {
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.travel721"));
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        FloatingActionButton filterButton = root.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(view -> {
            FilterBottomSheetFragment filterBottomSheetFragment = FilterBottomSheetFragment.newInstance(callingLoader);
            filterBottomSheetFragment.show(getFragmentManager(),
                    "filter_sheet_fragment");
        });

        cardStackView = root.findViewById(R.id.card_stack_view);

        if (cardArrayList.isEmpty())
            root.findViewById(R.id.no_more_events_tv).setVisibility(View.VISIBLE);

        initialise();


        return root;
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    // Keep a track of the CardView and it's adapter
    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackLayoutManager;
    ArrayList<Card> cardArrayList;

    String CARD_SWIPE_REQUEST_TAG = "CardSwipeRequestTag";

    @Override
    public void onDetach() {
        super.onDetach();
        queue.cancelAll(CARD_SWIPE_REQUEST_TAG);
        queue.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        queue.cancelAll(CARD_SWIPE_REQUEST_TAG);
        queue.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        queue.cancelAll(CARD_SWIPE_REQUEST_TAG);
        queue.stop();
    }

    void initialise() {
        // Create card stack manager
        cardStackLayoutManager = new CardStackLayoutManager(getContext(), this);
        cardStackLayoutManager.setStackFrom(StackFrom.Top);
        cardStackLayoutManager.setVisibleCount(2);
        cardStackLayoutManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        List<Direction> directions = new ArrayList<>();
        directions.add(Direction.Right);
        directions.add(Direction.Left);
        directions.add(Direction.Bottom);
        cardStackLayoutManager.setDirections(directions);
        queue = Volley.newRequestQueue(getContext());
        // Create card stack adapter
        CardStackAdapter cardStackAdapter = new CardStackAdapter(cardArrayList);

        cardStackView.setLayoutManager(cardStackLayoutManager);
        cardStackView.setAdapter(cardStackAdapter);
    }

    private boolean snackShown = false;

    @Override
    public void onCardDragging(Direction direction, float ratio) {
        if (!snackShown) {
            snackShown = true;
            Snackbar.make(cardStackView, "Pulldown on an event to show more information", Snackbar.LENGTH_LONG).show();
        }
    }

    RequestQueue queue;

    @Override
    public void onCardSwiped(Direction direction) {
        // Removes TSnackBar
        if (cardStackLayoutManager.getChildCount() == 0) {
            TextView tv = root.findViewById(R.id.no_more_events_tv);
            tv.setVisibility(View.VISIBLE);
        }
        // Gets the Card index
        int index = cardStackLayoutManager.getTopPosition() - 1;
//        int index = cardStackLayoutManager.getTopPosition();


        if (cardArrayList.get(index) instanceof EventCard) {
            EventCard eventCard = (EventCard) cardArrayList.get(index);

            StringRequest stringRequest;
            String url = API_ROOT_URL + "eventProfiles?access_token=" + getArguments().getString("accessToken");
            // Each case makes a call to the Analytics API
            //vector drawable
            TSnackbar snackbar = TSnackbar
                    .make(root.findViewById(R.id.rootConstraintLayout), "", TSnackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.WHITE);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.WHITE);
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setPadding(0, 50, 0, 50);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            switch (direction) {
                case Left:
                    snackbar.dismiss();
                    String[] negative_terms = getResources().getStringArray(R.array.negative_terms);
                    textView.setText(getRandom(negative_terms));
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    snackbar.show();
                    AnalyticsHelper.logEvent(getContext(), AnalyticsHelper.USER_SWIPED_LEFT, null);
                    // Dislikes
                    // Request a string response from the provided URL.
                    stringRequest = new StringRequest(Request.Method.POST, url,
                            response -> {
                                // Display the first 500 characters of the response string.
                                Log.v("SWIPE", "Successfully registered dislike on " + eventCard.getName());
                            }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show()) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("swipe", "false");
                            map.put("eventSourceId", eventCard.getEventSourceID());
                            map.put("profileId", getArguments().getString("fiid"));
                            return map;
                        }
                    };

                    // Add the request to the RequestQueue.
                    stringRequest.setTag(CARD_SWIPE_REQUEST_TAG);
                    queue.add(stringRequest);
                    // TODO make a request to the API
                    break;
                case Right:
                    snackbar.dismiss();
                    String[] positive_terms = getResources().getStringArray(R.array.positive_terms);
                    textView.setText(getRandom(positive_terms));
                    textView.setTextColor(Color.rgb(0, 230, 118));
                    snackbar.show();
                    AnalyticsHelper.logEvent(getContext(), AnalyticsHelper.USER_SWIPED_RIGHT, null);

                    // Likes
                    // Request a string response from the provided URL.
                    stringRequest = new StringRequest(Request.Method.POST, url,
                            response -> {
                                // Display the first 500 characters of the response string.
                                Log.v("SWIPE", "Successfully registered like dislike on " + eventCard.getName());
                            }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show()) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("swipe", "true");
                            map.put("eventSourceId", eventCard.getEventSourceID());
                            map.put("profileId", getArguments().getString("fiid"));
                            return map;
                        }
                    };

                    // Add the request to the RequestQueue.
                    stringRequest.setTag(CARD_SWIPE_REQUEST_TAG);
                    queue.add(stringRequest);
                    // TODO make a request to the API
                    break;
                case Bottom:
                    AnalyticsHelper.logEvent(getContext(), AnalyticsHelper.USER_SWIPED_DOWN, null);
                    Intent i = new Intent(getContext(), EventMoreInfoActivity.class);
                    i.putExtra("eventCard", cardArrayList.get(cardStackLayoutManager.getTopPosition() - 1));
                    startActivity(i);
//                    overridePendingTransition(R.anim.slide_in_from_top, 0);

                    break;
            }
        }
        if (cardArrayList.get(index) instanceof FeedbackCard) {
            switch (direction) {
                case Left:
                    AnalyticsHelper.logEvent(getContext(), USER_NEGATIVE_FEEDBACK, null);
                    break;
                case Right:
                    AnalyticsHelper.logEvent(getContext(), USER_POSITIVE_FEEDBACK, null);
                    break;
            }
        }
//        cardArrayList.remove(cardArrayList.get(index));
    }


    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }

    boolean buttonPushed = false;

    public void dislikesTopCard(View view) {
        buttonPushed = true;
        cardStackLayoutManager.setSwipeAnimationSetting(new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(SLIDE_ANIMATION_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .build());
        cardStackView.swipe();

    }

    public void likesTopCard(View view) {
        buttonPushed = true;

        cardStackLayoutManager.setSwipeAnimationSetting(new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(SLIDE_ANIMATION_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .build());
        cardStackView.swipe();
    }


    public static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
}