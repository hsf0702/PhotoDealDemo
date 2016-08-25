package com.example.a835127729qqcom.photodealdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a835127729qqcom.photodealdemo.R;

import java.util.Arrays;


/**
 * Created by 835127729qq.com on 16/8/24.
 */
public class RotatableEditText extends RelativeLayout implements View.OnTouchListener{
    private Context mContext;
    private ImageButton deleteBtn,rotateBtn;
    private TextView rotateTextView;
    private EditText rotateEditText;
    private RelativeLayout textLayout;
    private DeleteBtnClickListener mDeleteBtnClickListener;
    private ViewConfiguration mViewConfiguration;
    private int touchSlop;
    private int mParentWidth,mParentHeight;

    public RotatableEditText(Context context,int parentWidth,int parentHeight) {
        super(context);
        mParentHeight = parentHeight;
        mParentWidth = parentWidth;
        init(context);
    }

    public RotatableEditText(Context context) {
        super(context);
        init(context);
    }

    public RotatableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RotatableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        mViewConfiguration = ViewConfiguration.get(mContext);
        touchSlop = mViewConfiguration.getScaledTouchSlop();
        setOnTouchListener(this);
        initViews();
        setUpDeleteBtn();
        //setUpRotateBtn();
        //setUpRotateTextView();
        setUpRotateEditText();
    }

    private void initViews(){
        LayoutInflater.from(mContext).inflate(R.layout.rotatable_edittext, this,true);
        textLayout = (RelativeLayout) findViewById(R.id.text_layout);
        rotateTextView = (TextView) findViewById(R.id.rotate_textview);
        rotateEditText = (EditText) findViewById(R.id.rotate_edittext);
        deleteBtn = (ImageButton) findViewById(R.id.delete_btn);
        rotateBtn = (ImageButton) findViewById(R.id.rotate_btn);
        rotateBtn.setEnabled(false);
        rotateBtn.setClickable(false);
    }

    private float lastX,lastY;
    private int lastLeftMargin,lastTopMargin;
    private int stage = 0;

    TouchRotateBtnHandler mTouchRotateBtnHandler = new TouchRotateBtnHandler();


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //Log.i("cky","1");

        clearEditFocus();
        int action = motionEvent.getAction();
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        float x= motionEvent.getX();
        float y = motionEvent.getY();
        /*
        Log.i("cky","x="+x+",y="+y+",rawx="+rawX+",rawY="+rawY);
        //处理rotatebtn
        Log.i("cky","x="+x+",y="+y+",rawx="+rawX+",rawY="+rawY);

        Rect rect2 = new Rect();
        Point p = new Point();
        RotatableEditText.this.getGlobalVisibleRect(rect2,p);
        Log.i("cky","rect="+(rect2)+",point="+p);

        int[] location = new int[2];
        RotatableEditText.this.getLocationOnScreen(location);
        Log.i("cky","location="+ Arrays.toString(location));
        Rect r2 = new Rect();
        RotatableEditText.this.getLocalVisibleRect(r2);
        Log.i("cku","LocalVisibleRect="+ r2.toString());
        */
        //RotatableEditText.this.get
        //Log.i("cky","w="+ RotatableEditText.this.getWidth()+",h="+RotatableEditText.this.getHeight());
        if(!mTouchRotateBtnHandler.isInit){
            mTouchRotateBtnHandler.init();
        }
        if(isTouchRotateBtn((int)x,(int) y)){
            //Log.i("cky","rotate");
            mTouchRotateBtnHandler.handleTouchRotateBtn(view,motionEvent);
            //handleTouchRotateBtn(view,motionEvent);
            return true;
        }
        if(stage==1){
            mTouchRotateBtnHandler.handleTouchRotateBtn(view,motionEvent);
            //handleTouchRotateBtn(view,motionEvent);
            return true;
        }
        if(stage!=0){//在旋转过程,不被拉伸
            return true;
        }
        //其他范围拖动
        RelativeLayout.LayoutParams params = (LayoutParams) getLayoutParams();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                lastX = rawX;
                lastY = rawY;
                lastLeftMargin = params.leftMargin;
                lastTopMargin = params.topMargin;
                mTouchRotateBtnHandler.changeLastRect();
                //Log.i("cky","按下 x="+rawX+",y="+rawY);
                break;
            case MotionEvent.ACTION_MOVE:
                int newLeftMargin = (int) (lastLeftMargin + rawX - lastX);
                int newTopMargin = (int) (lastTopMargin + rawY - lastY);
                if(isOutOfParent(newLeftMargin,newTopMargin)) break;
                //Log.i("cky","移动 x="+rawX+",y="+rawY);
                params.setMargins(newLeftMargin,newTopMargin, params.rightMargin,params.bottomMargin);
                mTouchRotateBtnHandler.changeRect((int)(rawX - lastX),(int)(rawY - lastY));
                //Log.i("cky","位置 leftMargin="+params.leftMargin+",topMargin="+params.topMargin);
                setLayoutParams(params);
                view.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(lastX==rawX&&lastY==rawY&&isTouchTextLayout((int)rawX,(int)rawY)) {
                    rotateTextView.setVisibility(GONE);
                    rotateEditText.setVisibility(VISIBLE);
                    rotateEditText.requestFocus();
                    ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(rotateEditText, 0);
                }
        }
        return true;
    }

    private void handleTouchRotateBtn(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float x = motionEvent.getRawX();
        float y = motionEvent.getRawY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
        }
    }

    private class TouchRotateBtnHandler{
        boolean isInit = false;
        float lastX,lastY;
        int mWidth,mHeight;
        int centerX,centerY;
        float pivotX,pivotY;
        Rect lastRect,rect;
        float textSize = 0;
        double lastAngle = 0;
        double currentAngle = 0;
        double startAngle = 0;


        public void init(){
            isInit = true;
            mWidth = rotateTextView.getMeasuredWidth();
            mHeight = rotateTextView.getMeasuredHeight();
            rect = new Rect();
            lastRect = rect;
            //(RotatableEditText.this).getLocalVisibleRect(rect);
            //Log.i("cky","rect1="+rect);
            //((RotatableTextCloudLayout)RotatableEditText.this.getParent()).getChildVisibleRect(RotatableEditText.this,rect,null);
            //Log.i("cky","rect2="+rect);
            (RotatableEditText.this).getGlobalVisibleRect(rect);
            //Log.i("cky","rect3="+rect);
            centerX = rect.centerX();
            centerY = rect.centerY();
            startAngle = Math.toDegrees(Math.atan(1.0d*(centerY-rect.bottom)/(rect.right-centerX)));
            pivotX = RotatableEditText.this.getPivotX();
            pivotY = RotatableEditText.this.getPivotY();
            textSize = rotateTextView.getTextSize();
        }

        private void changeLastRect(){
            lastRect = rect;
        }

        public void changeRect(int leftOffset,int topOffset){
            /*
            (RotatableEditText.this).getGlobalVisibleRect(rect);
            centerX = rect.centerX();
            centerY = rect.centerY();
            startAngle = 0;//Math.toDegrees(Math.atan(1.0d*(centerY-rect.bottom)/(rect.right-centerX)));
            Log.i("cky","startAngle is "+startAngle);
            pivotX = pivotX+leftOffset;
            pivotY = pivotY+topOffset;
            textSize = rotateTextView.getTextSize();
            */

            rect = new Rect(lastRect.left+leftOffset,lastRect.top+topOffset,lastRect.right+leftOffset,lastRect.bottom+topOffset);
            centerX = rect.centerX();
            centerY = rect.centerY();
            //startAngle = Math.toDegrees(Math.atan(1.0d*(centerY-rect.bottom)/(rect.right-centerX)));
            pivotX = pivotX+leftOffset;
            pivotY = pivotY+topOffset;
            //startAngle = 0;
            textSize = rotateTextView.getTextSize();
            Rect r = new Rect();
            (RotatableEditText.this).getGlobalVisibleRect(r);
        }

        private void rotate(float x,float y){
            double angle = 0;
            float dx = x - centerX;
            float dy = y -centerY;
            if(dx < 0){
                if(dy<0){
                    angle = 180-Math.toDegrees(Math.atan(dy/dx));
                }else if(dy==0){
                    angle = 180;
                }else{
                    angle = Math.toDegrees(Math.atan(dy/-dx))+180;
                }
            }else if(dx == 0){
                if(dy > 0) angle = 270;
                else angle = 90;
            }else{
                if(dy<0){
                    angle = Math.toDegrees(Math.atan(-dy/dx));
                }else if(dy==0){
                    angle = 0;
                }else{
                    angle = 360-Math.toDegrees(Math.atan(dy/dx));
                }
            }
            //Log.i("cky","angle="+angle);
            //Log.i("cky","startangle="+(startAngle));
            currentAngle = angle - startAngle;
            Log.i("cky","deangle="+currentAngle);
            //Log.i("cky","pivotX="+RotatableEditText.this.getPivotX()+",pivotY="+RotatableEditText.this.getPivotY());
            //RotatableEditText.this.setPivotX(pivotX);
            //RotatableEditText.this.setPivotY(pivotY);
            RotatableEditText.this.setRotation(-(float) (currentAngle));
        }

        public void handleTouchRotateBtn(View view, MotionEvent motionEvent){
            int action = motionEvent.getAction();
            float x = motionEvent.getRawX();
            float y = motionEvent.getRawY();

            switch (action){
                case MotionEvent.ACTION_DOWN:
                    stage = 1;
                    lastX = x;
                    lastY = y;

                    //Log.i("cky","centerX="+centerX+",centerY="+centerY);
                    //Log.i("cky","lastX="+lastX+",lastY="+lastY);
                    //Log.i("cky","centerX="+centerX+",PivotX="+R);
                    break;
                case MotionEvent.ACTION_MOVE:
                    //rotate
                    rotate(x,y);

                    //scale
                        /*
                        int xa = (int) (lastX-rect.centerX());
                        int ya = (int) (lastY-rect.centerY());
                        int xb = (int) (rawX-rect.centerX());
                        int yb = (int) (rawY-rect.centerY());
                        float srcLen = (float) Math.sqrt(xa*xa+ya*ya);
                        float curLen = (float) Math.sqrt(xb*xb+yb*yb);
                        float scale = curLen/srcLen;
                        Log.i("cky","scale="+scale);
                        int newWidth = (int) (mWidth*(scale));
                        int newHeight = (int) (mHeight*(scale));
                        rotateTextView.getMatrix().setScale(scale,scale);

                        /*
                        double srcLen = Math.sqrt(rect.width()*rect.width() + rect.height()*rect.height());
                        float xa = x - rect.left;
                        float ya = y - rect.top;
                        double destLen = Math.sqrt(xa*xa + ya*ya);
                        float scaleX = (float) (destLen/srcLen);
                        float scaleY = scaleX;
                        if(scaleX < 0.5) break;
                        rotateTextView.setWidth((int) (mWidth*scaleX));
                        rotateTextView.setHeight((int) (mHeight*scaleX));
                        //rotateTextView.setTextSize(textSize*scaleX);
                        rotateEditText.setWidth((int) (mWidth*scaleX));
                        rotateEditText.setHeight((int) (mHeight*scaleX));
                        */
                        /*
                        rotateTextView.setScaleX(scaleX);
                        rotateTextView.setScaleY(scaleY);
                        rotateEditText.setScaleX(scaleX);
                        rotateEditText.setScaleY(scaleY);
                        */
                        /*
                        rotateTextView.setWidth(newWidth);
                        rotateEditText.setWidth(newWidth);
                        rotateTextView.setHeight(newHeight);
                        rotateEditText.setHeight(newHeight);
                        */
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    rotate(x,y);
                    lastAngle = currentAngle;
                    stage = 0;
                    break;
            }
        }
    }

    private boolean isTouchRotateBtn(int x,int y){
        Rect rect = new Rect(rotateBtn.getLeft(),rotateBtn.getTop(),rotateBtn.getRight(),rotateBtn.getBottom());
        return rect.contains(x,y);
    }

    private boolean isTouchTextLayout(int x,int y){
        Rect rect = new Rect(textLayout.getLeft(),textLayout.getTop(),textLayout.getRight(),textLayout.getBottom());
        return rect.contains(x,y);
    }

    private void setUpRotateTextView(){
        rotateTextView.setOnTouchListener(new OnTouchListener() {
            private float lastDownX,lastDownY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                float x = motionEvent.getRawX();
                float y = motionEvent.getRawY();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        lastDownX = x;
                        lastDownY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("cky","lastDownX="+lastDownX+",x="+x+"lastDownY="+lastDownY+",y="+y);
                        if(lastDownX == x && lastDownY==y){//点击
                            Log.i("cky","点击");
                            rotateTextView.setVisibility(GONE);
                            rotateEditText.setVisibility(VISIBLE);
                            rotateEditText.requestFocus();
                            ((InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(rotateEditText, 0);
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void setUpRotateEditText(){
        rotateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                rotateTextView.setText(editable.toString());
            }
        });
    }

    private void setUpDeleteBtn(){
        deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("cky","2");
                mDeleteBtnClickListener.onClick(RotatableEditText.this);
            }
        });
    }

    private void setUpRotateBtn(){
        rotateBtn.setOnTouchListener(new OnTouchListener() {
            float lastX,lastY;
            int mWidth,mHeight;
            int centerX,centerY;
            float pivotX,pivotY;
            Rect rect;
            float textSize = 0;
            double mCurrentAngle = 0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Log.i("cky","3");
                int action = motionEvent.getAction();
                float rawX = motionEvent.getRawX();
                float rawY = motionEvent.getRawY();
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                Log.i("cky","x="+x+",y="+y+",rawx="+rawX+",rawY="+rawY);

                Rect rect2 = new Rect();
                Point p = new Point();
                RotatableEditText.this.getGlobalVisibleRect(rect2,p);
                Log.i("cky","rect="+(rect2)+",point="+p);

                int[] location = new int[2];
                RotatableEditText.this.getLocationOnScreen(location);
                Log.i("cky","location="+ Arrays.toString(location));
                Rect r2 = new Rect();
                RotatableEditText.this.getLocalVisibleRect(r2);
                Log.i("cku","LocalVisibleRect="+ r2.toString());
                //RotatableEditText.this.get
                Log.i("cky","w="+ RotatableEditText.this.getWidth()+",h="+RotatableEditText.this.getHeight());
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        lastX = rawX;
                        lastY = rawY;
                        mWidth = rotateTextView.getMeasuredWidth();
                        mHeight = rotateTextView.getMeasuredHeight();
                        rect = new Rect();
                        RotatableEditText.this.getGlobalVisibleRect(rect);
                        centerX = rect.centerX();
                        centerY = rect.centerY();
                        pivotX = RotatableEditText.this.getPivotX();
                        pivotY = RotatableEditText.this.getPivotY();
                        textSize = rotateTextView.getTextSize();
                        //Log.i("cky","centerX="+centerX+",PivotX="+R);
                        break;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        //rotate
                        double angle = 0;
                        float dx = rawX - centerX;
                        float dy = rawY -centerY;
                        if(dx < 0){
                            if(dy<0){
                                angle = 180-Math.toDegrees(Math.atan(dy/dx));
                            }else if(dy==0){
                                angle = 180;
                            }else{
                                angle = Math.toDegrees(Math.atan(dy/-dx))+180;
                            }
                        }else if(dx == 0){
                            if(dy > 0) angle = 270;
                            else angle = 90;
                        }else{
                            if(dy<0){
                                angle = Math.toDegrees(Math.atan(-dy/dx));
                            }else if(dy==0){
                                angle = 0;
                            }else{
                                angle = 360-Math.toDegrees(Math.atan(dy/dx));
                            }
                        }
                        //Log.i("cky","angle="+angle);
                        double startAngle = Math.toDegrees(Math.atan(1.0d*(centerY-rect.bottom)/(rect.right-centerX)));
                        //Log.i("cky","startangle="+(startAngle));

                        //Log.i("cky","deangle="+(angle-startAngle));
                        RotatableEditText.this.setPivotX(pivotX);
                        //RotatableEditText.this.setScaleX();
                        RotatableEditText.this.setPivotY(pivotY);
                        RotatableEditText.this.setRotation(-(float) (angle-startAngle));


                        //scale
                        /*
                        int xa = (int) (lastX-rect.centerX());
                        int ya = (int) (lastY-rect.centerY());
                        int xb = (int) (rawX-rect.centerX());
                        int yb = (int) (rawY-rect.centerY());
                        float srcLen = (float) Math.sqrt(xa*xa+ya*ya);
                        float curLen = (float) Math.sqrt(xb*xb+yb*yb);
                        float scale = curLen/srcLen;
                        Log.i("cky","scale="+scale);
                        int newWidth = (int) (mWidth*(scale));
                        int newHeight = (int) (mHeight*(scale));
                        rotateTextView.getMatrix().setScale(scale,scale);
                        */
                        /*
                        double srcLen = Math.sqrt(rect.width()*rect.width() + rect.height()*rect.height());
                        float xa = rawX - rect.left;
                        float ya = rawY - rect.top;
                        double destLen = Math.sqrt(xa*xa + ya*ya);
                        float scaleX = (float) (destLen/srcLen);
                        float scaleY = scaleX;
                        if(scaleX < 0.5) break;
                        rotateTextView.setWidth((int) (mWidth*scaleX));
                        rotateTextView.setHeight((int) (mHeight*scaleX));
                        //rotateTextView.setTextSize(textSize*scaleX);
                        rotateEditText.setWidth((int) (mWidth*scaleX));
                        rotateEditText.setHeight((int) (mHeight*scaleX));
                        */
                        /*
                        rotateTextView.setScaleX(scaleX);
                        rotateTextView.setScaleY(scaleY);
                        rotateEditText.setScaleX(scaleX);
                        rotateEditText.setScaleY(scaleY);
                        */
                        /*
                        rotateTextView.setWidth(newWidth);
                        rotateEditText.setWidth(newWidth);
                        rotateTextView.setHeight(newHeight);
                        rotateEditText.setHeight(newHeight);
                        */
                        invalidate();

                        break;
                }
                return false;
            }
        });
    }


    /**
     * 判断是否被拖出边界
     * @return
     */
    public boolean isOutOfParent(int leftMargin,int topMargin){
        if(leftMargin<0 || topMargin<0 || getWidth()+leftMargin > mParentWidth || getHeight()+topMargin>mParentHeight){
            return true;
        }
        return false;
    }

    public boolean clearEditFocus(){
        boolean flag = rotateEditText.hasFocus();
        rotateTextView.setVisibility(VISIBLE);
        rotateEditText.setVisibility(GONE);
        rotateEditText.clearFocus();
        return flag;
    }

    public boolean containXY(float x, float y){
        RectF rectF = new RectF(getLeft(), getTop(), getRight(), getBottom());
        return rectF.contains(x,y);
    }

    public interface DeleteBtnClickListener{
        void onClick(RotatableEditText rotatableEditText);
    }

    public void setDeleteBtnClickListener(DeleteBtnClickListener deleteBtnClickListener){
        mDeleteBtnClickListener = deleteBtnClickListener;
    }
}