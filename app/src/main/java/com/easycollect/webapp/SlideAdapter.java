package com.easycollect.webapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;

    // list of images
    public int[] lst_images = {
            R.drawable.colligo1,
            R.drawable.colligo2,
            R.drawable.colligo3,
            R.drawable.colligo4,
    };
    // list of titles
    public String[] lst_title = {
            "Bill Gates",
            "Bill Gates",
            "Bill Gates",
            "Bill Gates",
    };

    public TutorialStep[] tutorial_configuration = {
            new TutorialStep(false),
            new TutorialStep(false),
            new TutorialStep(false),
            new TutorialStep(true)
    };

    // list of descriptions
    public String[] lst_description = {
            "Success is a lousy teacher. It seduces smart people into thinking they can't lose.",
            "It's fine to celebrate success but it is more important to heed the lessons of failure.",
            "We all need people who will give us feedback. That's how we improve.",
            "If you can't make it good, at least make it look good.",
            "Everyone needs a coach. It doesn't matter whether you're a basketball player or a tennis player.",
            "If your culture doesn't like geeks, you are in real trouble.",
            "Your most unhappy customers are your greatest source of learning.",
            "Life is not fair; get used to it.",
            "If you think your teacher is tough, wait till you get a boss.",
            "If your Business is not on the Internet, then your business will be out of Business.",
            "We have got to put a lot of money into changing behavior.",
            "To win big, you sometimes have to take big risks."
    };
    // list of background colors
    public int[]  lst_backgroundcolor = {
            Color.rgb(255,255,255),
            Color.rgb(83,164,81),
            Color.rgb(255,255,255),
            Color.rgb(83,164,81),
    };

    public SlideAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lst_title.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide,container,false);
        RelativeLayout layoutslide = (RelativeLayout) view.findViewById(R.id.slidelinearlayout);
        ImageView imgslide = (ImageView)  view.findViewById(R.id.slideimg);
        //TextView txttitle= (TextView) view.findViewById(R.id.txttitle);
        //TextView description = (TextView) view.findViewById(R.id.txtdescription);
        layoutslide.setBackgroundColor(lst_backgroundcolor[position]);
        imgslide.setImageResource(lst_images[position]);

        Button endTutorialButton = (Button) view.findViewById(R.id.tutorial_end);
        // In case of lastStep == true we add the 'Exit Tutorial' Button
        if(tutorial_configuration[position].isFinalStep()){

            endTutorialButton.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent startIntent = new Intent(context, MainActivity.class);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(startIntent);
                }
            });
        }else{
            endTutorialButton.setVisibility(View.GONE);
        }
        //txttitle.setText(lst_title[position]);
        //description.setText(lst_description[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}