package lilithwittmann.de.voicepitchanalyzer;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import lilithwittmann.de.voicepitchanalyzer.models.Recording;
import lilithwittmann.de.voicepitchanalyzer.utils.AudioPlayer;
import lilithwittmann.de.voicepitchanalyzer.utils.PitchCalculator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordingOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordingOverviewFragment extends Fragment implements SurfaceHolder.Callback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";
    SurfaceView gradient;
    SurfaceHolder gradientHolder;
    private Recording currentRecord;

    public RecordingOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber
     * @param recording
     * @return A new instance of fragment RecordingOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordingOverviewFragment newInstance(int sectionNumber, Recording recording) {
        RecordingOverviewFragment fragment = new RecordingOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable(Recording.KEY, recording);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.currentRecord = this.getArguments().getParcelable(Recording.KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recording_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.gradient = (SurfaceView) view.findViewById(R.id.gradient_canvas);
        this.gradientHolder = this.gradient.getHolder();
        this.gradientHolder.addCallback(this);

        AudioPlayer player = new AudioPlayer(getActivity().getFileStreamPath(this.currentRecord.getRecording()));
        player.play();

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Canvas canvas = surfaceHolder.lockCanvas();
        Double pitchRange = PitchCalculator.maxPitch - PitchCalculator.minPitch;
        Double pxPerHz = this.gradient.getHeight() / pitchRange;

        Paint p = new Paint();
        Paint textPaint = new Paint();

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);

        p.setColor(Color.MAGENTA);
        p.setAlpha(200);
        textPaint.setColor(Color.MAGENTA);
        textPaint.setAlpha(200);
        canvas.drawARGB(255, 255, 255, 255);
        canvas.drawRect(0,
                this.gradient.getHeight() - (float) ((PitchCalculator.minFemalePitch - PitchCalculator.minPitch) * pxPerHz),
                this.gradient.getWidth(),
                this.gradient.getHeight() - (float) ((PitchCalculator.maxFemalePitch - PitchCalculator.minPitch) * pxPerHz),
                p
        );

        canvas.drawText("female range", 10, this.gradient.getHeight() - (float)
                ((PitchCalculator.maxFemalePitch - PitchCalculator.minPitch) * pxPerHz) - 25, textPaint);

        p.setColor(Color.BLUE);
        p.setAlpha(90);
        textPaint.setColor(Color.BLUE);
        textPaint.setAlpha(90);
        canvas.drawRect(0,
                this.gradient.getHeight() - (float) ((PitchCalculator.minMalePitch - PitchCalculator.minPitch) * pxPerHz),
                this.gradient.getWidth(),
                this.gradient.getHeight() - (float) ((PitchCalculator.maxMalePitch - PitchCalculator.minPitch) * pxPerHz),
                p
        );

        canvas.drawText("male range", 10, this.gradient.getHeight() - (float)
                ((PitchCalculator.minMalePitch - PitchCalculator.minPitch) * pxPerHz) + 35, textPaint);


        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        //draw range
        paint.setStrokeWidth((float) ((this.currentRecord.getRange().getMax() - this.currentRecord.getRange().getMin()) * pxPerHz));
        paint.setAlpha(120);
        canvas.drawLine(0, this.gradient.getHeight() - (float) ((this.currentRecord.getRange().getMin() - PitchCalculator.minPitch) * pxPerHz), this.gradient.getWidth(), this.gradient.getHeight() - (float) ((this.currentRecord.getRange().getMin() - PitchCalculator.minPitch) * pxPerHz), paint);
        paint.setStrokeWidth(10);
        paint.setAlpha(255);
        paint.setColor(Color.BLACK);
        canvas.drawLine(0, this.gradient.getHeight() - (float) ((this.currentRecord.getRange().getAvg() - PitchCalculator.minPitch) * pxPerHz), this.gradient.getWidth(), this.gradient.getHeight() - (float) ((this.currentRecord.getRange().getAvg() - PitchCalculator.minPitch) * pxPerHz), paint);

        textPaint.setColor(Color.BLACK);
        textPaint.setAlpha(120);
        canvas.drawText("your range", 10, this.gradient.getHeight() -
                (float) ((this.currentRecord.getRange().getMin() -
                        ((float) ((this.currentRecord.getRange().getMax() -
                                this.currentRecord.getRange().getMin())) / 2) -
                        PitchCalculator.minPitch) * pxPerHz) + 35, textPaint);

        surfaceHolder.unlockCanvasAndPost(canvas);
        Log.d("foo", String.valueOf(this.gradient.getHeight() - (float) (PitchCalculator.maxFemalePitch * pxPerHz)));
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
