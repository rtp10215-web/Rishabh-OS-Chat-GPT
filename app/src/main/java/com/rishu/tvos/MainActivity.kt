package com.rishu.tvos

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.graphics.*
import android.view.*
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {
    private lateinit var screen: Dashboard
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screen = Dashboard()
        setContentView(screen)
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
            keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN ||
            keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            screen.handleKey(keyCode); return true
        }
        return super.onKeyDown(keyCode, event)
    }

    inner class Dashboard : View(this@MainActivity) {
        private val p = Paint(Paint.ANTI_ALIAS_FLAG)
        private var dark = true
        private var selected = 0
        private val items = listOf("YouTube", "Netflix", "JioHotstar", "Prime Video", "Live TV", "Settings")
        private val packages = listOf("com.google.android.youtube.tv", "com.netflix.ninja",
            "in.startv.hotstar", "com.amazon.amazonvideo.livingroom", "", "")
        private val cards = listOf("CINEMA", "ARCADE", "ASSISTANT")
        private val rects = mutableListOf<RectF>()
        private var pulse = 0f

        override fun onDraw(c: Canvas) {
            super.onDraw(c)
            val w = width.toFloat(); val h = height.toFloat()
            val bg1 = if (dark) Color.rgb(5,13,35) else Color.rgb(220,239,250)
            val bg2 = if (dark) Color.rgb(18,48,83) else Color.rgb(120,177,213)
            p.shader = LinearGradient(0f,0f,w,h,bg1,bg2,Shader.TileMode.CLAMP)
            c.drawRect(0f,0f,w,h,p); p.shader = null
            p.color = (if (dark) 0x223FAAFF else 0x2280BDE8).toInt()
            c.drawCircle(w*.78f,h*.18f,h*.34f,p)
            for (i in 0..65) {
                val x = ((i*173)%997)/997f*w
                val y = ((i*271)%991)/991f*h
                p.color = 0x66FFFFFF.toInt()
                c.drawCircle(x,y,if(i%5==0) 2.2f else 1.1f,p)
            }
            val white = if (dark) Color.WHITE else Color.rgb(15,37,58)
            val muted = if (dark) 0xFFB9CDE0.toInt() else 0xFF34536C.toInt()
            drawText(c, greeting(), 42f, 42f, 22f, muted)
            drawText(c, SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault()).format(Date()),42f,72f,14f,muted)
            drawText(c, SimpleDateFormat("h:mm", Locale.getDefault()).format(Date()),42f,160f,72f,white)
            drawText(c, "YOUR ENTERTAINMENT, REIMAGINED",45f,190f,13f,muted)
            glass(c, RectF(w-300f,35f,w-42f,145f), 0x553C7BA8, 24f)
            drawText(c,"TODAY",w-275f,68f,14f,white)
            drawText(c,"Weather card",w-275f,98f,19f,white)
            drawText(c,"Connect internet for live weather",w-275f,122f,10f,muted)
            drawText(c,"PICK UP WHERE YOU LEFT OFF",42f,238f,14f,muted)
            val cardY=260f; val cardW=(w-116f)/3f
            for (i in cards.indices) {
                val r=RectF(42f+i*(cardW+16f),cardY,42f+i*(cardW+16f)+cardW,cardY+118f)
                glass(c,r, if(i==0) 0x664A75B8 else 0x554B6688,22f)
                drawText(c,cards[i],r.left+20f,r.top+43f,17f,white)
                drawText(c,when(i){0->"Featured movies & shows";1->"Your games and apps";else->"Quick controls"},r.left+20f,r.top+72f,12f,muted)
            }
            drawText(c,"APPS",42f,h-100f,13f,muted)
            rects.clear()
            val dockY=h-82f; val gap=12f; val dockW=(w-84f-gap*5)/6f
            for(i in items.indices){
                val left=42f+i*(dockW+gap); val r=RectF(left,dockY,left+dockW,dockY+54f); rects.add(r)
                glass(c,r,if(i==selected) 0xAA237FB8.toInt() else 0x554A6788,15f)
                if(i==selected){
                    p.style=Paint.Style.STROKE; p.strokeWidth=3f+ pulse*1.5f; p.color=0xFF78D9FF.toInt()
                    c.drawRoundRect(r,15f,15f,p); p.style=Paint.Style.FILL
                }
                drawText(c,items[i],r.left+12f,r.top+33f,14f,white)
            }
            pulse = (pulse + .08f) % 1f
            postInvalidateDelayed(70)
        }
        private fun greeting(): String {
            val hour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            return when(hour){ in 5..11->"Good morning, Rishu"; in 12..16->"Good afternoon, Rishu"; in 17..20->"Good evening, Rishu"; else->"Good night, Rishu" }
        }
        private fun drawText(c:Canvas,s:String,x:Float,y:Float,size:Float,color:Int){
            p.shader=null;p.style=Paint.Style.FILL;p.color=color;p.textSize=size;p.typeface=Typeface.create("sans-serif",Typeface.NORMAL);c.drawText(s,x,y,p)
        }
        private fun glass(c:Canvas,r:RectF,color:Int,rad:Float){
            p.shader=LinearGradient(r.left,r.top,r.right,r.bottom,color,0x223FFFFF,Shader.TileMode.CLAMP)
            p.style=Paint.Style.FILL
            c.drawRoundRect(r,rad,rad,p)
            p.shader=null
            p.color=0x55FFFFFF
            p.style=Paint.Style.STROKE
            p.strokeWidth=1.2f
            c.drawRoundRect(r,rad,rad,p)
            p.style=Paint.Style.FILL
        }
        fun handleKey(key:Int){
            when(key){
                KeyEvent.KEYCODE_DPAD_LEFT -> selected=(selected+items.size-1)%items.size
                KeyEvent.KEYCODE_DPAD_RIGHT -> selected=(selected+1)%items.size
                KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> launchSelected()
            }
            invalidate()
        }
        private fun launchSelected(){
            when(selected){
                4 -> Toast.makeText(this@MainActivity,"Open Live TV from your TV's Apps if available.",Toast.LENGTH_LONG).show()
                5 -> { dark=!dark; invalidate() }
                else -> {
                    val pkg=packages[selected]
                    val intent=if(pkg.isNotEmpty()) packageManager.getLaunchIntentForPackage(pkg) else null
                    if(intent!=null) startActivity(intent)
                    else Toast.makeText(this@MainActivity,items[selected]+" is not installed or its TV app is unavailable.",Toast.LENGTH_SHORT).show()
                }
            }
        }
        override fun onTouchEvent(e:android.view.MotionEvent):Boolean{
            if(e.action==MotionEvent.ACTION_UP){ for(i in rects.indices){if(rects[i].contains(e.x,e.y)){selected=i;launchSelected();return true}}}
            return true
        }
    }
}