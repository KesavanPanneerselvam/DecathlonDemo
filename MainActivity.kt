package com.juagri.testimagemap

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.juagri.testimagemap.test.ImageMap


class MainActivity : AppCompatActivity() {
    private lateinit var imageMapView:ImageMap
    private lateinit var markerView:RelativeLayout
    private lateinit var mutableBitmap: Bitmap

    val markerPositions = mutableListOf<MarkerPosition>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageMapView = findViewById(R.id.image)
        markerView = findViewById(R.id.marker_view)
        //imageMapView.setImageResource(R.drawable.test)

        val myOptions = BitmapFactory.Options()
        mutableBitmap = BitmapFactory.decodeResource(resources, R.drawable.test, myOptions)
        /*imageMapView.setImageBitmap(mutableBitmap)
        imageMapView.post({

        })*/
        val allPositions = listOf(
            MarkerPosition("Item 1",160f,111f),
            MarkerPosition("Item 2",445f,282f),
            MarkerPosition("Item 3",454f,172f),
            MarkerPosition("Item 4",176f,270f)
        )
        allPositions.forEach {it->
            val imageWidth = 598
            val imageHeight = 364
            val bitmapWidth = mutableBitmap.width
            val bitmapHeight = mutableBitmap.height
            val xRatio = (it.xPos/imageWidth) * 10f
            val yRatio = (it.yPos/imageHeight) * 10f
            var xPos = (bitmapWidth/10)*xRatio
            var yPos = (bitmapHeight/10)*yRatio
            xPos -= 20
            yPos -= 50
            markerPositions.add(MarkerPosition(it.name,xPos, yPos))
            mutableBitmap = overlay(xPos,yPos)
        }
        imageMapView.setImageBitmap(mutableBitmap)
        //imageMapView.setOnBubbleClickeListener(this);
        //imageMapView.setOnShapeClickeListener(this);
        /*imageMapView.setOnTouchListener { view, motionEvent ->
           when(motionEvent.action){
               MotionEvent.ACTION_POINTER_UP ->{
                   Toast.makeText(
                       this@MainActivity,
                       "ACTION_DOWN - X: " + motionEvent.x + "Y: " + motionEvent.y,
                       Toast.LENGTH_LONG
                   ).show()
               }
               else ->{}
           }
            false
        }*/
        imageMapView.setOnTouchListener { view: View?, mv: MotionEvent ->
            when (mv.action) {
                /*MotionEvent.ACTION_UP -> Toast.makeText(
                    this@MainActivity,
                    "ACTION_UP - X: " + mv.x + "Y: " + mv.y,
                    Toast.LENGTH_LONG
                ).show()*/
                MotionEvent.ACTION_DOWN ->                     //marker.setX(mv.getX());
                    //marker.setY(mv.getY());
                   /* Toast.makeText(
                        this@MainActivity,
                        "ACTION_DOWN - X: " + mv.x + "Y: " + mv.y,
                        Toast.LENGTH_LONG
                    ).show()*/
                showToast(mv.x,mv.y)
                else -> Toast.makeText(
                    this@MainActivity,
                    "default - X: " + mv.x + "Y: " + mv.y,
                    Toast.LENGTH_LONG
                )
            }
            false
        }
    }

    private fun showToast(xPos: Float,yPos: Float){
        markerPositions.forEach {
            Log.d("123123123213", "Click X=$xPos Y=$yPos")
            Log.d("123123123213", "Item X=${it.xPos} Y=${it.yPos}")
            Log.d("123123123213", "Item ${it.name} X=${it.xPos+5} Y=${it.yPos+5}")
            Log.d("123123123213", "------------------------------------------")
            if(((it.xPos <= xPos) && ((it.xPos+5) <= xPos))
                && ((it.yPos <= yPos) && ((it.yPos+5) <= yPos))){
                Toast.makeText(
                    this@MainActivity,
                    it.name+" - X: " + xPos + "Y: " + yPos,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun overlay(xPos:Float,yPos:Float): Bitmap {
        val myOptions = BitmapFactory.Options()
        val bmp2 = BitmapFactory.decodeResource(resources, R.drawable.icon_marker_tick, myOptions)

        val bmOverlay = Bitmap.createBitmap(mutableBitmap.width, mutableBitmap.height, mutableBitmap.config)
        val canvas = Canvas(bmOverlay)
        canvas.drawBitmap(mutableBitmap, Matrix(), null)
        val paint = Paint()
        paint.color = Color.parseColor("#CD5C5C")
        canvas.drawBitmap(drawableToBitmap(ContextCompat.getDrawable(this@MainActivity,R.drawable.icon_marker_tick)!!),xPos-10, yPos-30, null)
        return bmOverlay
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    data class MarkerPosition(val name: String,val xPos:Float,val yPos:Float)
}