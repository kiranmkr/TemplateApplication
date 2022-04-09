package com.example.testingapplication

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.testingapplication.datamodel.ImageView
import com.example.testingapplication.datamodel.Root
import com.example.testingapplication.datamodel.TextView
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    var imageViewArray: ArrayList<ImageView> = ArrayList()
    var textViewJson: ArrayList<TextView> = ArrayList()
    var rootLayout: RelativeLayout? = null
    var screenRatioFactor: Double = 1.0
    var screenWidth: Double = 720.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootLayout = findViewById(R.id.root_layout)

        val viewTreeObserver = rootLayout?.viewTreeObserver
        viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            override fun onGlobalLayout() {
                rootLayout?.viewTreeObserver?.removeOnGlobalLayoutListener(this)

                if (rootLayout != null) {
                    screenWidth = rootLayout!!.width.toDouble()
                }

                try {

                    if (loadJSONFromAsset() != null) {

                        val obj = JSONObject(loadJSONFromAsset()!!)
                        val om = ObjectMapper()
                        val root: Root = om.readValue(obj.toString(), Root::class.java)

                        if (root.absoluteLayout != null) {
                            screenRatioFactor =
                                screenWidth / root.absoluteLayout!!.androidLayoutWidth!!.replace(
                                    "dp",
                                    ""
                                ).toDouble()

                            if (root.absoluteLayout!!.imageView != null) {
                                root.absoluteLayout!!.imageView!!.forEachIndexed { index, imageView ->
                                    imageViewArray.add(index, imageView)
                                }
                            }
                            if (root.absoluteLayout!!.textView != null) {
                                root.absoluteLayout?.textView?.forEachIndexed { index, textview ->
                                    textViewJson.add(index, textview)
                                }
                            }
                        }

                        if (imageViewArray.size > 0) {
                            addImage(imageViewArray)
                        }

                        if (textViewJson.size > 0) {
                            addText(textViewJson)
                        }

                        /* root.absoluteLayout?.androidLayoutWidth?.let {
                             screenRatioFactor = screenWidth / it.replace("dp", "").toDouble()
                         }

                         root.absoluteLayout?.imageView?.let {

                             if (it.size > 0) {

                                 it.forEachIndexed { index, element ->
                                     imageView?.add(index, element)
                                 }

                                 if (imageView != null && imageView!!.size > 0) {
                                     addImage(imageView!!)
                                     Log.d("myError", "Size is not  null")
                                 } else {
                                     Log.d("myTag", "image list is null")
                                 }

                             }else{
                                 Log.d("myError", "Size is null")
                             }
                         }

                         root.absoluteLayout?.textView?.let {
                             if (it.size > 0) {

                                 it.forEachIndexed { index, element ->
                                     textViewJson?.add(index, element)
                                 }

                                 if (textViewJson != null && textViewJson!!.size > 0) {
                                     addText(textViewJson!!)
                                 } else {
                                     Log.d("myTag", "Array size is null")
                                 }

                             }
                         }*/

                    } else {
                        Log.d("myError", "wrong json")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("myTag", "exception")
                }

            }
        })


    }

    var newImageView: android.widget.ImageView? = null

    private fun addImage(array: ArrayList<ImageView>) {

        array.forEachIndexed { _, imageView ->

            newImageView = android.widget.ImageView(this@MainActivity)

            if (imageView.appSrcCompat != null) {

                val resources: Resources = resources
                val resourceId: Int = resources.getIdentifier(
                    imageView.appSrcCompat.toString().replace("@drawable/", ""),
                    "drawable",
                    packageName
                )

                newImageView?.setImageDrawable(resources.getDrawable(resourceId))
            }

            if (imageView.androidBackground != null) {

                newImageView?.setBackgroundColor(Color.parseColor(imageView.androidBackground))
            }

            if (imageView.androidLayoutWidth != null && imageView.androidLayoutHeight != null) {

                val width = (imageView.androidLayoutWidth.toString()
                    .replace("dp", "")).toInt() * screenRatioFactor
                val height = (imageView.androidLayoutHeight.toString()
                    .replace("dp", "")).toInt() * screenRatioFactor
                val layoutParams = RelativeLayout.LayoutParams(width.toInt(), height.toInt())
                newImageView?.layoutParams = layoutParams
            }

            if (imageView.androidLayoutX != null) {
                newImageView?.x = (imageView.androidLayoutX!!.replace("dp", "").toDouble()
                        * screenRatioFactor).toFloat()
            }

            if (imageView.androidLayoutY != null) {
                newImageView?.y = (imageView.androidLayoutY!!.replace("dp", "").toDouble()
                        * screenRatioFactor).toFloat()
            }

            if (imageView.androidRotation != null) {
                newImageView?.rotation = imageView.androidRotation!!.toFloat()
            }

            if (imageView.androidAlpha != null) {
                newImageView?.alpha = imageView.androidAlpha!!.toFloat()
            }

            rootLayout?.addView(newImageView)

        }

        /*it.forEachIndexed { index, imageView ->

            newImageView = android.widget.ImageView(this)

            it[index].appSrcCompat?.let {

                Log.d("myFile", "${it.toString()}")

                val resources: Resources = resources
                val resourceId: Int = resources.getIdentifier(
                    "${it.toString().replace("@drawable/", "")}",
                    "drawable",
                    packageName
                )

                newImageView?.setImageDrawable(resources.getDrawable(resourceId))
            }

            it[index].androidBackground?.let {
                newImageView?.setBackgroundColor(Color.parseColor(it))
            }

            val width = (it[index].androidLayoutWidth.toString()
                .replace("dp", "")).toInt() * screenRatioFactor
            val height = (it[index].androidLayoutHeight.toString()
                .replace("dp", "")).toInt() * screenRatioFactor

            val parms = RelativeLayout.LayoutParams(width.toInt(), height.toInt())
            newImageView?.layoutParams = parms
            newImageView?.x =
                (it.get(index).androidLayoutX?.replace("dp", "")
                    ?.toFloat()?.times(screenRatioFactor))!!.toFloat()
            newImageView?.y =
                (it.get(index).androidLayoutY?.replace("dp", "")
                    ?.toFloat()?.times(screenRatioFactor))!!.toFloat()

            if (it[index].androidRotation != null) {
                Log.d("myRotation", "roration is  not null")
                newImageView?.rotation = it[index].androidRotation?.toFloat()!!
            } else {
                Log.d("myRotation", "roration is null")
            }

            it[index].androidAlpha?.let {
                newImageView?.alpha = it.toFloat()
            }

            rootLayout?.addView(newImageView)
        }*/

    }

    var newTextView: android.widget.TextView? = null

    private fun addText(arratlist: ArrayList<TextView>) {

        arratlist.forEachIndexed { _, textView ->

            newTextView = android.widget.TextView(this@MainActivity)

            if (textView.androidText != null) {
                newTextView?.text = textView.androidText
            }

           /* if (textView.androidLayoutX != null) {
                Log.d("myTag", "${textView.androidLayoutX}")
                val androidX: Double = textView.androidLayoutX!!.toDouble() * screenRatioFactor
                newTextView?.x = androidX.toFloat()
            }*/

            rootLayout?.addView(newTextView)
        }

        /*it.forEachIndexed { index, textView ->

            newTextView = android.widget.TextView(this@MainActivity)

            newTextView?.text = it[index].androidText

            newTextView?.x =
                (it.get(index).androidLayoutX?.replace("dp", "")
                    ?.toFloat()?.times(screenRatioFactor))!!.toFloat()
            newTextView?.y =
                (it.get(index).androidLayoutY?.replace("dp", "")
                    ?.toFloat()?.times(screenRatioFactor))!!.toFloat()

            newTextView?.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                ((it[index].androidTextSize?.replace(
                    "sp",
                    ""
                ))?.toFloat()?.times(screenRatioFactor))!!.toFloat()
            )


            if (it[index].androidRotation != null) {
                Log.d("myRotation", "roration is  not null")
                newTextView?.rotation = it[index].androidRotation?.toFloat()!!
            } else {
                Log.d("myRotation", "roration is null")
            }

            Log.d("myTag", "${it.get(index).androidTextSize}")
            newTextView?.setTextColor(Color.parseColor(it.get(index).androidTextColor))
            if (it[index].androidBackground != null) {
                newTextView?.setBackgroundColor(Color.parseColor(it[index].androidBackground))
            } else {
                Log.d("myBackgroudColor", "Color is null")
            }

            if (it[index].androidLetterSpacing != null) {
                newTextView?.letterSpacing = it[index].androidLetterSpacing?.toFloat()!!
            } else {
                Log.d("myLetterSpacing", "values is null")
            }

            if (it[index].androidTextAlignment != null) {
                Log.d("myTextAlignment", "values is not null")
                when {
                    it[index].androidTextAlignment.equals("center", ignoreCase = true) -> {

                        Log.d("myTextAlignment", "values in center")
                        newTextView?.textAlignment = View.TEXT_ALIGNMENT_CENTER

                    }
                    it[index].androidTextAlignment.equals("textEnd", ignoreCase = true) -> {

                        Log.d("myTextAlignment", "values in textEnd")
                        newTextView?.textAlignment = View.TEXT_ALIGNMENT_TEXT_END

                    }
                    it[index].androidTextAlignment.equals("textStart", ignoreCase = true) -> {
                        Log.d("myTextAlignment", "values in textStart")
                        newTextView?.textAlignment = View.TEXT_ALIGNMENT_TEXT_START

                    }

                }
            } else {
                Log.d("myTextAlignment", "values is null")
            }

            if (it[index].androidAlpha != null) {
                newTextView?.alpha = it[index].androidAlpha?.toFloat()!!
            } else {
                Log.d("myAndroidAlpha", "values is null")
            }

            if (!it[index].androidLayoutWidth.equals("wrap_content") && !it[index].androidLayoutHeight.equals(
                    "wrap_content"
                )
            ) {

                val width =
                    (it.get(index).androidLayoutWidth.toString()
                        .replace("dp", "")).toInt() * screenRatioFactor
                val height =
                    (it.get(index).androidLayoutHeight.toString()
                        .replace("dp", "")).toInt() * screenRatioFactor
                val parms = RelativeLayout.LayoutParams(width.toInt(), height.toInt())
                newTextView?.layoutParams = parms

            } else {
                Log.d("myLayoutWidth", "values is null")
            }

            if (it[index].androidFontFamily != null) {

                val resources: Resources = resources
                val resourceId: Int = resources.getIdentifier(
                    it.get(index).androidFontFamily.toString().replace("@font/", ""),
                    "font",
                    packageName
                )

                val typeface = ResourcesCompat.getFont(this@MainActivity, resourceId)
                if (typeface != null) {
                    newTextView?.typeface = typeface
                }

            } else {
                Log.d("myFontValue", "values is null")
            }

            rootLayout?.addView(newTextView)
        }*/
    }

    private fun loadJSONFromAsset(): String? {

        var json: String? = null

        json = try {
            val `is`: InputStream = this.assets.open("2.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json

    }

    @Throws(IOException::class)
    fun AssetJSONFile(filename: String, context: Context): String? {
        val manager: AssetManager = context.assets
        val file = manager.open(filename)
        val formArray = ByteArray(file.available())
        file.read(formArray)
        file.close()
        return String(formArray)
    }
}